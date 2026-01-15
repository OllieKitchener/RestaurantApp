package com.yourorg.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.yourorg.restaurantapp.model.MenuItem;
import com.yourorg.restaurantapp.util.OnItemClickListener;
import com.yourorg.restaurantapp.viewmodel.MenuViewModel;

import java.util.ArrayList;
import java.util.List;

public class MenuDishesActivity extends AppCompatActivity {

    private String category;
    private MenuViewModel menuViewModel;
    private DishAdapter adapter;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_dishes);

        category = getIntent().getStringExtra("CATEGORY_NAME");
        if (category == null) category = "Dishes";
        
        TextView title = findViewById(R.id.dish_list_title);
        title.setText(category);

        emptyView = findViewById(R.id.empty_view);

        RecyclerView recyclerView = findViewById(R.id.dishes_recycler_view);
        adapter = new DishAdapter(this::openDishDetails, this::confirmDelete);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);
        menuViewModel.menuLiveData.observe(this, this::updateDishes);

        setupNavigation();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data every time the screen is shown.
        // This is important so the list updates after an item is added or deleted.
        menuViewModel.loadMenuFromDatabase();
    }
    
    private void setupNavigation() {
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
        findViewById(R.id.homeButton).setOnClickListener(v -> {
            Intent intent = new Intent(this, App.isStaffLoggedIn() ? StaffHomeActivity.class : GuestHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
        findViewById(R.id.notificationsButton).setOnClickListener(v -> startActivity(new Intent(this, NotificationsActivity.class)));
        findViewById(R.id.settingsButton).setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
    }

    private void updateDishes(List<MenuItem> menuItems) {
        List<MenuItem> filtered = new ArrayList<>();
        if (menuItems != null) {
            for (MenuItem item : menuItems) {
                if (item != null && item.category != null && category.equalsIgnoreCase(item.category)) {
                    filtered.add(item);
                }
            }
        }
        
        adapter.setDishes(filtered);
        emptyView.setVisibility(filtered.isEmpty() ? View.VISIBLE : View.GONE);
    }

    // This method is passed to the adapter to handle delete clicks.
    private void confirmDelete(MenuItem item) {
        new AlertDialog.Builder(this)
            .setTitle("Delete Dish")
            .setMessage("Are you sure you want to delete '" + item.name + "'?")
            .setPositiveButton("Yes", (dialog, which) -> {
                // On confirmation, call the ViewModel to delete the item.
                // LiveData will automatically trigger updateDishes to refresh the list.
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