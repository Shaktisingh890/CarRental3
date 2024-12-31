package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.models.response.CustomerCarResponse;

import java.util.List;

public class ConfirmationActivity extends AppCompatActivity {

    private static final String TAG = "ConfirmationActivity";  // Tag for logging

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        // Initialize UI components
        ImageView backIcon = findViewById(R.id.backIcon);
        Button confirmButton = findViewById(R.id.confirmButton);
        TextView carNameTextView = findViewById(R.id.car_name);
        TextView carPriceTextView = findViewById(R.id.car_price);
        TextView pickupLocationTextView = findViewById(R.id.pickup_location);
        TextView returnLocationTextView = findViewById(R.id.return_location);
        TextView pickupDateTextView = findViewById(R.id.pickup_date);
        TextView returnDateTextView = findViewById(R.id.return_date);
        TextView totalRentTextView = findViewById(R.id.total_rent);
        TextView totalPaymentTextView = findViewById(R.id.total_payment);
        TextView perDayPriceTextView = findViewById(R.id.perday_price);

        // Get data from the Intent
        Intent intent = getIntent();
        CustomerCarResponse selectedCar = intent.getParcelableExtra("SELECTED_CAR");
        String pickUpLocation = intent.getStringExtra("pickupLocation");
        String returnLocation = intent.getStringExtra("returnLocation");
        String pickUpDateTime = intent.getStringExtra("pickupDateTime");
        String returnDateTime = intent.getStringExtra("returnDateTime");

        // Log the received data for debugging
        Log.d(TAG, "Received car data: " + selectedCar);
        Log.d(TAG, "Pick-up Location: " + pickUpLocation);
        Log.d(TAG, "Return Location: " + returnLocation);
        Log.d(TAG, "Pick-up DateTime: " + pickUpDateTime);
        Log.d(TAG, "Return DateTime: " + returnDateTime);

        // Set data to UI components
        if (selectedCar != null) {
            carNameTextView.setText(selectedCar.getBrand());
            carPriceTextView.setText("$" + selectedCar.getPricePerDay());
        }
        pickupLocationTextView.setText(pickUpLocation);
        returnLocationTextView.setText(returnLocation);
        pickupDateTextView.setText(pickUpDateTime);
        returnDateTextView.setText(returnDateTime);

        ImageView carImage0 = findViewById(R.id.car_image0);
        // Set car images using Glide
        List<String> carImages = selectedCar.getImages();
        if (carImages != null && !carImages.isEmpty()) {
            if (carImages.size() > 0) {
                Glide.with(this)
                        .load(carImages.get(0))
                        .placeholder(R.drawable.profile)
                        .error(R.drawable.profile)
                        .into(carImage0);
            } else {
                carImage0.setImageResource(R.drawable.profile);
            }

            // Calculate total rent and payment (Assume a fixed duration for simplicity)
            int rentPerDay = selectedCar != null ? selectedCar.getPricePerDay() : 30;  // Example: default to $30 if no car data
            int totalRent = rentPerDay * 2;  // Example: Assuming a 2-day rent

            totalRentTextView.setText("Total 2 day rent: $" + totalRent);
            totalPaymentTextView.setText("Total payment: $" + totalRent);
            perDayPriceTextView.setText("Per day car rent: $" + rentPerDay);

            // Back button functionality
            backIcon.setOnClickListener(v -> finish());

            // Confirm button functionality - Now opens PaymentActivity directly
            confirmButton.setOnClickListener(v -> {

                // Pass car data and total amount to PaymentActivity
                Intent paymentIntent = new Intent(ConfirmationActivity.this, PaymentActivity.class);
                paymentIntent.putExtra("totalAmount", totalRent);  // Pass the total amount
                paymentIntent.putExtra("carDetails", selectedCar);  // Pass selected car details
                startActivity(paymentIntent);
                finish();
            });
        }
    }
}
