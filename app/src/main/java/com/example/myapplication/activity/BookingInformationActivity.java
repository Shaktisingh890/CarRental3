package com.example.myapplication.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.models.response.CustomerCarResponse;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BookingInformationActivity extends AppCompatActivity {

    private static final String TAG = "BookingInformationActivity";

    private ImageView carImage;
    private Switch switchDriver;
    private EditText pickUpLocation, returnLocation, pickUpDateTime, returnDateTime;
    private Button btnContinue;
    private ImageView backButton;
    private CustomerCarResponse selectedCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_information);

        // Initialize views
        carImage = findViewById(R.id.carImage);
        switchDriver = findViewById(R.id.switchDriver);
        pickUpLocation = findViewById(R.id.pickUpLocation);
        returnLocation = findViewById(R.id.returnLocation);
        pickUpDateTime = findViewById(R.id.pickUpDateTime);
        returnDateTime = findViewById(R.id.returnDateTime);
        btnContinue = findViewById(R.id.btnContinue);
        backButton = findViewById(R.id.back_button);

        // Retrieve the car object from the intent
        Intent intent = getIntent();
        selectedCar = intent.getParcelableExtra("SELECTED_CAR");

        if (selectedCar != null) {
            Log.d(TAG, "Selected Car: " + selectedCar.toString());
            populateCarImage(selectedCar);
        } else {
            Log.e(TAG, "No car details received!");
        }

        switchDriver.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // If switch is ON, make fields editable
                pickUpLocation.setFocusable(true);
                pickUpLocation.setFocusableInTouchMode(true);
                pickUpLocation.setText(""); // Optionally clear the field

                returnLocation.setFocusable(true);
                returnLocation.setFocusableInTouchMode(true);
                returnLocation.setText(""); // Optionally clear the field
            } else {
                // If switch is OFF, make fields non-editable and set values from selectedCar
                pickUpLocation.setText(selectedCar != null ? selectedCar.getPickupLocation() : "");
                pickUpLocation.setFocusable(false);
                pickUpLocation.setFocusableInTouchMode(false);

                returnLocation.setText(selectedCar != null ? selectedCar.getDropoffLocation() : "");
                returnLocation.setFocusable(false);
                returnLocation.setFocusableInTouchMode(false);
            }
        });

        // Back button functionality
        backButton.setOnClickListener(v -> finish());

        // Set click listeners for date and time pickers
        pickUpDateTime.setOnClickListener(v -> showDateTimePicker(dateTime -> pickUpDateTime.setText(dateTime)));
        returnDateTime.setOnClickListener(v -> showDateTimePicker(dateTime -> returnDateTime.setText(dateTime)));

        // Continue button functionality
        btnContinue.setOnClickListener(v -> handleContinueButton());
    }

    // Populate the car image using Glide
    private void populateCarImage(CustomerCarResponse car) {
        List<String> carImages = car.getImages();

        if (carImages != null && !carImages.isEmpty()) {
            Glide.with(this)
                    .load(carImages.get(0)) // Load the first image from the list
                    .placeholder(R.drawable.profile) // Placeholder while loading
                    .error(R.drawable.profile) // Fallback image if there's an error
                    .into(carImage);
        } else {
            carImage.setImageResource(R.drawable.profile); // Fallback if no images are available
        }
    }

    // Handle the Continue button click
    private void handleContinueButton() {
        String pickUp = pickUpLocation.getText().toString();
        String returnLoc = returnLocation.getText().toString();
        String pickUpTime = pickUpDateTime.getText().toString();
        String returnTime = returnDateTime.getText().toString();

        if (pickUp.isEmpty() || returnLoc.isEmpty() || pickUpTime.isEmpty() || returnTime.isEmpty()) {
            Toast.makeText(BookingInformationActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else {
            // Create an Intent to start PersonalInfoActivity
            Intent intent = new Intent(BookingInformationActivity.this, PersonalInfoActivity.class);

            // Pass data to the next activity
            intent.putExtra("pickupLocation", pickUp);
            intent.putExtra("returnLocation", returnLoc);
            intent.putExtra("pickupDateTime", pickUpTime);
            intent.putExtra("returnDateTime", returnTime);

            // Pass the selectedCar object as well
            intent.putExtra("SELECTED_CAR", selectedCar);

            // Start PersonalInfoActivity
            startActivity(intent);
        }
    }

    // Method to show DatePicker and TimePicker dialogs
    private void showDateTimePicker(OnDateTimeSelectedListener listener) {
        final Calendar calendar = Calendar.getInstance();

        // Show DatePickerDialog
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            // Show TimePickerDialog after date is selected
            new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                // Format and return the selected date and time
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                String formattedDateTime = sdf.format(calendar.getTime());
                listener.onDateTimeSelected(formattedDateTime);
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    // Interface for handling date and time selection
    private interface OnDateTimeSelectedListener {
        void onDateTimeSelected(String dateTime);
    }
}
