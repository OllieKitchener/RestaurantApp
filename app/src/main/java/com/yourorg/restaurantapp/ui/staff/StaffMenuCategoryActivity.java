package com.yourorg.restaurantapp.ui.staff;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;
import com.example.myapplication.R;
import com.yourorg.restaurantapp.MenuDishesActivity;
import com.yourorg.restaurantapp.NotificationsActivity;
import com.yourorg.restaurantapp.SettingsActivity;
import com.yourorg.restaurantapp.StaffHomeActivity;

public class StaffMenuCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_menu_category);

        // Set up category cards
        setupCardClickListener(R.id.card_recommended, "Recommended");
        setupCardClickListener(R.id.card_deals, "Deals");
        setupCardClickListener(R.id.card_meat, "Meat");
        setupCardClickListener(R.id.card_fish, "Fish");
        setupCardClickListener(R.id.card_vegetarian, "Vegetarian");

        // --- Back Button Navigation ---
        Button backButton = findViewById(R.id.backButton);
        if(backButton != null) backButton.setOnClickListener(v -> finish());

        // --- Bottom Nav Bar Logic (Staff Context) ---
        Button homeButton = findViewById(R.id.homeButton);
        if(homeButton != null) homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, StaffHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        Button notificationsButton = findViewById(R.id.notificationsButton);
        if(notificationsButton != null) notificationsButton.setOnClickListener(v -> startActivity(new Intent(this, NotificationsActivity.class)));

        Button settingsButton = findViewById(R.id.settingsButton);
        if(settingsButton != null) settingsButton.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
    }

    private void setupCardClickListener(int cardId, String categoryName) {
        MaterialCardView card = findViewById(cardId);
        if (card != null) {
            card.setOnClickListener(v -> {
                Intent intent = new Intent(this, MenuDishesActivity.class);
                intent.putExtra("CATEGORY_NAME", categoryName);
                startActivity(intent);
            });
        }
    }
}