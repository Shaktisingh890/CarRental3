package com.example.myapplication.activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.Manifest;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myapplication.R;

public class MyDocumentsActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;
    private static final int PERMISSION_REQUEST_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_documents);

        Button btnDrivingLicense = findViewById(R.id.btnDrivingLicense);
        Button btnIdCard = findViewById(R.id.btnIdCard);
        Button btnVehicleReg = findViewById(R.id.btnVehicleReg);
        Button btnVehicleImage = findViewById(R.id.btnVehicleImage);
        Button btnContinue = findViewById(R.id.btnContinue);
        // Find the backArrow ImageView
        ImageView backArrow = findViewById(R.id.backArrow);
        // Set a click listener to handle the back navigation
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the previous page
                finish();
            }
        });
        btnDrivingLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomUploadDialog();
            }
        });

        btnIdCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomUploadDialog();
            }
        });

        btnVehicleReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomUploadDialog();
            }
        });

        btnVehicleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomUploadDialog();
            }
        });


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSuccessDialog();
            }
        });
    }

    private void showCustomUploadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View customLayout = getLayoutInflater().inflate(R.layout.upload_driver_documents, null);
        builder.setView(customLayout);

        AlertDialog dialog = builder.create();
        dialog.show();

        // Handle clicks for options in the dialog
        customLayout.findViewById(R.id.btnTakePhoto).setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(cameraIntent, REQUEST_CAMERA);
            }
            dialog.dismiss();
        });

        customLayout.findViewById(R.id.btnGallery).setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, REQUEST_GALLERY);
            dialog.dismiss();
        });

        customLayout.findViewById(R.id.btnCancel).setOnClickListener(v -> {
            dialog.dismiss();
        });
    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View customLayout = getLayoutInflater().inflate(R.layout.driver_dialog_success, null);
        builder.setView(customLayout);

        AlertDialog dialog = builder.create();
        dialog.show();

        // Dismiss dialog when the "OK" button is clicked
        customLayout.findViewById(R.id.btnOk).setOnClickListener(v -> dialog.dismiss());
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA && data != null) {
                // Camera image captured
                Bundle extras = data.getExtras();
                Uri imageUri = (Uri) extras.get(MediaStore.EXTRA_OUTPUT); // Or use a Bitmap
                Toast.makeText(this, "Photo captured: " + imageUri, Toast.LENGTH_SHORT).show();
            } else if (requestCode == REQUEST_GALLERY && data != null) {
                // Gallery image selected
                Uri selectedImageUri = data.getData();
                Toast.makeText(this, "Image selected: " + selectedImageUri, Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                    100);
        }
    }


}
