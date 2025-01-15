package com.example.myapplication.activity;

import static com.example.myapplication.utils.MyFirebaseMessagingService.NOTIFICATIONS_KEY;
import static com.example.myapplication.utils.MyFirebaseMessagingService.PREFS_NAME;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.NotificationAdapter;
import com.example.myapplication.models.response.BookingDetailsResponse;
import com.example.myapplication.models.response.Notification;

import com.example.myapplication.models.response.NotificationResponse;
import com.example.myapplication.network.ApiService;
import com.example.myapplication.network.RetrofitClient;
import com.example.myapplication.utils.MyFirebaseMessagingService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import com.example.myapplication.utils.MyFirebaseMessagingService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PartnerNotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;

    private Button clearNotification;
    private List<Notification> notifications = new ArrayList<>();

    public String partnerStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_notification);

        recyclerView = findViewById(R.id.recyclerViewNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch notifications from SharedPreferences
        fetchNotificationsFromDatabase();


        // Log notifications for debugging before setting the adapter
        Log.d("onCreate", "Notifications before setting adapter:");
        for (Notification notification : notifications) {
            Log.d("onCreate", notification.toString());
        }

        // Set up the adapter to display notifications
        adapter = new NotificationAdapter(this,notifications);
        recyclerView.setAdapter(adapter);

        // Close activity with back button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        clearNotification=findViewById(R.id.clearAllButton);
        MyFirebaseMessagingService myService = new MyFirebaseMessagingService();
        clearNotification.setOnClickListener(view -> {
            myService.deleteAllNotificationsFromBackend(getApplicationContext()); // Clear notifications
            Toast.makeText(this, "All notifications cleared", Toast.LENGTH_SHORT).show();

            // Restart the activity to reflect the changes
            finish(); // Finish the current activity
            startActivity(getIntent()); // Start the same activity again
        });


    }

    private void fetchNotificationsFromDatabase() {
        Log.d("fetchNotifications", "Fetching notifications from database via API");

        ApiService apiService = RetrofitClient.getRetrofitInstance(this).create(ApiService.class);

        apiService.getNotifications().enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Clear the list to avoid duplicate entries
                    if (notifications == null) {
                        notifications = new ArrayList<>();
                    } else {
                        notifications.clear();
                    }

                    // Extract and add notifications from the response
                    NotificationResponse notificationResponse = response.body();
                    List<Notification> fetchedNotifications = notificationResponse.getData();

                    if (fetchedNotifications != null) {
                        notifications.addAll(fetchedNotifications);

                        // Log notifications for debugging
                        for (Notification notification : notifications) {
                            Log.d("fetchNotifications", "Title: " + notification.getTitle() +
                                    ", Body: " + notification.getBody() +
                                    ", isRead: " + notification.getId() +
                                    ", Booking ID: " + notification.getBookingId());
                        }

                        // Notify adapter of data change
                        adapter.notifyDataSetChanged();

                        Log.d("fetchNotifications", "Total notifications fetched: " + notifications.size());
                    }
                } else {
                    Log.e("fetchNotifications", "API response error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                Log.e("fetchNotifications", "API call failed: ", t);
            }
        });
    }








}
