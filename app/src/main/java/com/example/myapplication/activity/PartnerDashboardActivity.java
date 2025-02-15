package com.example.myapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.myapplication.R;
import com.example.myapplication.dialog.*;
import com.example.myapplication.models.response.UserProfileResponse;
import com.example.myapplication.network.ApiService;
import com.example.myapplication.network.RetrofitClient;
import com.example.myapplication.utils.NavigationUtil;
import com.example.myapplication.utils.ProgressBarUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.myapplication.utils.SharedPreferencesManager;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PartnerDashboardActivity extends AppCompatActivity {

    private Button tabEarning, tabMyCars;
    private BottomNavigationView bottomNav;
    private View progressOverlay;
    private ProgressBar progressBar;

    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_dashboard);

        progressBar = findViewById(R.id.progressBar);
        progressOverlay = findViewById(R.id.progressOverlay);
        imageView = findViewById(R.id.logoImage);

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
        // Set the listener for item selection
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle the selection using NavigationUtil
                return NavigationUtil.handleBottomNavigationSelection(PartnerDashboardActivity.this, item.getItemId());
            }
        });;

        // Handle intent that switches to My Cars tab
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("tab") && "MyCars".equals(intent.getStringExtra("tab"))) {
            loadFragment(new MyCarsFragment());
        }
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
        // Check if the current fragment is EarningFragment
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (currentFragment instanceof EarningFragment) {
            // Show the logout confirmation dialog
            showLogoutConfirmationDialog();
        } else if (currentFragment instanceof MyCarsFragment) {
            // If on MyCarsFragment, load EarningFragment
            loadFragment(new EarningFragment());
        } else {
            // Otherwise, use the default back button behavior
            super.onBackPressed();
        }
    }
    public static class SharedPreferencesManager {
        public static void clearSession(Context context) {
            SharedPreferences preferences = context.getSharedPreferences("YourSharedPrefName", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
        }
    }

    private void showLogoutConfirmationDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_logout_confirmation, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();

        Button cancelButton = dialogView.findViewById(R.id.cancelButton);
        Button logoutButton = dialogView.findViewById(R.id.logoutButton);

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        logoutButton.setOnClickListener(v -> {
            dialog.dismiss();
            SharedPreferencesManager.clearSession(this); // Call the static method
            Intent intent = new Intent(PartnerDashboardActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }



}