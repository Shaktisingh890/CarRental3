package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class MyRideDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ride_details);

        // Back button functionality
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> {
            // Finish the current activity
            finish();
        });

        // Contact Us button functionality
        Button contactButton = findViewById(R.id.contactButton);
        contactButton.setOnClickListener(view -> {
            // Example: Show a toast message or redirect to another activity
            Toast.makeText(MyRideDetailsActivity.this, "Contact Us clicked", Toast.LENGTH_SHORT).show();

            // Uncomment to navigate to another activity
            // Intent intent = new Intent(RideDetailsActivity.this, ContactUsActivity.class);
            // startActivity(intent);
        });
    }
}
