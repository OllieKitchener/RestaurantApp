package com.yourorg.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    public static List<String> notificationMessages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);

        LinearLayout notificationsContainer = findViewById(R.id.notificationsContainer);

        if (notificationsContainer == null) {
            finish(); // Failsafe
            return;
        }

        if (notificationMessages.isEmpty()) {
            TextView textView = new TextView(this);
            textView.setText("No new notifications.");
            textView.setTextSize(18);
            notificationsContainer.addView(textView);
        } else {
            for (String message : notificationMessages) {
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
        if(homeButton != null) homeButton.setOnClickListener(v -> startActivity(new Intent(this, GuestHomeActivity.class)));

        Button notificationsButton = findViewById(R.id.notificationsButton);
        if(notificationsButton != null) notificationsButton.setOnClickListener(v -> recreate());

        Button settingsButton = findViewById(R.id.settingsButton);
        if(settingsButton != null) settingsButton.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
    }
}