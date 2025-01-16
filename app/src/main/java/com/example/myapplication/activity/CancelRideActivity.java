package com.example.myapplication.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;

public class CancelRideActivity extends AppCompatActivity {

    private RadioGroup reasonRadioGroup;
    private EditText otherReasonEditText;
    private Button submitReasonButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_ride);

        reasonRadioGroup = findViewById(R.id.reasonRadioGroup);
        otherReasonEditText = findViewById(R.id.otherReasonEditText);
        submitReasonButton = findViewById(R.id.submitReasonButton);

        // Listener to show/hide EditText
        reasonRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.reasonOther) {
                otherReasonEditText.setVisibility(View.VISIBLE);
            } else {
                otherReasonEditText.setVisibility(View.GONE);
            }
        });

        // Handle button click
        submitReasonButton.setOnClickListener(v -> {
            int selectedId = reasonRadioGroup.getCheckedRadioButtonId();

            if (selectedId == -1) {
                Toast.makeText(CancelRideActivity.this, "Please select a reason", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton selectedRadioButton = findViewById(selectedId);
            String reason = selectedRadioButton.getText().toString();

            String additionalDetails = otherReasonEditText.getVisibility() == View.VISIBLE
                    ? otherReasonEditText.getText().toString()
                    : "";

            // Display the selection
            String message = "Reason: " + reason;
            if (!additionalDetails.isEmpty()) {
                message += "\nAdditional Details: " + additionalDetails;
            }

            Toast.makeText(CancelRideActivity.this, message, Toast.LENGTH_LONG).show();

            // TODO: Send the data to the server or process it further
        });
    }
}
