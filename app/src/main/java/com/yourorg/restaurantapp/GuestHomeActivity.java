package com.yourorg.restaurantapp;

import android.os.Bundle;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.google.android.material.button.MaterialButton;

// Adding all necessary imports for the activities this screen opens
import com.yourorg.restaurantapp.MenuCategoryActivity;
import com.yourorg.restaurantapp.BookInActivity;
import com.yourorg.restaurantapp.ReservationsActivity;
import com.yourorg.restaurantapp.NotificationsActivity;
import com.yourorg.restaurantapp.SettingsActivity;

public class GuestHomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        // Main action buttons
        MaterialButton menuButton = findViewById(R.id.menuButton);
        if (menuButton != null) {
            menuButton.setOnClickListener(v -> startActivity(new Intent(this, MenuCategoryActivity.class)));
        }

        MaterialButton bookInButton = findViewById(R.id.bookInButton);
        if (bookInButton != null) {
            bookInButton.setOnClickListener(v -> startActivity(new Intent(this, BookInActivity.class)));
        }

        MaterialButton reservationsButton = findViewById(R.id.reservationsButton);
        if (reservationsButton != null) {
            reservationsButton.setOnClickListener(v -> startActivity(new Intent(this, ReservationsActivity.class)));
        }
        
        // Bottom navigation buttons
        MaterialButton homeButton = findViewById(R.id.homeButton);
        if (homeButton != null) {
             homeButton.setOnClickListener(v -> recreate());
        }

        MaterialButton notificationsButton = findViewById(R.id.notificationsButton);
        if (notificationsButton != null) {
            notificationsButton.setOnClickListener(v -> startActivity(new Intent(this, NotificationsActivity.class)));
        }

        MaterialButton settingsButton = findViewById(R.id.settingsButton);
        if (settingsButton != null) {
            settingsButton.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
        }
    }
}