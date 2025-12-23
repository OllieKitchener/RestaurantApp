package com.yourorg.restaurantapp.ui.guest;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// Correcting the import to use the project's actual namespace
import com.example.myapplication.R;
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
        viewModel.menuLiveData.observe(this, this::onMenuLoaded);
        viewModel.error.observe(this, s -> Toast.makeText(this, s, Toast.LENGTH_LONG).show());

        progressBar.setVisibility(View.VISIBLE);
        viewModel.loadMenu();
    }

    private void onMenuLoaded(List<MenuItem> items) {
        progressBar.setVisibility(View.GONE);
        adapter.submitList(items);
    }

    private void onMenuItemClicked(MenuItem item) {
        // TODO: open Menu Details / Reservation screen
        Toast.makeText(this, "Clicked: " + item.name, Toast.LENGTH_SHORT).show();
    }
}