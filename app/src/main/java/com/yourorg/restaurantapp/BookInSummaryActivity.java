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

        reservationViewModel = new ViewModelProvider(this).get(ReservationViewModel.class);

        Button backButton = findViewById(R.id.backButton);
        Button confirmButton = findViewById(R.id.confirmButton);
        TextView summaryText = findViewById(R.id.summaryText);

        if (backButton == null || confirmButton == null || summaryText == null) {
            Toast.makeText(this, "Layout is broken, cannot proceed.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        bookingName = getIntent().getStringExtra("name");
        bookingPartySizeStr = getIntent().getStringExtra("partySize");
        bookingDate = getIntent().getStringExtra("date");
        bookingTime = getIntent().getStringExtra("time");
        bookingDateTime = bookingDate + " at " + bookingTime;

        try {
            bookingPartySize = Integer.parseInt(bookingPartySizeStr);
        } catch (NumberFormatException e) {
            bookingPartySize = 1;
        }

        String summary = "Name: " + bookingName + "\n" +
                         "Party Size: " + bookingPartySizeStr + "\n" +
                         "Date: " + bookingDate + "\n" +
                         "Time: " + bookingTime;
        summaryText.setText(summary);

        backButton.setOnClickListener(v -> finish());

        confirmButton.setOnClickListener(v -> {
            ReservationEntity newReservation = new ReservationEntity(bookingName, bookingPartySize, bookingDateTime);
            reservationViewModel.createReservationLocal(newReservation);

            NotificationHelper notificationHelper = new NotificationHelper(this);
            notificationHelper.showNotification(1, "Booking Confirmed!", "Your booking details are in the app.");

            String customerNotificationMessage = "Booking Confirmed!\n" + summary;
            SharedBookingData.customerNotificationMessages.add(customerNotificationMessage);

            String staffNotificationMessage = "NEW BOOKING!\nName: " + bookingName + ", Party: " + bookingPartySize + ", Time: " + bookingDateTime;
            SharedBookingData.staffNotificationMessages.add(staffNotificationMessage);

            Intent homeIntent = new Intent(BookInSummaryActivity.this, GuestHomeActivity.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(homeIntent);
            finish();
        });
    }
}