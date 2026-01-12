package com.yourorg.restaurantapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.yourorg.restaurantapp.data.local.entities.ReservationEntity;
import com.yourorg.restaurantapp.viewmodel.ReservationViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BookInActivity extends AppCompatActivity {

    private final Calendar myCalendar = Calendar.getInstance();
    private TextInputEditText dateEditText;
    private TextInputEditText timeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_in);

        // --- View Initialization (with null checks) ---
        TextInputEditText nameEditText = findViewById(R.id.nameEditText);
        TextInputEditText partySizeEditText = findViewById(R.id.partySizeEditText);
        dateEditText = findViewById(R.id.dateEditText);
        timeEditText = findViewById(R.id.timeEditText);
        Button submitButton = findViewById(R.id.submitBookingButton);
        Button backButton = findViewById(R.id.backButton);

        if (nameEditText == null || partySizeEditText == null || dateEditText == null || 
            timeEditText == null || submitButton == null || backButton == null) {
            Toast.makeText(this, "Layout is broken, cannot proceed.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        backButton.setOnClickListener(v -> finish());

        setupDatePicker();
        setupTimePicker();

        submitButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String partySizeStr = partySizeEditText.getText().toString().trim();
            String dateStr = dateEditText.getText().toString().trim();
            String timeStr = timeEditText.getText().toString().trim();

            if(name.isEmpty() || partySizeStr.isEmpty() || dateStr.isEmpty() || timeStr.isEmpty()){
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int partySize = 0;
            try {
                partySize = Integer.parseInt(partySizeStr);
                if (partySize <= 0) {
                    Toast.makeText(this, "Party size must be at least 1", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid party size format", Toast.LENGTH_SHORT).show();
                return;
            }

            // --- Date and Time Validation ---
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US);
            Date selectedDateTime = null;
            try {
                selectedDateTime = dateTimeFormat.parse(dateStr + " " + timeStr);
            } catch (ParseException e) {
                Toast.makeText(this, "Invalid date or time format", Toast.LENGTH_SHORT).show();
                return;
            }

            // 1. Future Date Validation
            if (selectedDateTime != null && selectedDateTime.before(Calendar.getInstance().getTime())) {
                Toast.makeText(this, "Booking must be for a future date and time", Toast.LENGTH_SHORT).show();
                return;
            }

            // 2. Time Range Validation (12:00 PM to 8:59 PM)
            Calendar selectedCalendar = Calendar.getInstance();
            if (selectedDateTime != null) {
                selectedCalendar.setTime(selectedDateTime);
            }
            int hourOfDay = selectedCalendar.get(Calendar.HOUR_OF_DAY);

            // CRITICAL FIX: Change > 21 to >= 21 to exclude 9:00 PM (hour 21) and later
            if (hourOfDay < 12 || hourOfDay >= 21) { // 21 is 9 PM, so >= 21 means 9 PM onwards is invalid
                Toast.makeText(this, "Bookings can only be made between 12:00 PM and 8:59 PM", Toast.LENGTH_LONG).show();
                return;
            }

            // All validations passed, proceed to summary
            Intent intent = new Intent(BookInActivity.this, BookInSummaryActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("partySize", partySizeStr);
            intent.putExtra("date", dateStr);
            intent.putExtra("time", timeStr);
            startActivity(intent);
        });
    }

    private void setupDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateLabel();
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(BookInActivity.this, dateSetListener, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        // Set minimum date to today
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000); // Subtract 1 second to include today
        dateEditText.setOnClickListener(v -> datePickerDialog.show());
    }

    private void setupTimePicker() {
        TimePickerDialog.OnTimeSetListener timeSetListener = (view, hourOfDay, minute) -> {
            myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            myCalendar.set(Calendar.MINUTE, minute);
            updateTimeLabel();
        };
        timeEditText.setOnClickListener(v -> new TimePickerDialog(BookInActivity.this, timeSetListener, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show());
    }

    private void updateDateLabel() {
        String myFormat = "dd/MM/yyyy"; 
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateEditText.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateTimeLabel() {
        String myFormat = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        timeEditText.setText(sdf.format(myCalendar.getTime()));
    }
}