package com.example.myapplication.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.DriverAdapter;
import com.example.myapplication.models.response.Driver;
import com.example.myapplication.network.ApiService;
import com.example.myapplication.network.RetrofitClient;
import com.example.myapplication.models.response.AllDriversResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Partner_DriverListActivity extends AppCompatActivity {
    private RecyclerView recyclerViewDrivers;
    private DriverAdapter driverAdapter;
    private List<Driver> driverList;

    public String bookingId,notification_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partner_driverlist);
        bookingId=getIntent().getStringExtra("bookingId");
        notification_id=getIntent().getStringExtra("notification_id");

        recyclerViewDrivers = findViewById(R.id.recyclerViewDrivers);
        recyclerViewDrivers.setLayoutManager(new LinearLayoutManager(this));

        driverList = new ArrayList<>();
        driverAdapter = new DriverAdapter(this, driverList,bookingId,notification_id);
        recyclerViewDrivers.setAdapter(driverAdapter);

        // Fetch all drivers
        fetchAllDrivers();
    }

    private void fetchAllDrivers() {
        ApiService apiService = RetrofitClient.getRetrofitInstance(this).create(ApiService.class);

        Call<AllDriversResponse> call = apiService.getAllDrivers();
        call.enqueue(new Callback<AllDriversResponse>() {
            @Override
            public void onResponse(Call<AllDriversResponse> call, Response<AllDriversResponse> response) {
                if (response.isSuccessful()) {
                    AllDriversResponse driversResponse = response.body();
                    if (driversResponse != null && driversResponse.getStatusCode() == 200) {
                        List<Driver> drivers = driversResponse.getData();

                        // Update RecyclerView
                        driverList.clear();
                        driverList.addAll(drivers);
                        driverAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(
                                Partner_DriverListActivity.this,
                                "No drivers found!",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                } else {
                    Toast.makeText(
                            Partner_DriverListActivity.this,
                            "Failed to fetch drivers",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<AllDriversResponse> call, Throwable t) {
                Toast.makeText(
                        Partner_DriverListActivity.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}
