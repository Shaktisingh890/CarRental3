package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;

public class RideRequestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_request);

        // Find the backArrow ImageView
        ImageView backArrow = findViewById(R.id.backArrow);

        // Set a click listener to handle the back navigation
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the previous page
                finish();
            }
        });
        // Find the Accept Button
        Button acceptButton = findViewById(R.id.acceptButton);

        // Set a click listener for the Accept button
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to RideDetailsActivity
                Intent intent = new Intent(RideRequestActivity.this, RideDetailsActivity.class);
                startActivity(intent);
            }
        });
    }
}