package com.example.myapplication.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.CarListAdapter;
import com.example.myapplication.models.response.CustomerCarResponse;

import java.util.ArrayList;

public class CustomerCarDetailsActivity extends AppCompatActivity {

    // RecyclerView and Adapter
    private RecyclerView carRecyclerView;
    private CarListAdapter carListAdapter;

    // Store car data
    private ArrayList<CustomerCarResponse> carList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_car_details);

        // Initialize RecyclerView
        carRecyclerView = findViewById(R.id.carRecyclerView);
        carRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get car data from intent (Parcelable ArrayList)
        carList = getIntent().getParcelableArrayListExtra("CAR_DATA");

        if (carList != null && !carList.isEmpty()) {
            Log.d("CarDetailsActivity", "Received " + carList.size() + " cars.");

            // Initialize adapter and set data
            carListAdapter = new CarListAdapter(this, carList);
            carRecyclerView.setAdapter(carListAdapter);
        } else {
            Toast.makeText(this, "Car details not available", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
