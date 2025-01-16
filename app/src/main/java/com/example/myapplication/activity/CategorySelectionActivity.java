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

import java.util.ArrayList;

public class CategorySelectionActivity extends AppCompatActivity {

    private LinearLayout categoryContainer;
    private String costType; // To hold the cost type passed from DashboardActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selection);

        // Set up the back button
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Initialize the container where categories will be dynamically added
        categoryContainer = findViewById(R.id.categoryContainer);

        // Get the categories and costType passed from the previous activity
        ArrayList<String> categories = getIntent().getStringArrayListExtra("categories");
        costType = getIntent().getStringExtra("costType"); // Retrieve the costType from Intent

        if (categories != null && !categories.isEmpty()) {
            // Dynamically add category items to the container
            for (String category : categories) {
                addCategoryItem(category);
            }
        } else {
            // If no categories are received, show a placeholder or message
            TextView placeholder = new TextView(this);
            placeholder.setText("No categories available");
            placeholder.setTextSize(18);
            placeholder.setPadding(16, 16, 16, 16);
            categoryContainer.addView(placeholder);
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
                return R.drawable.suv1; // Replace with actual drawable names
            case "Sedan":
                return R.drawable.sedan;
            case "Hatchback":
                return R.drawable.hatchback1;
            case "Convertible":
                return R.drawable.convertible;
            default:
                return R.drawable.default_car_image; // Default image if category doesn't match
        }
    }

    private void openSubCategorySelection(String category) {
        // Create an Intent to navigate to SubCategorySelectionActivity
        Intent intent = new Intent(CategorySelectionActivity.this, SubCategorySelectionActivity.class);

        // Pass the selected category and the costType to the next activity
        intent.putExtra("CATEGORY", category);
        intent.putExtra("COST_TYPE", costType);

        startActivity(intent);
    }
}
