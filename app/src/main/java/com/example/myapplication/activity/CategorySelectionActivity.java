package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.activity.SubCategorySelectionActivity;

public class CategorySelectionActivity extends AppCompatActivity {

    private LinearLayout categoryContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selection);

        // Set up the back button
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Initialize the container where categories will be dynamically added
        categoryContainer = findViewById(R.id.categoryContainer);

        // Define the categories (for example purposes)
        String[] categories = {"SUV", "Sedan", "Hatchback", "Convertible"};

        // Dynamically add category items to the container
        for (String category : categories) {
            addCategoryItem(category);
        }
    }

    private void addCategoryItem(String categoryName) {
        // Inflate a new car item layout
        View categoryItemView = LayoutInflater.from(this).inflate(R.layout.car_item, categoryContainer, false);

        // Set the category name
        TextView carNameTextView = categoryItemView.findViewById(R.id.carName);
        carNameTextView.setText(categoryName);

        // Set the image based on category
        ImageView carImageView = categoryItemView.findViewById(R.id.carImage);
        int imageResId = getImageResourceForCategory(categoryName);
        carImageView.setImageResource(imageResId);

        // Set a click listener for each category item
        categoryItemView.setOnClickListener(v -> openSubCategorySelection(categoryName));

        // Add the inflated view to the container
        categoryContainer.addView(categoryItemView);
    }

    private int getImageResourceForCategory(String categoryName) {
        switch (categoryName) {
            case "SUV":
                return R.drawable.suv; // Replace with actual drawable names
            case "Sedan":
                return R.drawable.sedan;
            case "Hatchback":
                return R.drawable.hatchback;
            case "Convertible":
                return R.drawable.convertible;
            default:
                return R.drawable.default_car_image; // Default image if category doesn't match
        }
    }


    private void openSubCategorySelection(String category) {
        Intent intent = new Intent(CategorySelectionActivity.this, SubCategorySelectionActivity.class);
        intent.putExtra("CATEGORY", category); // Pass the selected category to the next activity
        startActivity(intent);
    }
}
