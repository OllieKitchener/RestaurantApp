package com.yourorg.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.yourorg.restaurantapp.util.NotificationHelper;
import com.example.myapplication.R;
import com.yourorg.restaurantapp.data.local.entities.ReservationEntity;
import com.yourorg.restaurantapp.viewmodel.ReservationViewModel;

public class BookInSummaryActivity extends AppCompatActivity {

    private ReservationViewModel reservationViewModel;
    private String bookingName, bookingPartySizeStr, bookingDate, bookingTime, bookingDateTime;
    private int bookingPartySize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_in_summary);

        // Initialize ViewModel using the standard provider, which uses the application context
        // and thus the Singleton Repository.
        reservationViewModel = new ViewModelProvider(this).get(ReservationViewModel.class);

        Button backButton = findViewById(R.id.backButton);
        Button confirmButton = findViewById(R.id.confirmButton);
        TextView summaryText = findViewById(R.id.summaryText);

        if (backButton == null || confirmButton == null || summaryText == null) {
            Toast.makeText(this, "Layout is broken, cannot proceed.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Retrieve individual booking details
        bookingName = getIntent().getStringExtra("name");
        bookingPartySizeStr = getIntent().getStringExtra("partySize");
        bookingDate = getIntent().getStringExtra("date");
        bookingTime = getIntent().getStringExtra("time");
        bookingDateTime = bookingDate + " at " + bookingTime;

        // Convert party size string to int
        try {
            bookingPartySize = Integer.parseInt(bookingPartySizeStr);
        } catch (NumberFormatException e) {
            bookingPartySize = 1; // Default to 1 if parsing fails
        }

        // Construct summary string here
        String summary = "Name: " + bookingName + "\n" +
                         "Party Size: " + bookingPartySizeStr + "\n" +
                         "Date: " + bookingDate + "\n" +
                         "Time: " + bookingTime;
        summaryText.setText(summary);

        backButton.setOnClickListener(v -> finish());

        confirmButton.setOnClickListener(v -> {
            // CRITICAL: Save the reservation to the shared Singleton Repository via ViewModel
            ReservationEntity newReservation = new ReservationEntity(bookingName, bookingPartySize, bookingDateTime);
            reservationViewModel.createReservationLocal(newReservation);

            // Show confirmation
            NotificationHelper notificationHelper = new NotificationHelper(this);
            notificationHelper.showNotification(1, "Booking Confirmed!", "Your booking details are in the app.");

            String notificationMessage = "Booking Confirmed!\n" + summary;
            NotificationsActivity.notificationMessages.add(notificationMessage);

            // Navigate back to Home
            Intent homeIntent = new Intent(BookInSummaryActivity.this, GuestHomeActivity.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(homeIntent);
            finish();
        });
    }
}