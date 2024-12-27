package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.activity.CarDetailsActivity;
import com.example.myapplication.models.response.CustomerCarResponse;

import java.util.List;

public class CarListAdapter extends RecyclerView.Adapter<CarListAdapter.CarViewHolder> {

    private Context context;
    private List<CustomerCarResponse> carList;

    public CarListAdapter(Context context, List<CustomerCarResponse> carList) {
        this.context = context;
        this.carList = carList;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_car_detail, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        CustomerCarResponse car = carList.get(position);

        // Set car details
        // Set car details
        holder.brandModelTextView.setText(String.format("%s %s", car.getBrand(), car.getModel()));
        holder.yearSeatsFuelTextView.setText(
                String.format("Seats: %d    |    Fuel: %s    |    Mileage: %d km/l",
                        car.getSeats(), car.getFuelType(), car.getMilage())
        );

        holder.pricePerDayTextView.setText(String.format("$%d/day", car.getPricePerDay()));



        // Load main image
        if (car.getImages() != null && !car.getImages().isEmpty()) {
            Glide.with(context).load(car.getImages().get(0)).into(holder.carMainImageView);

            // Set additional images using RecyclerView
//            holder.carImagesRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            CarImageAdapter adapter = new CarImageAdapter(context, car.getImages());
//            holder.carImagesRecyclerView.setAdapter(adapter);
        } else {
            Glide.with(context).load(R.drawable.profile).into(holder.carMainImageView); // Placeholder
            Toast.makeText(context, "No additional images available", Toast.LENGTH_SHORT).show();
        }

        // Set item click listener to navigate to CarDetailsActivity
        holder.itemView.setOnClickListener(v -> {
            // Pass the selected car details to CarDetailsActivity
            Intent intent = new Intent(context, CarDetailsActivity.class);
            intent.putExtra("SELECTED_CAR", car);  // Pass car object to CarDetailsActivity
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public static class CarViewHolder extends RecyclerView.ViewHolder {
        ImageView carMainImageView;
        TextView brandModelTextView, yearSeatsFuelTextView, pricePerDayTextView, milageColorTextView;
//        RecyclerView carImagesRecyclerView;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            carMainImageView = itemView.findViewById(R.id.carMainImageView);
            brandModelTextView = itemView.findViewById(R.id.brandModelTextView);
            yearSeatsFuelTextView = itemView.findViewById(R.id.yearSeatsFuelTextView);
            pricePerDayTextView = itemView.findViewById(R.id.pricePerDayTextView);
            milageColorTextView = itemView.findViewById(R.id.milageColorTextView);
//            carImagesRecyclerView = itemView.findViewById(R.id.carImagesRecyclerView);
        }
    }
}
