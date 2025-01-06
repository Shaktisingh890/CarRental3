package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.dialog.EarningFragment;
import com.example.myapplication.models.response.LogoutResponse;
import com.example.myapplication.network.ApiService;
import com.example.myapplication.network.RetrofitClient;
import com.example.myapplication.utils.SharedPreferencesManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PartnerProfileActivity extends AppCompatActivity {

    private ImageView profileImage;
    private TextView profileName, profilePhone;
    private ProgressBar progressBar;
    private BottomNavigationView bottomNav;
    private RelativeLayout optionEditProfile, optionChangePassword, optionContactUs, optionDeleteAccount, optionLogout;

    private String fullName,email,phoneNumber,address,imgUrl,upi_id,company_name,company_address,area,account_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_profile);

       Log.i("My Tag","Just Check");
        // Initialize Views
        profileImage = findViewById(R.id.profile_image);
        profileName = findViewById(R.id.profile_name);
        profilePhone = findViewById(R.id.profile_phone);
        progressBar = findViewById(R.id.progressBar);
        fullName= getIntent().getStringExtra("fullName");
        email = getIntent().getStringExtra("email");
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        address = getIntent().getStringExtra("address");
        imgUrl = getIntent().getStringExtra("imgUrl");
        upi_id=getIntent().getStringExtra("upi_id");
        account_number=getIntent().getStringExtra("account_number");
        company_address=getIntent().getStringExtra("company_address");
        company_name=getIntent().getStringExtra("company_name");
        area=getIntent().getStringExtra("area");


        Log.d("myfullname","my name is "+fullName);
        Log.d("myfullname","my name is "+phoneNumber);
        Log.d("myfullname","my name is "+imgUrl);
        Log.d("myfullname","my name is "+company_name);
        // Set up profile info
        if (fullName != null) {
            profileName.setText(fullName); // Set the user's full name
        } else {
            profileName.setText("Cameron Williamson");
        }

        if (profilePhone != null) {
            profilePhone.setText(phoneNumber); // Set the user's full name
        } else {
            profilePhone.setText("67899998765544");
        }

        if (profileImage != null && imgUrl != null && !imgUrl.isEmpty()) {
            // Use Glide to load the image from URL
            Glide.with(this)
                    .load(imgUrl)
                    .placeholder(R.drawable.profile) // Placeholder while loading
                    .error(R.drawable.profile)       // Fallback in case of an error
                    .into(profileImage);
        } else {
            profileImage.setImageResource(R.drawable.profile); // Set default profile picture
        }

        optionEditProfile = findViewById(R.id.option_edit_profile);
        optionChangePassword = findViewById(R.id.option_change_password);
        optionContactUs = findViewById(R.id.option_contact_us);
        optionDeleteAccount = findViewById(R.id.option_delete_account);
        optionLogout = findViewById(R.id.option_logout);
        bottomNav = findViewById(R.id.bottomNavView);
        // BottomNavigationView listener
        bottomNav.setSelectedItemId(R.id.nav_settings);
        bottomNav.setOnNavigationItemSelectedListener(this::navigateTo);



        // Set Click Listeners
        optionEditProfile.setOnClickListener(v -> navigateToEditProfile());
        optionChangePassword.setOnClickListener(v -> navigateToChangePassword());
        optionContactUs.setOnClickListener(v -> navigateToContactUs());
        optionDeleteAccount.setOnClickListener(v -> showDeleteAccountDialog());
        optionLogout.setOnClickListener(v -> showLogoutDialog());
    }



    private void navigateToEditProfile() {

        Intent intent = new Intent(PartnerProfileActivity.this, PartnerEditProfileActivity.class);
        intent.putExtra("fullName", fullName);
        intent.putExtra("email", email);
        intent.putExtra("phoneNumber", phoneNumber);
        intent.putExtra("address", address);
        intent.putExtra("company_address", company_address);
        intent.putExtra("company_name", company_name);
        intent.putExtra("area", area);
        intent.putExtra("account_number", account_number);
        intent.putExtra("upi_id", upi_id);
        intent.putExtra("imgUrl", imgUrl);
        startActivity(intent);
    }


    private void navigateToChangePassword() {
        Intent intent =new Intent(PartnerProfileActivity.this,PartnerChangePasswordActivity.class);
        startActivity(intent);
    }

    private void navigateToContactUs() {
     Intent intent =new Intent(PartnerProfileActivity.this, PartnerContactActivity.class);
     startActivity(intent);
    }

    private void showDeleteAccountDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.delete_account_dialog, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();

        Button cancelButton = dialogView.findViewById(R.id.cancelDeleteButton);
        Button deleteButton = dialogView.findViewById(R.id.confirmDeleteButton);

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        deleteButton.setOnClickListener(v -> {
            dialog.dismiss();
            deleteAccount();
        });
    }

    private void deleteAccount() {
        progressBar.setVisibility(View.VISIBLE);

        ApiService apiService = RetrofitClient.getRetrofitInstance(getApplicationContext()).create(ApiService.class);
        String accessToken = SharedPreferencesManager.getAccessToken(getApplicationContext());

        if (accessToken != null) {
            apiService.deleteAccount().enqueue(new Callback<LogoutResponse>() {
                @Override
                public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                    progressBar.setVisibility(View.GONE);

                    if (response.isSuccessful()) {
                        Toast.makeText(PartnerProfileActivity.this, "Account deleted successfully.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PartnerProfileActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(PartnerProfileActivity.this, "Failed to delete account. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LogoutResponse> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(PartnerProfileActivity.this, "Network error. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No active session found. Please log in.", Toast.LENGTH_SHORT).show();
        }
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
            dialog.dismiss();
            progressBar.setVisibility(View.VISIBLE);

            ApiService apiService = RetrofitClient.getRetrofitInstance(getApplicationContext()).create(ApiService.class);
            String accessToken = SharedPreferencesManager.getAccessToken(getApplicationContext());

            if (accessToken != null) {
                apiService.logoutUser().enqueue(new Callback<LogoutResponse>() {
                    @Override
                    public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                        progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful()) {
                            SharedPreferencesManager.clearTokens(PartnerProfileActivity.this);
                            Toast.makeText(PartnerProfileActivity.this, "Successfully logged out.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PartnerProfileActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(PartnerProfileActivity.this, "Logout failed. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LogoutResponse> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(PartnerProfileActivity.this, "Network error. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(PartnerProfileActivity.this, "No active session found. Please log in.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean navigateTo(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            startActivity(new Intent(PartnerProfileActivity.this, PartnerDashboardActivity.class));
            return true;
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(PartnerProfileActivity.this, PartnerProfileActivity.class));
            return true;
        } else if (id == R.id.nav_booking) {
            startActivity(new Intent(PartnerProfileActivity.this, PartnerBookingActivity.class));
            return true;
        }
        return false;
    }
}
