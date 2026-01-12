package com.yourorg.restaurantapp.ui.staff;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View; 
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.card.MaterialCardView;
import com.example.myapplication.R;
import com.yourorg.restaurantapp.MenuDishesActivity;
import com.yourorg.restaurantapp.NotificationsActivity;
import com.yourorg.restaurantapp.SettingsActivity;
import com.yourorg.restaurantapp.StaffHomeActivity;
import com.yourorg.restaurantapp.viewmodel.MenuViewModel;

import java.util.List;

public class StaffMenuCategoryActivity extends AppCompatActivity {

    private MenuViewModel menuViewModel;
    // Keep track of card IDs to update categories dynamically
    private MaterialCardView cardRecommended, cardDeals, cardMeat, cardFish, cardVegetarian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_menu_category);

        // Initialize ViewModel
        menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        // Initialize card views
        cardRecommended = findViewById(R.id.card_recommended);
        cardDeals = findViewById(R.id.card_deals);
        cardMeat = findViewById(R.id.card_meat);
        cardFish = findViewById(R.id.card_fish);
        cardVegetarian = findViewById(R.id.card_vegetarian);

        // Set up category cards
        setupCardClickListener(cardRecommended, "Recommended");
        setupCardClickListener(cardDeals, "Deals");
        setupCardClickListener(cardMeat, "Meat");
        setupCardClickListener(cardFish, "Fish");
        setupCardClickListener(cardVegetarian, "Vegetarian");

        // Observe categories LiveData from ViewModel
        menuViewModel.categoriesLiveData.observe(this, this::updateCategoryCards);

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

    @Override
    protected void onResume() {
        super.onResume();
        // CRITICAL: Load categories every time activity resumes to ensure fresh data
        menuViewModel.loadCategoriesFromDatabase();
    }

    private void setupCardClickListener(MaterialCardView card, String categoryName) {
        if (card != null) {
            card.setOnClickListener(v -> {
                Intent intent = new Intent(this, MenuDishesActivity.class);
                intent.putExtra("CATEGORY_NAME", categoryName);
                startActivity(intent);
            });
        }
    }

    // Method to dynamically update card visibility based on available categories
    private void updateCategoryCards(List<String> categories) {
        if (categories == null) return;

        // Hide all cards initially
        cardRecommended.setVisibility(View.GONE);
        cardDeals.setVisibility(View.GONE);
        cardMeat.setVisibility(View.GONE);
        cardFish.setVisibility(View.GONE);
        cardVegetarian.setVisibility(View.GONE);

        // Show cards only for existing categories
        if (categories.contains("Recommended")) cardRecommended.setVisibility(View.VISIBLE);
        if (categories.contains("Deals")) cardDeals.setVisibility(View.VISIBLE);
        if (categories.contains("Meat")) cardMeat.setVisibility(View.VISIBLE);
        if (categories.contains("Fish")) cardFish.setVisibility(View.VISIBLE);
        if (categories.contains("Vegetarian")) cardVegetarian.setVisibility(View.VISIBLE);
        // Add more categories as needed (if we add more cards and update this method)
    }
}