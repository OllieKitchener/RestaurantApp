package com.yourorg.restaurantapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.myapplication.R;
import com.yourorg.restaurantapp.data.local.entities.ReservationEntity;
import com.yourorg.restaurantapp.viewmodel.ReservationViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BookInActivity extends AppCompatActivity {

    private final Calendar myCalendar = Calendar.getInstance();
    private TextInputEditText dateEditText;
    private TextInputEditText timeEditText;
    private ReservationViewModel reservationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_in);

        reservationViewModel = new ViewModelProvider(this).get(ReservationViewModel.class);

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
            String name = nameEditText.getText().toString();
            String partySizeStr = partySizeEditText.getText().toString();
            String date = dateEditText.getText().toString();
            String time = timeEditText.getText().toString();

            if (name.isEmpty() || partySizeStr.isEmpty() || date.isEmpty() || time.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // --- Correctly Save to Database ---
            int partySize = 0;
            try {
                partySize = Integer.parseInt(partySizeStr);
            } catch (NumberFormatException e) { /* Failsafe */ }
            
            String dateTime = date + " at " + time;
            ReservationEntity newReservation = new ReservationEntity(name, partySize, dateTime);
            reservationViewModel.createReservationLocal(newReservation);

            // --- Navigate to Summary ---
            String summary = "Name: " + name + "\n" +
                             "Party Size: " + partySizeStr + "\n" +
                             "Date: " + date + "\n" +
                             "Time: " + time;

            Intent intent = new Intent(BookInActivity.this, BookInSummaryActivity.class);
            intent.putExtra("summary", summary);
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
        dateEditText.setOnClickListener(v -> new DatePickerDialog(BookInActivity.this, dateSetListener, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show());
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