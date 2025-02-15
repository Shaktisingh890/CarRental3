package com.example.myapplication.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.example.myapplication.activity.DriverDashboardActivity;

public class LocationUtils {

    private static final String TAG = "LocationUtil";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private static final int GPS_REQUEST_CODE = 1002;

    private FusedLocationProviderClient fusedLocationClient;
    private Context context;
    private LocationUpdateListener listener;
    private LocationCallback locationCallback;

    public LocationUtils(Context context, LocationUpdateListener listener) {
        this.context = context;
        this.listener = listener;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    // Check if location permissions are granted
    public boolean isLocationPermissionGranted() {
        boolean isFineLocationGranted = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean isCoarseLocationGranted = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        Log.d(TAG, "isLocationPermissionGranted: Fine: " + isFineLocationGranted + ", Coarse: " + isCoarseLocationGranted);
        return isFineLocationGranted && isCoarseLocationGranted;
    }

    // Request location permissions if not granted
    public void requestLocationPermissions(Activity activity) {
        Log.d(TAG, "requestLocationPermissions: Requesting location permissions");
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    // Enable the location services (GPS) by opening the settings
    public void enableLocationServices() {
        Log.d(TAG, "enableLocationServices: Checking if location is enabled");
        if (!isLocationEnabled()) {
            Log.d(TAG, "enableLocationServices: Location is disabled, opening settings");
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            ((Activity) context).startActivityForResult(intent, GPS_REQUEST_CODE);

        } else {
            Log.d(TAG, "enableLocationServices: Location is enabled, fetching location updates");
            fetchLocationWithUpdates();  // Use location updates instead of just fetching the last location
        }
    }

    // Check if location is enabled (GPS)
    public boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Log.d(TAG, "isLocationEnabled: " + isEnabled);
        return isEnabled;
    }

    // Fetch location with updates for real-time accuracy
    public void fetchLocationWithUpdates() {
        Log.d(TAG, "fetchLocationWithUpdates: Fetching location updates");
        if (isLocationPermissionGranted()) {
            if (isLocationEnabled()) {
                LocationRequest locationRequest = LocationRequest.create();
                locationRequest.setInterval(10000); // 10 seconds interval for location updates
                locationRequest.setFastestInterval(5000); // Fastest interval for location updates
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                locationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull com.google.android.gms.location.LocationResult locationResult) {
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            Location location = locationResult.getLastLocation();
                            if (location != null) {
                                Log.d(TAG, "onLocationResult: Location fetched: " + location.toString());
                                listener.onLocationFetched(location);
                            }
                        }
                    }

                    @Override
                    public void onLocationAvailability(@NonNull com.google.android.gms.location.LocationAvailability locationAvailability) {
                        if (!locationAvailability.isLocationAvailable()) {
                            Log.d(TAG, "onLocationAvailability: Location service is not available");
                            listener.onLocationError("Location service is not available.");
                        }
                    }
                };

                if (ActivityCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "fetchLocationWithUpdates: Permissions are not granted.");
                    return;
                }
                Log.d(TAG, "fetchLocationWithUpdates: Requesting location updates");
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
            } else {
                Log.d(TAG, "fetchLocationWithUpdates: Location services are disabled");
                listener.onLocationError("Location services are disabled. Please enable it.");
            }
        } else {
            Log.d(TAG, "fetchLocationWithUpdates: Location permission is not granted");
            listener.onLocationError("Location permission is not granted.");
        }
    }

    // Handle location permission result
    public void handlePermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "handlePermissionsResult: Permission granted, enabling location services");
                enableLocationServices();
               fetchLocationWithUpdates();

            } else {
                Log.d(TAG, "handlePermissionsResult: Permission denied, cannot access location");
                listener.onLocationError("Permission denied. Can't access location.");
            }
        }
    }

    public void restartActivity() {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            Intent intent = activity.getIntent();  // Get the current intent
            activity.finish();  // Finish the current activity
            activity.startActivity(intent);  // Start the activity again
        }
    }

    // Interface for location updates
    public interface LocationUpdateListener {
        void onLocationFetched(Location location);
        void onLocationError(String errorMessage);
    }
}
