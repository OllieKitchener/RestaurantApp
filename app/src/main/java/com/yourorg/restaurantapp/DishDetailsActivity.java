package com.yourorg.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.yourorg.restaurantapp.model.MenuItem;
import android.widget.Toast;

public class DishDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_MENU_ITEM = "com.yourorg.restaurantapp.MENU_ITEM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_details);

        TextView dishNameTitle = findViewById(R.id.dish_name_title);
        TextView dishDescription = findViewById(R.id.dish_description);
        TextView dishPrice = findViewById(R.id.dish_price);
        TextView dishCategory = findViewById(R.id.dish_category);
        TextView dishIngredients = findViewById(R.id.dish_ingredients);
        TextView dishAllergyInfo = findViewById(R.id.dish_allergy_info);
        Button backButton = findViewById(R.id.backButton);

        MenuItem dish = (MenuItem) getIntent().getSerializableExtra(EXTRA_MENU_ITEM);

        if (dish != null) {
            // Robustly setting text with null checks for all TextViews and data fields
            if (dishNameTitle != null) dishNameTitle.setText(dish.name != null ? dish.name : "N/A");
            if (dishDescription != null) dishDescription.setText(dish.description != null ? dish.description : "No description available.");
            if (dishPrice != null) dishPrice.setText(String.format("$%.2f", dish.price)); // Price should always be a double
            if (dishCategory != null) dishCategory.setText("Category: " + (dish.category != null ? dish.category : "N/A"));
            if (dishIngredients != null) dishIngredients.setText(dish.ingredients != null && !dish.ingredients.isEmpty() ? dish.ingredients : "No ingredients listed.");
            if (dishAllergyInfo != null) dishAllergyInfo.setText("Allergies: " + (dish.allergyInfo != null && !dish.allergyInfo.isEmpty() ? dish.allergyInfo : "None"));
        } else {
            Toast.makeText(this, "Dish details not found.", Toast.LENGTH_SHORT).show();
            finish();
        }

        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }

        // --- Bottom Nav Bar Logic ---
        Button homeButton = findViewById(R.id.homeButton);
        if(homeButton != null) homeButton.setOnClickListener(v -> startActivity(new Intent(this, GuestHomeActivity.class)));

        Button notificationsButton = findViewById(R.id.notificationsButton);
        if(notificationsButton != null) notificationsButton.setOnClickListener(v -> startActivity(new Intent(this, NotificationsActivity.class)));

        Button settingsButton = findViewById(R.id.settingsButton);
        if(settingsButton != null) settingsButton.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
    }
}