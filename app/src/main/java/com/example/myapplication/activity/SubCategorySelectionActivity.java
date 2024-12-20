package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.activity.CarDetailsActivity;

public class SubCategorySelectionActivity extends AppCompatActivity {

    private ListView subCategoryListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category_selection);

        // Back button setup
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Go back to the previous activity
            }
        });

        // Get the ListView for displaying subcategories
        subCategoryListView = findViewById(R.id.subCategoryListView);

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

        // Set up the adapter to display the subcategories
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, subCategories);
        subCategoryListView.setAdapter(adapter);

        // Set up an item click listener for the subcategory list
        subCategoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedSubCategory = subCategories[position];
                openCarDetailsScreen(selectedSubCategory);
            }
        });
    }

    // Open the CarDetailsActivity with the selected subcategory
    private void openCarDetailsScreen(String subCategory) {
        Intent intent = new Intent(SubCategorySelectionActivity.this, CarDetailsActivity.class);
        intent.putExtra("SUB_CATEGORY", subCategory);
        startActivity(intent);
    }
}
