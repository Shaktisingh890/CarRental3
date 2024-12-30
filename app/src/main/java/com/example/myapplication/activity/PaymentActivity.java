package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.R;
import com.phonepe.intent.sdk.api.B2BPGRequest;
import com.phonepe.intent.sdk.api.B2BPGRequestBuilder;
import com.phonepe.intent.sdk.api.PhonePe;
import com.phonepe.intent.sdk.api.PhonePeInitException;
import com.phonepe.intent.sdk.api.models.PhonePeEnvironment;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivity extends AppCompatActivity {

    private static final int B2B_PG_REQUEST_CODE = 1;
    private static final String MERCHANT_SECRET_KEY = "PGTESTPAYUAT86";  // Replace with your actual merchant secret key
    private static final String SALT = "96434309-7796-489d-8924-ab56988a6076";  // Define or fetch the salt value
    private static final String SALT_INDEX = "1";  // Define or fetch the salt index
    private static final String TAG = "PaymentActivity";  // Tag for logging

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Retrieve data passed from ConfirmationActivity
        Intent intent = getIntent();
        int totalAmount = intent.getIntExtra("totalAmount", 0);
        String carDetails = intent.getStringExtra("carDetails");

        Log.d(TAG, "onCreate: Total Amount: " + totalAmount);
        Log.d(TAG, "onCreate: Car Details: " + carDetails);

        try {
            // Initialize PhonePe SDK for production (Assuming another initialization method is needed)
            PhonePe.init(this, PhonePeEnvironment.SANDBOX,"PGTESTPAYUAT86","");
            Log.d(TAG, "PhonePe SDK initialized successfully.");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing PhonePe SDK: ", e);
            Toast.makeText(this, "Failed to initialize payment gateway.", Toast.LENGTH_SHORT).show();
        }

        Button btnPay = findViewById(R.id.btnPay);
        btnPay.setOnClickListener(v -> {
            Log.d(TAG, "Pay button clicked. Initiating payment...");
            initiatePayment(70);
        });
    }

    private void initiatePayment(int totalAmount) {
        try {
            Log.d(TAG, "Creating payment payload...");

            // Create the JSON payload with the total amount and other details
            String payload = "{\n" +
                    "  \"merchantId\": \"PGTESTPAYUAT86\",\n" +
                    "  \"merchantTransactionId\": \"AVCFG123456\",\n" +
                    "  \"merchantUserId\": \"90223250\",\n" +
                    "  \"amount\": " + totalAmount + ",\n" +
                    "  \"mobileNumber\": \"7300896284\",\n" +
                    "  \"callbackUrl\": \"http://192.168.1.11:3000/callback\",\n" +  // Use HTTPS callback URL
                    "  \"paymentInstrument\": {\n" +
                    "    \"type\": \"UPI_INTENT\",\n" +
                    "    \"targetApp\": \"com.phonepe.app\"\n" +
                    "  },\n" +
                    "  \"deviceContext\": {\n" +
                    "    \"deviceOS\": \"ANDROID\"\n" +
                    "  }\n" +
                    "}";

            Log.d(TAG, "Payload: " + payload);

            // Base64 encode the payload
            String base64Payload = encodeToBase64(payload);
            Log.d(TAG, "Base64 Payload: " + base64Payload);

            // Generate checksum
            String apiEndPoint = "/pg/v1/pay";
            String checksum = generateChecksum(base64Payload, apiEndPoint, SALT);
            Log.d(TAG, "Checksum: " + checksum);

            // Build the B2B PG request
            B2BPGRequest b2BPGRequest = new B2BPGRequestBuilder()
                    .setData(base64Payload)
                    .setChecksum(checksum)
                    .setUrl(apiEndPoint)
                    .build();

            // Start payment
            Log.d(TAG, "Starting payment process...");
            startActivityForResult(PhonePe.getImplicitIntent(this, b2BPGRequest, ""), B2B_PG_REQUEST_CODE);
        } catch (Exception e) {
            Log.e(TAG, "Error initiating payment: ", e);
            Toast.makeText(this, "Payment initiation failed.", Toast.LENGTH_SHORT).show();
        }
    }

    private String encodeToBase64(String payload) {
        try {
            return Base64.encodeToString(payload.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT).trim();
        } catch (Exception e) {
            Log.e(TAG, "Error encoding payload to Base64: ", e);
            return null;
        }
    }

    private String generateChecksum(String base64Body, String apiEndPoint, String salt) {
        try {
            // Concatenate base64Body, apiEndPoint, and salt
            String dataToSign = base64Body + apiEndPoint + salt;

            Log.d(TAG, "Data to sign for checksum: " + dataToSign);

            // Generate SHA-256 hash
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] checksumBytes = digest.digest(dataToSign.getBytes(StandardCharsets.UTF_8));
            String checksum = Base64.encodeToString(checksumBytes, Base64.DEFAULT).trim();

            // Append saltIndex ("1" in your case) to the checksum
            return checksum + "###" + SALT_INDEX;
        } catch (Exception e) {
            Log.e(TAG, "Error generating checksum: ", e);
            return null;
        }
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == B2B_PG_REQUEST_CODE) {
            if (data != null && data.getExtras() != null) {
                Bundle extras = data.getExtras();
                for (String key : extras.keySet()) {
                    Object value = extras.get(key);
                    Log.d(TAG, "Extra Data: " + key + " = " + value);
                }

                if (extras.containsKey("key_error_result")) {
                    String errorResult = extras.getString("key_error_result");
                    Log.e(TAG, "Error Result: " + errorResult);
                }
            }

            if (resultCode == RESULT_OK) {
                Log.d(TAG, "Payment Successful");
                // Handle success
            } else if (resultCode == RESULT_CANCELED) {
                String cancellationReason = data != null ? data.getStringExtra("reason") : "Unknown reason";
                Log.d(TAG, "Payment Canceled. Reason: " + cancellationReason);

                if (data != null) {
                    Log.d(TAG, "Data returned: " + data.toString());
                }
            } else {
                String errorMessage = data != null ? data.getStringExtra("errorMessage") : "Unknown error";
                Log.e(TAG, "Payment Failed. Error: " + errorMessage);
            }
        } else {
            Log.w(TAG, "Unhandled requestCode: " + requestCode);
        }
    }

}
