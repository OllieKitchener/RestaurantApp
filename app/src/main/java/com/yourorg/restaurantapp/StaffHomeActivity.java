package com.yourorg.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.google.android.material.button.MaterialButton;

public class StaffHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_home);

        MaterialButton manageMenuButton = findViewById(R.id.manageMenuButton);
        MaterialButton viewReservationsButton = findViewById(R.id.viewReservationsButton);
        MaterialButton logoutButton = findViewById(R.id.staffLogoutButton);
        MaterialButton homeButton = findViewById(R.id.homeButton);
        MaterialButton notificationsButton = findViewById(R.id.notificationsButton);
        MaterialButton settingsButton = findViewById(R.id.settingsButton);

        manageMenuButton.setOnClickListener(v -> {
            startActivity(new Intent(this, com.yourorg.restaurantapp.ui.staff.StaffManageMenuActivity.class));
        });

        viewReservationsButton.setOnClickListener(v -> {
            startActivity(new Intent(this, ReservationsActivity.class));
        });

        logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // Set up bottom navigation
        homeButton.setOnClickListener(v -> {
            // Already on the staff home screen, but can be used to refresh
            recreate();
        });

        notificationsButton.setOnClickListener(v -> {
            startActivity(new Intent(this, com.example.restaurantapp.NotificationsActivity.class));
        });

        settingsButton.setOnClickListener(v -> {
            startActivity(new Intent(this, SettingsActivity.class));
        });
    }
}