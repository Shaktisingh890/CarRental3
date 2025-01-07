package com.example.myapplication.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.DatePicker;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.models.request.BookingRequest;
import com.example.myapplication.models.response.BookingResponse;
import com.example.myapplication.models.response.CustomerCarResponse;
import com.example.myapplication.network.ApiService;
import com.example.myapplication.network.RetrofitClient;
import com.example.myapplication.utils.ProgressBarUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmationActivity extends AppCompatActivity {

    private static final String TAG = "ConfirmationActivity";  // Tag for logging
    private EditText pickupDateEditText;
    private EditText returnDateEditText;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    private String pickUpDateTime, returnDateTime,pickUpLocation,returnLocation,partnerId,carId;
    private boolean isDriverRequired;

    private int totalRent;

    private View progressOverlay;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        // Initialize UI components
        ImageView backIcon = findViewById(R.id.backIcon);
        Button confirmButton = findViewById(R.id.confirmButton);
        TextView carNameTextView = findViewById(R.id.car_name);
        TextView carPriceTextView = findViewById(R.id.car_price);
        TextView pickupLocationTextView = findViewById(R.id.pickup_location);
        TextView returnLocationTextView = findViewById(R.id.return_location);
        pickupDateEditText = findViewById(R.id.pickup_date);
        returnDateEditText = findViewById(R.id.return_date);
        TextView totalRentTextView = findViewById(R.id.total_rent);
        TextView totalPaymentTextView = findViewById(R.id.total_payment);
        TextView perDayPriceTextView = findViewById(R.id.perday_price);
        progressBar = findViewById(R.id.progressBar);
        progressOverlay = findViewById(R.id.progressOverlay);

        // Initialize calendar and date/time format
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        // Set default date in the fields (Optional)
        pickupDateEditText.setText(dateFormat.format(calendar.getTime()));
        returnDateEditText.setText(dateFormat.format(calendar.getTime()));
        // Set listeners for pickup and return date fields
        pickupDateEditText.setOnClickListener(v -> showDatePickerDialog(pickupDateEditText));
        returnDateEditText.setOnClickListener(v -> showDatePickerDialog(returnDateEditText));
        // Get data from the Intent
        Intent intent = getIntent();
        CustomerCarResponse selectedCar = intent.getParcelableExtra("SELECTED_CAR");

        pickUpLocation = intent.getStringExtra("pickupLocation");
        returnLocation = intent.getStringExtra("returnLocation");
        pickUpDateTime = intent.getStringExtra("pickupDateTime");
        returnDateTime = intent.getStringExtra("returnDateTime");
        partnerId=selectedCar.getPartnerId();
        carId=selectedCar.getId();
        isDriverRequired=intent.getBooleanExtra("isDriverRequired",false);



        // Log the received data for debugging
        Log.d(TAG, "Received car data: " + selectedCar);
        Log.d(TAG, "Pick-up Location: " + pickUpLocation);
        Log.d(TAG, "Return Location: " + returnLocation);
        Log.d(TAG, "Pick-up DateTime: " + pickUpDateTime);
        Log.d(TAG, "Return DateTime: " + returnDateTime);
        Log.d(TAG, "Return DateTime: " + partnerId);
        Log.d(TAG, "Return DateTime: " + carId);
        Log.d(TAG, "Return DateTime: " + isDriverRequired);


        // Set data to UI components
        if (selectedCar != null) {
            carNameTextView.setText(selectedCar.getBrand());
            carPriceTextView.setText("$" + selectedCar.getPricePerDay());
        }
        pickupLocationTextView.setText(pickUpLocation);
        returnLocationTextView.setText(returnLocation);
        pickupDateEditText.setText(pickUpDateTime);
        returnDateEditText.setText(returnDateTime);


        ImageView carImage0 = findViewById(R.id.car_image0);

        // Set car images using Glide
        List<String> carImages = selectedCar.getImages();
        if (carImages != null && !carImages.isEmpty()) {
            Glide.with(this)
                    .load(carImages.get(0))
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.profile)
                    .into(carImage0);
        } else {
            carImage0.setImageResource(R.drawable.profile);
        }

        // Calculate duration and total rent
        int rentPerHour = selectedCar != null ? selectedCar.getPricePerDay() / 24 : 1; // Example: default to $1/hour
        long durationInHours = calculateDurationInHours(pickUpDateTime, returnDateTime);
        totalRent = (int) (durationInHours * rentPerHour);

        // Update UI with calculated values
        totalRentTextView.setText("Total rent: $" + totalRent);
        totalPaymentTextView.setText("Total payment: $" + totalRent);
        perDayPriceTextView.setText("Per hour car rent: $" + rentPerHour);

        // Back button functionality
        backIcon.setOnClickListener(v -> finish());

        // Confirm button functionality - Now opens PaymentActivity directly
        confirmButton.setOnClickListener(v -> {
//            Intent paymentIntent = new Intent(ConfirmationActivity.this, PaymentActivity.class);
//            paymentIntent.putExtra("totalAmount", totalRent); // Pass the total amount
//            paymentIntent.putExtra("carDetails", selectedCar); // Pass selected car details
//            startActivity(paymentIntent);
            sendBookingRequest();
        });
    }

    /**
     * Calculates the duration in hours between two date-time strings.
     *
     * @param pickUpDateTime The pick-up date-time string (e.g., "06/01/2025 12:35").
     * @param returnDateTime The return date-time string (e.g., "08/01/2025 15:45").
     * @return The duration in hours.
     */
    private long calculateDurationInHours(String pickUpDateTime, String returnDateTime) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            Date pickupDate = format.parse(pickUpDateTime);
            Date returnDate = format.parse(returnDateTime);
            if (pickupDate != null && returnDate != null) {
                long diffInMillis = returnDate.getTime() - pickupDate.getTime();
                return TimeUnit.MILLISECONDS.toHours(diffInMillis);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "Error parsing date: " + e.getMessage());
        }
        return 0; // Default to 0 if parsing fails
    }
    private void sendBookingRequest() {
        // Prepare booking request data
        BookingRequest bookingRequest = new BookingRequest(
                pickUpLocation,
                returnLocation,
                pickUpDateTime,
                returnDateTime,
                partnerId,
                carId,
                isDriverRequired,

                calculateDurationInHours(pickUpDateTime, returnDateTime),
                totalRent
        );
        ProgressBarUtils.showProgress(progressOverlay, progressBar, true); // Using utility class

        // Get Retrofit instance and API interface
        ApiService apiService = RetrofitClient.getRetrofitInstance(ConfirmationActivity.this).create(ApiService.class);
        // Make API call
        Call<BookingResponse> call = apiService.createBooking(bookingRequest);
        call.enqueue(new Callback<BookingResponse>() {
            @Override
            public void onResponse(Call<BookingResponse> call, Response<BookingResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BookingResponse bookingResponse = response.body();
                    if (bookingResponse.isSuccess()) {
                        ProgressBarUtils.showProgress(progressOverlay, progressBar, false); // Using utility class

                        Toast.makeText(ConfirmationActivity.this, "Booking successful! ID: " + bookingResponse.getData().get_id(), Toast.LENGTH_SHORT).show();
                        // Redirect to success activity
                        Intent successIntent = new Intent(ConfirmationActivity.this, BookingSuccessActivity.class);
                        successIntent.putExtra("BOOKING_RESPONSE", bookingResponse); // bookingResponse is your Parcelable object

                        startActivity(successIntent);
                        finish();
                    } else {
                        Toast.makeText(ConfirmationActivity.this, "Booking failed: " + bookingResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ConfirmationActivity.this, "Failed to create booking!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BookingResponse> call, Throwable t) {
                ProgressBarUtils.showProgress(progressOverlay, progressBar, false); // Using utility class

                Toast.makeText(ConfirmationActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


// Method to show the DatePickerDialog
private void showDatePickerDialog(EditText editText) {
    DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
        // Set the selected date in the field
        calendar.set(year, month, dayOfMonth);
        editText.setText(dateFormat.format(calendar.getTime()));

        // After selecting the date, show the TimePickerDialog
        showTimePickerDialog(editText);
    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    datePickerDialog.show();
}

// Method to show the TimePickerDialog
private void showTimePickerDialog(EditText editText) {
    TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
        // Set the selected time in the field
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        // Format the time and update the EditText field
        String formattedTime = timeFormat.format(calendar.getTime());
        editText.setText(editText.getText().toString() + " " + formattedTime);  // Append time to the date
    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
    timePickerDialog.show();
}
}