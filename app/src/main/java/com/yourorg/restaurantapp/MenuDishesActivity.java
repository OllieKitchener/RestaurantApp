package com.yourorg.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.myapplication.R;
import com.yourorg.restaurantapp.model.MenuItem;
import com.yourorg.restaurantapp.viewmodel.MenuViewModel;

import java.util.ArrayList;
import java.util.List;

public class MenuDishesActivity extends AppCompatActivity {

    private String category;
    private LinearLayout dishesContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_dishes);

        // --- Basic Setup ---
        category = getIntent().getStringExtra("CATEGORY_NAME");
        if (category == null) category = "Dishes";
        
        TextView title = findViewById(R.id.dish_list_title);
        if(title != null) title.setText(category);

        // --- Container Setup ---
        dishesContainer = findViewById(R.id.dishes_container);
        
        if (dishesContainer == null) {
            finish(); 
            return;
        }

        // --- ViewModel Setup ---
        MenuViewModel menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);
        menuViewModel.menuLiveData.observe(this, this::displayDishes);
        menuViewModel.loadMenuFromDatabase();

        // --- Back Button Navigation ---
        Button backButton = findViewById(R.id.backButton);
        if(backButton != null) backButton.setOnClickListener(v -> finish());

        // --- Bottom Nav Bar Logic ---
        Button homeButton = findViewById(R.id.homeButton);
        if(homeButton != null) homeButton.setOnClickListener(v -> startActivity(new Intent(this, GuestHomeActivity.class)));

        Button notificationsButton = findViewById(R.id.notificationsButton);
        if(notificationsButton != null) notificationsButton.setOnClickListener(v -> startActivity(new Intent(this, NotificationsActivity.class)));

        Button settingsButton = findViewById(R.id.settingsButton);
        if(settingsButton != null) settingsButton.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
    }

    private void displayDishes(List<MenuItem> menuItems) {
        if (menuItems == null || dishesContainer == null) return;

        dishesContainer.removeAllViews();

        List<MenuItem> filteredDishes = new ArrayList<>();
        for (MenuItem item : menuItems) {
            if (item != null && item.category != null && item.category.equalsIgnoreCase(category)) {
                filteredDishes.add(item);
            }
        }

        if (filteredDishes.isEmpty()) {
            TextView emptyView = new TextView(this);
            emptyView.setText("No dishes in this category yet.");
            emptyView.setTextSize(18);
            dishesContainer.addView(emptyView);
        } else {
            for (MenuItem item : filteredDishes) {
                Button dishButton = new Button(this);
                dishButton.setText(item.name);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 8, 0, 8);
                dishButton.setLayoutParams(params);
                dishButton.setOnClickListener(v -> openDishDetails(item));
                dishesContainer.addView(dishButton);
            }
        }
    }

    private void openDishDetails(MenuItem dish) {
        Intent intent = new Intent(this, DishDetailsActivity.class);
        if (dish != null && dish.name != null) {
            intent.putExtra("DISH_NAME", dish.name);
            startActivity(intent);
        }
    }
}