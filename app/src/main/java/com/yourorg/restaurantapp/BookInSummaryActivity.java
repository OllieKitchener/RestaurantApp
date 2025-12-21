package com.yourorg.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.yourorg.restaurantapp.util.NotificationHelper;
import com.example.myapplication.R;

public class BookInSummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookin);

        Button backButton = findViewById(R.id.backButton);
        Button confirmButton = findViewById(R.id.confirmButton);
        TextView summaryText = findViewById(R.id.summaryText);

        String summary = getIntent().getStringExtra("summary");

        if (summaryText != null) {
            summaryText.setText(summary);
        }

        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }

        if (confirmButton != null) {
            confirmButton.setOnClickListener(v -> {
                // The booking is already saved, so we just show notifications and go home.
                NotificationHelper notificationHelper = new NotificationHelper(this);
                notificationHelper.showNotification(1, "Booking Confirmed!", "Your booking details are in the app.");

                String notificationMessage = "Booking Confirmed!\n" + summary;
                NotificationsActivity.notificationMessages.add(notificationMessage);

                Intent homeIntent = new Intent(BookInSummaryActivity.this, GuestHomeActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(homeIntent);
                finish();
            });
        }
    }
}