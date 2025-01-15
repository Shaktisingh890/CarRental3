package com.example.myapplication.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.response.CustomerBookingResponse;

import java.util.ArrayList;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private List<CustomerBookingResponse.BookingData> bookingList = new ArrayList<>();

    public void setBookingList(List<CustomerBookingResponse.BookingData> bookingList) {
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
        CustomerBookingResponse.BookingData bookingData = bookingList.get(position);

        // Log all the details in one statement
        String carModel = bookingData.getCarId() != null && bookingData.getCarId().getModel() != null ? bookingData.getCarId().getModel() : "Model not available";
        String carBrand = bookingData.getCarId() != null && bookingData.getCarId().getBrand() != null ? bookingData.getCarId().getBrand() : "Brand not available";
        String carColor = bookingData.getCarId() != null && bookingData.getCarId().getColor() != null ? bookingData.getCarId().getColor() : "Color not available";

        String logMessage = "Booking Data: " +
                "Booking ID: " + bookingData.getId() + ", " +
                "Customer ID: " + bookingData.getCustomerId() + ", " +
                "Car Model: " + carModel + ", " +
                "Car Brand: " + carBrand + ", " +
                "Car Color: " + carColor + ", " +
                "Total Amount: " + bookingData.getTotalAmount() + ", " +
                "Payment Status: " + bookingData.getPaymentStatus() + ", " +
                "Booking Status: " + bookingData.getStatus();

        Log.d("BookingAdapter", logMessage);

        // Bind the data from the Booking object to the ViewHolder
        holder.textCarModel.setText(carModel);
        holder.textBookingId.setText("Booking ID: " + bookingData.getId());
        holder.textLocation.setText("Pickup Location: " + (bookingData.getPickupLocation() != null ? bookingData.getPickupLocation() : "Not available"));
        holder.textTripStart.setText("Start Date: " + bookingData.getStartDate());
        holder.textTripEnd.setText("End Date: " + bookingData.getEndDate());
        holder.textPaidAmount.setText("Paid Amount: " + bookingData.getTotalAmount());
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
