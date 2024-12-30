package com.example.myapplication.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.models.response.CustomerCarResponse;
import com.example.myapplication.models.response.UploadIdResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.myapplication.network.ApiService;
import com.example.myapplication.network.RetrofitClient;


public class IdentificationActivity extends AppCompatActivity {

    private static final int REQUEST_FRONT_PHOTO = 1;
    private static final int REQUEST_BACK_PHOTO = 2;

    private RadioGroup radioGroup;
    private EditText nationalIdInput,passportIdInput;
    private Button nextButton;
    private ImageView backButton, frontPhoto, backPhoto;
    private Uri frontPhotoUri, backPhotoUri;
    private CustomerCarResponse selectedCar;



    private static final String TAG = "IdentificationActivity"; // Define TAG here
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identification);

        // Initialize Views
        radioGroup = findViewById(R.id.radio_group);
        nationalIdInput = findViewById(R.id.national_id_input);
        passportIdInput=findViewById(R.id.passport_id_input);
        frontPhoto = findViewById(R.id.frontImageView);
        backPhoto = findViewById(R.id.backImageView);
        nextButton = findViewById(R.id.next_button);
        backButton = findViewById(R.id.back_button);

        // Handle Radio Button Selection
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_national_id) {
                nationalIdInput.setVisibility(View.VISIBLE);
                passportIdInput.setVisibility(View.GONE);
            } else if (checkedId == R.id.radio_passport) {
                nationalIdInput.setVisibility(View.GONE);
                passportIdInput.setVisibility(View.VISIBLE);
            }
        });

        // Retrieve the car object from the intent
        Intent intent1 = getIntent();
        selectedCar = intent1.getParcelableExtra("SELECTED_CAR");
        String pickUpLocation = intent1.getStringExtra("pickupLocation");
        String returnLocation = intent1.getStringExtra("returnLocation");
        String pickUpDateTime = intent1.getStringExtra("pickupDateTime");
        String returnDateTime = intent1.getStringExtra("returnDateTime");
        if (selectedCar != null) {
            Log.d(TAG, "Selected Car: " + selectedCar.toString());
            Log.d(TAG, "Brand: " + pickUpLocation);
        } else {
            Log.e(TAG, "No car details received!");
        }
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
                uploadIdentificationData();
            }
        });

    }

    private void uploadIdentificationData() {
        String type = (radioGroup.getCheckedRadioButtonId() == R.id.radio_national_id) ? "National ID" : "Passport";
        String nationalId = nationalIdInput.getText().toString().trim();
        String passportNumber = passportIdInput.getText().toString().trim();

        Intent intent1 = getIntent();
        selectedCar = intent1.getParcelableExtra("SELECTED_CAR");
        String pickUpLocation = intent1.getStringExtra("pickupLocation");
        String returnLocation = intent1.getStringExtra("returnLocation");
        String pickUpDateTime = intent1.getStringExtra("pickupDateTime");
        String returnDateTime = intent1.getStringExtra("returnDateTime");

        // Create Retrofit instance
        RetrofitClient retrofitClient = new RetrofitClient();
        ApiService apiService = RetrofitClient.getRetrofitInstance(this).create(ApiService.class);

        // Create RequestBody parts
        RequestBody typeBody = RequestBody.create(MediaType.parse("text/plain"), type);
        RequestBody idBody = RequestBody.create(MediaType.parse("text/plain"), type.equals("National ID") ? nationalId : passportNumber);


        MultipartBody.Part frontPart = createImagePart("front_photo", frontPhotoUri);
        MultipartBody.Part backPart = createImagePart("back_photo", backPhotoUri);

        // Call API
        Call<UploadIdResponse> call = apiService.uploadId(typeBody,idBody, frontPart, backPart);
        call.enqueue(new Callback<UploadIdResponse>() {
            @Override
            public void onResponse(Call<UploadIdResponse> call, Response<UploadIdResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    Toast.makeText(IdentificationActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    // Proceed to next activity if needed
                    Intent intent = new Intent(IdentificationActivity.this,ConfirmationActivity.class);
                    // Put the necessary data into the Intent
                    intent.putExtra("SELECTED_CAR", selectedCar); // If it's a Parcelable object
                    intent.putExtra("pickupLocation", pickUpLocation);
                    intent.putExtra("returnLocation", returnLocation);
                    intent.putExtra("pickupDateTime", pickUpDateTime);
                    intent.putExtra("returnDateTime", returnDateTime);
                    startActivity(intent);
                } else {
                    Toast.makeText(IdentificationActivity.this, "Upload failed. Try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UploadIdResponse> call, Throwable t) {
                Toast.makeText(IdentificationActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Helper method to create MultipartBody.Part for an image
    private MultipartBody.Part createImagePart(String partName, Uri imageUri) {
        if (imageUri == null) return null;

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
            byte[] byteArray = stream.toByteArray();

            RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), byteArray);
            return MultipartBody.Part.createFormData(partName, partName + ".jpg", requestBody);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                // Decode the selected image to a bitmap
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
}
