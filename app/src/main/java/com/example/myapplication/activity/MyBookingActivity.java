package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.response.CustomerBookingResponse;
import com.example.myapplication.network.ApiService;
import com.example.myapplication.network.RetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.example.myapplication.adapter.BookingAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyBookingActivity extends AppCompatActivity {

    private static final String TAG = "MyBookingActivity"; // For logging
    private RecyclerView recyclerViewBookings;
    private BookingAdapter bookingAdapter;
    private ApiService apiService;

    List<CustomerBookingResponse.BookingData> bookings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_booking);

        ImageView backButton = findViewById(R.id.backButton);
        recyclerViewBookings = findViewById(R.id.recyclerViewBookings);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);

        // Set up RecyclerView
        recyclerViewBookings.setLayoutManager(new LinearLayoutManager(this));
        bookingAdapter = new BookingAdapter(getApplicationContext());
        bookingAdapter.setBookingList(bookings);
        recyclerViewBookings.setAdapter(bookingAdapter);

        // Initialize Retrofit
        apiService = RetrofitClient.getRetrofitInstance(this).create(ApiService.class);
        // Fetch bookings on activity creation
        fetchBookingsByUserId();

        // Back button click listener
        backButton.setOnClickListener(view -> finish());

        // BottomNavigationView listener
        bottomNavigationView.setSelectedItemId(R.id.nav_booking);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::navigateTo);
    }

    private void fetchBookingsByUserId() {
        Call<ResponseBody> call = apiService.getBookingsByUserId();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String jsonString = response.body().string();

                        // Parse the JSON manually
                        JSONObject jsonObject = new JSONObject(jsonString);

                        if (jsonObject.getBoolean("success")) {
                            JSONArray dataArray = jsonObject.getJSONArray("data");

                            Gson gson = new Gson();

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject bookingObject = dataArray.getJSONObject(i);

                                // Convert JSON object to CustomerBookingResponse
                                CustomerBookingResponse.BookingData booking = gson.fromJson(bookingObject.toString(), CustomerBookingResponse.BookingData.class);
                                bookings.add(booking);
                            }

                            // Log the bookings list
                            Log.d(TAG, "Bookings fetched: " + bookings);

                            // Update RecyclerView with the parsed list
                            bookingAdapter.setBookingList(bookings);
                        } else {
                            String message = jsonObject.optString("message", "Failed to fetch bookings");
                            Toast.makeText(MyBookingActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MyBookingActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MyBookingActivity.this, "Failed to fetch bookings", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MyBookingActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean navigateTo(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            startActivity(new Intent(MyBookingActivity.this, DashboardActivity.class));
            return true;
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(MyBookingActivity.this, ProfileActivity.class));
            return true;
        } else if (id == R.id.nav_booking) {
            startActivity(new Intent(MyBookingActivity.this, MyBookingActivity.class));
            return true;
        }
        return false;
    }
}
