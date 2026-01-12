package com.yourorg.restaurantapp.ui.guest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.yourorg.restaurantapp.DishDetailsActivity;
import com.yourorg.restaurantapp.GuestHomeActivity;
import com.yourorg.restaurantapp.NotificationsActivity;
import com.yourorg.restaurantapp.SettingsActivity;
import com.yourorg.restaurantapp.model.MenuItem;
import com.yourorg.restaurantapp.viewmodel.MenuViewModel;

import java.util.List;

public class GuestMenuActivity extends AppCompatActivity {
    private MenuViewModel viewModel;
    private RecyclerView recyclerView;
    private MenuAdapter adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_menu);

        recyclerView = findViewById(R.id.recycler_view_menu);
        progressBar = findViewById(R.id.progress_bar);

        adapter = new MenuAdapter(item -> onMenuItemClicked(item));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(MenuViewModel.class);
        // CRITICAL: Observe menuLiveData to update UI when data changes
        viewModel.menuLiveData.observe(this, this::onMenuLoaded);
        viewModel.error.observe(this, s -> Toast.makeText(this, s, Toast.LENGTH_LONG).show());

        // Data loading is now handled in onResume for consistent freshness

        // --- Bottom Nav Bar Logic (Guest Context) ---
        Button homeButton = findViewById(R.id.homeButton);
        if(homeButton != null) homeButton.setOnClickListener(v -> startActivity(new Intent(this, GuestHomeActivity.class)));

        Button notificationsButton = findViewById(R.id.notificationsButton);
        if(notificationsButton != null) notificationsButton.setOnClickListener(v -> startActivity(new Intent(this, NotificationsActivity.class)));

        Button settingsButton = findViewById(R.id.settingsButton);
        if(settingsButton != null) settingsButton.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        // CRITICAL: Load menu from repository every time activity resumes
        viewModel.loadMenuFromDatabase();
    }

    private void onMenuLoaded(List<MenuItem> items) {
        progressBar.setVisibility(View.GONE);
        adapter.submitList(items);
    }

    private void onMenuItemClicked(MenuItem item) {
        Intent intent = new Intent(this, DishDetailsActivity.class);
        intent.putExtra(DishDetailsActivity.EXTRA_MENU_ITEM, item);
        startActivity(intent);
    }
}