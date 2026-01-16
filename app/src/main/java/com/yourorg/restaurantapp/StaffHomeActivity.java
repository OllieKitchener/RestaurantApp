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

        setupButtons();
        setupBottomNavBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setupButtons() {
        Button manageMenuButton = findViewById(R.id.manageMenuButton);
        Button previewMenuButton = findViewById(R.id.previewMenuButton);

        if (manageMenuButton != null) {
            manageMenuButton.setOnClickListener(v -> startActivity(new Intent(this, StaffManageMenuActivity.class)));
        }

        if (previewMenuButton != null) {
            previewMenuButton.setOnClickListener(v -> startActivity(new Intent(this, StaffMenuCategoryActivity.class)));
        }
    }
    
    private void setupBottomNavBar() {
        Button backButton = findViewById(R.id.backButton);
        if(backButton != null) backButton.setOnClickListener(v -> finish());

        Button homeButton = findViewById(R.id.homeButton);
        if(homeButton != null) homeButton.setOnClickListener(v -> recreate());

        Button notificationsButton = findViewById(R.id.notificationsButton);
        if(notificationsButton != null) notificationsButton.setOnClickListener(v -> startActivity(new Intent(this, NotificationsActivity.class)));

        Button settingsButton = findViewById(R.id.settingsButton);
        if(settingsButton != null) {
            settingsButton.setOnClickListener(v -> startActivity(new Intent(this, StaffSettingsActivity.class)));
        }
    }
}