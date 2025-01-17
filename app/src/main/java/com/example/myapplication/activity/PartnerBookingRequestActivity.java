package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.models.response.BookingDetailsResponse;
import com.example.myapplication.network.ApiService;
import com.example.myapplication.network.RetrofitClient;
import com.example.myapplication.utils.MyFirebaseMessagingService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PartnerBookingRequestActivity extends AppCompatActivity {

    private TextView tvCarDetails, tvCarBrandModel, tvCarRent;
    private TextView tvBookingDetails, tvPickupLocation, tvReturnLocation, tvPickupTime, tvReturnTime;
    private Button btnAccept, btnReject;

    private String notificationId,partnerStatus;

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
        notificationId=getIntent().getStringExtra("notification_id");
     Log.d("notification_id","notificiation"+notificationId);
        Log.d("notification_id","notificiation"+bookingId);


        // Fetch booking details
        fetchBookingDetails(bookingId);




        // Handle Accept button click
        btnAccept.setOnClickListener(view -> {
            updateBookingStatus(bookingId, "confirmed", "ongoing", Partner_DriverListActivity.class);
        });

        btnReject.setOnClickListener(view -> {
            updateBookingStatus(bookingId, "rejected", "cancelled", BookingCancelledReasonActivity.class);
            MyFirebaseMessagingService myservice= new MyFirebaseMessagingService();
            myservice.deleteNotificationByIdFromBackend(this,notificationId);

        });

    }


    private void updateBookingStatus(String bookingId, String partnerStatus, String status, Class<?> nextActivity) {
        ApiService apiService = RetrofitClient.getRetrofitInstance(this).create(ApiService.class);

        // Call the API to update the booking status
        Call<Void> call = apiService.updateBookingStatus(bookingId, partnerStatus, status);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PartnerBookingRequestActivity.this, "Status updated successfully", Toast.LENGTH_SHORT).show();

                    // Redirect to the specified activity
                    Intent intent = new Intent(PartnerBookingRequestActivity.this, nextActivity);
                    intent.putExtra("bookingId",bookingId);
                    intent.putExtra("notification_id",notificationId);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(PartnerBookingRequestActivity.this, "Failed to update status", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(PartnerBookingRequestActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
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
                        tvCarDetails.setText("Booking Detail");
                        tvCarBrandModel.setText("Brand Model: " + bookingDetailsResponse.getData().getCarName()+"" +bookingDetailsResponse.getData().getCarModel());
                        tvCarRent.setText("Rent Per Day: $" + bookingDetailsResponse.getData().getPricePerDay());

                        tvBookingDetails.setText("Pickup & Drop-off Points");
                        tvPickupLocation.setText("Pickup Location: " + bookingDetailsResponse.getData().getPickupLocation());
                        tvReturnLocation.setText("Return Location: " + bookingDetailsResponse.getData().getDropoffLocation());
                        tvPickupTime.setText("Pickup Time: " + bookingDetailsResponse.getData().getStartDate());
                        tvReturnTime.setText("Return Time: " + bookingDetailsResponse.getData().getEndDate());
                        partnerStatus=bookingDetailsResponse.getData().getPartnerStatus();
                        Log.d("notification_id","fetchPartnerStatus"+bookingDetailsResponse.getData().getPartnerStatus());

                        if (partnerStatus != null && (partnerStatus.equals("confirmed") || partnerStatus.equals("rejected"))) {
                            // If the status is already confirmed or rejected, skip the status update and directly navigate to Partner_DriverListActivity
                            Intent intent = new Intent(PartnerBookingRequestActivity.this, Partner_DriverListActivity.class);
                            intent.putExtra("bookingId", bookingId);
                            intent.putExtra("notification_id", notificationId);
                            startActivity(intent);
                            finish();
                        }
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
