package com.yourorg.restaurantapp.ui.staff;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.yourorg.restaurantapp.MenuDishesActivity;
import com.yourorg.restaurantapp.NotificationsActivity;
import com.yourorg.restaurantapp.SettingsActivity;
import com.yourorg.restaurantapp.StaffHomeActivity;
import com.yourorg.restaurantapp.viewmodel.MenuViewModel;
import com.yourorg.restaurantapp.App;

public class StaffMenuCategoryActivity extends AppCompatActivity {

    private MenuViewModel menuViewModel;
    private CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_menu_category);

        RecyclerView recyclerView = findViewById(R.id.category_recycler_view);
        if (recyclerView != null) {
            // Using the onItemClick interface
            adapter = new CategoryAdapter(category -> onCategoryClicked(category));
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        }

        menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);
        menuViewModel.categoriesLiveData.observe(this, categories -> {
            if (categories != null && adapter != null) {
                adapter.submitList(new java.util.ArrayList<>(categories));
            }
        });

        Button backButton = findViewById(R.id.backButton);
        if(backButton != null) backButton.setOnClickListener(v -> finish());

        Button homeButton = findViewById(R.id.homeButton);
        if(homeButton != null) homeButton.setOnClickListener(v -> {
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
        if (menuViewModel != null) {
            menuViewModel.loadCategoriesFromDatabase();
        }
    }

    private void onCategoryClicked(String categoryName) {
        Intent intent = new Intent(this, MenuDishesActivity.class);
        intent.putExtra("CATEGORY_NAME", categoryName);
        startActivity(intent);
    }
}