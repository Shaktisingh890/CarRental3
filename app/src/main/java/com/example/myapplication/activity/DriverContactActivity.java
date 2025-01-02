package com.example.myapplication.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class DriverContactActivity extends AppCompatActivity {

    private ImageView backIcon;
    private EditText subjectInput, messageInput;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_contact);

        // Initialize views
        backIcon = findViewById(R.id.backIcon);
        subjectInput = findViewById(R.id.subjectInput);
        messageInput = findViewById(R.id.messageInput);
        submitButton = findViewById(R.id.submitButton);

        // Back button functionality
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Submit button functionality
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = subjectInput.getText().toString().trim();
                String message = messageInput.getText().toString().trim();

                if (subject.isEmpty() || message.isEmpty()) {
                    Toast.makeText(DriverContactActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Create and show an AlertDialog (popup)
                    new android.app.AlertDialog.Builder(DriverContactActivity.this)
                            .setTitle("Message Submitted")
                            .setMessage("Your message has been submitted successfully.")
                            .setPositiveButton("OK", null)
                            .show();

                    // Add further processing logic here (e.g., send to server)
                }
            }
        });

    }
}
