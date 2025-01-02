package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class DriverProfileActivity extends AppCompatActivity {

    private ImageView profileImage, close;
    private TextView profileName, profilePhone;
    private RelativeLayout optionEditProfile, rides, documents, earnings,faq,contact_us, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile); // Make sure your XML layout is correct

        // Initialize views
        profileImage = findViewById(R.id.profile_image);
        profileName = findViewById(R.id.profile_name);
        profilePhone = findViewById(R.id.profile_phone);
        optionEditProfile = findViewById(R.id.option_edit_profile);
        rides = findViewById(R.id.rides);
        documents = findViewById(R.id.documents);
        faq = findViewById(R.id.faq);
        contact_us = findViewById(R.id.contact_us);
        earnings = findViewById(R.id.earnings);
        logout = findViewById(R.id.logout);
        close = findViewById(R.id.close); // Close button

        // Set initial profile details
        setProfileDetails();

        // Handle clicks on menu items
        optionEditProfile.setOnClickListener(v -> {
            // Debugging
            Log.d("DriverProfileActivity", "Edit Profile clicked!");

            // Redirect to Edit Profile Activity
            Intent intent = new Intent(DriverProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });


        rides.setOnClickListener(v -> {
            // Redirect to Rides Activity
            Intent intent = new Intent(DriverProfileActivity.this, MyRidesActivity.class);
            startActivity(intent);
        });
        documents.setOnClickListener(v -> {
            // Redirect to Rides Activity
            Intent intent = new Intent(DriverProfileActivity.this, MyDocumentsActivity.class);
            startActivity(intent);
        });
        contact_us.setOnClickListener(v -> {
            // Redirect to Rides Activity
            Intent intent = new Intent(DriverProfileActivity.this,DriverContactActivity.class);
            startActivity(intent);
        });
        faq.setOnClickListener(v -> {
            // Redirect to Rides Activity
            Intent intent = new Intent(DriverProfileActivity.this,DriverFaqActivity.class);
            startActivity(intent);
        });
        earnings.setOnClickListener(v -> {
            // Redirect to Rides Activity
            Intent intent = new Intent(DriverProfileActivity.this,DriverEarningsActivity.class);
            startActivity(intent);
        });
        // Set logout logic if necessary
        logout.setOnClickListener(v -> finish()); // Logout logic (finish current activity)

        // Close activity when the close icon is clicked
        close.setOnClickListener(v -> finish()); // Close activity on click of the close icon
    }

    private void setProfileDetails() {
        // Example data: replace with actual user data
        profileName.setText("Cameron Williamson");
        profilePhone.setText("(219) 555-0114");
        profileImage.setImageResource(R.drawable.profile); // Replace with actual profile image loading logic
    }
}
