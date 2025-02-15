package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.BookingAdapter;
import com.example.myapplication.adapter.PartnerBookingAdapter;
import com.example.myapplication.models.response.CustomerBookingResponse;
import com.example.myapplication.network.ApiService;
import com.example.myapplication.network.RetrofitClient;
import com.example.myapplication.utils.NavigationUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PartnerBookingActivity extends AppCompatActivity {

    private static final String TAG = "MyBookingActivity"; // For logging
    private RecyclerView recyclerViewBookings;
    private PartnerBookingAdapter partnerBookingAdapter;
    private ApiService apiService;

    List<CustomerBookingResponse.BookingData> bookings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_booking);
        recyclerViewBookings = findViewById(R.id.recyclerViewBookings);
        ImageView backButton = findViewById(R.id.backButton);
        // Toolbar title and search icon setup
//        TextView toolbarTitle = findViewById(R.id.toolbarTitle);
//        ImageView toolbarSearchIcon = findViewById(R.id.toolbarSearchIcon);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView1);



       bottomNavigationView.setSelectedItemId(R.id.nav_booking);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle the selection using NavigationUtil
                return NavigationUtil.handleBottomNavigationSelection(PartnerBookingActivity.this, item.getItemId());
            }
        });;
        // Set up RecyclerView
        recyclerViewBookings.setLayoutManager(new LinearLayoutManager(this));
        partnerBookingAdapter = new PartnerBookingAdapter(getApplicationContext());
        partnerBookingAdapter.setBookingList(bookings);
        recyclerViewBookings.setAdapter(partnerBookingAdapter);


        // Back button click listener
        backButton.setOnClickListener(view -> finish());
        // Initialize Retrofit
        apiService = RetrofitClient.getRetrofitInstance(this).create(ApiService.class);
        // Fetch bookings on activity creation
        fetchBookingsByUserId();





    }


    private void fetchBookingsByUserId() {
        Call<ResponseBody> call = apiService.getBookingsByPartnerId();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String jsonString = response.body().string();

                        // Log the raw JSON response
                        Log.d(TAG, "Raw JSON response: " + jsonString);

                        // Parse the JSON manually
                        JSONObject jsonObject = new JSONObject(jsonString);

                        // Log the full JSONObject after parsing
                        Log.d(TAG, "Parsed JSON Object: " + jsonObject);

                        if (jsonObject.getBoolean("success")) {
                            // If the "success" field is true, parse the data array
                            JSONArray dataArray = jsonObject.getJSONArray("data");

                            // Log the data array length
                            Log.d(TAG, "Data Array Length: " + dataArray.length());

                            Gson gson = new Gson();

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject bookingObject = dataArray.getJSONObject(i);

                                // Log the booking object
                                Log.d(TAG, "Booking Object " + i + ": " + bookingObject);

                                // Convert JSON object to CustomerBookingResponse
                                CustomerBookingResponse.BookingData booking = gson.fromJson(bookingObject.toString(), CustomerBookingResponse.BookingData.class);
                                bookings.add(booking);
                            }

                            // Log the final bookings list
                            Log.d(TAG, "Bookings fetched: " + bookings);

                            // Update RecyclerView with the parsed list
                            partnerBookingAdapter.setBookingList(bookings);
                        } else {
                            // Log and display any failure message from the server
                            String message = jsonObject.optString("message", "Failed to fetch bookings");
                            Log.e(TAG, "Error message from server: " + message);
                            Toast.makeText(PartnerBookingActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        // Handle specific JSON parsing exceptions
                        Log.e(TAG, "JSONException: " + e.getMessage());
                        e.printStackTrace();
                        Toast.makeText(PartnerBookingActivity.this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        // Handle specific IO exceptions (if any)
                        Log.e(TAG, "IOException: " + e.getMessage());
                        e.printStackTrace();
                        Toast.makeText(PartnerBookingActivity.this, "Error reading response body", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        // Generic catch block for unexpected exceptions
                        Log.e(TAG, "Exception: " + e.getMessage());
                        e.printStackTrace();
                        Toast.makeText(PartnerBookingActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Log the failure response status and message
                    Log.e(TAG, "Failed to fetch bookings: " + response.code() + " - " + response.message());
                    Toast.makeText(PartnerBookingActivity.this, "Failed to fetch bookings", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Log the error message from failure
                Log.e(TAG, "Request failed: " + t.getMessage());
                Toast.makeText(PartnerBookingActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
