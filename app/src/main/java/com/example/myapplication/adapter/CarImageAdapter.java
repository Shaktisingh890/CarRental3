package com.example.myapplication.adapter;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

import java.util.List;

public class CarImageAdapter extends RecyclerView.Adapter<CarImageAdapter.CarImageViewHolder> {

    private final Context context;
    private final List<String> imageUrls;

    public CarImageAdapter(Context context, List<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public CarImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_car_image, parent, false);
        return new CarImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarImageViewHolder holder, int position) {
        Glide.with(context).load(imageUrls.get(position)).into(holder.carImageView);
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public static class CarImageViewHolder extends RecyclerView.ViewHolder {
        ImageView carImageView;

        public CarImageViewHolder(@NonNull View itemView) {
            super(itemView);
            carImageView = itemView.findViewById(R.id.carImageView);
        }
    }
}

