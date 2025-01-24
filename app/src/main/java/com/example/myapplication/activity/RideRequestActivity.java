package com.example.myapplication.activity;

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
import com.example.myapplication.models.response.BookingDetailsResponse;
import com.example.myapplication.network.ApiService;
import com.example.myapplication.network.RetrofitClient;
import com.example.myapplication.utils.MyFirebaseMessagingService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RideRequestActivity extends AppCompatActivity {
    private String notificationId,driverStatus,bookingId;
    TextView tvpickupLocation,tvdropoffLocation,tvCarRent,tvCustomerName,tvphoneNumber,tvCarName,tvCarModal,tvRegistrationNumber,tvpartnerName,tvpartnerPhone,tvcarPickupLocation,tvcarDropoffLocation, tvPickupTime,tvReturnTime;
    ImageView tvprofileimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_request);

        Button declineButton= findViewById(R.id.declineButton);

        tvpickupLocation=findViewById(R.id.pickupLocation);
        tvdropoffLocation=findViewById(R.id.dropoffLocation);
        tvCarRent=findViewById(R.id.total_rent);
        tvCustomerName=findViewById(R.id.customerName);
        tvprofileimage=findViewById(R.id.profile_image);
        tvphoneNumber=findViewById(R.id.phoneNumber);
        tvCarName=findViewById(R.id.carName);
        tvCarModal=findViewById(R.id.CarModal);
        tvRegistrationNumber=findViewById(R.id.RegistrationNumber);
        tvpartnerName=findViewById(R.id.partnerName);
        tvpartnerPhone=findViewById(R.id.partnerPhone);
        tvcarPickupLocation=findViewById(R.id.carPickupLocation);
        tvcarDropoffLocation=findViewById(R.id.carDropoffLocation);
        tvPickupTime=findViewById(R.id.startDate);
        tvReturnTime=findViewById(R.id.endDate);
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
        Button acceptButton = findViewById(R.id.acceptButton);

        // Set a click listener for the Accept button
        acceptButton.setOnClickListener(view -> {
            updateDriverStatus(bookingId, "accepted", "booked", RideDetailsActivity.class);
        });

        declineButton.setOnClickListener(view -> {
            updateDriverStatus(bookingId, "rejected", "cancelled", DeclineRideActivity.class);
//            MyFirebaseMessagingService myservice= new MyFirebaseMessagingService();
//            myservice.deleteNotificationByIdFromBackend(this,notificationId);

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
                        // Set profile image using Glide
                        String imageUrl = bookingDetailsResponse.getData().getCimage();  // Assuming getCImage() returns the image URL
                        Glide.with(RideRequestActivity.this)
                                .load(imageUrl)  // Load the image URL
                                .placeholder(R.drawable.profile)  // Set a placeholder image while loading
                                .error(R.drawable.profile)  // Set an error image if the image URL fails to load
                                .circleCrop()
                                .into(tvprofileimage);  // Set the image into ImageView
                        Log.d("Driver","driver"+bookingDetailsResponse.getData().getCname());
                        tvCustomerName.setText(bookingDetailsResponse.getData().getCname());
                        tvCarRent.setText("Rent Per Day: $" + bookingDetailsResponse.getData().getPricePerDay());
                        tvphoneNumber.setText("Phone: "+bookingDetailsResponse.getData().getCphone());
//                        tvBookingDetails.setText("Pickup & Drop-off Points");
                        tvpickupLocation.setText("Pickup Location: " + bookingDetailsResponse.getData().getPickupLocation());
                        tvdropoffLocation.setText("Return Location: " + bookingDetailsResponse.getData().getDropoffLocation());
                        tvPickupTime.setText("Pickup Time: " + bookingDetailsResponse.getData().getStartDate());
                        tvReturnTime.setText("Return Time: " + bookingDetailsResponse.getData().getEndDate());
                        driverStatus=bookingDetailsResponse.getData().getDriverStatus();
                        Log.d("notification_id","fetchDriverStatus"+bookingDetailsResponse.getData().getDriverStatus());

                        tvCarName.setText("Car Name: " + bookingDetailsResponse.getData().getCarName());
                        tvCarModal.setText("Car Modal: " + bookingDetailsResponse.getData().getCarModel());
                        tvRegistrationNumber.setText("Registration No: " + bookingDetailsResponse.getData().getRegistrationNumber());
                        tvpartnerName.setText("Name: " + bookingDetailsResponse.getData().getpartnerName());
                        tvpartnerPhone.setText("Contact No: " + bookingDetailsResponse.getData().getpartnerPhone());
                        tvcarPickupLocation.setText("Pickup Location: " + bookingDetailsResponse.getData().getcarPickupLocation());
                        tvcarDropoffLocation.setText("Return Location: " + bookingDetailsResponse.getData().getcarDropoffLocation());

                        if (driverStatus != null && (driverStatus.equals("confirmed") || driverStatus.equals("rejected"))) {
                            // If the status is already confirmed or rejected, skip the status update and directly navigate to Partner_DriverListActivity
                            Intent intent = new Intent(RideRequestActivity.this, RideDetailsActivity.class);
                            intent.putExtra("bookingId", bookingId);
                            intent.putExtra("notification_id", notificationId);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(RideRequestActivity.this, "No booking details available", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RideRequestActivity.this, "Failed to fetch booking details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BookingDetailsResponse> call, Throwable t) {
                Toast.makeText(RideRequestActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(RideRequestActivity.this, "Status updated successfully", Toast.LENGTH_SHORT).show();

                    // Redirect to the specified activity
                    Intent intent = new Intent(RideRequestActivity.this, nextActivity);
                    intent.putExtra("bookingId",bookingId);
                    intent.putExtra("notification_id",notificationId);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RideRequestActivity.this, "Failed to update status", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(RideRequestActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
