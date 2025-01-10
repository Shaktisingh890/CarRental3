package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.utils.SharedPreferencesManager;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Log.d("login", "onCreate: Splash screen started.");

        // Delay for the splash screen (e.g., 3 seconds)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("login", "run: Checking login status...");

                // Check if the user is logged in
                if (SharedPreferencesManager.isLoggedIn(MainActivity.this)) {
                    Log.d("login", "run: User is logged in.");

                    String accessToken = SharedPreferencesManager.getAccessToken(MainActivity.this);

                    if (accessToken != null) {
                        Log.d("login", "run: Access token retrieved: " + accessToken);

                        // Decode the accessToken to get the user type
                        try {
                            String[] splitToken = accessToken.split("\\.");
                            if (splitToken.length == 3) {
                                String payload = new String(Base64.decode(splitToken[1], Base64.URL_SAFE | Base64.NO_WRAP | Base64.NO_PADDING));
                                Log.d("login", "run: Decoded payload: " + payload);

                                JSONObject jsonObject = new JSONObject(payload);

                                // Get the "type" field from the payload
                                String userType = jsonObject.getString("type");
                                Log.d("login", "run: User type from token: " + userType);

                                // Use switch-case to redirect based on the user type
                                switch (userType) {
                                    case "customer":
                                        Log.d("login", "run: Redirecting to AdminDashboardActivity.");
                                        startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                                        break;
                                    case "driver":
                                        Log.d("login", "run: Redirecting to UserDashboardActivity.");
                                        startActivity(new Intent(MainActivity.this,DriverDashboardActivity.class));
                                        break;
                                    case "partner":
                                        Log.d("login", "run: Redirecting to PartnerDashboardActivity.");
                                        startActivity(new Intent(MainActivity.this, PartnerDashboardActivity.class));
                                        break;
                                    default:
                                        Log.d("login", "run: User type unknown. Redirecting to InterfaceActivity.");
                                        startActivity(new Intent(MainActivity.this, InterfaceActivity.class));
                                        break;
                                }
                            } else {
                                Log.e("login", "run: Invalid token structure. Redirecting to login.");
                                redirectToLogin();
                            }
                        } catch (Exception e) {
                            Log.e("login", "run: Error decoding token: ", e);
                            redirectToLogin();
                        }
                    } else {
                        Log.e("login", "run: Access token is null. Redirecting to login.");
                        redirectToLogin();
                    }
                } else {
                    Log.d("login", "run: User not logged in. Redirecting to login.");
                    redirectToLogin();
                }
            }
        }, 3000); // 3000 milliseconds = 3 seconds
    }

    private void redirectToLogin() {
        Log.d("login", "redirectToLogin: Redirecting to LoginActivity.");
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
