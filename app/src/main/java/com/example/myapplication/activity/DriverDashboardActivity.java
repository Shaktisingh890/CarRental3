package com.example.myapplication.activity;

import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class DriverDashboardActivity extends AppCompatActivity {

    private Switch availabilitySwitch;
    private TextView subtitleMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_dashboard);

        // Initialize Views
        availabilitySwitch = findViewById(R.id.onlineOfflineSwitch);


        // Set Switch Listener
        availabilitySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                availabilitySwitch.setText("Online");
                subtitleMessage.setText("You are now available to take rides.");
                Toast.makeText(this, "You are Online", Toast.LENGTH_SHORT).show();
            } else {
                availabilitySwitch.setText("Offline");
                subtitleMessage.setText("Go Offline for taking rest");
                Toast.makeText(this, "You are Offline", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
