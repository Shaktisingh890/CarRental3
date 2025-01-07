package com.example.myapplication.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.utils.ProgressBarUtils;
import com.example.myapplication.R;
import com.example.myapplication.models.response.CustomerCarResponse;
import com.example.myapplication.models.response.UploadIdResponse;
import com.example.myapplication.network.ApiService;
import com.example.myapplication.network.RetrofitClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IdentificationActivity extends AppCompatActivity {

    private static final int REQUEST_FRONT_PHOTO = 1;
    private static final int REQUEST_BACK_PHOTO = 2;

    private RadioGroup radioGroup;
    private EditText nationalIdInput, passportIdInput;
    private Button nextButton,updateButton,skipButton;
    private ImageView backButton, frontPhoto, backPhoto;
    private Uri frontPhotoUri, backPhotoUri;
    private CustomerCarResponse selectedCar;
    private View progressOverlay;
    private ProgressBar progressBar;

    private String pickUpLocation;
    private String returnLocation;
    private String pickUpDateTime;
    private String returnDateTime;
    private Boolean isDriverRequired;




    private static final String TAG = "IdentificationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identification);

        // Initialize Views
        radioGroup = findViewById(R.id.radio_group);
        nationalIdInput = findViewById(R.id.national_id_input);
        passportIdInput = findViewById(R.id.passport_id_input);
        frontPhoto = findViewById(R.id.frontImageView);
        backPhoto = findViewById(R.id.backImageView);
        nextButton = findViewById(R.id.next_button);
        backButton = findViewById(R.id.back_button);
        // Initialize Skip Button
        skipButton = findViewById(R.id.skip_button);


        updateButton = findViewById(R.id.update_button);
        progressBar = findViewById(R.id.progressBar);
        progressOverlay = findViewById(R.id.progressOverlay);

// Handle Radio Button Selection
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_national_id) {
                nationalIdInput.setVisibility(View.VISIBLE);
                passportIdInput.setVisibility(View.GONE);
            } else if (checkedId == R.id.radio_passport) {
                nationalIdInput.setVisibility(View.GONE);
                passportIdInput.setVisibility(View.VISIBLE);
            } else {
                // Show both inputs if no radio button is selected
                nationalIdInput.setVisibility(View.VISIBLE);
                passportIdInput.setVisibility(View.VISIBLE);
            }
        });

// Set initial visibility based on the default selection or no selection
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            // No radio button selected, hide both inputs
            nationalIdInput.setVisibility(View.VISIBLE);
            passportIdInput.setVisibility(View.VISIBLE);
        } else if (radioGroup.getCheckedRadioButtonId() == R.id.radio_national_id) {
            nationalIdInput.setVisibility(View.VISIBLE);
            passportIdInput.setVisibility(View.GONE);
        } else if (radioGroup.getCheckedRadioButtonId() == R.id.radio_passport) {
            nationalIdInput.setVisibility(View.GONE);
            passportIdInput.setVisibility(View.VISIBLE);
        }


        // Retrieve the car object from the intent
        Intent intent1 = getIntent();
        selectedCar = intent1.getParcelableExtra("SELECTED_CAR");
        pickUpLocation = intent1.getStringExtra("pickupLocation");
        returnLocation = intent1.getStringExtra("returnLocation");
        pickUpDateTime = intent1.getStringExtra("pickupDateTime");
        returnDateTime = intent1.getStringExtra("returnDateTime");
        isDriverRequired=intent1.getBooleanExtra("isDriverRequired",false);

        // Image selection for front photo
        frontPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_FRONT_PHOTO);
        });

        // Image selection for back photo
        backPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_BACK_PHOTO);
        });

        // Back Button
//        backButton.setOnClickListener(v -> finish());

        skipButton.setOnClickListener(v -> {
            Intent intent = new Intent(IdentificationActivity.this, ConfirmationActivity.class); // Replace 'NextActivity' with your next activity class
            intent.putExtra("SELECTED_CAR", selectedCar); // If it's a Parcelable object
            intent.putExtra("pickupLocation", pickUpLocation);
            intent.putExtra("returnLocation", returnLocation);
            intent.putExtra("pickupDateTime", pickUpDateTime);
            intent.putExtra("returnDateTime", returnDateTime);
            intent.putExtra("isDriverRequired", isDriverRequired);

            startActivity(intent);
            finish();
        });
// Back button functionality
        backButton.setOnClickListener(v -> finish());
        // Next Button
        nextButton.setOnClickListener(v -> {
            if (radioGroup.getCheckedRadioButtonId() == R.id.radio_national_id && nationalIdInput.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Please enter your National ID", Toast.LENGTH_SHORT).show();
            } else if (radioGroup.getCheckedRadioButtonId() == R.id.radio_passport && passportIdInput.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Please enter your Passport Number", Toast.LENGTH_SHORT).show();
            } else if (frontPhotoUri == null || backPhotoUri == null) {
                Toast.makeText(this, "Please upload both front and back photos", Toast.LENGTH_SHORT).show();
            } else {
                saveNewDocCustomer();
            }
        });

        updateButton.setOnClickListener(v -> {
            updateCustomerDoc(); // Update existing documents
        });
        // Fetch customer document data on activity load
        fetchCustomerDoc();
    }




    private void fetchCustomerDoc() {
        // Initialize Retrofit instance
        ProgressBarUtils.showProgress(progressOverlay, progressBar, true); // Using utility class

        ApiService apiService = RetrofitClient.getRetrofitInstance(this).create(ApiService.class);

        // Call API to fetch customer documents
        Call<UploadIdResponse> call = apiService.fetchCustomerDoc();
        call.enqueue(new Callback<UploadIdResponse>() {
            @Override
            public void onResponse(Call<UploadIdResponse> call, Response<UploadIdResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProgressBarUtils.showProgress(progressOverlay, progressBar, false); // Using utility class

                    UploadIdResponse uploadIdResponse = response.body();

                    if (uploadIdResponse.getData() != null) {
                        UploadIdResponse.Data data = uploadIdResponse.getData();

                        // Set ID type and ID number to appropriate fields
                        if (data.getIdType() != null) {
                            nationalIdInput.setText(data.getIdType());
                        }
                        if (data.getIdNumber() != null) {
                            passportIdInput.setText(data.getIdNumber());
                        }

                        // Load the images using Glide if available
                        List<String> idImages = data.getIdImages();
                        if (idImages != null && !idImages.isEmpty()) {
                            // Load the first image as the front photo
                            Glide.with(IdentificationActivity.this)
                                    .load(idImages.get(0))
                                    .placeholder(R.drawable.profile)
                                    .error(R.drawable.profile)
                                    .into(frontPhoto);

                            // Load the second image (if exists) as the back photo
                            if (idImages.size() > 1) {
                                Glide.with(IdentificationActivity.this)
                                        .load(idImages.get(1))
                                        .placeholder(R.drawable.profile)
                                        .error(R.drawable.profile)
                                        .into(backPhoto);
                            }

                            // Show Update & Skip buttons and hide Next button
                            updateButton.setVisibility(View.VISIBLE);
                            skipButton.setVisibility(View.VISIBLE);
                            nextButton.setVisibility(View.GONE);
                        } else {
                            showNextButtonOnly();
                        }
                    } else {
                        showNextButtonOnly();
                    }
                } else {
                    showNextButtonOnly();
                }
            }

            @Override
            public void onFailure(Call<UploadIdResponse> call, Throwable t) {
                ProgressBarUtils.showProgress(progressOverlay, progressBar, true); // Using utility class

                Toast.makeText(IdentificationActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                showNextButtonOnly();
            }
        });
    }

    // Utility method to handle UI when no data exists
    private void showNextButtonOnly() {
        nextButton.setVisibility(View.VISIBLE);
        updateButton.setVisibility(View.GONE);
        skipButton.setVisibility(View.GONE);
    }





    private void updateCustomerDoc() {
        // Create Retrofit instance
        RetrofitClient retrofitClient = new RetrofitClient();
        ApiService apiService = RetrofitClient.getRetrofitInstance(this).create(ApiService.class);
        String type = (radioGroup.getCheckedRadioButtonId() == R.id.radio_national_id) ? "National ID" : "Passport";
        String nationalId = nationalIdInput.getText().toString().trim();
        String passportNumber = passportIdInput.getText().toString().trim();
        // Create RequestBody parts
        RequestBody typeBody = RequestBody.create(MediaType.parse("text/plain"), type);
        RequestBody idBody = RequestBody.create(MediaType.parse("text/plain"), type.equals("National ID") ? nationalId : passportNumber);

        MultipartBody.Part frontPart = createImagePart("front_photo", frontPhotoUri);
        MultipartBody.Part backPart = createImagePart("back_photo", backPhotoUri);

        ProgressBarUtils.showProgress(progressOverlay, progressBar, true); // Using utility class

        // Call API to update customer documents
        Call<UploadIdResponse> call = apiService.saveNewDocCustomer(typeBody,idBody,frontPart, backPart);
        call.enqueue(new Callback<UploadIdResponse>() {
            @Override
            public void onResponse(Call<UploadIdResponse> call, Response<UploadIdResponse> response) {
                ProgressBarUtils.showProgress(progressOverlay, progressBar, false); // Using utility class

                if (response.isSuccessful() && response.body() != null) {

                    Toast.makeText(IdentificationActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(IdentificationActivity.this,ConfirmationActivity.class);
                    // Put the necessary data into the Intent
                    intent.putExtra("SELECTED_CAR", selectedCar); // If it's a Parcelable object
                    intent.putExtra("pickupLocation", pickUpLocation);
                    intent.putExtra("returnLocation", returnLocation);
                    intent.putExtra("pickupDateTime", pickUpDateTime);
                    intent.putExtra("returnDateTime", returnDateTime);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(IdentificationActivity.this, "Failed to update documents.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UploadIdResponse> call, Throwable t) {
                ProgressBarUtils.showProgress(progressOverlay, progressBar, false); // Using utility class

                Toast.makeText(IdentificationActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveNewDocCustomer() {
        // Create Retrofit instance
        RetrofitClient retrofitClient = new RetrofitClient();
        ProgressBarUtils.showProgress(progressOverlay, progressBar, true); // Using utility class

        ApiService apiService = RetrofitClient.getRetrofitInstance(this).create(ApiService.class);

        String type = (radioGroup.getCheckedRadioButtonId() == R.id.radio_national_id) ? "National ID" : "Passport";
        String nationalId = nationalIdInput.getText().toString().trim();
        String passportNumber = passportIdInput.getText().toString().trim();
        // Create RequestBody parts
        RequestBody typeBody = RequestBody.create(MediaType.parse("text/plain"), type);
        RequestBody idBody = RequestBody.create(MediaType.parse("text/plain"), type.equals("National ID") ? nationalId : passportNumber);

        MultipartBody.Part frontPart = createImagePart("front_photo", frontPhotoUri);
        MultipartBody.Part backPart = createImagePart("back_photo", backPhotoUri);

        // Call API to save new customer documents
        Call<UploadIdResponse> call = apiService.saveNewDocCustomer(typeBody, idBody, frontPart, backPart);
        call.enqueue(new Callback<UploadIdResponse>() {
            @Override
            public void onResponse(Call<UploadIdResponse> call, Response<UploadIdResponse> response) {
                ProgressBarUtils.showProgress(progressOverlay, progressBar, false); // Using utility class

                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(IdentificationActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(IdentificationActivity.this,ConfirmationActivity.class);
                    // Put the necessary data into the Intent
                    intent.putExtra("SELECTED_CAR", selectedCar); // If it's a Parcelable object
                    intent.putExtra("pickupLocation", pickUpLocation);
                    intent.putExtra("returnLocation", returnLocation);
                    intent.putExtra("pickupDateTime", pickUpDateTime);
                    intent.putExtra("returnDateTime", returnDateTime);
                    startActivity(intent);
                } else {
                    Toast.makeText(IdentificationActivity.this, "Failed to save new documents.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UploadIdResponse> call, Throwable t) {
                ProgressBarUtils.showProgress(progressOverlay, progressBar, false); // Using utility class

                Toast.makeText(IdentificationActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private MultipartBody.Part createImagePart(String partName, Uri imageUri) {
        if (imageUri == null) {
            Log.e("myproblem", partName + " URI is null");
            return null;
        }

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            if (bitmap == null) {
                Log.e("myproblem", "Failed to decode the image");
                return null;
            }

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
            byte[] byteArray = stream.toByteArray();

            RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), byteArray);
            return MultipartBody.Part.createFormData(partName, partName + ".jpg", requestBody);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("myproblem", "Error processing image", e);
            return null;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                // Get the image file size
                File file = new File(getRealPathFromURI(selectedImageUri));
                long imageSize = file.length(); // Get the image size in bytes

                // Check if the image size is greater than 150KB
                if (imageSize > 150 * 1024) { // 150KB in bytes
                    Toast.makeText(this, "Image size exceeds 150KB. Please select a smaller image.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Decode the selected image to a bitmap if the size is valid
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);

                if (requestCode == REQUEST_FRONT_PHOTO) {
                    // Set the selected image to the front photo ImageView
                    frontPhoto.setImageBitmap(bitmap);
                    frontPhotoUri = selectedImageUri;
                } else if (requestCode == REQUEST_BACK_PHOTO) {
                    // Set the selected image to the back photo ImageView
                    backPhoto.setImageBitmap(bitmap);
                    backPhotoUri = selectedImageUri;
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Helper method to get the real path from URI
    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        try (Cursor cursor = getContentResolver().query(uri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(projection[0]);
                return cursor.getString(columnIndex);
            }
        }
        return null;
    }

}