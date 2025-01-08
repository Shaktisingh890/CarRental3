package com.example.myapplication.activity;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.adapter.NotificationAdapter;
import com.example.myapplication.models.response.Notification;
import java.util.ArrayList;
import java.util.List;

public class PartnerNotificationActivity extends AppCompatActivity {
    private ImageView backIcon;
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<Notification> notificationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_notification);
        backIcon = findViewById(R.id.backIcon);
        recyclerView = findViewById(R.id.notificationRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
       // Back button functionality
        backIcon.setOnClickListener(v -> finish());
        // Populate the notifications list
        notificationList = new ArrayList<>();
        notificationList.add(new Notification("40% off today", "Hurry Up! Don’t sit at home today. Best car rental deal today.", "3 min ago"));
        notificationList.add(new Notification("Booking successful", "Congratulation. Your Audi AB L booked successfully.", "3 min ago"));

        adapter = new NotificationAdapter(notificationList);
        recyclerView.setAdapter(adapter);
    }
}
