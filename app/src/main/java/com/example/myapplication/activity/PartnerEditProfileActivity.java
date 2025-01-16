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
import com.example.myapplication.models.response.UserProfileResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PartnerEditProfileActivity extends AppCompatActivity {

    private ImageView profilePicture;
    private EditText partnerName, partnerPhone, partnerEmail, companyName, businessAddress, serviceArea, bankAccount, upiId;
    private Button saveButton, cancelButton;
    private ImageView backButton;
    private static final int REQUEST_GALLERY = 2;
    private static final int REQUEST_CAMERA = 3;
    private Uri selectedImageUri;

    private View progressOverlay;
    private ProgressBar progressBar;
    private ImageView imageView;
    private String fullName,email,phoneNumber,address,imgUrl,upi_id,company_name,company_address,area,account_number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_edit_profile);

        // Initialize Views
        profilePicture = findViewById(R.id.profile_picture);
        partnerName = findViewById(R.id.partner_name);
        partnerPhone = findViewById(R.id.partner_phone);
        partnerEmail = findViewById(R.id.partner_email);
        companyName = findViewById(R.id.company_name);
        businessAddress = findViewById(R.id.business_address);
        serviceArea = findViewById(R.id.service_area);
        bankAccount = findViewById(R.id.bank_account);
        upiId = findViewById(R.id.upi_id);
        saveButton = findViewById(R.id.save_button);
        cancelButton = findViewById(R.id.cancel_button);
        backButton = findViewById(R.id.back_button);
        progressBar = findViewById(R.id.progressBar);
        progressOverlay = findViewById(R.id.progressOverlay);
        imageView = findViewById(R.id.logoImage);

        // Handle Back Button click
        backButton.setOnClickListener(v -> finish());

        fullName= getIntent().getStringExtra("fullName");
        email = getIntent().getStringExtra("email");
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        address = getIntent().getStringExtra("address");
        imgUrl = getIntent().getStringExtra("imgUrl");
        upi_id=getIntent().getStringExtra("upi_id");
        account_number=getIntent().getStringExtra("account_number");
        company_address=getIntent().getStringExtra("company_address");
        company_name=getIntent().getStringExtra("company_name");
        area=getIntent().getStringExtra("area");

        Log.d("myfullname1","my name is "+fullName);
        Log.d("myfullname1","my name is bb "+phoneNumber);
        Log.d("myfullname1","my name is bbb "+imgUrl);
        Log.d("myfullname1","my name is bbb "+upi_id);

        Log.d("myfullname1","my name is bbb "+company_address);

        Log.d("myfullname1","my name is bbb "+area);

        Log.d("myfullname1","my name is bbb "+account_number);






        if (fullName != null) {
            partnerName.setText(fullName); // Set the user's full name
        } else {
            partnerName.setText("");
        }

        if (company_name != null) {
            companyName.setText(company_name); // Set the user's full name
        } else {
            companyName.setText("");
        }

        if ( area != null) {
            serviceArea.setText(area); // Set the user's full name
        } else {
            serviceArea.setText("");
        }

        if ( company_address != null) {
            businessAddress.setText(company_address); // Set the user's full name
        } else {
            businessAddress.setText("");
        }

        if ( upi_id!= null) {
            upiId.setText(upi_id); // Set the user's full name
        } else {
            upiId.setText("");
        }

        if ( account_number!= null) {
            bankAccount.setText(account_number); // Set the user's full name
        } else {
            bankAccount.setText("");
        }

        if (email != null) {
            partnerEmail.setText(email); // Set the user's full name
        } else {
            partnerEmail.setText("");
        }

        if (partnerPhone != null) {
            partnerPhone.setText(phoneNumber); // Set the user's full name
        } else {
            partnerPhone.setText("");
        }

        if (profilePicture != null && imgUrl != null && !imgUrl.isEmpty()) {
            // Use Glide to load the image from URL
            Glide.with(this)
                    .load(imgUrl)
                    .placeholder(R.drawable.profile) // Placeholder while loading
                    .error(R.drawable.profile)       // Fallback in case of an error
                    .into(profilePicture);
        } else {
            profilePicture.setImageResource(R.drawable.profile); // Set default profile picture
        }

        // Set Click Listeners
        profilePicture.setOnClickListener(v -> openImageOptions());

        saveButton.setOnClickListener(v -> updatePartnerProfile());
        cancelButton.setOnClickListener(v -> onCancelEdit());
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



    private void onChangeProfilePicture() {
        Toast.makeText(this, "Change Profile Picture clicked", Toast.LENGTH_SHORT).show();
        // Implement profile picture change functionality here
    }


    private void updatePartnerProfile() {
        String name = partnerName.getText().toString().trim();
        String phone = partnerPhone.getText().toString().trim();
        String email = partnerEmail.getText().toString().trim();
        String company = companyName.getText().toString().trim();
        String companyAddress = businessAddress.getText().toString().trim();
        String area = serviceArea.getText().toString().trim();
        String account = bankAccount.getText().toString().trim();
        String upi = upiId.getText().toString().trim();

        // Validation checks
        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || company.isEmpty() ||
                companyAddress.isEmpty() || area.isEmpty() || account.isEmpty() || upi.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressBarUtils.showProgress(progressOverlay, progressBar, true,imageView); // Using utility class

        // Prepare request bodies for all fields
        RequestBody nameRequestBody = RequestBody.create(MultipartBody.FORM, name);
        RequestBody phoneRequestBody = RequestBody.create(MultipartBody.FORM, phone);
        RequestBody emailRequestBody = RequestBody.create(MultipartBody.FORM, email);
        RequestBody companyRequestBody = RequestBody.create(MultipartBody.FORM, company);
        RequestBody companyAddressRequestBody = RequestBody.create(MultipartBody.FORM, companyAddress);
        RequestBody areaRequestBody = RequestBody.create(MultipartBody.FORM, area);
        RequestBody accountRequestBody = RequestBody.create(MultipartBody.FORM, account);
        RequestBody upiRequestBody = RequestBody.create(MultipartBody.FORM, upi);

        // Initialize API service
        ApiService apiService = RetrofitClient.getRetrofitInstance(PartnerEditProfileActivity.this).create(ApiService.class);

        // API call
        apiService.updatePartnerProfile(
                nameRequestBody,
                phoneRequestBody,
                emailRequestBody,
                companyRequestBody,
                companyAddressRequestBody,
                areaRequestBody,
                accountRequestBody,
                upiRequestBody
        ).enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProgressBarUtils.showProgress(progressOverlay, progressBar, false,imageView); // Hide progress ba
                    Toast.makeText(PartnerEditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(PartnerEditProfileActivity.this, PartnerDashboardActivity.class);

                    startActivity(intent);
                } else {
                    Toast.makeText(PartnerEditProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                ProgressBarUtils.showProgress(progressOverlay, progressBar, false,imageView); // Hide progress bar
                Toast.makeText(PartnerEditProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void onCancelEdit() {
        Toast.makeText(this, "Edit cancelled", Toast.LENGTH_SHORT).show();
        finish(); // Close the activity and go back to the previous screen
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


    private void uploadImageToServer() {
        if (selectedImageUri == null) return;

        File file = getImageFile();
        if (file == null) return;
        ProgressBarUtils.showProgress(progressOverlay, progressBar, true,imageView); // Using utility class

        ApiService apiService = RetrofitClient.getRetrofitInstance(PartnerEditProfileActivity.this).create(ApiService.class);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);

        apiService.uploadImage(part).enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                ProgressBarUtils.showProgress(progressOverlay, progressBar, false,imageView);
                if (response.isSuccessful()) {
                    Toast.makeText(PartnerEditProfileActivity.this, "Image Changed successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PartnerEditProfileActivity.this, PartnerDashboardActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(PartnerEditProfileActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                ProgressBarUtils.showProgress(progressOverlay, progressBar, false,imageView);

                Toast.makeText(PartnerEditProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GALLERY && data != null) {
                selectedImageUri = data.getData();
                profilePicture.setImageURI(selectedImageUri);
                uploadImageToServer();  // Upload image after selection from gallery
            } else if (requestCode == REQUEST_CAMERA && data != null) {
                // Handle camera photo capture
                Bitmap capturedImage = (Bitmap) data.getExtras().get("data");
                selectedImageUri = getImageUri(capturedImage);
                profilePicture.setImageURI(selectedImageUri);
                uploadImageToServer();  // Upload captured image to server
            }
        }
    }
}
