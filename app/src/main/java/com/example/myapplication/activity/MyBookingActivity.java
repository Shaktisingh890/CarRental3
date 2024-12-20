package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyBookingActivity extends AppCompatActivity {

    private RecyclerView recyclerViewBookings;
    private BookingAdapter bookingAdapter;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_booking);

        ImageView backButton = findViewById(R.id.backButton);
        recyclerViewBookings = findViewById(R.id.recyclerViewBookings);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);

        // Set up RecyclerView
        recyclerViewBookings.setLayoutManager(new LinearLayoutManager(this));
        bookingAdapter = new BookingAdapter();
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
        Call<List<CustomerBookingResponse>> call = apiService.getBookingsByUserId();

        call.enqueue(new Callback<List<CustomerBookingResponse>>() {
            @Override
            public void onResponse(Call<List<CustomerBookingResponse>> call, Response<List<CustomerBookingResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CustomerBookingResponse> bookings = response.body();
                    bookingAdapter.setBookingList(bookings); // Update RecyclerView data
                } else {
                    Toast.makeText(MyBookingActivity.this, "Failed to fetch bookings", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CustomerBookingResponse>> call, Throwable t) {
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
        } else if (item.getItemId() == R.id.nav_booking) {
            startActivity(new Intent(MyBookingActivity.this, MyBookingActivity.class));
            return true;
        }
        return false;
    }
}
