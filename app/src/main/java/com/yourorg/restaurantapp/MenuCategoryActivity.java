package com.yourorg.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;

import java.util.Arrays;
import java.util.List;

public class MenuCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_category);

        LinearLayout categoryContainer = findViewById(R.id.category_container);

        if (categoryContainer == null) {
            finish(); // Failsafe
            return;
        }

        List<String> categories = Arrays.asList("Recommended", "Deals", "Meat", "Fish", "Vegetarian");

        for (String category : categories) {
            Button categoryButton = new Button(this);
            categoryButton.setText(category);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 8, 0, 8);
            categoryButton.setLayoutParams(params);
            categoryButton.setOnClickListener(v -> openDishesForCategory(category));
            categoryContainer.addView(categoryButton);
        }
    }

    private void openDishesForCategory(String category) {
        Intent intent = new Intent(this, MenuDishesActivity.class);
        intent.putExtra("CATEGORY_NAME", category);
        startActivity(intent);
    }
}