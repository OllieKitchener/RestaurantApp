package com.yourorg.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;
import com.example.myapplication.R;

public class MenuCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_category);

        // Set up category cards by finding them in the XML and setting a click listener.
        setupCardClickListener(R.id.card_recommended, "Recommended");
        setupCardClickListener(R.id.card_deals, "Deals");
        setupCardClickListener(R.id.card_meat, "Meat");
        setupCardClickListener(R.id.card_fish, "Fish");
        setupCardClickListener(R.id.card_vegetarian, "Vegetarian");
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