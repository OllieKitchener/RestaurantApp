package com.yourorg.restaurantapp;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import android.content.Intent;
import com.google.android.material.button.MaterialButton;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        Button backButton = findViewById(R.id.backButton);
        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }

        MaterialButton homeButton = findViewById(R.id.homeButton);
        MaterialButton notificationsButton = findViewById(R.id.notificationsButton);
        MaterialButton settingsButton = findViewById(R.id.settingsButton);
        MaterialButton logoutButton = findViewById(R.id.logOutButton);

        if (homeButton != null) {
            homeButton.setOnClickListener(v -> {
                if (SharedBookingData.isStaffLoggedIn) {
                    startActivity(new Intent(this, StaffHomeActivity.class));
                } else {
                    startActivity(new Intent(this, GuestHomeActivity.class));
                }
            });
        }

        if (notificationsButton != null) {
            notificationsButton.setOnClickListener(v -> {
                startActivity(new Intent(this, NotificationsActivity.class));
            });
        }

        if (settingsButton != null) {
            settingsButton.setOnClickListener(v -> recreate());
        }

        if (logoutButton != null) {
            logoutButton.setOnClickListener(v -> {
                // Reset login state
                SharedBookingData.isStaffLoggedIn = false;
                
                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });
        }
    }
}