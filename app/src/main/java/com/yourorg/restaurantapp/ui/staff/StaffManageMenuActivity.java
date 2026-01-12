package com.yourorg.restaurantapp.ui.staff;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.content.Intent;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.yourorg.restaurantapp.GuestHomeActivity;
import com.yourorg.restaurantapp.NotificationsActivity;
import com.yourorg.restaurantapp.SettingsActivity;
import com.yourorg.restaurantapp.StaffHomeActivity;
import com.yourorg.restaurantapp.data.local.entities.MenuItemEntity;
import com.yourorg.restaurantapp.viewmodel.MenuViewModel;

public class StaffManageMenuActivity extends AppCompatActivity {
    private EditText etName, etDescription, etPrice, etIngredients, etAllergyInfo;
    private Spinner spinnerCategory; 
    private Button btnAdd, btnClearAll, btnAddSampleDishes;
    private ProgressBar progressBar;
    private MenuViewModel menuViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_manage_menu);

        etName = findViewById(R.id.et_name);
        etDescription = findViewById(R.id.et_description);
        etPrice = findViewById(R.id.et_price);
        spinnerCategory = findViewById(R.id.spinner_category);
        etIngredients = findViewById(R.id.et_ingredients);
        etAllergyInfo = findViewById(R.id.et_allergy_info);
        btnAdd = findViewById(R.id.btn_add);
        btnClearAll = findViewById(R.id.btn_clear_all);
        btnAddSampleDishes = findViewById(R.id.btn_add_sample_dishes);
        progressBar = findViewById(R.id.progress_bar);

        setupCategorySpinner();
        menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        btnAdd.setOnClickListener(v -> createMenuItem());
        btnClearAll.setOnClickListener(v -> clearAllDishes());
        btnAddSampleDishes.setOnClickListener(v -> addSampleDishes());

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

    private void setupCategorySpinner() {
        String[] categories = new String[]{"Recommended", "Deals", "Meat", "Fish", "Vegetarian"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    private void createMenuItem() {
        String name = etName.getText().toString().trim();
        String desc = etDescription.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();
        String priceStr = etPrice.getText().toString().trim();
        String ingredients = etIngredients.getText().toString().trim();
        String allergyInfo = etAllergyInfo.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Name and Price are required", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price format", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnAdd.setEnabled(false);

        MenuItemEntity newItem = new MenuItemEntity(name, desc, price, category, true, ingredients, allergyInfo);
        menuViewModel.addMenuItem(newItem, () -> {
            progressBar.setVisibility(View.GONE);
            btnAdd.setEnabled(true);
            // Toast.makeText(this, "Menu item added to database!", Toast.LENGTH_SHORT).show(); // Removed
            etName.setText("");
            etDescription.setText("");
            etPrice.setText("");
            etIngredients.setText("");
            etAllergyInfo.setText("");
            spinnerCategory.setSelection(0);
        });
    }

    private void clearAllDishes() {
        progressBar.setVisibility(View.VISIBLE);
        setButtonsEnabled(false);

        menuViewModel.clearAllMenuItems(() -> {
            progressBar.setVisibility(View.GONE);
            setButtonsEnabled(true);
            // Toast.makeText(this, "All dishes cleared from database!", Toast.LENGTH_SHORT).show(); // Removed
        });
    }

    private void addSampleDishes() {
        progressBar.setVisibility(View.VISIBLE);
        setButtonsEnabled(false);

        menuViewModel.populateDefaultDishes(() -> {
            progressBar.setVisibility(View.GONE);
            setButtonsEnabled(true);
            // Toast.makeText(StaffManageMenuActivity.this, "Sample dishes added to database!", Toast.LENGTH_SHORT).show(); // Removed
        });
    }
    
    private void setButtonsEnabled(boolean enabled) {
        btnAdd.setEnabled(enabled);
        btnClearAll.setEnabled(enabled);
        btnAddSampleDishes.setEnabled(enabled);
    }
}