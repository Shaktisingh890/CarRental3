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
import com.example.myapplication.models.response.CustomerCarResponse;
import java.util.List;

public class CarDetailsActivity extends AppCompatActivity {

    private static final String TAG = "CarDetailsActivity";
    private CustomerCarResponse selectedCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details); // Ensure this matches your layout file

        // Retrieve the car object from the intent
        Intent intent = getIntent();
        selectedCar = intent.getParcelableExtra("SELECTED_CAR");

        // Log the selected car for debugging
        if (selectedCar == null) {
            Log.e(TAG, "No car details received!");
        } else {
            Log.d(TAG, "Car Details: " + selectedCar.toString());
        }


        if (selectedCar != null) {
            Log.d(TAG, "Selected Car: " + selectedCar.toString());
            Log.d(TAG, "Brand: " + selectedCar.getBrand());
            Log.d(TAG, "Price Per Day: " + selectedCar.getPricePerDay());
            Log.d(TAG, "Images: " + selectedCar.getImages());
        } else {
            Log.e(TAG, "selectedCar is null.");
        }

        // Set up the back button functionality
        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish()); // Navigate back to the previous screen

        // Set up the "Book Now" button functionality
        Button bookNowButton = findViewById(R.id.bookNowButton);
        bookNowButton.setVisibility(View.VISIBLE);
        bookNowButton.setOnClickListener(v -> {
            Intent bookingIntent = new Intent(CarDetailsActivity.this, BookingInformationActivity.class);
            bookingIntent.putExtra("SELECTED_CAR", selectedCar); // Pass the selectedCar object
            startActivity(bookingIntent);
        });


        // Populate car data in the UI
        populateCarDetails();
    }

    /**
     * Populate the UI with the details of the selected car.
     */
    private void populateCarDetails() {
        if (selectedCar == null) {
            Log.e(TAG, "Cannot populate car details as no car is selected!");
            return;
        }

        // Set car name
        TextView carName = findViewById(R.id.car_name);
        if (selectedCar.getBrand() != null && !selectedCar.getBrand().isEmpty()) {
            carName.setText(selectedCar.getBrand());
        } else {
            carName.setText("Unknown Brand"); // Fallback for missing data
        }

        // Set car price
        TextView carPrice = findViewById(R.id.car_price);
        int pricePerDay = selectedCar.getPricePerDay();
        if (pricePerDay > 0) {
            carPrice.setText(String.format("$%d/day", pricePerDay));
        } else {
            carPrice.setText("Price not available"); // Fallback for invalid price
        }
        // Set car seats
        TextView carSeats = findViewById(R.id.car_seats);
        int numberOfSeats = selectedCar.getSeats();
        if (numberOfSeats > 0) {
            carSeats.setText(String.format("%d", numberOfSeats));
        } else {
            carSeats.setText("Seats not available"); // Fallback for invalid seat data
        }

        TextView carGps = findViewById(R.id.car_gps);
        TextView carBluetooth = findViewById(R.id.car_bluetooth);
        TextView carAirConditoner = findViewById(R.id.car_conditioning);
        List<String> carFeatures = selectedCar.getFeatures(); // Assuming getFeatures() returns a List<String>

// Check if the list contains "GPS" (case-insensitive for robustness)
        if (carFeatures != null && carFeatures.contains("GPS")) {
            carGps.setText("YES");
        } else {
            carGps.setText("NO"); // Fallback if GPS is not available in the features
        }


        if (carFeatures != null && carFeatures.contains("Bluetooth")) {
            carBluetooth.setText("YES");
        } else {
            carBluetooth.setText("NO"); // Fallback if GPS is not available in the features
        }


        if (carFeatures != null && carFeatures.contains("Air Conditioning")) {
            carAirConditoner.setText("YES");
        } else {
            carAirConditoner.setText("NO"); // Fallback if GPS is not available in the features
        }



        // Set car description
        TextView carDescription = findViewById(R.id.car_description);
        String description = selectedCar.getDescription();
        carDescription.setText(description);

        TextView carFuelTpye = findViewById(R.id.car_fuel);
        String FuelType = selectedCar.getFuelType();
        carFuelTpye.setText(FuelType);

        TextView carMilage = findViewById(R.id.car_milage);
        int mileage = selectedCar.getMilage(); // Assuming getMileage() returns an int
        carMilage.setText(String.format("%d km", mileage)); // Display mileage in kilometers


        // Get the image views
        ImageView carImage = findViewById(R.id.car_image);
        ImageView carImage0 = findViewById(R.id.car_image0);
        ImageView carImage1 = findViewById(R.id.car_image1);
        ImageView carImage2 = findViewById(R.id.car_image2);
        ImageView carImage3 = findViewById(R.id.car_image3);

        // Set car images using Glide
        List<String> carImages = selectedCar.getImages();
        if (carImages != null && !carImages.isEmpty()) {
            // Load each image safely if the URL is present
            if (carImages.size() > 0) {
                Glide.with(this)
                        .load(carImages.get(0))
                        .placeholder(R.drawable.profile)
                        .error(R.drawable.profile)
                        .into(carImage0);
            } else {
                carImage0.setImageResource(R.drawable.profile);
            }

            if (carImages.size() > 1) {
                Glide.with(this)
                        .load(carImages.get(1))
                        .placeholder(R.drawable.profile)
                        .error(R.drawable.profile)
                        .into(carImage1);
            } else {
                carImage1.setImageResource(R.drawable.profile);
            }

            if (carImages.size() > 2) {
                Glide.with(this)
                        .load(carImages.get(2))
                        .placeholder(R.drawable.profile)
                        .error(R.drawable.profile)
                        .into(carImage2);
            } else {
                carImage2.setImageResource(R.drawable.profile);
            }

            if (carImages.size() > 3) {
                Glide.with(this)
                        .load(carImages.get(3))
                        .placeholder(R.drawable.profile)
                        .error(R.drawable.profile)
                        .into(carImage3);
            } else {
                carImage3.setImageResource(R.drawable.profile);
            }

            // The main image (if present)
            Glide.with(this)
                    .load(carImages.get(0))
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.profile)
                    .into(carImage);
        } else {
            // Fallback to placeholder if no images are available
            carImage.setImageResource(R.drawable.profile);
            carImage0.setImageResource(R.drawable.profile);
            carImage1.setImageResource(R.drawable.profile);
            carImage2.setImageResource(R.drawable.profile);
            carImage3.setImageResource(R.drawable.profile);
        }
    }

}
