package com.yourorg.restaurantapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
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
    private MenuViewModel menuViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_dishes);

        category = getIntent().getStringExtra("CATEGORY_NAME");
        if (category == null) category = "Dishes";
        
        TextView title = findViewById(R.id.dish_list_title);
        if(title != null) title.setText(category);

        dishesContainer = findViewById(R.id.dishes_container);
        
        if (dishesContainer == null) {
            finish(); 
            return;
        }

        menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);
        menuViewModel.menuLiveData.observe(this, this::displayDishes);
        menuViewModel.loadMenuFromDatabase();

        Button backButton = findViewById(R.id.backButton);
        if(backButton != null) backButton.setOnClickListener(v -> finish());

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
                LinearLayout row = new LinearLayout(this);
                row.setOrientation(LinearLayout.HORIZONTAL);
                row.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                row.setPadding(0, 8, 0, 8);

                Button dishButton = new Button(this);
                dishButton.setText(item.name);
                dishButton.setTextSize(18f);
                dishButton.setPadding(16, 40, 16, 40);
                
                LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                    0, 
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f 
                );
                dishButton.setLayoutParams(btnParams);
                dishButton.setOnClickListener(v -> openDishDetails(item));
                row.addView(dishButton);

                if (SharedBookingData.isStaffLoggedIn) {
                    Button deleteButton = new Button(this);
                    deleteButton.setText("Delete");
                    deleteButton.setBackgroundColor(0xFFFF0000); 
                    deleteButton.setTextColor(0xFFFFFFFF); 
                    
                    LinearLayout.LayoutParams deleteParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    deleteParams.setMargins(8, 0, 0, 0);
                    deleteButton.setLayoutParams(deleteParams);
                    
                    deleteButton.setOnClickListener(v -> confirmDelete(item));
                    row.addView(deleteButton);
                }

                dishesContainer.addView(row);
            }
        }
    }

    private void confirmDelete(MenuItem item) {
        new AlertDialog.Builder(this)
            .setTitle("Delete Dish")
            .setMessage("Are you sure you want to delete " + item.name + "?")
            .setPositiveButton("Yes", (dialog, which) -> {
                // The menu will refresh automatically via LiveData, so no Toast needed.
                menuViewModel.deleteMenuItem(item, null); 
            })
            .setNegativeButton("No", null)
            .show();
    }

    private void openDishDetails(MenuItem dish) {
        Intent intent = new Intent(this, DishDetailsActivity.class);
        intent.putExtra(DishDetailsActivity.EXTRA_MENU_ITEM, dish);
        startActivity(intent);
    }
}