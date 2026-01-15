package com.yourorg.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.yourorg.restaurantapp.ui.staff.StaffManageMenuActivity;
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
                startActivity(new Intent(this, StaffManageMenuActivity.class));
            });
        }

        if (previewMenuButton != null) {
            previewMenuButton.setOnClickListener(v -> {
                // Navigate to the Staff Menu Preview screen
                startActivity(new Intent(this, StaffMenuCategoryActivity.class));
            });
        }

        Button backButton = findViewById(R.id.backButton);
        if(backButton != null) backButton.setOnClickListener(v -> finish());

        Button homeButton = findViewById(R.id.homeButton);
        if(homeButton != null) homeButton.setOnClickListener(v -> {
             // CRITICAL FIX: When on Staff Home, pressing Home should do nothing or refresh.
             // It should NOT reset isStaffLoggedIn to false.
             // If we were implementing a logout, we'd reset it here.
        });

        Button notificationsButton = findViewById(R.id.notificationsButton);
        if(notificationsButton != null) notificationsButton.setOnClickListener(v -> startActivity(new Intent(this, NotificationsActivity.class)));

        Button settingsButton = findViewById(R.id.settingsButton);
        if(settingsButton != null) settingsButton.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
    }
}