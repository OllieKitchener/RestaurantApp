package com.yourorg.restaurantapp.ui.staff;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

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
    private Button btnAdd, btnClearAll;
    private ProgressBar progressBar;
    private MenuViewModel menuViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_manage_menu);

        initializeViews();
        setupCategorySpinner();

        menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        setupClickListeners();
        setupNavigation();
    }

    private void initializeViews() {
        etName = findViewById(R.id.et_name);
        etDescription = findViewById(R.id.et_description);
        etPrice = findViewById(R.id.et_price);
        spinnerCategory = findViewById(R.id.spinner_category);
        etIngredients = findViewById(R.id.et_ingredients);
        etAllergyInfo = findViewById(R.id.et_allergy_info);
        btnAdd = findViewById(R.id.btn_add);
        btnClearAll = findViewById(R.id.btn_clear_all);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void setupClickListeners() {
        btnAdd.setOnClickListener(v -> addMenuItem());
        btnClearAll.setOnClickListener(v -> clearAllMenuItems());
    }
    
    private void setupNavigation() {
        Button backButton = findViewById(R.id.backButton);
        if(backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }

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
        String[] categories = {"Recommended", "Deals", "Meat", "Fish", "Vegetarian"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    private void addMenuItem() {
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

        setLoading(true);

        MenuItemEntity newItem = new MenuItemEntity(name, desc, price, category, true, ingredients, allergyInfo);
        menuViewModel.addMenuItem(newItem, () -> {
            setLoading(false);
            Toast.makeText(this, "Menu item added!", Toast.LENGTH_SHORT).show();
            clearForm();
        });
    }

    private void clearAllMenuItems() {
        setLoading(true);
        menuViewModel.clearAllMenuItems(() -> {
            setLoading(false);
            Toast.makeText(this, "All dishes cleared!", Toast.LENGTH_SHORT).show();
        });
    }

    private void clearForm() {
        etName.setText("");
        etDescription.setText("");
        etPrice.setText("");
        etIngredients.setText("");
        etAllergyInfo.setText("");
        spinnerCategory.setSelection(0);
    }

    private void setLoading(boolean loading) {
        if (loading) {
            progressBar.setVisibility(View.VISIBLE);
            btnAdd.setEnabled(false);
            btnClearAll.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            btnAdd.setEnabled(true);
            btnClearAll.setEnabled(true);
        }
    }
}