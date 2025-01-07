package com.example.myapplication.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.myapplication.R;
import com.example.myapplication.models.response.Booking;
import com.example.myapplication.network.ApiService;
import com.example.myapplication.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PartnerBookingRequestActivity extends AppCompatActivity {

    // UI elements
    private TextView tvCarBrandModel, tvCarRent, tvPickupLocation, tvReturnLocation, tvPickupTime, tvReturnTime;
    private Button btnAccept, btnReject;

    // API Service for network calls
    private ApiService apiService;
    private String bookingId;

    // Request code for notification permission
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_booking_request);

        // Check and request notification permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_REQUEST_CODE);
            }
        }

        // Initialize UI elements
        tvCarBrandModel = findViewById(R.id.tvCarBrandModel);
        tvCarRent = findViewById(R.id.tvCarRent);
        tvPickupLocation = findViewById(R.id.tvPickupLocation);
        tvReturnLocation = findViewById(R.id.tvReturnLocation);
        tvPickupTime = findViewById(R.id.tvPickupTime);
        tvReturnTime = findViewById(R.id.tvReturnTime);
        btnAccept = findViewById(R.id.btnAccept);
        btnReject = findViewById(R.id.btnReject);

        // Get booking ID from intent (this could be passed from the previous activity)
        bookingId = getIntent().getStringExtra("bookingId");

        // Initialize Retrofit and API Service
        Retrofit retrofit = RetrofitClient.getRetrofitInstance(this);
        apiService = retrofit.create(ApiService.class);

        // Fetch booking details from the server
        fetchBookingDetails();

        // Handle Accept button click
        btnAccept.setOnClickListener(view -> {
            // Handle accept action
//            updateBookingStatus("Accepted");
        });

        // Handle Reject button click
        btnReject.setOnClickListener(view -> {
            // Handle reject action
//            updateBookingStatus("Rejected");
        });
    }

    // Method to fetch booking details from the API
    private void fetchBookingDetails() {
        Call<Booking> call = apiService.getBookingDetails(bookingId);

        call.enqueue(new Callback<Booking>() {
            @Override
            public void onResponse(Call<Booking> call, Response<Booking> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Booking booking = response.body();

                    // Set the booking data to the UI elements
                    tvCarBrandModel.setText("Brand Model: " + booking.getCarModel());
                    tvCarRent.setText("Rent Per Day: $" + booking.getRentPerDay());
                    tvPickupLocation.setText("Pickup Location: " + booking.getPickUpLocation());
                    tvReturnLocation.setText("Return Location: " + booking.getReturnLocation());
                    tvPickupTime.setText("Pickup Time: " + booking.getPickUpTime());
                    tvReturnTime.setText("Return Time: " + booking.getReturnTime());
                } else {
                    Toast.makeText(PartnerBookingRequestActivity.this, "Failed to fetch booking details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Booking> call, Throwable t) {
                Toast.makeText(PartnerBookingRequestActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to update the booking status (accept/reject)
//    private void updateBookingStatus(String status) {
//        // Create the request to update the booking status
//        Call<Void> updateCall = apiService.updateBookingStatus(bookingId, status);
//
//        updateCall.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.isSuccessful()) {
//                    Toast.makeText(PartnerBookingRequestActivity.this, "Booking " + status, Toast.LENGTH_SHORT).show();
//                    // Optionally, you can close the activity after accepting/rejecting the booking
//                    // finish();
//                } else {
//                    Toast.makeText(PartnerBookingRequestActivity.this, "Failed to update booking status", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Toast.makeText(PartnerBookingRequestActivity.this, "Error updating booking status", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    // Override this method to handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                Toast.makeText(this, "Notification Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                // Permission denied
                Toast.makeText(this, "Notification Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
