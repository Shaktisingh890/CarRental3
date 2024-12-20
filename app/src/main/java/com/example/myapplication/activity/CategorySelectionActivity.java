package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.models.response.Car;

import java.util.List;

public class CategorySelectionActivity extends AppCompatActivity {

    private LinearLayout categoryContainer;
    private TextView carName,carSeats,carPrice,carCategory,carDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selection);

        // Set up the back button
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Initialize the container where categories will be dynamically added
        categoryContainer = findViewById(R.id.categoryContainer);

        // Get the car list passed from the DashboardActivity via Intent
        Intent intent = getIntent();
        List<Car> carList = intent.getParcelableArrayListExtra("carList");

        Log.d("CategorySelectionActivity", "Received carList: " + (carList != null ? carList.size() : "null"));

        // If the car list is not null, populate the categories
        if (carList != null) {
            populateCategories(carList);
        } else {
            Toast.makeText(this, "No categories available.", Toast.LENGTH_SHORT).show();
        }
    }

    private void populateCategories(List<Car> carList) {
        Log.d("CategorySelectionActivity", "Populating categories...");
        for (Car car : carList) {
            addCategoryItem(car);
        }
    }

    private void addCategoryItem(Car car) {
        Log.d("CategorySelectionActivity", "Adding category for car: " + car.getBrand() + " " + car.getModel());

        // Inflate a new car item layout
        View categoryItemView = LayoutInflater.from(this).inflate(R.layout.car_item, categoryContainer, false);

        // Initialize the car name TextView
        carName = categoryItemView.findViewById(R.id.car_name); // Ensure we're getting the correct reference
        carCategory = categoryItemView.findViewById(R.id.carCategory);
        carPrice = categoryItemView.findViewById(R.id.carPrice);
        carSeats = categoryItemView.findViewById(R.id.carSeats);
        carDetails= categoryItemView.findViewById(R.id.carDetails);
        if (carName != null) {
            // Set the car brand and model as the name
            carName.setText(car.getBrand() + " " + car.getModel());
            Log.d("CategorySelectionActivity", "Car name set: " + car.getBrand() + " " + car.getModel());
        } else {
            Log.e("CategorySelectionActivity", "TextView carName is null!");
        }
        if (carCategory != null) {
            // Set the car year
            carCategory.setText("Category: " + car.getCategory());
            Log.d("CategorySelectionActivity", "Car Category set: " + car.getCategory());
        } else {
            Log.e("CategorySelectionActivity", "TextView carCategory is null!");
        }
        if (carPrice != null) {
            // Set the car year
            carPrice.setText("$ " + car.getPricePerDay()+""+"/day");
            Log.d("CategorySelectionActivity", "Car Price set: " + car.getPricePerDay());
        } else {
            Log.e("CategorySelectionActivity", "TextView carPrice is null!");
        }
        if (carSeats != null) {
            // Set the car year
            carSeats.setText("Seats: " + car.getSeats());
            Log.d("CategorySelectionActivity", "Car seats set: " + car.getSeats());
        } else {
            Log.e("CategorySelectionActivity", "TextView carSeats is null!");
        }
        if (carDetails != null) {
            // Set the car year
            carDetails.setText("Fuel: " + car.getFuelType());
            Log.d("CategorySelectionActivity", "Car Fuel set: " + car.getFuelType());
        } else {
            Log.e("CategorySelectionActivity", "TextView carDetails is null!");
        }

        // Set the first image from the list (if available) using Glide
        ImageView carImageView = categoryItemView.findViewById(R.id.carImage);

        if (carImageView != null) {
            if (car.getImages() != null && !car.getImages().isEmpty()) {
                Glide.with(this)
                        .load(car.getImages().get(0)) // Load the first image
                        .placeholder(R.drawable.default_car_image) // Fallback image
                        .into(carImageView);
                Log.d("CategorySelectionActivity", "Loaded car image: " + car.getImages().get(0));
            } else {
                carImageView.setImageResource(R.drawable.default_car_image); // Default image
                Log.d("CategorySelectionActivity", "No images available, using default image.");
            }
        } else {
            Log.e("CategorySelectionActivity", "ImageView carImage is null!");
        }

        // Set a click listener for each category item
        categoryItemView.setOnClickListener(v -> openSubCategorySelection(car.getCategory()));

        // Add the inflated view to the container
        categoryContainer.addView(categoryItemView);
    }

    private void openSubCategorySelection(String category) {
        Log.d("CategorySelectionActivity", "Opening sub-category selection for category: " + category);

        Intent intent = new Intent(CategorySelectionActivity.this, SubCategorySelectionActivity.class);
        intent.putExtra("CATEGORY", category); // Pass the selected category to the next activity
        startActivity(intent);
    }
}
