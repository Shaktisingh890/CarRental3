package com.example.myapplication.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.example.myapplication.models.response.CustomerCarResponse;
import com.phonepe.intent.sdk.api.PhonePe;
import com.phonepe.intent.sdk.api.PhonePeInitException;
import com.phonepe.intent.sdk.api.models.PhonePeEnvironment;
import com.phonepe.intent.sdk.api.B2BPGRequest;
import com.phonepe.intent.sdk.api.B2BPGRequestBuilder;
import org.json.JSONObject;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PaymentActivity extends AppCompatActivity {


    private static final String TAG = "PaymentActivity"; // Define a tag for logging
    private static final int B2B_PG_REQUEST_CODE = 1; // Request code for startActivityForResult

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Retrieve data from intent
        Intent intent = getIntent();
        int totalAmount = intent.getIntExtra("totalAmount", 0);
        CustomerCarResponse carDetails = intent.getParcelableExtra("carDetails");


        Log.d("Amount","total amoount is "+totalAmount);
        // Initialize PhonePe SDK

            PhonePe.init(this, PhonePeEnvironment.SANDBOX, "ATMOSTUAT", "");
            Log.d(TAG, "PhonePe SDK initialized successfully");






        Log.d(TAG, "Starting payment process...");
                initiateB2BPayment(totalAmount);

    }

    private void initiateB2BPayment(int totalAmount) {
        String base64Body = preparePayload(totalAmount); // Prepare the payload and Base64 encode it
        if (base64Body == null) {
            Log.e(TAG, "Payload preparation failed, aborting payment initiation");
            Toast.makeText(this, "Payment failed: Payload preparation error", Toast.LENGTH_SHORT).show();
            return;
        }

        String salt = "58a63b64-574d-417a-9214-066bee1e4caa"; // Replace with your actual salt
        String saltIndex = "1"; // Replace with your actual salt index
        String apiEndPoint = "/pg/v1/pay"; // Replace with your actual API endpoint

        // Log the generated base64 payload
        Log.d(TAG, "Base64 Payload: " + base64Body);

        // Generate checksum in the required format
        String checksum = sha256(base64Body + apiEndPoint + salt) + "###1";

        // Log the generated checksum
        Log.d(TAG, "Generated Checksum: " + checksum);

        // Create B2BPGRequest
        B2BPGRequest b2BPGRequest = new B2BPGRequestBuilder()
                .setData(base64Body)
                .setChecksum(checksum)
                .setUrl(apiEndPoint)
                .build();

        try {
            Log.d(TAG, "Starting PhonePe payment flow...");
            startActivityForResult(PhonePe.getImplicitIntent(
                    this, b2BPGRequest, "com.phonepe.app"), B2B_PG_REQUEST_CODE);
        } catch (PhonePeInitException e) {
            Log.e(TAG, "PhonePe SDK Initialization Error: ", e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == B2B_PG_REQUEST_CODE) { // Check for PhonePe Payment Response
            if (data != null) {
                try {
                    // Get the instrumentResponse JSON string
                    String instrumentResponse = data.getStringExtra("instrumentResponse");

                    if (instrumentResponse != null) {
                        JSONObject responseJson = new JSONObject(instrumentResponse);

                        // Extract the intent URL
                        String intentUrl = responseJson.getString("intentUrl");

                        if (intentUrl != null && !intentUrl.isEmpty()) {
                            // Launch UPI Intent for payment
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(intentUrl));
                            intent.putExtra(Intent.EXTRA_REFERRER, Uri.parse("android-app://com.phonepe.app"));
                            startActivity(intent); // Start UPI payment
                        } else {
                            Toast.makeText(this, "Invalid Payment URL", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "No Payment URL Found", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error Processing Payment Response", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Payment Response Missing", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private String preparePayload(int totalAmount) {
        try {
            // Build the JSON payload
            JSONObject paymentPayload = new JSONObject();
            paymentPayload.put("merchantId", "ATMOSTUAT");
            paymentPayload.put("merchantTransactionId", "23456678766554");
            paymentPayload.put("merchantUserId", "M_23250000");
            paymentPayload.put("amount", totalAmount);
            paymentPayload.put("mobileNumber", "7300896284");
            paymentPayload.put("callbackUrl", "http://192.168.1.16:3000/callback");

            // Payment instrument details
            JSONObject paymentInstrument = new JSONObject();
            paymentInstrument.put("type", "PAY_PAGE");
            paymentInstrument.put("targetApp", "com.phonepe.app");

            // Device context details
            JSONObject deviceContext = new JSONObject();
            deviceContext.put("deviceOS", "ANDROID");

            // Add the payment instrument and device context to the main payload
            paymentPayload.put("paymentInstrument", paymentInstrument);
            paymentPayload.put("deviceContext", deviceContext);

            // Convert the payload to a string and Base64 encode it
            String payloadString = paymentPayload.toString();
            Log.d(TAG, "Payload JSON String: " + payloadString); // Log the payload before encoding

            String base64Payload = Base64.encodeToString(
                    payloadString.getBytes(StandardCharsets.UTF_8),
                    Base64.NO_WRAP
            );

            // Log the Base64 encoded payload
            Log.d(TAG, "Base64 Encoded Payload: " + base64Payload);

            return base64Payload;
        } catch (Exception e) {
            e.printStackTrace();
            // Log the exception if there's an error in preparing the payload
            Log.e(TAG, "Error preparing payload: ", e);
            return null;
        }
    }

    public static String sha256(String input) {
        try {
            // Create a MessageDigest instance for SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Convert the input string to a byte array using UTF-8 encoding
            byte[] bytes = input.getBytes("UTF-8");

            // Compute the hash
            byte[] digest = md.digest(bytes);

            // Convert the digest to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                // Format each byte to a two-digit hexadecimal string
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e(TAG, "Error generating SHA-256 hash: ", e);
            return null;
        }
    }
}
