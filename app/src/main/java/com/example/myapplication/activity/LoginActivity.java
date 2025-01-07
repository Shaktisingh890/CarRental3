package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.models.request.LoginRequest;
import com.example.myapplication.models.response.LoginResponse;
import com.example.myapplication.network.ApiService;
import com.example.myapplication.network.RetrofitClient;
import com.example.myapplication.utils.SharedPreferencesManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private View progressOverlay; // Added overlay for blur effect
    private ProgressBar progressBar;
    EditText loginEmail, loginPassword;
    Button loginButton;
    TextView signupRedirectText;
    private String token,token1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (FirebaseApp.getApps(this).isEmpty()) {
            Log.d("Firebase", "Firebase not initialized");
        } else {
            Log.d("Firebase", "Firebase initialized");
        }


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                         token1 = task.getResult();
                        Log.d("FCM Token", "Device token1: " + token1);
                    } else {
                        Log.e("FCM Token1", "Failed to get token");
                    }
                });

        // Initialize views
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);
        signupRedirectText = findViewById(R.id.signupRedirectText);
        progressBar = findViewById(R.id.progressBar);
        progressOverlay = findViewById(R.id.progressOverlay); // Overlay view for blur effect

        // Set login button click listener
        loginButton.setOnClickListener(v -> handleLogin());

        // Set signup redirect text click listener
        signupRedirectText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupDashboardActivity.class);
            startActivity(intent);
        });
    }

    private void handleLogin() {
        String email = loginEmail.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();

        // Validate input
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
            return;
        }




                        // Show progress bar and overlay
        showProgress(true); // Modified to include overlay visibility

        // Make the POST request to the login API
        ApiService apiService = RetrofitClient.getRetrofitInstance(LoginActivity.this).create(ApiService.class);
        LoginRequest loginRequest = new LoginRequest(email, password);

        Log.d("FCM My Token", "Device token: " + token1);
        loginRequest.setDeviceToken(token1);

        Call<LoginResponse> call = apiService.login(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                showProgress(false); // Modified to include overlay visibility

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse.isSuccess()) {
                        // Save tokens to SharedPreferences
                        SharedPreferencesManager.saveTokens(LoginActivity.this, loginResponse.getData().getAccessToken(), loginResponse.getData().getRefreshToken(),true);

                        // Determine user role and navigate accordingly
                        String role = loginResponse.getData().getUser().getRole();
                        Intent intent;

                        switch (role) {
                            case "customer":
                                intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                break;
                            case "partner":
                                intent = new Intent(LoginActivity.this, PartnerDashboardActivity.class);
                                break;
                            case "driver":
                                intent = new Intent(LoginActivity.this, DriverDashboardActivity.class);
                                break;
                            default:
                                Toast.makeText(LoginActivity.this, "Unknown role: " + role, Toast.LENGTH_SHORT).show();
                                return;
                        }
                        intent.putExtra("user_email", loginResponse.getData().getUser().getEmail());
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String errorMessage = response.message() != null ? response.message() : "Unknown error";
                    Toast.makeText(LoginActivity.this, "Login Failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Unsuccessful response: " + errorMessage);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                showProgress(false); // Modified to include overlay visibility
                Toast.makeText(LoginActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Login request failed", t);
            }
        });
    }

    // Method to handle showing and hiding progress bar with overlay
    private void showProgress(boolean show) {
        if (show) {
            progressOverlay.setVisibility(View.VISIBLE); // Show overlay
            progressBar.setVisibility(View.VISIBLE); // Show progress bar
        } else {
            progressOverlay.setVisibility(View.GONE); // Hide overlay
            progressBar.setVisibility(View.GONE); // Hide progress bar
        }
    }
}
