package com.example.myapplication.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.ImageViewHolder> {

    private Context context;
    private List<Integer> imageList;
    private static final String TAG = "ImageSliderAdapter"; // Debug Tag

    public ImageSliderAdapter(Context context, List<Integer> imageList) {
        this.context = context;
        this.imageList = imageList;

        if (imageList == null || imageList.isEmpty()) {
            Log.e(TAG, "Image list is null or empty!");
        } else {
            Log.d(TAG, "Image list initialized with " + imageList.size() + " images.");
        }
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout
        View view = LayoutInflater.from(context).inflate(R.layout.slider_item1, parent, false);
        if (view == null) {
            Log.e(TAG, "View is null when inflating slider_item1!");
        }
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        if (imageList == null || imageList.isEmpty()) {
            Log.e(TAG, "Image list is empty or null!");
            return;
        }

        if (position < 0 || position >= imageList.size()) {
            Log.e(TAG, "Invalid position: " + position);
            return;
        }

        if (holder.imageView == null) {
            Log.e(TAG, "ImageView is null in ViewHolder at position: " + position);
            return;
        }

        Log.d(TAG, "Setting image for position " + position);
        holder.imageView.setImageResource(imageList.get(position));
    }

    @Override
    public int getItemCount() {
        return (imageList != null) ? imageList.size() : 0;
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.sliderImageView);
            if (imageView == null) {
                Log.e(TAG, "ImageView is null inside ViewHolder constructor!");
            }
        }
    }
}
