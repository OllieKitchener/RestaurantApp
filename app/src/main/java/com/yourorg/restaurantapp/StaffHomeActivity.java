package com.yourorg.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.yourorg.restaurantapp.data.repository.RestaurantRepository;
import com.yourorg.restaurantapp.ui.staff.StaffManageMenuActivity;
import com.yourorg.restaurantapp.ui.staff.StaffMenuCategoryActivity;

public class StaffHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_home);

        // Data reset is now handled in onResume for guaranteed freshness.

        Button manageMenuButton = findViewById(R.id.manageMenuButton);
        Button previewMenuButton = findViewById(R.id.previewMenuButton);

        if (manageMenuButton != null) {
            manageMenuButton.setOnClickListener(v -> startActivity(new Intent(this, StaffManageMenuActivity.class)));
        }

        if (previewMenuButton != null) {
            previewMenuButton.setOnClickListener(v -> startActivity(new Intent(this, StaffMenuCategoryActivity.class)));
        }

        setupBottomNavBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // CRITICAL FIX: Reset all in-memory data every time the home screen is shown.
        RestaurantRepository.getInstance(getApplicationContext()).resetData();
    }

    private void setupBottomNavBar() {
        Button backButton = findViewById(R.id.backButton);
        if(backButton != null) backButton.setOnClickListener(v -> finish());

        Button homeButton = findViewById(R.id.homeButton);
        if(homeButton != null) homeButton.setOnClickListener(v -> recreate());

        Button notificationsButton = findViewById(R.id.notificationsButton);
        if(notificationsButton != null) notificationsButton.setOnClickListener(v -> startActivity(new Intent(this, NotificationsActivity.class)));

        Button settingsButton = findViewById(R.id.settingsButton);
        if(settingsButton != null) settingsButton.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
    }
}