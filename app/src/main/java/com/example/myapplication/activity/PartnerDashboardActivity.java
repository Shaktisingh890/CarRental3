package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.myapplication.R;
import com.example.myapplication.dialog.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PartnerDashboardActivity extends AppCompatActivity {

    private Button tabEarning, tabMyCars;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_dashboard);

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
            startActivity(new Intent(PartnerDashboardActivity.this, PartnerProfileActivity.class));
            return true;
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

}