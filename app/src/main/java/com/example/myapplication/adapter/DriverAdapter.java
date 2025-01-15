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
import com.example.myapplication.R;
import com.example.myapplication.activity.DriverDetailActivity;
import com.example.myapplication.models.response.Driver;

import java.util.List;

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.DriverViewHolder> {

    private final Context context;
    private final List<Driver> driverList;

    public String bookingId,notification_id;

    public DriverAdapter(Context context, List<Driver> driverList,String bookingId,String notification_id) {
        this.context = context;
        this.driverList = driverList;
        this.bookingId=bookingId;
        this.notification_id=notification_id;
    }

    @NonNull
    @Override
    public DriverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_driver, parent, false);
        return new DriverViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverViewHolder holder, int position) {
        Driver driver = driverList.get(position);

        // Set driver name
        holder.tvDriverName.setText(driver.getFullName());

        // Set driver phone number
        holder.tvDriverPhone.setText("Phone: " + driver.getPhoneNumber());

        // Set availability status
        if (driver.isAvailabilityStatus()) {
            holder.tvAvailabilityStatus.setText("Status: Available");
            holder.tvAvailabilityStatus.setTextColor(context.getResources().getColor(R.color.green));
        } else {
            holder.tvAvailabilityStatus.setText("Status: Unavailable");
            holder.tvAvailabilityStatus.setTextColor(context.getResources().getColor(R.color.red));
        }

        // Load driver image using Glide
        Glide.with(context)
                .load(driver.getImgUrl())
                .placeholder(R.drawable.profile) // Placeholder image
                .error(R.drawable.profile) // Fallback image in case of error
                .into(holder.imgDriver);

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DriverDetailActivity.class);
            intent.putExtra("driverId", driver.getId());
            intent.putExtra("driverName", driver.getFullName());
            intent.putExtra("driverPhone", driver.getPhoneNumber());
            intent.putExtra("driverEmail", driver.getEmail());
            intent.putExtra("driverLicenseNumber", driver.getLicenseNumber());
            intent.putExtra("driverLicenseExpiry", driver.getLicenseExpiryDate());
            intent.putExtra("frontPhotoImageView", driver.getLicenseFrontImgUrl());
            intent.putExtra("backPhotoImageView", driver.getLicenseBackImgUrl());
            intent.putExtra("driverImgUrl", driver.getImgUrl());
            intent.putExtra("availabilityStatus", driver.isAvailabilityStatus());
           intent.putExtra("bookingId",bookingId);
           intent.putExtra("notification_id",notification_id);

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return driverList.size();
    }

    public static class DriverViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgDriver;
        private final TextView tvDriverName;
        private final TextView tvDriverPhone;
        private final TextView tvAvailabilityStatus;

        public DriverViewHolder(@NonNull View itemView) {
            super(itemView);

            imgDriver = itemView.findViewById(R.id.imgDriver);
            tvDriverName = itemView.findViewById(R.id.tvDriverName);
            tvDriverPhone = itemView.findViewById(R.id.tvDriverPhone);
            tvAvailabilityStatus = itemView.findViewById(R.id.tvAvailabilityStatus);
        }
    }
}
