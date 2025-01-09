package com.example.myapplication.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.models.response.BookingDetailsResponse;
import com.example.myapplication.network.ApiService;
import com.example.myapplication.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PartnerBookingRequestActivity extends AppCompatActivity {

    private TextView tvCarDetails, tvCarBrandModel, tvCarRent;
    private TextView tvBookingDetails, tvPickupLocation, tvReturnLocation, tvPickupTime, tvReturnTime;
    private Button btnAccept, btnReject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_booking_request); // Use your XML layout here

        // Initialize views
        tvCarDetails = findViewById(R.id.tvCarDetails);
        tvCarBrandModel = findViewById(R.id.tvCarBrandModel);
        tvCarRent = findViewById(R.id.tvCarRent);
        tvBookingDetails = findViewById(R.id.tvBookingDetails);
        tvPickupLocation = findViewById(R.id.tvPickupLocation);
        tvReturnLocation = findViewById(R.id.tvReturnLocation);
        tvPickupTime = findViewById(R.id.tvPickupTime);
        tvReturnTime = findViewById(R.id.tvReturnTime);
        btnAccept = findViewById(R.id.btnAccept);
        btnReject = findViewById(R.id.btnReject);

        // Get the bookingId from the intent extras
        String bookingId = getIntent().getStringExtra("bookingId");

        // Fetch booking details
        fetchBookingDetails(bookingId);

        // Handle Accept button click
        btnAccept.setOnClickListener(view -> {
            // Handle accept booking logic here
            Toast.makeText(PartnerBookingRequestActivity.this, "Booking Accepted", Toast.LENGTH_SHORT).show();
        });

        // Handle Reject button click
        btnReject.setOnClickListener(view -> {
            // Handle reject booking logic here
            Toast.makeText(PartnerBookingRequestActivity.this, "Booking Rejected", Toast.LENGTH_SHORT).show();
        });
    }

    private void fetchBookingDetails(String bookingId) {
        ApiService apiService = RetrofitClient.getRetrofitInstance(this).create(ApiService.class);

        // Call the API to get booking details by bookingId
        Call<BookingDetailsResponse> call = apiService.getBookingDetails1(bookingId);
        call.enqueue(new Callback<BookingDetailsResponse>() {
            @Override
            public void onResponse(Call<BookingDetailsResponse> call, Response<BookingDetailsResponse> response) {
                if (response.isSuccessful()) {
                    BookingDetailsResponse bookingDetailsResponse = response.body();
                    if (bookingDetailsResponse != null) {
                        // Populate the UI with the booking details
                        tvCarDetails.setText("Car Details");
                        tvCarBrandModel.setText("Brand Model: " + bookingDetailsResponse.getData().getCarName()+"" +bookingDetailsResponse.getData().getCarModel());
                        tvCarRent.setText("Rent Per Day: $" + bookingDetailsResponse.getData().getPricePerDay());

                        tvBookingDetails.setText("Booking Details");
                        tvPickupLocation.setText("Pickup Location: " + bookingDetailsResponse.getData().getPickupLocation());
                        tvReturnLocation.setText("Return Location: " + bookingDetailsResponse.getData().getDropoffLocation());
                        tvPickupTime.setText("Pickup Time: " + bookingDetailsResponse.getData().getStartDate());
                        tvReturnTime.setText("Return Time: " + bookingDetailsResponse.getData().getEndDate());
                    } else {
                        Toast.makeText(PartnerBookingRequestActivity.this, "No booking details available", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PartnerBookingRequestActivity.this, "Failed to fetch booking details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BookingDetailsResponse> call, Throwable t) {
                Toast.makeText(PartnerBookingRequestActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
