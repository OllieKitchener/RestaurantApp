package com.yourorg.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.yourorg.restaurantapp.ui.staff.StaffMenuCategoryActivity; // Correct import

public class StaffHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_home);

        Button manageMenuButton = findViewById(R.id.manageMenuButton);
        Button previewMenuButton = findViewById(R.id.previewMenuButton);

        if (manageMenuButton != null) {
            manageMenuButton.setOnClickListener(v -> {
                startActivity(new Intent(this, com.yourorg.restaurantapp.ui.staff.StaffManageMenuActivity.class));
            });
        }

        if (previewMenuButton != null) {
            previewMenuButton.setOnClickListener(v -> {
                // Now navigating to the Staff-specific menu preview, keeping staff context
                startActivity(new Intent(this, StaffMenuCategoryActivity.class));
            });
        }

        // --- Back Button Navigation ---
        Button backButton = findViewById(R.id.backButton);
        if(backButton != null) backButton.setOnClickListener(v -> finish());

        // --- Bottom Nav Bar Logic ---
        Button homeButton = findViewById(R.id.homeButton);
        if(homeButton != null) homeButton.setOnClickListener(v -> {
             // Already at Staff Home, maybe refresh or do nothing
        });

        Button notificationsButton = findViewById(R.id.notificationsButton);
        if(notificationsButton != null) notificationsButton.setOnClickListener(v -> startActivity(new Intent(this, NotificationsActivity.class)));

        Button settingsButton = findViewById(R.id.settingsButton);
        if(settingsButton != null) settingsButton.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
    }
}