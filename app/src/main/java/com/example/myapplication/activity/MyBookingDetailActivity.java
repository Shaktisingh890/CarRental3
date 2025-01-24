package com.example.myapplication.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.models.response.CustomerBookingResponse;

public class MyBookingDetailActivity extends AppCompatActivity {

    private LinearLayout layoutCarDetails, layoutDriverDetails, layoutPaymentInfo;
    private TextView tvCarDetails, tvDriverDetails, tvPaymentInfo;

    private String pickupLocation,dropoffLocation,bookingAmount,driverName,drivercontact,bookingStartDate,bookingEndDate,carName,carRegistrationNumber,status;

    private String Tag="MyBooking";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_booking_detail);




        CustomerBookingResponse.BookingData bookingData = getIntent().getParcelableExtra("booking_data");
        Log.d(Tag,"my details :"+ bookingData.getPickupLocation());
        Log.d(Tag,"my details"+bookingData.getCarId());
        Log.d(Tag,"my details"+bookingData.getCarId().getModel());
//        Log.d(Tag,"my details"+bookingData.getDriverId().getFullName());
        Log.d(Tag,"my details"+bookingData.getDriverId());

        pickupLocation=bookingData.getPickupLocation();
       dropoffLocation=bookingData.getDropoffLocation();
//         driverName=bookingData.getDriverId().getFullName();
//       drivercontact=bookingData.getDriverId().getPhoneNumber();
       bookingStartDate=bookingData.getStartDate();
      bookingEndDate=bookingData.getEndDate();
        carName=bookingData.getCarId().getBrand();
       carRegistrationNumber=bookingData.getCarId().getRegistrationNumber();
       bookingAmount= String.valueOf(bookingData.getTotalAmount());
       status=bookingData.getStatus();
       Log.d("mystatus1","status"+status);


        // Initialize layouts and text views
        layoutCarDetails = findViewById(R.id.layoutCarDetails);
        layoutDriverDetails = findViewById(R.id.layoutDriverDetails);
        layoutPaymentInfo = findViewById(R.id.layoutPaymentInfo);

        tvCarDetails = findViewById(R.id.tvCarDetails);

       TextView tvCarName=findViewById(R.id.tvCarName);
       TextView tvCarModel=findViewById(R.id.tvCarModel);
       TextView tvCarRegistrationNumber=findViewById(R.id.tvCarRegistrationNumber);
       TextView tvDriverName=findViewById(R.id.tvDriverName);
       TextView tvDriverContact=findViewById(R.id.tvDriverContact);
        TextView tvpickupLocation=findViewById(R.id.pickupLocation);
        TextView tvdropoffLocation=findViewById(R.id.dropoffLocation);
        TextView tvbooking_start_date=findViewById(R.id.booking_start_date);
        TextView tvbooking_end_date=findViewById(R.id.booking_end_date);
        TextView tvbookingAmount=findViewById(R.id.bookingAmount);

        tvCarName.setText("CarName:"+carName);
        tvCarModel.setText("Modal:"+bookingData.getCarId().getModel());
        tvCarRegistrationNumber.setText("Registration Number: "+carRegistrationNumber);
        tvpickupLocation.setText("Pickup Location: "+pickupLocation);
        tvdropoffLocation.setText("Dropoff Location: "+dropoffLocation);
        tvbooking_start_date.setText("Booking Date: "+bookingStartDate);
        tvbooking_end_date.setText("Bookind End Date: "+bookingEndDate);
        tvbookingAmount.setText("Booking Amount: "+bookingAmount);





        // Conditionally handle driver details
        if (bookingData.getDriverId() != null) {
            driverName = bookingData.getDriverId().getFullName();
            drivercontact = bookingData.getDriverId().getPhoneNumber();
            tvDriverName.setText(driverName);
            tvDriverContact.setText(drivercontact);
        } else {
            // Hide the driver details layout if driver information is null
//           layoutDriverDetails.setVisibility(View.GONE);
//            findViewById(R.id.tvDriverDetails).setVisibility(View.GONE);
            TextView driverName=findViewById(R.id.tvDriverName);
            driverName.setText("No Driver Assign By Partner ");
        }
//        tvDriverName.setText();

//
//
        TextView pickupLocation=findViewById(R.id.pickupLocation);
       TextView dropoffLocation=findViewById(R.id.dropoffLocation);
      TextView booking_start_date=findViewById(R.id.booking_start_date);
      TextView booking_end_date=findViewById(R.id.booking_end_date);

        tvDriverDetails = findViewById(R.id.tvDriverDetails);
        tvPaymentInfo = findViewById(R.id.tvPaymentInfo);

        // Toggle Car Details Accordion
        tvCarDetails.setOnClickListener(v -> toggleVisibility(layoutCarDetails));

        // Toggle Driver Details Accordion
        tvDriverDetails.setOnClickListener(v -> toggleVisibility(layoutDriverDetails));

        // Toggle Payment Info Accordion
        tvPaymentInfo.setOnClickListener(v -> toggleVisibility(layoutPaymentInfo));

        // Cancel Booking Button
        findViewById(R.id.btnCancelBooking).setOnClickListener(v ->
                Toast.makeText(this, "Booking Cancelled", Toast.LENGTH_SHORT).show()
        );

        // Download Invoice Button
        findViewById(R.id.btnDownloadInvoice).setOnClickListener(v ->
                Toast.makeText(this, "Invoice Downloaded", Toast.LENGTH_SHORT).show()
        );

        // Example logic to update status
        updateBookingStatus(status);
    }

    private void toggleVisibility(LinearLayout layout) {
        if (layout.getVisibility() == View.GONE) {
            layout.setVisibility(View.VISIBLE);
        } else {
            layout.setVisibility(View.GONE);
        }
    }

    private void updateBookingStatus(String status) {
        View viewPending = findViewById(R.id.viewPending);
        View viewOngoing = findViewById(R.id.viewOngoing);
        View viewBooked = findViewById(R.id.viewBooked);
        View viewCompleted = findViewById(R.id.viewCompleted);
        View viewCancelled = findViewById(R.id.cancelled);

        // Reset all
        resetStatus(viewPending);
        resetStatus(viewOngoing);
        resetStatus(viewBooked);
        resetStatus(viewCompleted);
        resetStatus(viewCancelled);

        // Highlight based on status
        switch (status) {
            case "pending":
                highlightStatus(viewPending, "pending");
                break;
            case "ongoing":
                highlightStatus(viewOngoing, "ongoing");
                break;
            case "booked":
                highlightStatus(viewBooked, "booked");
                break;
            case "cancelled":
                highlightStatus(viewCancelled, "cancelled");
                break;
            case "completed":
                highlightStatus(viewCompleted, "completed");
                break;
        }
    }

    private void resetStatus(View view) {
        view.setBackgroundResource(R.drawable.status_circle_pending); // Default style
    }

    private void highlightStatus(View view, String status) {
        int drawableResource;
        switch (status) {
            case "pending":
                drawableResource = R.drawable.status_circle_pending;
                break;
            case "ongoing":
                drawableResource = R.drawable.status_circle_ongoing;
                break;
            case "booked":
                drawableResource = R.drawable.status_circle_booked;
                break;
            case "cancelled":
                drawableResource = R.drawable.status_circle_booked;
                break;
            case "completed":
                drawableResource = R.drawable.status_circle_completed;
                break;
            default:
                drawableResource = R.drawable.status_circle_completed; // Default style
                break;
        }
        view.setBackgroundResource(drawableResource);
    }
}
