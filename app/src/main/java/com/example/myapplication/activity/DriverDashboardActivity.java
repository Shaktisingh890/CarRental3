package com.example.myapplication.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import android.Manifest;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myapplication.R;
import com.example.myapplication.network.ApiService;
import com.example.myapplication.network.RetrofitClient;
import com.example.myapplication.utils.LocationUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverDashboardActivity extends AppCompatActivity implements LocationUtils.LocationUpdateListener {
    private Switch availabilitySwitch;
    private TextView subtitleMessage;

    // Declare a Handler and Runnable to repeat the location request
    private Handler locationHandler;
    private Runnable locationRunnable;

    private Socket mSocket;

    private double latitude;
    private double longitude;

    private static final String TAG="DriverDashboardLocation";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private ImageView menuIcon;
    private FusedLocationProviderClient fusedLocationClient;
    private DrawerLayout drawerLayout;
    private LocationUtils locationUtils;

    private boolean isInitialLoad = true; // Flag to differentiate between initial load and user interaction

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_dashboard);

        // Initialize LocationUtil
        locationUtils = new LocationUtils(this, this);

        // Check if location permissions are granted, if not request them




        if (!locationUtils.isLocationPermissionGranted()) {
            Log.d("location2", "if part of dashboard");
            // If permissions are granted, enable location services (GPS check)
            locationUtils.requestLocationPermissions(this);
            locationUtils.fetchLocationWithUpdates();

        } else {
            // If permissions are not granted, request location permissions
            Log.d("location2", "else part of dashboard");
            locationUtils.fetchLocationWithUpdates();
        }





        drawerLayout=findViewById(R.id.drawerLayout);
        availabilitySwitch = findViewById(R.id.onlineOfflineSwitch);
        subtitleMessage = findViewById(R.id.subtitleMessage);
        menuIcon=findViewById(R.id.menuIcon);
        availabilitySwitch = findViewById(R.id.onlineOfflineSwitch);
        subtitleMessage = findViewById(R.id.subtitleMessage);
        RelativeLayout optionEditProfile = findViewById(R.id.option_edit_profile);
        RelativeLayout rides = findViewById(R.id.rides);
        RelativeLayout documents = findViewById(R.id.documents);
        RelativeLayout faq = findViewById(R.id.faq);
        RelativeLayout contact_us = findViewById(R.id.contact_us);
        RelativeLayout earnings = findViewById(R.id.earnings);
        ImageView closeIcon = findViewById(R.id.close);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true); // Enables debugging for WebView
        }

        // Set up the WebView
        WebView mapWebView = findViewById(R.id.mapWebView);

        // Enable JavaScript
        WebSettings webSettings = mapWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Expose JavaScript function to Android
        mapWebView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void updateMap(double lat, double lon) {
                Log.d("WebView", "Received location update: " + lat + ", " + lon);
            }
        }, "Android");

        // Load the map HTML
        mapWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("DriverDashboard", "Map page loaded successfully");

                // Call the JavaScript function to update the map
                if (latitude != 0 && longitude != 0) {
                    loadLeafletMap(latitude, longitude);
                }
            }
        });

        mapWebView.loadUrl("file:///android_asset/leaflet_map.html");











    // Fetch the availability status on activity start
        fetchAvailabilityStatus();

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


        try {
            mSocket = IO.socket("http://192.168.1.42:3000"); // Replace with your server URL
        } catch (Exception e) {
            e.printStackTrace();
        }

        mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                // Connection successful
                Log.d("Socket", "Connected to server.");
                // You can update UI or call any other method after the connection
            }
        });

        mSocket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                // Connection failed
                Log.e("Socket", "Connection failed: " + args[0]);
                // Handle error, maybe show a message to the user
            }
        });

//        mSocket.on("locationUpdate", onLocationUpdate);

        mSocket.connect();


        // Set up switch change listener
        availabilitySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isInitialLoad) { // Only trigger update when it's a user interaction
                Map<String, Boolean> availabilityStatus = new HashMap<>();
                availabilityStatus.put("availability", isChecked);
                updateAvailabilityStatus(availabilityStatus, isChecked);
            }
        });

        // Find the notification icon
        ImageButton notificationIcon = findViewById(R.id.notificationIcon);
        // Set click listener to navigate to PartnerNotificationActivity
        notificationIcon.setOnClickListener(v -> {
            Intent intent = new Intent(DriverDashboardActivity.this, DriverNotificationActivity.class);
            startActivity(intent);
        });





    }



    @Override
    public void onLocationFetched(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        Log.d("LocationUtil","i am onLocationFetched");

        // Send the fetched location to your backend, e.g., Socket.IO
//        sendLocationToBackend(latitude, longitude);

        // Update the map or UI with the location
        loadLeafletMap(latitude, longitude);

        // Log the latitude and longitude
        Log.d("LocationUtil", "Location: Lat=" + latitude + ", Lon=" + longitude);
    }


    @Override
    public void onLocationError(String errorMessage) {
        // Handle any errors related to location (e.g., permission issues, GPS disabled)
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationUtils.handlePermissionsResult(requestCode, permissions, grantResults);
    }






    private void fetchAvailabilityStatus() {
        ApiService apiService = RetrofitClient.getRetrofitInstance(this).create(ApiService.class);

        Call<Map<String, Boolean>> call = apiService.getAvailabilityStatus();
        call.enqueue(new Callback<Map<String, Boolean>>() {
            @Override
            public void onResponse(Call<Map<String, Boolean>> call, Response<Map<String, Boolean>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boolean isOnline = response.body().get("availability");
                    isInitialLoad = true; // Set flag to true for initial load
                    availabilitySwitch.setChecked(isOnline);
                    updateUIBasedOnStatus(isOnline);
                    isInitialLoad = false; // Reset flag after the initial state is set
                } else {
                    Toast.makeText(DriverDashboardActivity.this, "Failed to fetch status", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Boolean>> call, Throwable t) {
                Toast.makeText(DriverDashboardActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateUIBasedOnStatus(boolean isOnline) {
        if (isOnline) {
            availabilitySwitch.setText("Online");
            subtitleMessage.setText("You are now available to take rides.");
            showCustomToast("You are Online", R.drawable.toast_background, R.drawable.ic_toast_icon);
        } else {
            availabilitySwitch.setText("Offline");
            subtitleMessage.setText("Go Offline for taking rest");
            showCustomToast("You are Offline", R.drawable.toast_background_red, R.drawable.cross);
        }
    }


    private void updateAvailabilityStatus(Map<String, Boolean> availabilityStatus, boolean isChecked) {
        ApiService apiService = RetrofitClient.getRetrofitInstance(this).create(ApiService.class);

        Call<Void> call = apiService.updateAvailabilityStatus(availabilityStatus);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    updateUIBasedOnStatus(isChecked);
                } else {
                    Toast.makeText(DriverDashboardActivity.this, "Failed to update status", Toast.LENGTH_SHORT).show();
                    availabilitySwitch.setChecked(!isChecked); // Revert switch to previous state
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(DriverDashboardActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                availabilitySwitch.setChecked(!isChecked); // Revert switch to previous state
            }
        });
    }



    private void loadLeafletMap(double latitude, double longitude) {
        WebView mapWebView = findViewById(R.id.mapWebView);
        Log.d("LocationUtil","call the loadleafletmap function");
        // Call the JavaScript function in the HTML with the latitude and longitude
        String script = "javascript:updateMap(" + latitude + ", " + longitude + ")";
        mapWebView.evaluateJavascript(script, null);
    }


    private void sendLocationToBackend(double latitude, double longitude) {
        try {
            // Create a JSONObject to send latitude and longitude
            JSONObject locationData = new JSONObject();
            locationData.put("latitude", latitude);
            locationData.put("longitude", longitude);

            // Send the location data to the backend using Socket.IO
            mSocket.emit("locationUpdate", locationData);

            Log.d(TAG, "Location sent to backend: Latitude = " + latitude + ", Longitude = " + longitude);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    private void clearSession() {
        SharedPreferences preferences = getSharedPreferences("YourSharedPrefName", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    private void showCustomToast(String message, int backgroundResource, int iconResource) {
        // Custom Toast Implementation
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
