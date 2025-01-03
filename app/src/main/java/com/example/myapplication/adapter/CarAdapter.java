package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.activity.CarDetailsActivity;
import com.example.myapplication.R;
import com.example.myapplication.activity.CarDetailsActivity1;
import com.example.myapplication.models.response.CarDetailsResponse;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {

    private List<CarDetailsResponse.Car> carList;
    private Context context;

    public CarAdapter(List<CarDetailsResponse.Car> carList, Context context) {
        this.carList = carList;
        this.context = context;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        CarDetailsResponse.Car car = carList.get(position);

        holder.carName.setText(car.getBrand() + " " + car.getModel());
        holder.carCategory.setText(car.getCategory());
        holder.carYear.setText("Year: " + car.getYear());
        holder.carDetails.setText("Fuel: " + car.getFuelType());
        holder.carSeats.setText("Seats: " + car.getSeats());
        holder.carPrice.setText("$" + car.getPricePerDay() + "/day");

        // Load the car image using Glide
        String imageUrl = null;
        List<String> imageUrls = car.getImages();
        if (imageUrls != null && !imageUrls.isEmpty()) {
            imageUrl = imageUrls.get(0);  // Get the first image URL
        }

        if (imageUrl != null) {
            Glide.with(holder.carImage.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.profile) // Optional placeholder
                    .into(holder.carImage);
        }

        // Set click listener for the item
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CarDetailsActivity1.class);
            intent.putExtra("SELECTED_CAR", car);  // Pass the car object to CarDetailsActivity
            context.startActivity(intent);
        });

        holder.editIcon.setOnClickListener(v -> {
            // Code to handle edit action
        });

        holder.deleteIcon.setOnClickListener(v -> {
            // Code to handle delete action
        });
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public static class CarViewHolder extends RecyclerView.ViewHolder {
        TextView carName, carModel, carYear, carPrice, carDetails, carCategory, carSeats;
        ImageView carImage, editIcon, deleteIcon;

        public CarViewHolder(View itemView) {
            super(itemView);
            carName = itemView.findViewById(R.id.car_name);
            carModel = itemView.findViewById(R.id.car_modal);
            carYear = itemView.findViewById(R.id.car_year);
            carPrice = itemView.findViewById(R.id.carPrice);
            carDetails = itemView.findViewById(R.id.carDetails);
            carCategory = itemView.findViewById(R.id.carCategory);
            carSeats = itemView.findViewById(R.id.carSeats);
            carImage = itemView.findViewById(R.id.carImage);
            editIcon = itemView.findViewById(R.id.editIcon);
            deleteIcon = itemView.findViewById(R.id.deleteIcon);
        }
    }
}
