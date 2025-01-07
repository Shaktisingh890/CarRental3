package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.models.response.ChangePasswordResponse;
import com.example.myapplication.models.request.ChangePasswordRequest;
import com.example.myapplication.network.RetrofitClient;
import com.google.android.material.textfield.TextInputEditText;
import com.example.myapplication.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PartnerChangePasswordActivity extends AppCompatActivity {

    private TextInputEditText currentPasswordEditText, passwordEditText, confirmPasswordEditText;
    private TextView passwordStrengthText, passwordMatchError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_change_password);

        // Initialize views
        ImageView backArrow = findViewById(R.id.backArrow);
        currentPasswordEditText = findViewById(R.id.currentPasswordEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        passwordStrengthText = findViewById(R.id.passwordStrengthText);
        passwordMatchError = findViewById(R.id.passwordMatchError);
        Button registerButton = findViewById(R.id.registerButton);

        // Back arrow functionality
        backArrow.setOnClickListener(v -> onBackPressed());

        // Password strength validation
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePasswordStrength(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Reset password button functionality
        registerButton.setOnClickListener(v -> resetPassword());
    }

    private void validatePasswordStrength(String password) {
        if (password.length() >= 8) {
            passwordStrengthText.setText("Password strength: Strong");
            passwordStrengthText.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            passwordStrengthText.setText("Must be at least 8 characters.");
            passwordStrengthText.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }
    }

    private void resetPassword() {
        String currentPassword = currentPasswordEditText.getText().toString();
        String newPassword = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            passwordMatchError.setVisibility(View.VISIBLE);
            return;
        }

        passwordMatchError.setVisibility(View.GONE);

        // Create the request object
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(currentPassword, newPassword, confirmPassword);

        // Create a Retrofit instance
        ApiService apiService = RetrofitClient.getRetrofitInstance(this).create(ApiService.class);

        // Make the API call to change the password
        Call<ChangePasswordResponse> call = apiService.changePassword(changePasswordRequest);
        call.enqueue(new Callback<ChangePasswordResponse>() {
            @Override
            public void onResponse(Call<ChangePasswordResponse> call, Response<ChangePasswordResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ChangePasswordResponse changePasswordResponse = response.body();
                    if (changePasswordResponse.isSuccess()) {
                        Toast.makeText(PartnerChangePasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PartnerChangePasswordActivity.this,LoginActivity.class);
                        // Put the necessary data into the Intent

                        startActivity(intent);
                        finish();
                        // Optionally navigate to another screen or perform additional actions
                    } else {
                        Toast.makeText(PartnerChangePasswordActivity.this, changePasswordResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PartnerChangePasswordActivity.this, "Password change failed. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChangePasswordResponse> call, Throwable t) {
                Toast.makeText(PartnerChangePasswordActivity.this, "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
