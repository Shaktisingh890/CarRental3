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

public class SubCategorySelectionActivity extends AppCompatActivity {

    private LinearLayout subCategoryContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category_selection);

        // Back button setup
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Initialize the container where subcategories will be dynamically added
        subCategoryContainer = findViewById(R.id.subCategoryContainer);

        // Get the category from the intent
        String category = getIntent().getStringExtra("CATEGORY");

        // Dynamically set subcategories based on the category passed in the intent
        String[] subCategories;
        if ("SUV".equals(category)) {
            subCategories = new String[]{"Luxury", "Economy", "Compact"};
        } else if ("Sedan".equals(category)) {
            subCategories = new String[]{"Standard", "Luxury"};
        } else {
            subCategories = new String[]{"Default"};
        }

        // Populate the subcategories dynamically
        populateSubCategories(subCategories);
    }

    private void populateSubCategories(String[] subCategories) {
        for (String subCategory : subCategories) {
            addSubCategoryItem(subCategory);
        }
    }

    private void addSubCategoryItem(String subCategory) {
        // Inflate a new subcategory item layout
        View subCategoryItemView = LayoutInflater.from(this).inflate(R.layout.sub_category_item, subCategoryContainer, false);

        // Initialize the subcategory name TextView
        TextView subCategoryName = subCategoryItemView.findViewById(R.id.subCategoryName);
        subCategoryName.setText(subCategory);

        // Set a click listener for each subcategory item
        subCategoryItemView.setOnClickListener(v -> openCarDetailsScreen(subCategory));

        // Add the inflated view to the container
        subCategoryContainer.addView(subCategoryItemView);
    }

    // Open the CarDetailsActivity with the selected subcategory
    private void openCarDetailsScreen(String subCategory) {
        Intent intent = new Intent(SubCategorySelectionActivity.this, CarDetailsActivity.class);
        intent.putExtra("SUB_CATEGORY", subCategory);
        startActivity(intent);
    }
}
