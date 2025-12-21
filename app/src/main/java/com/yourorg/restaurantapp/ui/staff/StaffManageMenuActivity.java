package com.yourorg.restaurantapp.ui.staff;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.yourorg.restaurantapp.data.local.entities.MenuItemEntity;
import com.yourorg.restaurantapp.viewmodel.MenuViewModel;
import com.yourorg.restaurantapp.GuestHomeActivity;
import com.yourorg.restaurantapp.NotificationsActivity;
import com.yourorg.restaurantapp.SettingsActivity;
import com.google.android.material.button.MaterialButton;

public class StaffManageMenuActivity extends AppCompatActivity {
    private EditText etName, etDescription, etPrice;
    private Spinner categorySpinner;
    private MenuViewModel menuViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_manage_menu);

        menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        etName = findViewById(R.id.et_name);
        etDescription = findViewById(R.id.et_description);
        etPrice = findViewById(R.id.et_price);
        categorySpinner = findViewById(R.id.categorySpinner);
        Button btnAdd = findViewById(R.id.btn_add);
        Button backButton = findViewById(R.id.backButton);
        MaterialButton homeButton = findViewById(R.id.homeButton);
        MaterialButton notificationsButton = findViewById(R.id.notificationsButton);
        MaterialButton settingsButton = findViewById(R.id.settingsButton);

        // Setup Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.menu_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> createMenuItem());
        backButton.setOnClickListener(v -> finish());

        // Bottom navigation
        homeButton.setOnClickListener(v -> startActivity(new Intent(this, GuestHomeActivity.class)));
        notificationsButton.setOnClickListener(v -> startActivity(new Intent(this, NotificationsActivity.class)));
        settingsButton.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
    }

    private void createMenuItem() {
        String name = etName.getText().toString().trim();
        String desc = etDescription.getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString();
        double price = 0.0;
        try {
            price = Double.parseDouble(etPrice.getText().toString().trim());
        } catch (NumberFormatException e) {
            etPrice.setError("Invalid price");
            return;
        }

        if (name.isEmpty()) {
            etName.setError("Name is required");
            return;
        }

        MenuItemEntity newItem = new MenuItemEntity();
        newItem.name = name;
        newItem.description = desc;
        newItem.price = price;
        newItem.category = category;
        newItem.available = true;

        menuViewModel.addMenuItem(newItem);

        // Clear fields after adding
        etName.setText("");
        etDescription.setText("");
        etPrice.setText("");
    }
}