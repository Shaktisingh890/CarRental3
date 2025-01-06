package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.models.response.CarDetailsResponse;
import java.util.List;

public class CarDetailsActivity1 extends AppCompatActivity {

    private static final String TAG = "CarDetailsActivity1";
    private CarDetailsResponse.Car selectedCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details); // Ensure this matches your layout file

        // Retrieve the car object from the intent
        Intent intent = getIntent();
        selectedCar = intent.getParcelableExtra("SELECTED_CAR");

        if (selectedCar == null) {
            Log.e(TAG, "No car details received!");
            finish();
            return;
        }

        Log.d(TAG, "Selected Car: " + selectedCar);
        Log.d("myimages", "Images: " + selectedCar.getImages());

        // Set up the back button functionality
        setupBackButton();



        // Populate car data in the UI
        populateCarDetails();
    }

    /**
     * Set up the back button functionality.
     */
    private void setupBackButton() {
        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());
    }

    /**
     * Set up the "Book Now" button functionality.
     */


    /**
     * Populate the UI with the details of the selected car.
     */
    private void populateCarDetails() {
        // Set car name
        TextView carName = findViewById(R.id.car_name);
        carName.setText(getValidString(selectedCar.getBrand(), "Unknown Brand"));

        // Set car price
        TextView carPrice = findViewById(R.id.car_price);
        carPrice.setText(selectedCar.getPricePerDay() > 0
                ? String.format("$%d/day", selectedCar.getPricePerDay())
                : "Price not available");

        // Set car seats
        TextView carSeats = findViewById(R.id.car_seats);
        carSeats.setText(selectedCar.getSeats() > 0
                ? String.format("%d", selectedCar.getSeats())
                : "Seats not available");

        // Set car features
        setFeatureAvailability(R.id.car_gps, "GPS");
        setFeatureAvailability(R.id.car_bluetooth, "Bluetooth");
        setFeatureAvailability(R.id.car_conditioning, "Air Conditioning");

//        // Set car description
        TextView carDescription = findViewById(R.id.car_description);
        carDescription.setText(getValidString(selectedCar.getDescription(), "No description available"));

        // Set car fuel type
        TextView carFuelType = findViewById(R.id.car_fuel);
        carFuelType.setText(getValidString(selectedCar.getFuelType(), "Unknown"));

        // Set car mileage
        TextView carMileage = findViewById(R.id.car_milage);
        carMileage.setText(selectedCar.getMilage() > 0
                ? String.format("%d km", selectedCar.getMilage())
                : "Mileage not available");

        List<String> images1 = selectedCar.getImages();



        // Load car images
        loadCarImages(selectedCar.getImages());

    }

    /**
     * Helper method to set feature availability in the UI.
     */
    private void setFeatureAvailability(int textViewId, String feature) {
        TextView featureTextView = findViewById(textViewId);
        List<String> features = selectedCar.getFeatures();
        featureTextView.setText(features != null && features.contains(feature) ? "YES" : "NO");
    }

    /**
     * Helper method to load car images using Glide.
     */
    private void loadCarImages(List<String> images) {
        if (images == null || images.isEmpty()) {
            setDefaultImages();
            return;
        }
        Log.d("myimages", "Car Images: " + images);
        // ImageView IDs
        int[] imageViews = {
                R.id.car_image,
                R.id.car_image0,
                R.id.car_image1,
                R.id.car_image2,
                R.id.car_image3
        };

        for (int i = 0; i < 4; i++) {
            ImageView imageView = findViewById(imageViews[i]);
            if (i < images.size()) {
                Glide.with(this)
                        .load(images.get(i))
                        .placeholder(R.drawable.profile)
                        .error(R.drawable.profile)
                        .into(imageView);
            } else {
                Log.d("my element", String.valueOf(i));
                imageView.setImageResource(R.drawable.profile);
            }
        }
    }

    /**
     * Helper method to set default images when no images are available.
     */
    private void setDefaultImages() {
        int[] imageViews = {
                R.id.car_image,
                R.id.car_image0,
                R.id.car_image1,
                R.id.car_image2,
                R.id.car_image3
        };



        for (int imageViewId : imageViews) {
            ImageView imageView = findViewById(imageViewId);
            imageView.setImageResource(R.drawable.profile);
        }
    }

    /**
     * Helper method to return a valid string or a default fallback.
     */
    private String getValidString(String value, String fallback) {
        return (value != null && !value.isEmpty()) ? value : fallback;
    }
}
