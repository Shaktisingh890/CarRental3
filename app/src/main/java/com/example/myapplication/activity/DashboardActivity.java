package com.example.myapplication.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import com.example.myapplication.R;
import com.example.myapplication.adapter.AyaStoriesAdapter;
import com.example.myapplication.adapter.ImageSliderAdapter;
import com.example.myapplication.models.response.Car;
import com.example.myapplication.models.response.CategoryResponse;
import com.example.myapplication.models.response.UserProfileResponse;
import com.example.myapplication.network.ApiService;
import com.example.myapplication.network.RetrofitClient;
import com.example.myapplication.utils.ProgressBarUtils;
import com.example.myapplication.utils.SharedPreferencesManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    private Button lowCostButton, normalCostButton, searchCarButton;
    private Spinner pickupSpinner, dropoffSpinner;

    private Uri selectedImageUri;
    private View progressOverlay;
    private TextView tripStartDateTextView, tripStartTimeTextView, dropOffDateTextView, dropOffTimeTextView;
    private int year, month, day, hour, minute;
    private ProgressBar progressBar;

    private ImageView imageView;
    // Flags to identify which TextView to update
    private boolean isPickupDate = true;
    private boolean isPickupTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isLoggedIn = SharedPreferencesManager.isLoggedIn(this);
        if(!isLoggedIn){
            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();  // Finish current activity
            return;
        }
        setContentView(R.layout.activity_dashboard);

        // Initialize UI components
        lowCostButton = findViewById(R.id.lowCostButton);
        normalCostButton = findViewById(R.id.normalCostButton);
        searchCarButton = findViewById(R.id.search_car_button);
        pickupSpinner = findViewById(R.id.pickupSpinner);
        dropoffSpinner = findViewById(R.id.dropoffSpinner);
        tripStartDateTextView = findViewById(R.id.trip_start_date);
        tripStartTimeTextView = findViewById(R.id.trip_start_time);
        dropOffDateTextView = findViewById(R.id.trip_end_date);
        dropOffTimeTextView = findViewById(R.id.trip_end_time);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);
//        ViewPager2 viewPager2 = findViewById(R.id.ayaStoriesSlider);
//        TabLayout tabLayout = findViewById(R.id.sliderDots);
//        LinearLayout commentsContainer = findViewById(R.id.customerCommentsContainer);
        progressBar = findViewById(R.id.progressBar);
        progressOverlay = findViewById(R.id.progressOverlay);
        imageView = findViewById(R.id.logoImage);

        // Initialize Spinners with data
        String[] locations = {"CT – Cape Town Airport", "Cape Town - City", "Johannesburg - City"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, locations);
        pickupSpinner.setAdapter(adapter2);
        dropoffSpinner.setAdapter(adapter2);

        // Setup Slider
        List<Integer> storyImages = Arrays.asList(R.drawable.sedan, R.drawable.suv, R.drawable.hatchback);
        AyaStoriesAdapter sliderAdapter = new AyaStoriesAdapter(this, storyImages);
//        viewPager2.setAdapter(sliderAdapter);
//        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {}).attach();

        // Add Customer Comments
//        addCustomerComments(commentsContainer);

        // Set current date and time
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        // Set default date and time
        tripStartDateTextView.setText(getFormattedDate(day, month, year));
        tripStartTimeTextView.setText(getFormattedTime(hour, minute));
        dropOffDateTextView.setText(getFormattedDate(day, month, year));
        dropOffTimeTextView.setText(getFormattedTime(hour, minute));

        // Set Click listeners for Date and Time TextViews
        tripStartDateTextView.setOnClickListener(v -> {
            isPickupDate = true;
            showDatePickerDialog();
        });

        dropOffDateTextView.setOnClickListener(v -> {
            isPickupDate = false;
            showDatePickerDialog();
        });

        tripStartTimeTextView.setOnClickListener(v -> {
            isPickupTime = true;
            showTimePickerDialog();
        });

        dropOffTimeTextView.setOnClickListener(v -> {
            isPickupTime = false;
            showTimePickerDialog();
        });



        // Button listeners
        searchCarButton.setOnClickListener(v -> performSearch());
        lowCostButton.setOnClickListener(v -> openCarSelection("low_cost"));
        normalCostButton.setOnClickListener(v -> openCarSelection("normal_cost"));

        // BottomNavigationView listener
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::navigateTo);

        // Initialize ViewPager2 for Image Slider
        ViewPager2 imageSlider = findViewById(R.id.imageSlider);

        // List of image resource IDs
        Integer[] images = {R.drawable.tio, R.drawable.tio1, R.drawable.tio3};
        ImageSliderAdapter adapter = new ImageSliderAdapter(this, Arrays.asList(images));

        // Set the adapter for the slider
        imageSlider.setAdapter(adapter);
    }
    @Override
    public void onBackPressed() {
        // Show logout confirmation dialog
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

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        logoutButton.setOnClickListener(v -> {
            dialog.dismiss();
            // Clear user session and navigate to login
            clearSession(this);
            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Finish DashboardActivity
        });
    }

    public static void clearSession(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("YourSharedPrefName", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear(); // Clear all stored data
        editor.apply();
    }

    // Show DatePickerDialog
    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            // Update the selected date
            year = year1;
            month = month1;
            day = dayOfMonth;

            // Update the correct TextView
            if (isPickupDate) {
                tripStartDateTextView.setText(getFormattedDate(day, month, year));
            } else {
                dropOffDateTextView.setText(getFormattedDate(day, month, year));
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    // Show TimePickerDialog
    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute1) -> {
            // Update the selected time
            hour = hourOfDay;
            minute = minute1;

            // Update the correct TextView
            if (isPickupTime) {
                tripStartTimeTextView.setText(getFormattedTime(hour, minute));
            } else {
                dropOffTimeTextView.setText(getFormattedTime(hour, minute));
            }
        }, hour, minute, false);
        timePickerDialog.show();
    }
    // Format date and time

    // Perform search
    private void performSearch() {
        String pickupSpinnerValue = pickupSpinner.getSelectedItem().toString();
        String dropoffSpinnerValue = dropoffSpinner.getSelectedItem().toString();

        String searchDetails = "Searching for cars at " + pickupSpinnerValue +
                "\nTrip Start: " + tripStartDateTextView.getText() + " " + tripStartTimeTextView.getText() +
                "\nTrip End: " + dropOffDateTextView.getText() + " " + dropOffTimeTextView.getText();
        Toast.makeText(this, searchDetails, Toast.LENGTH_LONG).show();
    }

    // Format date in "Month Day, Year" format
    private String getFormattedDate(int day, int month, int year) {
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return String.format("%s %d, %d", months[month], day, year);
    }

    // Format time in "Hour:Minute AM/PM" format
    private String getFormattedTime(int hour, int minute) {
        String amPm = (hour < 12) ? "AM" : "PM";
        int hour12 = (hour % 12 == 0) ? 12 : (hour % 12);
        return String.format("%02d:%02d %s", hour12, minute, amPm);
    }

    // Open Car Selection Activity
    private void openCarSelection(String costType) {
        ApiService apiService = RetrofitClient.getRetrofitInstance(this).create(ApiService.class);

        apiService.getCarCategories().enqueue(new retrofit2.Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, retrofit2.Response<CategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<String> categories = response.body().getData();
                    Log.d("Categories", "Fetched categories: " + categories);

                    // Pass the categories to the next activity
                    Intent intent = new Intent(DashboardActivity.this, CategorySelectionActivity.class);
                    intent.putStringArrayListExtra("categories", new ArrayList<>(categories));
                    intent.putExtra("costType", costType); // Add costType to the Intent

                    startActivity(intent);
                } else {
                    Toast.makeText(DashboardActivity.this, "Failed to fetch categories.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                Toast.makeText(DashboardActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("CategoryFetchError", "Error: " + t.getMessage());
            }
        });
    }



    // Handle bottom navigation
    private boolean navigateTo(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            return true;
        }  else if (id == R.id.nav_profile) {
            fetchUserProfile();


        }
        else if (item.getItemId() == R.id.nav_booking){
            startActivity(new Intent(DashboardActivity.this, MyBookingActivity.class));
            return true;
        }

        return false;
    }

    // CustomerComment class
    static class CustomerComment {
        private final String name;
        private final String comment;

        public CustomerComment(String name, String comment) {
            this.name = name;
            this.comment = comment;
        }

        public String getName() {
            return name;
        }

        public String getComment() {
            return comment;
        }
    }

    // Interface for date-time selection callback
    public interface OnDateTimeSelectedListener {
        void onDateTimeSelected(String dateTime);
    }

    private void fetchUserProfile() {
        Log.d("fetchUserProfile", "Making API call to fetch user profile");
        ProgressBarUtils.showProgress(progressOverlay, progressBar, true, imageView); // Using utility class

        ApiService apiService = RetrofitClient.getRetrofitInstance(DashboardActivity.this).create(ApiService.class);
        apiService.getUserProfile().enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                ProgressBarUtils.showProgress(progressOverlay, progressBar, false, imageView); // Using utility class

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
                    Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
                    intent.putExtra("fullName", user.getData().getFullName());
                    intent.putExtra("email", user.getData().getEmail());
                    intent.putExtra("phoneNumber", user.getData().getPhoneNumber());
                    intent.putExtra("address", user.getData().getAddress());
                    intent.putExtra("imgUrl", user.getData().getImgUrl());
                    startActivity(intent);
                } else {
                    // Log the error body
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                        Log.d("fetchUserProfile", "Error Body: " + errorBody);
                    } catch (IOException e) {
                        Log.e("fetchUserProfile", "Error parsing error body: " + e.getMessage(), e);
                    }
                    Log.d("fetchUserProfile", "Failed to fetch profile. Error Code: " + response.code());
                    Toast.makeText(DashboardActivity.this, "Failed to fetch profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                Log.e("fetchUserProfile", "Error: " + t.getMessage(), t);
                Toast.makeText(DashboardActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
