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
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_dishes);

        category = getIntent().getStringExtra("CATEGORY_NAME");
        if (category == null) category = "Dishes";
        
        TextView title = findViewById(R.id.dish_list_title);
        title.setText(category);

        emptyView = findViewById(R.id.empty_view);
        recyclerView = findViewById(R.id.dishes_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);
        menuViewModel.menuLiveData.observe(this, this::updateDishes);

        setupNavigation();
        
        // Load data only once when the activity is first created.
        menuViewModel.loadMenuFromDatabase();
    }
    
    // CRITICAL FIX: The "Scorched Earth" approach.
    // Every time the screen is shown, we create a brand new, clean adapter.
    // This prevents any corrupted state from recycled views from causing a crash.
    @Override
    protected void onResume() {
        super.onResume();
        adapter = new DishAdapter(this::openDishDetails, this::confirmDelete);
        recyclerView.setAdapter(adapter);
        
        // The LiveData observer will automatically re-deliver the last known data
        // to our new adapter via the updateDishes method, populating the screen.
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
        if (adapter == null) return;

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

    private void confirmDelete(MenuItem item) {
        new AlertDialog.Builder(this)
            .setTitle("Delete Dish")
            .setMessage("Are you sure you want to delete '" + item.name + "'?")
            .setPositiveButton("Yes", (dialog, which) -> menuViewModel.deleteMenuItem(item, null))
            .setNegativeButton("No", null)
            .show();
    }

    private void openDishDetails(MenuItem dish) {
        Intent intent = new Intent(this, DishDetailsActivity.class);
        intent.putExtra(DishDetailsActivity.EXTRA_MENU_ITEM, dish);
        startActivity(intent);
    }
}