package com.example.myapplication.utils;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.myapplication.activity.MainActivity;
import com.example.myapplication.activity.PartnerBookingRequestActivity;
import com.example.myapplication.activity.PartnerNotificationActivity;
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
    private String bookingId, click_action;

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
            click_action = remoteMessage.getData().get("click_action");
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
        Intent intent;

        // Check click_action to decide which activity to open
        if (click_action != null) {
            switch (click_action) {
                case "OPEN_PARTNER_BOOKING_REQUEST":
                    // Redirect to PartnerBookingRequestActivity
                    intent = new Intent(this, PartnerBookingRequestActivity.class);
                    break;
                case "open_partner_notification":
                    // Redirect to PartnerNotificationActivity
                    intent = new Intent(this, PartnerNotificationActivity.class);
                    break;
                default:
                    // Default redirection to MainActivity
                    intent = new Intent(this, MainActivity.class);
                    break;
            }
        } else {
            // Default redirection to MainActivity if no click_action is provided
            intent = new Intent(this, MainActivity.class);
        }

        // Add bookingId as an extra to the intent, if available
        if (bookingId != null) {
            intent.putExtra("bookingId", bookingId);
            Log.d("mynotification", "bookingId passed: " + bookingId);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder notificationBuilder;

        // Check Android version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "my_channel_id";
            String channelName = "my_Channel";
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // Create notification channel if it doesn't exist
            NotificationChannel notificationChannel = new NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationChannel.setDescription("my_Channel");
            notificationManager.createNotificationChannel(notificationChannel);

            // Use the channel ID for the notification builder
            notificationBuilder = new NotificationCompat.Builder(this, channelId);
        } else {
            notificationBuilder = new NotificationCompat.Builder(this, "my_channel_id");
        }

        // Build the notification
        notificationBuilder
                .setSmallIcon(R.drawable.baseline_circle_notifications_24)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Check permission for posting notifications (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                Log.d("mynotification", "Permission not granted for posting notifications");
                return;
            }
        }

        // Show the notification
        notificationManager.notify(0, notificationBuilder.build());
        Log.d("mynotification", "Notification shown: " + title);
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
