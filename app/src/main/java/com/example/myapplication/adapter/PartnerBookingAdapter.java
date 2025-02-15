package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide; // Import Glide
import com.example.myapplication.R;
import com.example.myapplication.activity.MyBookingDetailActivity;
import com.example.myapplication.models.response.CustomerBookingResponse;

import java.util.ArrayList;
import java.util.List;

public class PartnerBookingAdapter extends RecyclerView.Adapter<PartnerBookingAdapter.BookingViewHolder> {

    private final Context context;
    private List<CustomerBookingResponse.BookingData> bookingList = new ArrayList<>();

    public PartnerBookingAdapter(Context context) {
        this.context = context;
    }

    public void setBookingList(List<CustomerBookingResponse.BookingData> bookingList) {
        if (bookingList != null) {
            this.bookingList = bookingList;
            notifyDataSetChanged();
        } else {
            Log.w("BookingAdapter", "Booking list is null, setting an empty list.");
            this.bookingList = new ArrayList<>();
        }
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

        if (bookingData == null) {
            Log.e("BookingAdapter", "Booking data at position " + position + " is null.");
            return;
        }

        // Safely extract data for car name, model, paid amount, and image URL
        String carName = bookingData.getCarId() != null && bookingData.getCarId().getBrand() != null
                ? bookingData.getCarId().getBrand() : "Car name not available";

        String carModel = bookingData.getCarId() != null && bookingData.getCarId().getModel() != null
                ? bookingData.getCarId().getModel() : "Model not available";

        String paidAmount = String.valueOf(bookingData.getTotalAmount()) != null
                ? "Paid Amount: " + String.valueOf(bookingData.getTotalAmount()) : "Paid Amount not available";

        // Assuming car images are available in bookingData.getCarId().getImages()
        List<String> carImages = bookingData.getCarId().getImages();
        String carImageUrl = "";

        if (carImages != null && !carImages.isEmpty()) {
            carImageUrl = carImages.get(0); // Get the first image in the list
        }

        Log.d("BookingAdapter", "Binding data for booking ID: " + bookingData.getId());

        // Bind data to UI components
        holder.textCarName.setText(carName);
        holder.textCarModel.setText(carModel);
        holder.textPaidAmount.setText(paidAmount);

        // Load image using Glide
        if (!carImageUrl.isEmpty()) {
            Glide.with(context)
                    .load(carImageUrl)
                    .into(holder.imageCar);
        } else {
            // Optionally set a placeholder or empty image if no image URL is available
            Glide.with(context)
                    .load(R.drawable.profile) // Use a placeholder image
                    .into(holder.imageCar);
        }

        // Handle card click event
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, MyBookingDetailActivity.class);
            intent.putExtra("booking_data", bookingData); // Assuming BookingData is Parcelable or Serializable

            // Check if the context is not an Activity, add FLAG_ACTIVITY_NEW_TASK
            if (!(context instanceof android.app.Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return bookingList != null ? bookingList.size() : 0;
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView textCarName, textCarModel, textPaidAmount;
        ImageView imageCar;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            textCarName = itemView.findViewById(R.id.textCarName); // Added for car name
            textCarModel = itemView.findViewById(R.id.textCarModel);
            textPaidAmount = itemView.findViewById(R.id.textPaidAmount);
            imageCar = itemView.findViewById(R.id.imageCar); // ImageView for car image
        }
    }
}
