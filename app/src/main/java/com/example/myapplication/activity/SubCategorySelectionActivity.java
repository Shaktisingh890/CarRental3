package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.myapplication.utils.ProgressBarUtils;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.activity.CarDetailsActivity;
import com.example.myapplication.models.response.CustomerCarResponse;
import com.example.myapplication.models.response.SubCategoryResponse;
import com.example.myapplication.network.ApiService;
import com.example.myapplication.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCategorySelectionActivity extends AppCompatActivity {

    private ListView subCategoryListView;
    private View progressOverlay;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category_selection);

        // Back button setup
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Get the ListView for displaying subcategories
        subCategoryListView = findViewById(R.id.subCategoryListView);
        progressBar = findViewById(R.id.progressBar);
        progressOverlay = findViewById(R.id.progressOverlay);
        // Get the category from the intent
        String category = getIntent().getStringExtra("CATEGORY");

        // Fetch subcategories using Retrofit
        if (category != null) {
            fetchSubCategories(category);
        } else {
            Toast.makeText(this, "Category not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchSubCategories(String category) {

        ProgressBarUtils.showProgress(progressOverlay, progressBar, true); // Using utility class

        ApiService apiService = RetrofitClient.getRetrofitInstance(this).create(ApiService.class);

        // Make the network call
        apiService.getSubCategories(category).enqueue(new Callback<SubCategoryResponse>() {
            @Override
            public void onResponse(Call<SubCategoryResponse> call, Response<SubCategoryResponse> response) {
                ProgressBarUtils.showProgress(progressOverlay, progressBar, false); // Using utility class

                if (response.isSuccessful() && response.body() != null) {
                    SubCategoryResponse subCategoryResponse = response.body();
                    List<String> subCategories = subCategoryResponse.getSubcategories();

                    // Set up the adapter to display the subcategories
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(SubCategorySelectionActivity.this, android.R.layout.simple_list_item_1, subCategories);
                    subCategoryListView.setAdapter(adapter);

                    // Set up an item click listener for the subcategory list
                    subCategoryListView.setOnItemClickListener((parent, view, position, id) -> {
                        String selectedSubCategory = subCategories.get(position);
                        openCarDetailsScreen(selectedSubCategory);
                    });
                } else {
                    Toast.makeText(SubCategorySelectionActivity.this, "Failed to fetch subcategories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubCategoryResponse> call, Throwable t) {
                ProgressBarUtils.showProgress(progressOverlay, progressBar, false); // Using utility class

                Toast.makeText(SubCategorySelectionActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Open the CarDetailsActivity with the selected subcategory
    // Open the CarDetailsActivity with the selected subcategory
    private void openCarDetailsScreen(String subCategory) {
        // Define the filter type based on user selection
        String filterType = getIntent().getStringExtra("COST_TYPE"); // Pass this value based on button selection
        String category = getIntent().getStringExtra("CATEGORY");   // Get the category from intent

        if (category == null || filterType == null) {
            Toast.makeText(this, "Missing required parameters", Toast.LENGTH_SHORT).show();
            return;
        }

        // Call the API
        ApiService apiService = RetrofitClient.getRetrofitInstance(this).create(ApiService.class);
        apiService.getCarsBySubCategory(category, subCategory, filterType).enqueue(new Callback<List<CustomerCarResponse>>() {
            @Override
            public void onResponse(Call<List<CustomerCarResponse>> call, Response<List<CustomerCarResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Pass the car data to the CarDetailsActivity
                    Intent intent = new Intent(SubCategorySelectionActivity.this, CustomerCarDetailsActivity.class);
                    intent.putParcelableArrayListExtra("CAR_DATA", new ArrayList<>(response.body())); // Assuming CustomerCarResponse is Parcelable
                    startActivity(intent);
                } else {
                    Toast.makeText(SubCategorySelectionActivity.this, "No cars found for the selected filter", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CustomerCarResponse>> call, Throwable t) {
                Toast.makeText(SubCategorySelectionActivity.this, "Failed to fetch car details: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}

