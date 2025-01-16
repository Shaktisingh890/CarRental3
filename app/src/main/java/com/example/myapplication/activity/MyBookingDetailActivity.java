package com.example.myapplication.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class MyBookingDetailActivity extends AppCompatActivity {

    private LinearLayout layoutCarDetails, layoutDriverDetails, layoutPaymentInfo;
    private TextView tvCarDetails, tvDriverDetails, tvPaymentInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_booking_detail);

        // Initialize layouts and text views
        layoutCarDetails = findViewById(R.id.layoutCarDetails);
        layoutDriverDetails = findViewById(R.id.layoutDriverDetails);
        layoutPaymentInfo = findViewById(R.id.layoutPaymentInfo);

        tvCarDetails = findViewById(R.id.tvCarDetails);
        tvDriverDetails = findViewById(R.id.tvDriverDetails);
        tvPaymentInfo = findViewById(R.id.tvPaymentInfo);

        // Toggle Car Details Accordion
        tvCarDetails.setOnClickListener(v -> toggleVisibility(layoutCarDetails));

        // Toggle Driver Details Accordion
        tvDriverDetails.setOnClickListener(v -> toggleVisibility(layoutDriverDetails));

        // Toggle Payment Info Accordion
        tvPaymentInfo.setOnClickListener(v -> toggleVisibility(layoutPaymentInfo));

        // Cancel Booking Button
        findViewById(R.id.btnCancelBooking).setOnClickListener(v ->
                Toast.makeText(this, "Booking Cancelled", Toast.LENGTH_SHORT).show()
        );

        // Download Invoice Button
        findViewById(R.id.btnDownloadInvoice).setOnClickListener(v ->
                Toast.makeText(this, "Invoice Downloaded", Toast.LENGTH_SHORT).show()
        );

        // Example logic to update status
        updateBookingStatus("Ongoing");
    }

    private void toggleVisibility(LinearLayout layout) {
        if (layout.getVisibility() == View.GONE) {
            layout.setVisibility(View.VISIBLE);
        } else {
            layout.setVisibility(View.GONE);
        }
    }

    private void updateBookingStatus(String status) {
        View viewPending = findViewById(R.id.viewPending);
        View viewOngoing = findViewById(R.id.viewOngoing);
        View viewBooked = findViewById(R.id.viewBooked);
        View viewCompleted = findViewById(R.id.viewCompleted);

        // Reset all
        resetStatus(viewPending);
        resetStatus(viewOngoing);
        resetStatus(viewBooked);
        resetStatus(viewCompleted);

        // Highlight based on status
        switch (status) {
            case "Pending":
                highlightStatus(viewPending);
                break;
            case "Ongoing":
                highlightStatus(viewOngoing);
                break;
            case "Booked":
                highlightStatus(viewBooked);
                break;
            case "Completed":
                highlightStatus(viewCompleted);
                break;
        }
    }

    private void resetStatus(View view) {
        view.setBackgroundResource(R.drawable.status_circle_pending); // Default style
    }

    private void highlightStatus(View view) {
        view.setBackgroundResource(R.drawable.status_circle_ongoing); // Active style
    }
}
