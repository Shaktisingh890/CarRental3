package com.example.myapplication.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.RideAdapter;
import com.example.myapplication.models.response.Ride;

import java.util.ArrayList;
import java.util.List;

public class MyRidesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MyRidesActivity", "onCreate called");
        setContentView(R.layout.activity_my_rides);
        // Find the backArrow ImageView
        ImageView backArrow = findViewById(R.id.backArrow);
        // Set a click listener to handle the back navigation
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the previous page
                finish();
            }
        });

        RecyclerView rideRecyclerView = findViewById(R.id.rideRecyclerView);
        Log.d("MyRidesActivity", "RecyclerView initialized");
        rideRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Ride> rides = new ArrayList<>();
        rides.add(new Ride("Sun, 21st Apr . 5:00 PM", "₹ 350", "Panniyankara, Kozhikode", "HighLite, Kozhikode", "Completed"));
        rides.add(new Ride("Mon, 1st Apr . 4:00 PM", "₹ 350", "Panniyankara, Kozhikode", "Meenchanda, Kozhikode", "Cancelled"));

        RideAdapter adapter = new RideAdapter(rides);
        rideRecyclerView.setAdapter(adapter);
        Log.d("MyRidesActivity", "Adapter set with data");
    }

}
