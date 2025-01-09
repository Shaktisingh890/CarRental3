package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.response.Notification;
import com.example.myapplication.activity.PartnerBookingRequestActivity;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private static final String TAG = "NotificationAdapter"; // Debug tag for logs
    private List<Notification> notifications;
    private Context context; // Context to start the activity

    // Constructor
    public NotificationAdapter(Context context, List<Notification> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
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
            Log.d(TAG, "onBindViewHolder: Notification at position " + position + " is read (dimmed).");
        } else {
            holder.itemView.setAlpha(1.0f); // Normal effect
            Log.d(TAG, "onBindViewHolder: Notification at position " + position + " is unread (normal).");
        }

        // Handle click to mark as read/unread based on notification ID
        holder.itemView.setOnClickListener(v -> {
            if (!notification.isRead()) { // Only process if unread
                // Mark the notification as read
                notification.setRead(true);
                notifyItemChanged(position); // Refresh this item in the adapter

                // Debug log for click action
                Log.d("myapp1", "onBindViewHolder: Notification clicked at position " + position +
                        ". Marked as read and updated.");
            } else {
                // Optionally, if you want to toggle between read/unread
                notification.setRead(false); // Mark as unread
                notifyItemChanged(position); // Refresh this item in the adapter

                Log.d("myapp1", "onBindViewHolder: Notification clicked at position " + position +
                        ". Marked as unread and updated.");
            }

            String bookingId = notification.getBookingId();

            // Log and pass the bookingId if it exists
            if (bookingId != null) {
                Log.d("myapp1", "onBindViewHolder: Booking ID found: " + bookingId);
                // Send the bookingId to PartnerBookingRequestActivity
                Intent intent = new Intent(context, PartnerBookingRequestActivity.class);
                intent.putExtra("bookingId", bookingId);
                context.startActivity(intent);
            } else {
                Log.d("myapp1", "onBindViewHolder: No bookingId found in SharedPreferences.");
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: Total notifications = " + notifications.size()); // Debug log
        return notifications.size();
    }

    // ViewHolder Class
    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView title, body;

        public NotificationViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notification_title);
            body = itemView.findViewById(R.id.notification_body);
            Log.d(TAG, "NotificationViewHolder: ViewHolder created."); // Debug log
        }
    }
}
