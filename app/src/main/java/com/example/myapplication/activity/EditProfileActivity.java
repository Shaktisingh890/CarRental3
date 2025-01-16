package com.example.myapplication.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.models.response.ImageResponse;
import com.example.myapplication.models.response.UserProfileResponse;
import com.example.myapplication.network.ApiService;
import com.example.myapplication.network.RetrofitClient;
import com.example.myapplication.utils.ProgressBarUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.util.Log;

public class EditProfileActivity extends AppCompatActivity {
    private EditText etName, etEmail, etPhone, etAddress;
    private Button btnUpdate, btnDeleteImage;
    private ImageView profileImage, backButton;
    private static final int REQUEST_GALLERY = 2;
    private static final int REQUEST_CAMERA = 3;
    private Uri selectedImageUri;
    private View progressOverlay;
    private ProgressBar progressBar;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        progressBar = findViewById(R.id.progressBar);
        progressOverlay = findViewById(R.id.progressOverlay);
        imageView = findViewById(R.id.logoImage);
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        etAddress = findViewById(R.id.et_address);
        profileImage = findViewById(R.id.profile_image);
        btnUpdate = findViewById(R.id.btn_update);

        backButton = findViewById(R.id.back_button);

        backButton.setOnClickListener(v -> finish());

        profileImage.setOnClickListener(v -> openImageOptions());
        btnUpdate.setOnClickListener(v -> updateUserProfile());


        fetchUserProfile();
    }

    private void openImageOptions() {
        // Show a dialog to choose between camera, gallery, or Google Photos
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");

        // Intent to capture photo using camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Combine gallery and camera in a chooser
        Intent chooser = Intent.createChooser(intent, "Choose or Take a photo");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{cameraIntent});
        startActivityForResult(chooser, REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GALLERY && data != null) {
                selectedImageUri = data.getData();
                profileImage.setImageURI(selectedImageUri);
                uploadImageToServer();  // Upload image after selection from gallery
            } else if (requestCode == REQUEST_CAMERA && data != null) {
                // Handle camera photo capture
                Bitmap capturedImage = (Bitmap) data.getExtras().get("data");
                selectedImageUri = getImageUri(capturedImage);
                profileImage.setImageURI(selectedImageUri);
                uploadImageToServer();  // Upload captured image to server
            }
        }
    }

    private Uri getImageUri(Bitmap bitmap) {
        File file = new File(getCacheDir(), "profile_image.jpg");
        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            return Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void uploadImageToServer() {
        if (selectedImageUri == null) return;

        File file = getImageFile();
        if (file == null) return;
        ProgressBarUtils.showProgress(progressOverlay, progressBar, true, imageView); // Using utility class

        ApiService apiService = RetrofitClient.getRetrofitInstance(EditProfileActivity.this).create(ApiService.class);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);

        apiService.uploadImage(part).enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                ProgressBarUtils.showProgress(progressOverlay, progressBar, false, imageView);
                if (response.isSuccessful()) {
                    Toast.makeText(EditProfileActivity.this, "Image Changed successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                ProgressBarUtils.showProgress(progressOverlay, progressBar, false, imageView);

                Toast.makeText(EditProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private File getImageFile() {
        File file = new File(getCacheDir(), "profile_image.jpg");
        try (FileOutputStream out = new FileOutputStream(file)) {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }

    private void updateUserProfile() {
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String phoneNumber = etPhone.getText().toString();
        String address = etAddress.getText().toString();

        if (name.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }
        ProgressBarUtils.showProgress(progressOverlay, progressBar, true, imageView); // Using utility class

        ApiService apiService = RetrofitClient.getRetrofitInstance(EditProfileActivity.this).create(ApiService.class);
        RequestBody nameRequestBody = RequestBody.create(MultipartBody.FORM, name);
        RequestBody emailRequestBody = RequestBody.create(MultipartBody.FORM, email);
        RequestBody phoneRequestBody = RequestBody.create(MultipartBody.FORM, phoneNumber);
        RequestBody addressRequestBody = RequestBody.create(MultipartBody.FORM, address);

        apiService.updateUserProfile(nameRequestBody, emailRequestBody, phoneRequestBody, addressRequestBody)
                .enqueue(new Callback<UserProfileResponse>() {
                    @Override
                    public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                        ProgressBarUtils.showProgress(progressOverlay, progressBar, false, imageView); // Using utility class

                        if (response.isSuccessful()) {

                            Toast.makeText(EditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditProfileActivity.this, DashboardActivity.class);
                            startActivity(intent);
                                    } else {
                            Toast.makeText(EditProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                        ProgressBarUtils.showProgress(progressOverlay, progressBar, false, imageView); // Using utility class

                        Toast.makeText(EditProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchUserProfile() {
        Log.d("fetchUserProfile", "Making API call to fetch user profile");
        ProgressBarUtils.showProgress(progressOverlay, progressBar, true, imageView); // Using utility class

        ApiService apiService = RetrofitClient.getRetrofitInstance(EditProfileActivity.this).create(ApiService.class);
        apiService.getUserProfile().enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                ProgressBarUtils.showProgress(progressOverlay, progressBar, false, imageView); // Using utility class

                // Log the HTTP status code
                Log.d("fetchUserProfile", "Response Code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    // Log the raw response body
                    Log.d("fetchUserProfile", "Response Body: " + response.body().toString());

                    // Log individual fields from the response
                    UserProfileResponse user = response.body();
//                    Log.d("fetchUserProfile", "Full Name: " + user.getData().getFullName());
//                    Log.d("fetchUserProfile", "Email: " + user.getData().getEmail());
//                    Log.d("fetchUserProfile", "Phone Number: " + user.getData().getPhoneNumber());
//                    Log.d("fetchUserProfile", "Address: " + user.getData().getAddress());
//                    Log.d("fetchUserProfile", "Image URL: " + user.getData().getImgUrl());

                    // Populate the UI
                    etName.setText(user.getData().getFullName());
                    etEmail.setText(user.getData().getEmail());
                    etPhone.setText(user.getData().getPhoneNumber());
                    etAddress.setText(user.getData().getAddress());
                    Glide.with(EditProfileActivity.this).load(user.getData().getImgUrl()).into(profileImage);
                } else {
                    // Log the error body
                    try {
                        ProgressBarUtils.showProgress(progressOverlay, progressBar, false, imageView); // Using utility class

                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                        Log.d("fetchUserProfile", "Error Body: " + errorBody);
                    } catch (IOException e) {
                        ProgressBarUtils.showProgress(progressOverlay, progressBar, false,imageView); // Using utility class

                        Log.e("fetchUserProfile", "Error parsing error body: " + e.getMessage(), e);
                    }
                    ProgressBarUtils.showProgress(progressOverlay, progressBar, false,imageView); // Using utility class

                    Log.d("fetchUserProfile", "Failed to fetch profile. Error Code: " + response.code());
                    Toast.makeText(EditProfileActivity.this, "Failed to fetch profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                // Log the error message
                Log.e("fetchUserProfile", "Error: " + t.getMessage(), t);
                Toast.makeText(EditProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }




}
