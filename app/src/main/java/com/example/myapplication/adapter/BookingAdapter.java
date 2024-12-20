package com.example.myapplication.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.response.Car;
import com.example.myapplication.models.response.CustomerBookingResponse;

import java.util.ArrayList;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private List<CustomerBookingResponse> bookingList = new ArrayList<>();

    public void setBookingList(List<CustomerBookingResponse> bookingList) {
        this.bookingList = bookingList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        // Access the Booking object from the CustomerBookingResponse
        CustomerBookingResponse bookingResponse = bookingList.get(position);

        // Log all the details in one statement
        Car car = bookingResponse.getCarId();
        if (car != null) {
            String carModel = car.getModel() != null ? car.getModel() : "Model not available";
            String carBrand = car.getBrand() != null ? car.getBrand() : "Brand not available";
            String carColor = car.getColor() != null ? car.getColor() : "Color not available";

            String logMessage = "Booking Data: " +
                    "Booking ID: " + bookingResponse.get_id() + ", " +
                    "Customer ID: " + bookingResponse.getCustomerId() + ", " +
                    "Car Model: " + carModel + ", " +
                    "Car Brand: " + carBrand + ", " +
                    "Car Year: " + car.getYear() + ", " +
                    "Car Color: " + carColor + ", " +
                    "Total Amount: " + bookingResponse.getTotalAmount() + ", " +
                    "Payment Status: " + bookingResponse.getPaymentStatus() + ", " +
                    "Booking Status: " + bookingResponse.getStatus();

            Log.d("BookingAdapter", logMessage);
        } else {
            Log.d("BookingAdapter", "Car data is null");
        }

        // Bind the data from the Booking object to the ViewHolder
        holder.textCarModel.setText(car.getModel());
        holder.textBookingId.setText("Booking ID: " + bookingResponse.get_id());
        holder.textLocation.setText("Location: " + car.getColor()); // Replace with actual location if available
        holder.textTripStart.setText("Start Date: " + bookingResponse.getStartDate());
        holder.textTripEnd.setText("End Date: " + bookingResponse.getEndDate());
        holder.textPaidAmount.setText("Paid Amount: " + bookingResponse.getTotalAmount());
        // You may need to convert the car image data (if available) to an image resource.
    }


    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView textCarModel, textBookingId, textLocation, textTripStart, textTripEnd, textPaidAmount;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            textCarModel = itemView.findViewById(R.id.textCarModel); // Ensure this ID exists
            textBookingId = itemView.findViewById(R.id.textBookingId);
            textLocation = itemView.findViewById(R.id.textLocation);
            textTripStart = itemView.findViewById(R.id.textTripStart);
            textTripEnd = itemView.findViewById(R.id.textTripEnd);
            textPaidAmount = itemView.findViewById(R.id.textPaidAmount);
        }
    }
}
