package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.models.response.BookingResponse;

public class BookingSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_success);

        // Initialize views
        TextView bookingStatus = findViewById(R.id.bookingStatus);
        TextView carName = findViewById(R.id.carName);
        TextView pickupLocation = findViewById(R.id.pickupLocation);
        TextView tripDates = findViewById(R.id.tripDates);
        TextView rentalFees = findViewById(R.id.rentalFees);
        TextView totalFees = findViewById(R.id.totalFees);
        Button backToHomeBtn = findViewById(R.id.backToHomeBtn);
        BookingResponse bookingResponse = getIntent().getParcelableExtra("BOOKING_RESPONSE");


        // Set values for each field dynamically with better formatting
        bookingStatus.setText("Booking Status: " + bookingResponse.getData().getPaymentStatus());
        carName.setText(bookingResponse.getData().getCarData().getBrand() + " " + bookingResponse.getData().getCarData().getModel());
        pickupLocation.setText("Pick-up and Return:\n" +
                "Pick-up: " + bookingResponse.getData().getPickupLocation() + "\n" +
                "Drop-off: " + bookingResponse.getData().getDropoffLocation());
        tripDates.setText("Trip Dates:\n" +
                "Start: " + bookingResponse.getData().getStartDate() + "\n" +
                "End: " + bookingResponse.getData().getEndDate());
        rentalFees.setText("Rental Fees:\n" +
                "One Day Rent: $" + bookingResponse.getData().getCarData().getPricePerDay() + "\n" +
                "Duration: " + bookingResponse.getData().getDurationInDays() + " day(s)");
        totalFees.setText("Total Fees: $" + bookingResponse.getData().getTotalAmount());


        // Button to go back to home
        backToHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to the main screen (Home)
                Intent intent = new Intent(BookingSuccessActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
