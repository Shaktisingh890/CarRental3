package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.models.response.CarDetailsResponse;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {

    private List<CarDetailsResponse.Car> carList;

    public CarAdapter(List<CarDetailsResponse.Car> carList) {
        this.carList = carList;
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

        holder.carName.setText(car.getBrand() +" "+ car.getModel());
        holder.carCategory.setText(car.getCategory());
        holder.carYear.setText("Year: " + car.getYear());
        holder.carDetails.setText("Fuel: " + car.getFuelType());
//        holder.carMileage.setText("Mileage: " + car.getMilage());
        holder.carSeats.setText("Seats: " + car.getSeats());
        holder.carPrice.setText( "$"+car.getPricePerDay() + "/day");

        // Get the first image URL from the array (if available)
        // Get the first image URL from the List (if available)
        String imageUrl = null;
        List<String> imageUrls = car.getImages(); // Assuming this is a List<String>

        if (imageUrls != null && !imageUrls.isEmpty()) {
            imageUrl = imageUrls.get(0);  // Get the first image URL
        }

        // Load car image using Glide if URL exists
        if (imageUrl != null) {
            Glide.with(holder.carImage.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.profile) // Optional placeholder while the image loads
                    .into(holder.carImage);
        }

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
        TextView carName, carModel, carYear, carPrice, carDetails,carCategory,carSeats;
        ImageView carImage, editIcon, deleteIcon;

        public CarViewHolder(View itemView) {
            super(itemView);
            carName = itemView.findViewById(R.id.car_name);
            carModel = itemView.findViewById(R.id.car_modal);
            carYear = itemView.findViewById(R.id.car_year);
//            carMileage = itemView.findViewById(R.id.car_mileage);
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
