package com.example.myapplication.activity;

import static com.example.myapplication.utils.MyFirebaseMessagingService.NOTIFICATIONS_KEY;
import static com.example.myapplication.utils.MyFirebaseMessagingService.PREFS_NAME;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.NotificationAdapter;
import com.example.myapplication.models.response.Notification;

import com.example.myapplication.utils.MyFirebaseMessagingService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PartnerNotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<Notification> notifications = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_notification);

        recyclerView = findViewById(R.id.recyclerViewNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch notifications from SharedPreferences
        fetchNotificationsFromSharedPreferences();

        // Set up the adapter to display notifications
        adapter = new NotificationAdapter(this,notifications);
        recyclerView.setAdapter(adapter);

        // Close activity with back button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
    }

    private void fetchNotificationsFromSharedPreferences() {
        Log.d("fetchNotifications", "Fetching notifications from SharedPreferences");

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String notificationsJson = sharedPreferences.getString(NOTIFICATIONS_KEY, "[]");

        try {
            JSONArray notificationsArray = new JSONArray(notificationsJson);

            // Clear the list to avoid duplicate entries
            if (notifications == null) {
                notifications = new ArrayList<>();
            } else {
                notifications.clear();
            }

            for (int i = 0; i < notificationsArray.length(); i++) {
                JSONObject notificationJson = notificationsArray.getJSONObject(i);

                String id = notificationJson.optString("id", ""); // Fetch ID
                String title = notificationJson.optString("title", "No Title");
                String body = notificationJson.optString("body", "No Body");
                String bookingId = notificationJson.optString("bookingId", "No bookingid");
                boolean isRead = notificationJson.optBoolean("isRead", false);

                Log.d("fetchNotifications", "Title: " + title + ", Body: " + body + ", isRead: " + isRead +",bookingid"+bookingId);

                notifications.add(new Notification(id, title, body, isRead,bookingId));
            }

        } catch (JSONException e) {
            Log.d("fetchNotifications", "JSON parsing error: ", e);
            e.printStackTrace();
        }

        Log.d("fetchNotifications", "Total notifications fetched: " + notifications.size());
    }


}
