package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.models.response.BookingDetailsResponse;
import com.example.myapplication.network.ApiService;
import com.example.myapplication.network.RetrofitClient;
import com.example.myapplication.utils.MyFirebaseMessagingService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeclineRideActivity extends AppCompatActivity {

    private RadioGroup rgReasons;
    private EditText etCustomReason;
    private Button btnCancel, btnSubmitReason;

    private String notificationId,driverStatus,bookingId;
    ImageView tvprofileimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decline_ride);

        Button btnSubmitReason= findViewById(R.id.btnSubmitReason);

        // Find the backArrow ImageView
        ImageView backArrow = findViewById(R.id.backArrow);

        bookingId = getIntent().getStringExtra("bookingId");
        notificationId=getIntent().getStringExtra("notification_id");

        fetchBookingDetails(bookingId);

        // Set a click listener to handle the back navigation
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the previous page
                finish();
            }
        });

        // Find the Accept Button
        Button btnCancel = findViewById(R.id.btnCancel);

        // Set a click listener for the Accept button
        btnCancel.setOnClickListener(view -> {
            updateDriverStatus(bookingId, "accepted", "booked", RideDetailsActivity.class);
        });

        btnSubmitReason.setOnClickListener(view -> {
            updateDriverStatus(bookingId, "rejected", "cancelled", DriverDashboardActivity.class);
            MyFirebaseMessagingService myservice= new MyFirebaseMessagingService();
            myservice.deleteNotificationByIdFromBackend(this,notificationId);

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


                        if (driverStatus != null && (driverStatus.equals("confirmed") || driverStatus.equals("rejected"))) {
                            // If the status is already confirmed or rejected, skip the status update and directly navigate to Partner_DriverListActivity
                            Intent intent = new Intent(DeclineRideActivity.this, RideDetailsActivity.class);
                            intent.putExtra("bookingId", bookingId);
                            intent.putExtra("notification_id", notificationId);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(DeclineRideActivity.this, "No booking details available", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DeclineRideActivity.this, "Failed to fetch booking details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BookingDetailsResponse> call, Throwable t) {
                Toast.makeText(DeclineRideActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateDriverStatus(String bookingId, String driverStatus, String status, Class<?> nextActivity) {
        ApiService apiService = RetrofitClient.getRetrofitInstance(this).create(ApiService.class);

        // Call the API to update the booking status
        Call<Void> call = apiService.updateDriverStatus(bookingId,driverStatus, status);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DeclineRideActivity.this, "Status updated successfully", Toast.LENGTH_SHORT).show();

                    // Redirect to the specified activity
                    Intent intent = new Intent(DeclineRideActivity.this, nextActivity);
                    intent.putExtra("bookingId",bookingId);
                    intent.putExtra("notification_id",notificationId);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(DeclineRideActivity.this, "Failed to update status", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(DeclineRideActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
