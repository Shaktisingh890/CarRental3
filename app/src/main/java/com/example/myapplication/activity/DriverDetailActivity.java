package com.example.myapplication.activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.network.ApiService;
import com.example.myapplication.network.RetrofitClient;
import com.example.myapplication.utils.MyFirebaseMessagingService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverDetailActivity extends AppCompatActivity {
    private TextView tvDriverName, tvDriverPhone, tvAvailabilityStatus, tvDriverEmail, tvDriverLicenseNumber, tvDriverLicenseExpiry;
    private ImageView imgDriver, frontPhotoImageView, backPhotoImageView;

    private String driverId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_detail);

        tvDriverName = findViewById(R.id.tvDriverName);
        tvDriverPhone = findViewById(R.id.tvDriverPhone);
        tvDriverEmail = findViewById(R.id.tvDriverEmail);
        tvDriverLicenseNumber = findViewById(R.id.tvDriverLicenseNumber);
        tvDriverLicenseExpiry = findViewById(R.id.tvDriverLicenseExpiry);
        frontPhotoImageView = findViewById(R.id.frontPhotoImageView);
        backPhotoImageView = findViewById(R.id.backPhotoImageView);
        tvAvailabilityStatus = findViewById(R.id.tvAvailabilityStatus);
        imgDriver = findViewById(R.id.imgDriver);

        // Get data from intent
        String driverName = getIntent().getStringExtra("driverName");
        String driverPhone = getIntent().getStringExtra("driverPhone");
        String driverEmail = getIntent().getStringExtra("driverEmail");
        String driverLicenseNumber = getIntent().getStringExtra("driverLicenseNumber");
        String driverLicenseExpiry = getIntent().getStringExtra("driverLicenseExpiry");
        String frontPhotoImageUrl = getIntent().getStringExtra("frontPhotoImageView");
        String backPhotoImageUrl = getIntent().getStringExtra("backPhotoImageView");
        String driverImgUrl = getIntent().getStringExtra("driverImgUrl");

        boolean availabilityStatus = getIntent().getBooleanExtra("availabilityStatus", false);

        // Set data to views
        tvDriverName.setText(driverName);
        tvDriverPhone.setText("Phone: " + driverPhone);
        tvDriverEmail.setText(driverEmail);
        tvDriverLicenseNumber.setText(driverLicenseNumber);
        tvDriverLicenseExpiry.setText(driverLicenseExpiry);

        driverId = getIntent().getStringExtra("driverId");
               Log.d("driverid","driver "+driverId);

        // Load Front and Back Photos using Glide or any image loading library
        Glide.with(this)
                .load(frontPhotoImageUrl) // URL or File path
                .placeholder(R.drawable.round_document_scanner_24) // Placeholder image while loading
                .into(frontPhotoImageView); // Corrected this line

        Glide.with(this)
                .load(backPhotoImageUrl) // URL or File path
                .placeholder(R.drawable.round_document_scanner_24) // Placeholder image while loading
                .into(backPhotoImageView); // Corrected this line

        tvAvailabilityStatus.setText(availabilityStatus ? "Available" : "Unavailable");
        tvAvailabilityStatus.setTextColor(
                availabilityStatus ? getResources().getColor(R.color.green) : getResources().getColor(R.color.red)
        );

        Glide.with(this)
                .load(driverImgUrl)
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .into(imgDriver);

        // Handle button clicks (Java way)
        Button assignDriverButton = findViewById(R.id.assignDriverButton);
        assignDriverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFirebaseMessagingService myService = new MyFirebaseMessagingService();

                String bookingId = getIntent().getStringExtra("bookingId");


                String notification_id= getIntent().getStringExtra("notification_id");
                Log.d("my","kjjk"+notification_id);
                if (bookingId != null && !bookingId.isEmpty()) {
                    assignDriver(bookingId,driverId);
                    myService.deleteNotificationByIdFromBackend(getApplicationContext(),notification_id);
                } else {
                    Toast.makeText(DriverDetailActivity.this, "Booking ID is missing!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to previous activity
                onBackPressed();
            }
        });

        // Open full-screen photo when clicked
        frontPhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFullScreenImageDialog(frontPhotoImageUrl);
            }
        });

        backPhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFullScreenImageDialog(backPhotoImageUrl);
            }
        });
    }


    // Method to update driver status
    private void assignDriver(String bookingId,String driverId) {
        ApiService apiService = RetrofitClient.getRetrofitInstance(this).create(ApiService.class);

        Call<Void> call = apiService.assignDriver(bookingId,driverId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DriverDetailActivity.this, "Driver status updated successfully!", Toast.LENGTH_SHORT).show();
                    redirectToPartnerBookingActivity();
                } else {
                    Toast.makeText(DriverDetailActivity.this, "Failed to update driver status. Code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(DriverDetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to redirect to PartnerBookingActivity
    private void redirectToPartnerBookingActivity() {
        Intent intent = new Intent(DriverDetailActivity.this, PartnerBookingActivity.class);
        startActivity(intent);
        finish();
    }

    // Method to open full-screen image in a dialog
    private void openFullScreenImageDialog(String imageUrl) {
        // Create dialog
        Dialog dialog = new Dialog(DriverDetailActivity.this);
        dialog.setContentView(R.layout.full_screen_image_dialog);

        // Find the ImageView in the dialog
        ImageView fullScreenImageView = dialog.findViewById(R.id.fullScreenImageView);

        // Use Glide to load the image
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.round_document_scanner_24)
                .into(fullScreenImageView);

        // Show the dialog
        dialog.show();
    }
}
