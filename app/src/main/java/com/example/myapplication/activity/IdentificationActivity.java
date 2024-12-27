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
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import java.io.IOException;

public class IdentificationActivity extends AppCompatActivity {

    private static final int REQUEST_FRONT_PHOTO = 1;
    private static final int REQUEST_BACK_PHOTO = 2;

    private RadioGroup radioGroup;
    private EditText nationalIdInput;
    private Button nextButton;
    private ImageView backButton, frontPhoto, backPhoto;
    private Uri frontPhotoUri, backPhotoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identification);

        // Initialize Views
        radioGroup = findViewById(R.id.radio_group);
        nationalIdInput = findViewById(R.id.national_id_input);
        frontPhoto = findViewById(R.id.frontImageView);
        backPhoto = findViewById(R.id.backImageView);
        nextButton = findViewById(R.id.next_button);
        backButton = findViewById(R.id.back_button);

        // Handle Radio Button Selection
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_national_id) {
                nationalIdInput.setVisibility(View.VISIBLE);
            } else if (checkedId == R.id.radio_passport) {
                nationalIdInput.setVisibility(View.GONE);
            }
        });

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
            String nationalId = nationalIdInput.getText().toString().trim();
            if (radioGroup.getCheckedRadioButtonId() == R.id.radio_national_id && nationalId.isEmpty()) {
                Toast.makeText(this, "Please enter your National ID", Toast.LENGTH_SHORT).show();
            } else {
                // Proceed to ConfirmationActivity
                Intent intent = new Intent(IdentificationActivity.this, ConfirmationActivity.class);
                startActivity(intent);
            }
        });
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
