package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activity.BookingCancelledReasonActivity;
import com.example.myapplication.activity.DriverBookingRequestActivity;
import com.example.myapplication.activity.DriverDashboardActivity;
import com.example.myapplication.activity.PartnerBookingRequestActivity;
import com.example.myapplication.activity.Partner_DriverListActivity;
import com.example.myapplication.activity.RideDetailsActivity;
import com.example.myapplication.activity.RideRequestActivity;
import com.example.myapplication.models.response.BookingDetailsResponse;
import com.example.myapplication.models.response.Notification;
import com.example.myapplication.network.ApiService;
import com.example.myapplication.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverNotificationAdapter extends RecyclerView.Adapter<DriverNotificationAdapter.NotificationViewHolder> {

    private static final String TAG = "NotificationAdapter"; // Debug tag for logs
    private List<Notification> notifications;
    private Context context; // Context to start the activity

    // Constructor
    public DriverNotificationAdapter(Context context, List<Notification> notifications) {
        this.context = context;
        this.notifications = notifications;

        // Log notifications for debugging during initialization
        logNotifications("NotificationAdapter initialized");
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.driver_notification_item, parent, false);
        Log.d(TAG, "onCreateViewHolder: View created for position " + viewType); // Debug log
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        // Get the notification at the given position
        Notification notification = notifications.get(position);

        // Set title and body text
        holder.title.setText(notification.getTitle());
        holder.body.setText(notification.getBody());

        // Debug log for binding data
        Log.d(TAG, "onBindViewHolder: Binding notification at position " + position +
                " with title: " + notification.getTitle() +
                " and isRead: " + notification.isRead());

        // Apply dim effect for read notifications
        if (notification.isRead()) {
            holder.itemView.setAlpha(0.5f); // Dim effect
        } else {
            holder.itemView.setAlpha(1.0f); // Normal effect
        }

        // Handle click to mark as read/unread based on notification ID
        holder.itemView.setOnClickListener(v -> {
            if (!notification.isRead()) { // Only process if unread
                notification.setRead(true);
                notifyItemChanged(position); // Refresh this item in the adapter
                Log.d(TAG, "onBindViewHolder: Marked notification as read at position " + position);
            } else {
                notification.setRead(false); // Mark as unread
                notifyItemChanged(position); // Refresh this item in the adapter
                Log.d(TAG, "onBindViewHolder: Marked notification as unread at position " + position);
            }

            String bookingId = notification.getBookingId();
            String notification_id=notification.getId();

            // Log and pass the bookingId if it exists
            if (bookingId != null) {
                Log.d(TAG, "onBindViewHolder: Booking ID found: " + bookingId);
                fetchBookingDetails(bookingId, position,notification_id);
            } else {
                Log.d(TAG, "onBindViewHolder: No bookingId found for notification at position " + position);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: Total notifications = " + notifications.size()); // Debug log
        return notifications.size();
    }

    // Helper method to log all notifications
    private void logNotifications(String message) {
        Log.d(TAG, message + ": Total notifications = " + notifications.size());
        for (int i = 0; i < notifications.size(); i++) {
            Notification notification = notifications.get(i);
            Log.d(TAG, "Notification " + i + ": " +
                    "Title = " + notification.getTitle() + ", " +
                    "Body = " + notification.getBody() + ", " +
                    "isRead = " + notification.isRead() + ", " +
                    "Booking ID = " + notification.getBookingId());
        }
    }

    // Call this method externally if notifications are updated
    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
        logNotifications("Notifications updated");
        notifyDataSetChanged(); // Notify adapter of data changes
    }

    private void fetchBookingDetails(String bookingId, int position,String notification_id) {
        ApiService apiService = RetrofitClient.getRetrofitInstance(context.getApplicationContext()).create(ApiService.class);

        // Call the API to get booking details by bookingId
        Call<BookingDetailsResponse> call = apiService.getBookingDetails1(bookingId);
        call.enqueue(new Callback<BookingDetailsResponse>() {
            @Override
            public void onResponse(Call<BookingDetailsResponse> call, Response<BookingDetailsResponse> response) {
                if (response.isSuccessful()) {
                    BookingDetailsResponse bookingDetailsResponse = response.body();
                    if (bookingDetailsResponse != null) {
                        String driverStatus = bookingDetailsResponse.getData().getDriverStatus();
                        Log.d(TAG, "fetchBookingDetails: Partner Status = " + driverStatus);

                        // Check partner status
                        if ("accepted".equalsIgnoreCase(driverStatus)) {
                            // Redirect to PartnerDriverListActivity if partner status is confirmed
                            Intent intent = new Intent(context, DriverDashboardActivity.class);
                            intent.putExtra("bookingId", bookingId); // Pass bookingId to the next activity
                            intent.putExtra("notification_id", notification_id);
                            Toast.makeText(context, "Already Accepted This Booking , Check Your Bookings", Toast.LENGTH_SHORT).show();

                            context.startActivity(intent);
                        } else if ("rejected".equalsIgnoreCase(driverStatus)) {
                            // If the partner status is rejected, redirect to BookingCancelledReasonActivity
                            Intent intent = new Intent(context, BookingCancelledReasonActivity.class);
                            intent.putExtra("bookingId", bookingId);
                            intent.putExtra("notification_id", notification_id);
                            Toast.makeText(context, "Rejected This Booking , Check Your Bookings", Toast.LENGTH_SHORT).show();

                            context.startActivity(intent);
                        } else {
                            // If the partner status is not confirmed or rejected, continue to the booking request activity
                            Intent intent = new Intent(context, RideRequestActivity.class);
                            intent.putExtra("bookingId", bookingId);
                            intent.putExtra("notification_id", notification_id);
                            context.startActivity(intent);
                        }
                    } else {
                        Toast.makeText(context, "No booking details available", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Failed to fetch booking details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BookingDetailsResponse> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ViewHolder Class
    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView title, body;

        public NotificationViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notification_title);
            body = itemView.findViewById(R.id.notification_body);
        }
    }
}

