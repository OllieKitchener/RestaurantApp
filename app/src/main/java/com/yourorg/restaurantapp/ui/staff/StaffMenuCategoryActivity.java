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
import com.yourorg.restaurantapp.SharedBookingData; // Import SharedBookingData

import java.util.List;

public class StaffMenuCategoryActivity extends AppCompatActivity {

    private MenuViewModel menuViewModel;
    private CategoryAdapter adapter;
    private MaterialCardView cardRecommended, cardDeals, cardMeat, cardFish, cardVegetarian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_menu_category);

        menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        cardRecommended = findViewById(R.id.card_recommended);
        cardDeals = findViewById(R.id.card_deals);
        cardMeat = findViewById(R.id.card_meat);
        cardFish = findViewById(R.id.card_fish);
        cardVegetarian = findViewById(R.id.card_vegetarian);

        setupCardClickListener(cardRecommended, "Recommended");
        setupCardClickListener(cardDeals, "Deals");
        setupCardClickListener(cardMeat, "Meat");
        setupCardClickListener(cardFish, "Fish");
        setupCardClickListener(cardVegetarian, "Vegetarian");

        menuViewModel.categoriesLiveData.observe(this, this::updateCategoryCards);

        Button backButton = findViewById(R.id.backButton);
        if(backButton != null) backButton.setOnClickListener(v -> finish());

        Button homeButton = findViewById(R.id.homeButton);
        if(homeButton != null) homeButton.setOnClickListener(v -> {
            // CRITICAL: When navigating Home from Staff Preview, reset staff status
            SharedBookingData.isStaffLoggedIn = false;
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

    private void updateCategoryCards(List<String> categories) {
        if (categories == null) return;

        if (cardRecommended != null) cardRecommended.setVisibility(categories.contains("Recommended") ? View.VISIBLE : View.GONE);
        if (cardDeals != null) cardDeals.setVisibility(categories.contains("Deals") ? View.VISIBLE : View.GONE);
        if (cardMeat != null) cardMeat.setVisibility(categories.contains("Meat") ? View.VISIBLE : View.GONE);
        if (cardFish != null) cardFish.setVisibility(categories.contains("Fish") ? View.VISIBLE : View.GONE);
        if (cardVegetarian != null) cardVegetarian.setVisibility(categories.contains("Vegetarian") ? View.VISIBLE : View.GONE);
    }
}