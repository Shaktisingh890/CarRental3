package com.example.myapplication.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.R;
import com.example.myapplication.activity.PartnerBookingActivity;
import com.example.myapplication.activity.PartnerProfileActivity;
import com.example.myapplication.activity.PartnerDashboardActivity;
import com.example.myapplication.dialog.EarningFragment;
import com.example.myapplication.models.response.UserProfileResponse;
import com.example.myapplication.network.ApiService;
import com.example.myapplication.network.RetrofitClient;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NavigationUtil {

    // Method to navigate to a specific fragment
    public static void navigateToFragment(PartnerDashboardActivity activity, Fragment fragment) {
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    // Method to handle bottom navigation item selection
    public static boolean handleBottomNavigationSelection(Context context, int itemId) {
        if (context instanceof PartnerDashboardActivity) {
            PartnerDashboardActivity activity = (PartnerDashboardActivity) context;

            // Use if-else instead of switch to avoid constant expression issue
            if (itemId == R.id.nav_home) {
                navigateToFragment(activity, new EarningFragment());
                return true;
            } else if (itemId == R.id.nav_profile) {
                fetchUserProfile(activity); // Pass activity instance
                return true;
            } else if (itemId == R.id.nav_booking) {
                navigateToBookingActivity(activity);
                return true;
            }
        }
        return false;
    }

    // Method to navigate to PartnerBookingActivity
    private static void navigateToBookingActivity(PartnerDashboardActivity activity) {
        Intent intent = new Intent(activity, PartnerBookingActivity.class);
        activity.startActivity(intent);
    }



    // Fetch user profile and pass data to the profile activity
    private static void fetchUserProfile(PartnerDashboardActivity activity) {
        Log.d("fetchUserProfile", "Making API call to fetch user profile");

        // Show loading progress
        ProgressBarUtils.showProgress(activity.findViewById(R.id.progressOverlay), activity.findViewById(R.id.progressBar), true, activity.findViewById(R.id.logoImage));

        ApiService apiService = RetrofitClient.getRetrofitInstance(activity).create(ApiService.class);
        apiService.getUserProfile().enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                // Hide loading progress
                ProgressBarUtils.showProgress(activity.findViewById(R.id.progressOverlay), activity.findViewById(R.id.progressBar), false, activity.findViewById(R.id.logoImage));

                if (response.isSuccessful() && response.body() != null) {
                    UserProfileResponse user = response.body();

                    Log.d("fetchUserProfile", "Full Name: " + user.getData().getFullName());
                    Log.d("fetchUserProfile", "Email: " + user.getData().getEmail());

                    // Pass data to ProfileActivity using Intent
                    Intent intent = new Intent(activity, PartnerProfileActivity.class);
                    populateUserData(user, intent);

                    // Start the activity
                    activity.startActivity(intent);
                    activity.finish();
                } else {
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                // Hide loading progress
                ProgressBarUtils.showProgress(activity.findViewById(R.id.progressOverlay), activity.findViewById(R.id.progressBar), false, activity.findViewById(R.id.logoImage));

                Log.e("fetchUserProfile", "Error: " + t.getMessage(), t);
                Toast.makeText(activity, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to populate intent with user profile data
    private static void populateUserData(UserProfileResponse user, Intent intent) {
        if (user.getData() != null) {
            intent.putExtra("fullName", safeGet(user.getData().getFullName()));
            intent.putExtra("email", safeGet(user.getData().getEmail()));
            intent.putExtra("phoneNumber", safeGet(user.getData().getPhoneNumber()));
            intent.putExtra("address", safeGet(user.getData().getAddress()));
            intent.putExtra("imgUrl", safeGet(user.getData().getImgUrl()));

            if (user.getData().getPaymentDetails() != null) {
                intent.putExtra("upi_id", safeGet(user.getData().getPaymentDetails().getUpiId()));
                intent.putExtra("account_number", safeGet(user.getData().getPaymentDetails().getAccountNumber()));
            }

            if (user.getData().getBusinessInfo() != null) {
                intent.putExtra("company_address", safeGet(user.getData().getBusinessInfo().getCompanyAddress()));
                intent.putExtra("company_name", safeGet(user.getData().getBusinessInfo().getCompanyName()));
                intent.putExtra("area", safeGet(user.getData().getBusinessInfo().getServiceArea()));
            }
        }
    }

    // Helper method to safely retrieve data or return empty string if null
    private static String safeGet(String value) {
        return value != null ? value : "";
    }

    // Method to handle error response
    private static void handleErrorResponse(Response<UserProfileResponse> response) {
        try {
            String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
            Log.d("fetchUserProfile", "Error Body: " + errorBody);
        } catch (IOException e) {
            Log.e("fetchUserProfile", "Error parsing error body: " + e.getMessage(), e);
        }
        Log.d("fetchUserProfile", "Failed to fetch profile. Error Code: " + response.code());
    }
}
