package com.yourorg.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;

import java.util.Collections;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);

        LinearLayout notificationsContainer = findViewById(R.id.notificationsContainer);

        if (notificationsContainer == null) {
            finish(); 
            return;
        }

        List<String> messagesToDisplay;
        String homeActivityClass; // To determine where 'Home' button should go

        if (SharedBookingData.isStaffLoggedIn) {
            messagesToDisplay = SharedBookingData.staffNotificationMessages;
            homeActivityClass = StaffHomeActivity.class.getName();
        } else {
            messagesToDisplay = SharedBookingData.customerNotificationMessages;
            homeActivityClass = GuestHomeActivity.class.getName();
        }

        if (messagesToDisplay.isEmpty()) {
            TextView textView = new TextView(this);
            textView.setText("No new notifications.");
            textView.setTextSize(18);
            notificationsContainer.addView(textView);
        } else {
            // Display messages in reverse chronological order (most recent first)
            List<String> reversedMessages = new java.util.ArrayList<>(messagesToDisplay);
            Collections.reverse(reversedMessages);

            for (String message : reversedMessages) {
                TextView textView = new TextView(this);
                textView.setText(message);
                textView.setTextSize(16);
                textView.setPadding(0, 8, 0, 8);
                notificationsContainer.addView(textView);
            }
        }

        // --- Back Button Navigation ---
        Button backButton = findViewById(R.id.backButton);
        if(backButton != null) backButton.setOnClickListener(v -> finish());

        // --- Bottom Nav Bar Logic ---
        Button homeButton = findViewById(R.id.homeButton);
        if(homeButton != null) homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, (Class<?>) null);
            try {
                intent.setClass(this, Class.forName(homeActivityClass));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            } catch (ClassNotFoundException e) {
                // Fallback or log error
                e.printStackTrace();
                startActivity(new Intent(this, GuestHomeActivity.class)); // Default fallback
            }
        });

        Button notificationsButton = findViewById(R.id.notificationsButton);
        if(notificationsButton != null) notificationsButton.setOnClickListener(v -> recreate()); // Recreate to refresh

        Button settingsButton = findViewById(R.id.settingsButton);
        if(settingsButton != null) settingsButton.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
    }
}