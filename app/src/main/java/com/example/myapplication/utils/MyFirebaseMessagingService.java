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
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.myapplication.R;
import com.example.myapplication.activity.MainActivity;
import com.example.myapplication.activity.PartnerBookingRequestActivity;
import com.example.myapplication.activity.PartnerNotificationActivity;
import com.example.myapplication.models.response.Notification;
import com.example.myapplication.network.ApiService;
import com.example.myapplication.network.RetrofitClient;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Retrofit;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String PREFS_NAME = "com.example.app.PREFERENCES";
    public static final String NOTIFICATIONS_KEY = "notifications";
    private static final String TAG = "MyFirebaseMessagingService";

    private String title, body;
    private String bookingId, click_action;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceived called");

        // Handle notification payload
        if (remoteMessage.getNotification() != null) {
            title = remoteMessage.getNotification().getTitle();
            body = remoteMessage.getNotification().getBody();
            Log.d(TAG, "Received notification - Title: " + title + ", Body: " + body);
        } else {
            Log.d(TAG, "Notification payload is null.");
        }

        if (remoteMessage.getData().size() > 0) {
            bookingId = remoteMessage.getData().get("bookingId");
            click_action = remoteMessage.getData().get("click_action");
            Log.d(TAG, "Data payload - Booking ID: " + bookingId + ", Click Action: " + click_action);
        }

        // Store notification in SharedPreferences
        // Store notification in the backend
        storeNotificationInBackend(title, body, bookingId);

        // Show notification with permission check
        if (checkNotificationPermission()) {
            showNotification(title, body, click_action, bookingId);
        }
    }

    private void storeNotificationInBackend(String title, String body, String bookingId) {
        // Create a Notification object
        String uniqueId = UUID.randomUUID().toString();
        Notification notification = new Notification(uniqueId,title,body, false,bookingId);
        Log.d("my notification","title"+title);
        Log.d("my notification","body"+body);

        Log.d("my notification","bookingId");




        // Get an instance of Retrofit and the ApiService
        Retrofit retrofit = RetrofitClient.getRetrofitInstance(getApplicationContext());
        ApiService apiService = retrofit.create(ApiService.class);

        // Make the API call
        Call<Void> call = apiService.storeNotification(notification);
        call.enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Notification stored successfully in the backend.");
                } else {
                    Log.e(TAG, "Failed to store notification. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Error storing notification in backend: " + t.getMessage(), t);
            }
        });
    }


    private boolean checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Notification permission not granted.");
                return false;
            }
        }
        return true;
    }

    private void showNotification(String title, String body, String clickAction, String bookingId) {
        Intent intent;

        // Choose target activity based on click_action
        if (clickAction != null) {
            switch (clickAction) {
                case "OPEN_PARTNER_BOOKING_REQUEST":
                    intent = new Intent(this, PartnerBookingRequestActivity.class);
                    break;
                case "open_partner_notification":
                    intent = new Intent(this, PartnerNotificationActivity.class);
                    break;
                default:
                    intent = new Intent(this, MainActivity.class);
                    break;
            }
        } else {
            intent = new Intent(this, MainActivity.class);
        }

        // Pass bookingId to the target activity if available
        if (bookingId != null) {
            intent.putExtra("bookingId", bookingId);
            Log.d(TAG, "Passing bookingId: " + bookingId);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder notificationBuilder;

        // Create a notification channel for Android O+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "my_channel_id";
            String channelName = "My Channel";
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationChannel notificationChannel = new NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationChannel.setDescription("Notifications for app");
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }

            notificationBuilder = new NotificationCompat.Builder(this, channelId);
        } else {
            notificationBuilder = new NotificationCompat.Builder(this, "my_channel_id");
        }

        // Build and display the notification
        notificationBuilder
                .setSmallIcon(R.drawable.baseline_circle_notifications_24)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            notificationManager.notify(0, notificationBuilder.build());
            Log.d(TAG, "Notification shown successfully: " + title);
        }
    }

    // Method to delete all notifications
    public void deleteAllNotificationsFromBackend(Context context) {
        // Get an instance of Retrofit and the ApiService
        Retrofit retrofit = RetrofitClient.getRetrofitInstance(context);
        ApiService apiService = retrofit.create(ApiService.class);

        // Make the API call
        Call<Void> call = apiService.deleteAllNotifications();
        call.enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "All notifications deleted successfully from the backend.");
                } else {
                    Log.e(TAG, "Failed to delete all notifications. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Error deleting all notifications from backend: " + t.getMessage(), t);
            }
        });
    }

    // Method to delete a specific notification by ID
    public void deleteNotificationByIdFromBackend(Context context,String notificationId) {
        // Get an instance of Retrofit and the ApiService
        ApiService apiService = RetrofitClient.getRetrofitInstance(context).create(ApiService.class);

        // Make the API call
        Call<Void> call = apiService.deleteNotificationById(notificationId);
        call.enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Notification deleted successfully from the backend. ID: " + notificationId);
                } else {
                    Log.e(TAG, "Failed to delete notification. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Error deleting notification with ID " + notificationId + " from backend: " + t.getMessage(), t);
            }
        });
    }

}
