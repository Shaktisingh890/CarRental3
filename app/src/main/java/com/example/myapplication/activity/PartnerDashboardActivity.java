package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.myapplication.R;
import com.example.myapplication.dialog.*;
import com.example.myapplication.models.response.UserProfileResponse;
import com.example.myapplication.network.ApiService;
import com.example.myapplication.network.RetrofitClient;
import com.example.myapplication.utils.ProgressBarUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PartnerDashboardActivity extends AppCompatActivity {

    private Button tabEarning, tabMyCars;
    private BottomNavigationView bottomNav;
    private View progressOverlay;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_dashboard);

        progressBar = findViewById(R.id.progressBar);
        progressOverlay = findViewById(R.id.progressOverlay);

        tabEarning = findViewById(R.id.tab_earning);
        tabMyCars = findViewById(R.id.tab_my_cars);

        // Initialize BottomNavigationView
        bottomNav = findViewById(R.id.bottomNavView);

        // Set default fragment (EarningFragment)
        loadFragment(new EarningFragment());

        // Set click listener for "Earning" tab
        tabEarning.setOnClickListener(v -> loadFragment(new EarningFragment()));

        // Set click listener for "My Cars" tab
        tabMyCars.setOnClickListener(v -> loadFragment(new MyCarsFragment()));
        // Find the notification icon
        ImageButton notificationIcon = findViewById(R.id.notificationIcon);
        // Set click listener to navigate to PartnerNotificationActivity
        notificationIcon.setOnClickListener(v -> {
            Intent intent = new Intent(PartnerDashboardActivity.this, PartnerNotificationActivity.class);
            startActivity(intent);
        });
        // BottomNavigationView listener
        bottomNav.setSelectedItemId(R.id.nav_home);
        bottomNav.setOnNavigationItemSelectedListener(this::navigateTo);

        // Handle intent that switches to My Cars tab
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("tab") && "MyCars".equals(intent.getStringExtra("tab"))) {
            loadFragment(new MyCarsFragment());
        }
    }

    // Method to navigate based on BottomNavigationView item selection
    private boolean navigateTo(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            loadFragment(new EarningFragment());  // Load the appropriate fragment
            return true;
        } else if (id == R.id.nav_profile) {
            fetchUserProfile();
        } else if (id == R.id.nav_booking) {
            startActivity(new Intent(PartnerDashboardActivity.this, PartnerBookingActivity.class));
            return true;
        }
        return false;
    }

    // Method to load the given fragment into the container
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)  // Make sure the container's ID is correct
                .commit();
    }
    @Override
    public void onBackPressed() {
        // Check if the current fragment is MyCarsFragment
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment instanceof MyCarsFragment) {
            // If on the My Cars tab, simply stay in the PartnerDashboardActivity
            // Optionally, you can load the EarningFragment if you want to show that fragment
            loadFragment(new EarningFragment());
        } else {
            // Otherwise, use the default behavior for the back button (which finishes the activity)
            super.onBackPressed();
        }
    }

    private void fetchUserProfile() {
        Log.d("fetchUserProfile", "Making API call to fetch user profile");
        ProgressBarUtils.showProgress(progressOverlay, progressBar, true); // Using utility class

        ApiService apiService = RetrofitClient.getRetrofitInstance(PartnerDashboardActivity.this).create(ApiService.class);
        apiService.getUserProfile().enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                ProgressBarUtils.showProgress(progressOverlay, progressBar, false); // Using utility class

                // Log the HTTP status code
                Log.d("fetchUserProfile", "Response Code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    UserProfileResponse user = response.body();

                    // Log individual fields from the response
                    Log.d("fetchUserProfile", "Full Name: " + user.getData().getFullName());
                    Log.d("fetchUserProfile", "Email: " + user.getData().getEmail());
                    Log.d("fetchUserProfile", "Phone Number: " + user.getData().getPhoneNumber());
                    Log.d("fetchUserProfile", "Address: " + user.getData().getAddress());
                    Log.d("fetchUserProfile", "Image URL: " + user.getData().getImgUrl());

                    // Pass data to ProfileActivity using Intent
                    Intent intent = new Intent(PartnerDashboardActivity.this, PartnerProfileActivity.class);


// Check and send user details
                    if (user.getData() != null) {
                        intent.putExtra("fullName", user.getData().getFullName() != null ? user.getData().getFullName() : "");
                        intent.putExtra("email", user.getData().getEmail() != null ? user.getData().getEmail() : "");
                        intent.putExtra("phoneNumber", user.getData().getPhoneNumber() != null ? user.getData().getPhoneNumber() : "");
                        intent.putExtra("address", user.getData().getAddress() != null ? user.getData().getAddress() : "");
                        intent.putExtra("imgUrl", user.getData().getImgUrl() != null ? user.getData().getImgUrl() : "");

                        // Check and send payment details
                        if (user.getData().getPaymentDetails() != null) {
                            intent.putExtra("upi_id", user.getData().getPaymentDetails().getUpiId() != null ?
                                    user.getData().getPaymentDetails().getUpiId() : "");
                            intent.putExtra("account_number", user.getData().getPaymentDetails().getAccountNumber() != null ?
                                    user.getData().getPaymentDetails().getAccountNumber() : "");
                        } else {
                            intent.putExtra("upi_id", "");
                            intent.putExtra("account_number", "");
                        }

                        // Check and send business info
                        if (user.getData().getBusinessInfo() != null) {
                            intent.putExtra("company_address", user.getData().getBusinessInfo().getCompanyAddress() != null ?
                                    user.getData().getBusinessInfo().getCompanyAddress() : "");
                            intent.putExtra("company_name", user.getData().getBusinessInfo().getCompanyName() != null ?
                                    user.getData().getBusinessInfo().getCompanyName() : "");
                            intent.putExtra("area", user.getData().getBusinessInfo().getServiceArea() != null ?
                                    user.getData().getBusinessInfo().getServiceArea() : "");
                        } else {
                            intent.putExtra("company_address", "");
                            intent.putExtra("company_name", "");
                            intent.putExtra("area", "");
                        }
                    }

// Start the activity
                    startActivity(intent);
                    finish();






                    startActivity(intent);
                } else {
                    // Log the error body
                    try {
                        ProgressBarUtils.showProgress(progressOverlay, progressBar, false); // Using utility class

                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                        Log.d("fetchUserProfile", "Error Body: " + errorBody);
                    } catch (IOException e) {
                        Log.e("fetchUserProfile", "Error parsing error body: " + e.getMessage(), e);
                    }
                    Log.d("fetchUserProfile", "Failed to fetch profile. Error Code: " + response.code());
                    Toast.makeText(PartnerDashboardActivity.this, "Failed to fetch profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                ProgressBarUtils.showProgress(progressOverlay, progressBar, false); // Using utility class

                Log.e("fetchUserProfile", "Error: " + t.getMessage(), t);
                Toast.makeText(PartnerDashboardActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}