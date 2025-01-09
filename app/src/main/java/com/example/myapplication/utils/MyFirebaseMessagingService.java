package com.example.myapplication.utils;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.example.myapplication.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String PREFS_NAME = "com.example.app.PREFERENCES";
    public static final String NOTIFICATIONS_KEY = "notifications";
    private static final String TAG = "MyFirebaseMessagingService"; // Debug tag

    private String title, body;
    private String bookingId;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("mynotificaion", "onMessageReceived called"); // Debug log

        // Handle notification payload
        if (remoteMessage.getNotification() != null) {
            title = remoteMessage.getNotification().getTitle();
            body = remoteMessage.getNotification().getBody();
            Log.d("mynotification", "Received notification - Title: " + title + ", Body: " + body);
        } else {
            Log.d("mynotification", "Received data - Title:error");
        }
        if (remoteMessage.getData().size() > 0) {
            bookingId = remoteMessage.getData().get("bookingId");
            String click_action = remoteMessage.getData().get("click_action");
            Log.d("mynotification", "RemoteMessage: " + bookingId);
            Log.d("mynotification", "Received notification - Title: " + click_action); // Debug log
        }

        // Store notification in SharedPreferences
        storeNotificationInSharedPreferences(title, body, bookingId);

        // Show notification in the notification bar
        showNotification(title, body);
    }

    private void storeNotificationInSharedPreferences(String title, String body, String bookingId) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Get the existing notifications from SharedPreferences
        String existingNotificationsJson = sharedPreferences.getString(NOTIFICATIONS_KEY, "[]");

        try {
            JSONArray notificationsArray = new JSONArray(existingNotificationsJson);

            // Generate a unique ID using the current timestamp
            String notificationId = String.valueOf(System.currentTimeMillis());

            // Create a new notification JSON object
            JSONObject newNotification = new JSONObject();
            newNotification.put("id", notificationId); // Unique ID for each notification
            newNotification.put("title", title);
            newNotification.put("body", body);
            newNotification.put("bookingId", bookingId); // Include the bookingId
            newNotification.put("isRead", false);  // Initially unread

            // Add the new notification to the array
            notificationsArray.put(newNotification);

            // Save updated notifications back to SharedPreferences
            editor.putString(NOTIFICATIONS_KEY, notificationsArray.toString());
            editor.apply();

            Log.d("n1", "Stored new notification: " + newNotification.toString());

        } catch (JSONException e) {
            Log.e(TAG, "Error storing notification", e);
        }
    }

    private void showNotification(String title, String body) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "default")
                .setSmallIcon(R.drawable.baseline_circle_notifications_24)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Log.d("mynotificaion", "Permission not granted for posting notifications"); // Debug log
            return;
        }

        notificationManager.notify(0, notificationBuilder.build());
        Log.d("mynotificaion", "Notification shown: " + title); // Debug log
    }

    public static void deleteAllNotifications(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Clear all notifications
        editor.putString(NOTIFICATIONS_KEY, "[]");
        editor.apply();

        Log.d(TAG, "All notifications deleted from SharedPreferences");
    }

}
