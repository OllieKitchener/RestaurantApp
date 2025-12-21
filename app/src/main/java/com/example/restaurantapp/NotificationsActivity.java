package com.example.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.example.myapplication.R;

import com.yourorg.restaurantapp.GuestHomeActivity;
import com.yourorg.restaurantapp.SettingsActivity;
import com.yourorg.restaurantapp.ReservationDetailsActivity;

import java.util.ArrayList;
import java.util.Collections;

public class NotificationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);

        Button backButton = findViewById(R.id.backButton);
        LinearLayout notificationsContainer = findViewById(R.id.notificationsContainer);
        MaterialButton homeButton = findViewById(R.id.homeButton);
        MaterialButton notificationsButton = findViewById(R.id.notificationsButton);
        MaterialButton settingsButton = findViewById(R.id.settingsButton);

        backButton.setOnClickListener(v -> finish());

        // Dynamically add notifications from the master list
        notificationsContainer.removeAllViews();
        // Use the fully qualified name to avoid class name conflict
        ArrayList<String> reversedNotifications = new ArrayList<>(com.yourorg.restaurantapp.NotificationsActivity.notificationMessages);
        Collections.reverse(reversedNotifications);

        for (String message : reversedNotifications) {
            MaterialButton notificationView = new MaterialButton(this);
            notificationView.setText(message);
            notificationView.setCornerRadius(12);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 16);
            notificationView.setLayoutParams(params);

            // Set the click listener to open the details screen
            notificationView.setOnClickListener(v -> {
                Intent intent = new Intent(NotificationsActivity.this, ReservationDetailsActivity.class);
                intent.putExtra("RESERVATION_DETAILS", message);
                startActivity(intent);
            });

            notificationsContainer.addView(notificationView);
        }

        homeButton.setOnClickListener(v -> {
            startActivity(new Intent(this, GuestHomeActivity.class));
        });

        notificationsButton.setOnClickListener(v -> { /* Already here */ });

        settingsButton.setOnClickListener(v -> {
            startActivity(new Intent(this, SettingsActivity.class));
        });
    }
}