package com.example.myapplication.activity;

import android.content.Intent;
import android.database.Cursor;
import android.content.CursorLoader;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.models.response.DriverResponse1;
import com.example.myapplication.network.ApiService;
import com.example.myapplication.network.RetrofitClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DriverSignupActivity extends AppCompatActivity {

    private EditText fullNameEditText, emailEditText, mobileEditText, passwordEditText, licenseNumberEditText, licenseExpiryDateEditText;
    private Button registerButton;
    private CheckBox termsCheckBox;
    private ImageView backArrow, frontPhoto, backPhoto;

    private Retrofit retrofit;
    private ApiService apiService;

    private Uri frontPhotoUri, backPhotoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_signup);

        // Initialize UI elements
        fullNameEditText = findViewById(R.id.fullNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        mobileEditText = findViewById(R.id.mobileEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        licenseNumberEditText = findViewById(R.id.licenseNumberEditText);
        licenseExpiryDateEditText = findViewById(R.id.licenseExpiryDateEditText);
        registerButton = findViewById(R.id.registerButton);
        termsCheckBox = findViewById(R.id.termsCheckBox);
        backArrow = findViewById(R.id.backArrow);
        frontPhoto = findViewById(R.id.frontImageView);
        backPhoto = findViewById(R.id.backImageView);

        retrofit = RetrofitClient.getRetrofitInstance(DriverSignupActivity.this);
        apiService = retrofit.create(ApiService.class);

        // Back button
        backArrow.setOnClickListener(v -> onBackPressed());

        // Register button
        registerButton.setOnClickListener(v -> {
            String fullName = fullNameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String mobile = mobileEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String licenseNumber = licenseNumberEditText.getText().toString().trim();
            String licenseExpiryDate = licenseExpiryDateEditText.getText().toString().trim();
            boolean isTermsAccepted = termsCheckBox.isChecked();

            if (fullName.isEmpty() || email.isEmpty() || mobile.isEmpty() || password.isEmpty() || licenseNumber.isEmpty() || licenseExpiryDate.isEmpty()) {
                Toast.makeText(DriverSignupActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            } else if (!isTermsAccepted) {
                Toast.makeText(DriverSignupActivity.this, "Please agree to the terms and conditions", Toast.LENGTH_SHORT).show();
            } else {
                registerDriverWithImages(fullName, email, mobile, password, licenseNumber, licenseExpiryDate, frontPhotoUri, backPhotoUri);
            }
        });

        // Image selection for front photo
        frontPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
        });

        // Image selection for back photo
        backPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 2);
        });
    }

    private void registerDriverWithImages(String fullName, String email, String phoneNumber, String password, String licenseNumber, String licenseExpiryDate, Uri frontPhotoUri, Uri backPhotoUri) {
        // Convert all text data to RequestBody
        RequestBody fullNamePart = RequestBody.create(MediaType.parse("text/plain"), fullName);
        RequestBody emailPart = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody phoneNumberPart = RequestBody.create(MediaType.parse("text/plain"), phoneNumber);
        RequestBody passwordPart = RequestBody.create(MediaType.parse("text/plain"), password);
        RequestBody licenseNumberPart = RequestBody.create(MediaType.parse("text/plain"), licenseNumber);
        RequestBody licenseExpiryDatePart = RequestBody.create(MediaType.parse("text/plain"), licenseExpiryDate);

        MultipartBody.Part frontPhotoPart = null;
        MultipartBody.Part backPhotoPart = null;

        // Handle front photo if provided
        if (frontPhotoUri != null) {
            String frontPhotoPath = createTempFileFromUri(frontPhotoUri);
            if (frontPhotoPath != null) {
                File frontFile = new File(frontPhotoPath);
                RequestBody frontPhotoRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), frontFile);
                frontPhotoPart = MultipartBody.Part.createFormData("licenseFront", frontFile.getName(), frontPhotoRequestBody);
            }
        }

        // Handle back photo if provided
        if (backPhotoUri != null) {
            String backPhotoPath = createTempFileFromUri(backPhotoUri);
            if (backPhotoPath != null) {
                File backFile = new File(backPhotoPath);
                RequestBody backPhotoRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), backFile);
                backPhotoPart = MultipartBody.Part.createFormData("licenseBack", backFile.getName(), backPhotoRequestBody);
            }
        }

        // API call to register the driver
        Call<DriverResponse1> call = apiService.registerDriverWithPhotos(
                frontPhotoPart, backPhotoPart,
                fullNamePart, emailPart, phoneNumberPart, passwordPart,
                licenseNumberPart, licenseExpiryDatePart
        );

        call.enqueue(new Callback<DriverResponse1>() {
            @Override
            public void onResponse(Call<DriverResponse1> call, Response<DriverResponse1> response) {
                if (response.isSuccessful()) {
                    Log.d("DriverSignupActivity", "Registration successful. Response: " + response.body());
                    Toast.makeText(DriverSignupActivity.this, "Driver registration successful", Toast.LENGTH_SHORT).show();
                    // Redirect to LoginActivity after successful registration
                    Intent intent = new Intent(DriverSignupActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        Log.e("DriverSignupActivity", "Registration failed. Error: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(DriverSignupActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DriverResponse1> call, Throwable t) {
                Log.e("DriverSignupActivity", "API call failed. Error: " + t.getMessage());
                Toast.makeText(DriverSignupActivity.this, "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String createTempFileFromUri(Uri uri) {
        File tempFile = null;
        try {
            String fileName = System.currentTimeMillis() + ".jpg";
            tempFile = new File(getCacheDir(), fileName);
            try (InputStream inputStream = getContentResolver().openInputStream(uri);
                 FileOutputStream outputStream = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                Log.d("DriverSignupActivity", "Temp file created: " + tempFile.getAbsolutePath());
            }
        } catch (Exception e) {
            Log.e("DriverSignupActivity", "Error creating temp file", e);
        }
        return tempFile != null ? tempFile.getAbsolutePath() : null;
    }

    private void deleteTempFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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

                if (requestCode == 1) {
                    // Set the selected image to the front photo ImageView
                    Log.d("DriverSignupActivity", "Front photo URI: " + selectedImageUri.toString());
                    frontPhoto.setImageBitmap(bitmap);
                    frontPhotoUri = selectedImageUri;
                } else if (requestCode == 2) {
                    // Set the selected image to the back photo ImageView
                    Log.d("DriverSignupActivity", "Back photo URI: " + selectedImageUri.toString());
                    backPhoto.setImageBitmap(bitmap);
                    backPhotoUri = selectedImageUri;
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Helper method to get the real file path from URI
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }

}
