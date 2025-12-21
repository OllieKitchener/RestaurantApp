package com.yourorg.restaurantapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;

public class DishDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_details);

        TextView dishNameTitle = findViewById(R.id.dish_name_title);
        Button backButton = findViewById(R.id.backButton);

        String dishName = getIntent().getStringExtra("DISH_NAME");

        if (dishNameTitle != null) {
            if (dishName != null) {
                dishNameTitle.setText(dishName);
            } else {
                dishNameTitle.setText("Dish Details");
            }
        }

        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }
    }
}