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
        
        setupButtons();
    }

    @Override
    protected void onResume() {
        super.onResume();
        RestaurantRepository.getInstance(getApplicationContext()).resetMenuData();
    }

    private void setupButtons() {
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

        Button homeButton = findViewById(R.id.homeButton);
        if(homeButton != null) homeButton.setOnClickListener(v -> recreate());

        Button notificationsButton = findViewById(R.id.notificationsButton);
        if(notificationsButton != null) notificationsButton.setOnClickListener(v -> startActivity(new Intent(this, NotificationsActivity.class)));

        Button settingsButton = findViewById(R.id.settingsButton);
        if(settingsButton != null) settingsButton.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
    }
}