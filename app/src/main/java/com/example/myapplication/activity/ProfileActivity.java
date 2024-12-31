package com.example.myapplication.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.dialog.*;

import com.example.myapplication.R;
import com.example.myapplication.models.response.LogoutResponse;
import com.example.myapplication.network.ApiService;
import com.example.myapplication.network.RetrofitClient;
import com.example.myapplication.utils.ProgressBarUtils;
import com.example.myapplication.utils.SharedPreferencesManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.Toast;
import retrofit2.Call;

public class ProfileActivity extends AppCompatActivity {

    private TextView profileName, profilePhone;
    private ImageView profileImage;
    private RelativeLayout optionEditProfile, optionMyBooking,optionNotification,
    optionSettings,optionTermsConditions,optionCustomerSupport,optionRateUs,optionLogout,optionFaq;

    private BottomNavigationView bottomNavView;

    private View progressOverlay;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Get the passed data from the Intent
        String email = getIntent().getStringExtra("user_email");
        String fullName = getIntent().getStringExtra("user_fullName");
        // Initialize views
        profileName = findViewById(R.id.profile_name);
        profilePhone = findViewById(R.id.profile_phone);
        profileImage = findViewById(R.id.profile_image);
        optionEditProfile  = findViewById(R.id.option_edit_profile);
        optionMyBooking  = findViewById(R.id.option_my_booking);
        optionNotification = findViewById(R.id.option_notification);
        optionSettings = findViewById(R.id.option_settings);
        optionTermsConditions = findViewById(R.id.option_terms_conditions);
        optionCustomerSupport = findViewById(R.id.option_customer_support);
        optionRateUs = findViewById(R.id.option_rate_us);
        optionLogout = findViewById(R.id.option_logout);
        optionFaq = findViewById(R.id.option_faq);
        // Initialize views
        bottomNavView = findViewById(R.id.bottomNavView);
        progressBar = findViewById(R.id.progressBar);
        progressOverlay = findViewById(R.id.progressOverlay);

        // Set Profile tab as active
        bottomNavView.setSelectedItemId(R.id.nav_profile);

        // Set up profile info
        if (fullName != null) {
            profileName.setText(fullName); // Set the user's full name
        } else {
            profileName.setText("Cameron Williamson");
        }

        profilePhone.setText("(219) 555-0114");
        profileImage.setImageResource(R.drawable.profile); // Replace with actual image resource if needed

        // Set up onClickListeners for options
        optionEditProfile.setOnClickListener(v -> {
            // Redirect to EditProfileActivity
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });


        optionMyBooking.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, MyBookingActivity.class);
            startActivity(intent);
        });

        optionNotification.setOnClickListener(v -> {
            // Handle Notifications click
            // Intent to Notifications activity
        });

       
        optionSettings.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, SettingActivity.class);
            startActivity(intent);
        });

        optionTermsConditions.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, TermsConditionsActivity.class);
            startActivity(intent);
        });

        optionCustomerSupport.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, CustomerSupportActivity.class);
            startActivity(intent);
        });

        optionRateUs.setOnClickListener(v -> {
            // Show the rating dialog when clicked
            RatingDialogFragment ratingDialogFragment = new RatingDialogFragment();
            ratingDialogFragment.show(getSupportFragmentManager(), "RatingDialog");
        });
        // Set up logout functionality
        optionLogout.setOnClickListener(v -> showLogoutDialog());

        optionFaq.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, FAQActivity.class);
            startActivity(intent);
        });
        // Set up Bottom Navigation
        bottomNavView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(ProfileActivity.this, DashboardActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                return true;
            }
            else if (item.getItemId() == R.id.nav_booking){
                startActivity(new Intent(ProfileActivity.this, MyBookingActivity.class));
                return true;
            }

            return false;

        });

    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_logout_confirmation, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();

        Button cancelButton = dialogView.findViewById(R.id.cancelButton);
        Button logoutButton = dialogView.findViewById(R.id.logoutButton);

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        logoutButton.setOnClickListener(v -> {
            dialog.dismiss();  // Dismiss any previous dialog

            ProgressBarUtils.showProgress(progressOverlay, progressBar, true); // Using utility class


            // Call the logout API using Retrofit
            ApiService apiService = RetrofitClient.getRetrofitInstance(getApplicationContext()).create(ApiService.class);
            String accessToken = SharedPreferencesManager.getAccessToken(getApplicationContext());

            if (accessToken != null) {
                apiService.logoutUser().enqueue(new retrofit2.Callback<LogoutResponse>() {
                    @Override
                    public void onResponse(Call<LogoutResponse> call, retrofit2.Response<LogoutResponse> response) {
                        ProgressBarUtils.showProgress(progressOverlay, progressBar, false); // Using utility class


                        if (response.isSuccessful()) {
                            // Clear session and navigate to LoginActivity
                            SharedPreferencesManager.clearTokens(ProfileActivity.this);

                            Toast.makeText(ProfileActivity.this, "Successfully logged out.", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(ProfileActivity.this, "Logout failed. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LogoutResponse> call, Throwable t) {
                        // Hide the ProgressBar in case of failure
                        ProgressBarUtils.showProgress(progressOverlay, progressBar, false); // Using utility class

                        Toast.makeText(ProfileActivity.this, "Network error. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(ProfileActivity.this, "No active session found. Please log in.", Toast.LENGTH_SHORT).show();
            }
        });


    }
}