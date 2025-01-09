package com.example.myapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.myapplication.R;
import androidx.appcompat.app.AlertDialog;
public class DriverDashboardActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageView menuIcon;
    private Switch availabilitySwitch;
    private TextView subtitleMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_dashboard);

        // Initialize Views
        drawerLayout = findViewById(R.id.drawerLayout);
        menuIcon = findViewById(R.id.menuIcon);
        availabilitySwitch = findViewById(R.id.onlineOfflineSwitch);
        subtitleMessage = findViewById(R.id.subtitleMessage);
        RelativeLayout optionEditProfile = findViewById(R.id.option_edit_profile);
        RelativeLayout rides = findViewById(R.id.rides);
        RelativeLayout documents = findViewById(R.id.documents);
        RelativeLayout faq = findViewById(R.id.faq);
        RelativeLayout contact_us = findViewById(R.id.contact_us);
        RelativeLayout earnings = findViewById(R.id.earnings);
        ImageView closeIcon = findViewById(R.id.close);

        // Set up hamburger menu click listener
        menuIcon.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(findViewById(R.id.driverProfileLayout))) {
                drawerLayout.closeDrawer(findViewById(R.id.driverProfileLayout));
            } else {
                drawerLayout.openDrawer(findViewById(R.id.driverProfileLayout));
            }
        });

        // Set up click listener for Edit Profile
        optionEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(DriverDashboardActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });
        rides.setOnClickListener(v -> {
            Intent intent = new Intent(DriverDashboardActivity.this, MyRidesActivity.class);
            startActivity(intent);
        });
        documents.setOnClickListener(v -> {
            Intent intent = new Intent(DriverDashboardActivity.this, MyDocumentsActivity.class);
            startActivity(intent);
        });
        faq.setOnClickListener(v -> {
            Intent intent = new Intent(DriverDashboardActivity.this, DriverFaqActivity.class);
            startActivity(intent);
        });
        contact_us.setOnClickListener(v -> {
            Intent intent = new Intent(DriverDashboardActivity.this, DriverContactActivity.class);
            startActivity(intent);
        });
        earnings.setOnClickListener(v -> {
            Intent intent = new Intent(DriverDashboardActivity.this, DriverEarningsActivity.class);
            startActivity(intent);
        });
        // Set click listener for Close icon
        closeIcon.setOnClickListener(v -> {
            Intent intent = new Intent(DriverDashboardActivity.this, DriverDashboardActivity.class);
            startActivity(intent);
        });

        // Set Switch Listener
        availabilitySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                availabilitySwitch.setText("Online");
                subtitleMessage.setText("You are now available to take rides.");
                showCustomToast("You are Online", R.drawable.toast_background, R.drawable.ic_toast_icon);
                // Redirect to RideRequestActivity
                Intent intent = new Intent(DriverDashboardActivity.this, RideRequestActivity.class);
                startActivity(intent);
            } else {
                availabilitySwitch.setText("Offline");
                subtitleMessage.setText("Go Offline for taking rest");
                showCustomToast("You are Offline", R.drawable.toast_background_red, R.drawable.cross);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Show the logout confirmation dialog when back button is pressed
        showLogoutConfirmationDialog();
    }

    private void showLogoutConfirmationDialog() {
        // Inflate the dialog layout
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_logout_confirmation, null);

        // Build the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();

        // Set up button click listeners
        Button cancelButton = dialogView.findViewById(R.id.cancelButton);
        Button logoutButton = dialogView.findViewById(R.id.logoutButton);

        // Cancel the dialog and stay in the current activity
        cancelButton.setOnClickListener(v -> dialog.dismiss());

        // Logout and redirect to LoginActivity
        logoutButton.setOnClickListener(v -> {
            dialog.dismiss();
            // Clear user session and navigate to login page
            clearSession(); // Ensure you clear the session here
            Intent intent = new Intent(DriverDashboardActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish(); // Close the current activity
        });
    }

    // Clear session method
    private void clearSession() {
        SharedPreferences preferences = getSharedPreferences("YourSharedPrefName", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
    private void showCustomToast(String message, int backgroundResource, int iconResource) {
        // Inflate the custom layout
        LayoutInflater inflater = getLayoutInflater();
        View customToastView = inflater.inflate(R.layout.custom_toast, findViewById(R.id.toastContainer));

        // Set the message text
        TextView toastMessage = customToastView.findViewById(R.id.toastMessage);
        toastMessage.setText(message);

        // Set the background resource (green for online, red for offline)
        customToastView.setBackgroundResource(backgroundResource);

        // Set the icon resource
        ImageView toastIcon = customToastView.findViewById(R.id.toastIcon);
        toastIcon.setImageResource(iconResource);

        // Create the Toast and show it
        Toast customToast = new Toast(getApplicationContext());
        customToast.setDuration(Toast.LENGTH_SHORT);
        customToast.setView(customToastView);
        customToast.show();
    }
}
