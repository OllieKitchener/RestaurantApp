package com.yourorg.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.yourorg.restaurantapp.util.NotificationHelper;
import com.example.myapplication.R;

public class BookInSummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_in_summary);

        Button backButton = findViewById(R.id.backButton);
        Button confirmButton = findViewById(R.id.confirmButton);
        TextView summaryText = findViewById(R.id.summaryText);

        if (backButton == null || confirmButton == null || summaryText == null) {
            Toast.makeText(this, "Layout is broken, cannot proceed.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        String summary = getIntent().getStringExtra("summary");

        summaryText.setText(summary);

        backButton.setOnClickListener(v -> finish());

        confirmButton.setOnClickListener(v -> {
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