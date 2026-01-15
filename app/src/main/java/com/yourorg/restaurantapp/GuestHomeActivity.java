package com.yourorg.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.yourorg.restaurantapp.data.repository.RestaurantRepository;

public class GuestHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_home);
        
        // Data reset is now handled in onResume for guaranteed freshness.

        Button menuButton = findViewById(R.id.menuButton);
        Button bookTableButton = findViewById(R.id.bookTableButton);
        Button myReservationsButton = findViewById(R.id.myReservationsButton);

        if (menuButton != null) {
            menuButton.setOnClickListener(v -> startActivity(new Intent(this, MenuCategoryActivity.class)));
        }

        if (bookTableButton != null) {
            bookTableButton.setOnClickListener(v -> startActivity(new Intent(this, BookInActivity.class)));
        }

        if (myReservationsButton != null) {
            myReservationsButton.setOnClickListener(v -> startActivity(new Intent(this, ReservationsActivity.class)));
        }

        // --- Bottom Nav Bar Logic ---
        setupBottomNavBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // CRITICAL FIX: Reset all in-memory data every time the home screen is shown.
        // This acts as the "mini memory wipe" you observed, preventing cumulative errors.
        RestaurantRepository.getInstance(getApplicationContext()).resetData();
    }

    private void setupBottomNavBar() {
        Button homeButton = findViewById(R.id.homeButton);
        if(homeButton != null) homeButton.setOnClickListener(v -> recreate());

        Button notificationsButton = findViewById(R.id.notificationsButton);
        if(notificationsButton != null) notificationsButton.setOnClickListener(v -> startActivity(new Intent(this, NotificationsActivity.class)));

        Button settingsButton = findViewById(R.id.settingsButton);
        if(settingsButton != null) settingsButton.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
    }
}