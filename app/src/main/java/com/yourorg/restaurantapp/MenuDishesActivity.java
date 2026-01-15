package com.yourorg.restaurantapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.yourorg.restaurantapp.model.MenuItem;
import com.yourorg.restaurantapp.viewmodel.MenuViewModel;
import com.yourorg.restaurantapp.DishAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MenuDishesActivity extends AppCompatActivity {

    private String category;
    private MenuViewModel menuViewModel;
    private DishAdapter adapter;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_dishes);

        // Safely get category from intent
        if (getIntent() != null) {
            category = getIntent().getStringExtra("CATEGORY_NAME");
        }
        if (category == null) {
            category = "Dishes"; // Fallback category
        }
        
        TextView title = findViewById(R.id.dish_list_title);
        if(title != null) {
            title.setText(category);
        }

        emptyView = findViewById(R.id.empty_view);

        // Setup RecyclerView
        RecyclerView recyclerView = findViewById(R.id.dishes_recycler_view);
        if (recyclerView != null) {
            adapter = new DishAdapter(this::openDishDetails, this::confirmDelete);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        } else {
            // If RecyclerView itself is null, something is fundamentally wrong with the layout.
            Toast.makeText(this, "Error initializing UI elements (RecyclerView).", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Setup ViewModel
        menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);
        // CRITICAL: Ensure observation is only set up once and robustly.
        menuViewModel.menuLiveData.observe(this, menuItems -> {
            // Pass this activity's lifecycle owner to observe, ensures proper cleanup
            updateDishes(menuItems);
        });

        // --- Navigation ---
        Button backButton = findViewById(R.id.backButton);
        if(backButton != null) backButton.setOnClickListener(v -> finish());
        
        Button homeButton = findViewById(R.id.homeButton);
        if(homeButton != null) homeButton.setOnClickListener(v -> {
            boolean isStaff = SharedBookingData.isStaffLoggedIn;
            Intent intent = new Intent(this, isStaff ? StaffHomeActivity.class : GuestHomeActivity.class);
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
        // Ensure menu data is loaded when activity becomes visible
        if (menuViewModel != null) {
            menuViewModel.loadMenuFromDatabase();
        }
    }

    private void updateDishes(List<MenuItem> menuItems) {
        // Robust null checks
        if (menuItems == null || adapter == null || emptyView == null) return; 

        List<MenuItem> filteredDishes = new ArrayList<>();
        for (MenuItem item : menuItems) {
            // Additional null check for individual item properties
            if (item != null && item.category != null && item.category.equalsIgnoreCase(category)) {
                filteredDishes.add(item);
            }
        }
        
        // CRITICAL: Always submit a *new* list to ListAdapter to avoid ConcurrentModificationException
        adapter.submitList(new ArrayList<>(filteredDishes));

        emptyView.setVisibility(filteredDishes.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void confirmDelete(MenuItem item) {
        new AlertDialog.Builder(this)
            .setTitle("Delete Dish")
            .setMessage("Are you sure you want to delete '" + item.name + "'?")
            .setPositiveButton("Yes", (dialog, which) -> {
                if (menuViewModel != null) {
                    menuViewModel.deleteMenuItem(item, () -> {
                        // UI will update automatically via LiveData observation upon successful deletion.
                    });
                }
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