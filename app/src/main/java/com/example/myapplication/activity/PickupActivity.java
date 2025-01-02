package com.example.myapplication.activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class PickupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup); // Ensure this matches your layout XML file name

        // Set up distance and pickup details
        setupUI();
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
    }

    private void setupUI() {
        // Cast the views to TextView before calling setText()
        TextView distanceTextView = findViewById(R.id.distanceTextView);
        TextView timeTextView = findViewById(R.id.timeTextView);
        TextView pickupLocationTextView = findViewById(R.id.pickupLocationTextView);

        // Set text for the TextViews
        if (distanceTextView != null) {
            distanceTextView.setText("Distance: 2km");
        }
        if (timeTextView != null) {
            timeTextView.setText("Your destination is 3 min away");
        }
        if (pickupLocationTextView != null) {
            pickupLocationTextView.setText("Panniyankara, Kozhikode, Kerala");
        }
    }
}
