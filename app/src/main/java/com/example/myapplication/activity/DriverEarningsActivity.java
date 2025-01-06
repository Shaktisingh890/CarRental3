package com.example.myapplication.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DriverEarningsActivity extends AppCompatActivity {

    private LinearLayout dateContainer;
    private TextView monthYearSelector;
    private Calendar selectedCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_earnings);

        dateContainer = findViewById(R.id.dateContainer);
        monthYearSelector = findViewById(R.id.monthYearSelector);

        selectedCalendar = Calendar.getInstance();

        // Initially set to the current month/year
        updateMonthYearDisplay();
        loadDatesForMonth(selectedCalendar.get(Calendar.YEAR), selectedCalendar.get(Calendar.MONTH));

        // Open Month/Year picker when clicked
        monthYearSelector.setOnClickListener(v -> showMonthYearPicker());
    }

    private void showMonthYearPicker() {
        // Create DatePickerDialog with the current year and month
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar, // Optional for style
                (view, year, month, dayOfMonth) -> {
                    selectedCalendar.set(year, month, 1);
                    updateMonthYearDisplay();
                    loadDatesForMonth(year, month);
                },
                selectedCalendar.get(Calendar.YEAR),
                selectedCalendar.get(Calendar.MONTH),
                1 // Default day (not used)
        );

        // Hide the day picker
        try {
            DatePicker datePicker = datePickerDialog.getDatePicker();
            int daySpinnerId = getResources().getIdentifier("day", "id", "android");
            if (daySpinnerId != 0) {
                View daySpinner = datePicker.findViewById(daySpinnerId);
                if (daySpinner != null) {
                    daySpinner.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        datePickerDialog.show();
    }

    private void updateMonthYearDisplay() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        monthYearSelector.setText(dateFormat.format(selectedCalendar.getTime()));
    }

    private void loadDatesForMonth(int year, int month) {
        dateContainer.removeAllViews(); // Clear previous dates

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd", Locale.getDefault());
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int day = 1; day <= daysInMonth; day++) {
            calendar.set(Calendar.DAY_OF_MONTH, day);

            String formattedDate = dateFormat.format(calendar.getTime());

            // Inflate a date item dynamically
            TextView dateView = (TextView) LayoutInflater.from(this).inflate(R.layout.item_date, dateContainer, false);
            dateView.setText(formattedDate);

            // Handle click events for the date
            dateView.setOnClickListener(v -> {
                Toast.makeText(this, "Selected: " + formattedDate, Toast.LENGTH_SHORT).show();
            });

            // Add the view to the container
            dateContainer.addView(dateView);
        }
    }
}
