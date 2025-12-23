package com.yourorg.restaurantapp.ui.staff;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// Correcting the import to use the project's actual namespace
import com.example.myapplication.R;
import com.yourorg.restaurantapp.api.RestaurantApi;
import com.yourorg.restaurantapp.api.ApiClient;
import com.yourorg.restaurantapp.model.MenuItem;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffManageMenuActivity extends AppCompatActivity {
    private EditText etName, etDescription, etPrice;
    private Button btnAdd;
    private ProgressBar progressBar;

    private RestaurantApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_manage_menu);

        etName = findViewById(R.id.et_name);
        etDescription = findViewById(R.id.et_description);
        etPrice = findViewById(R.id.et_price);
        btnAdd = findViewById(R.id.btn_add);
        progressBar = findViewById(R.id.progress_bar);

        api = ApiClient.getClient("https://api.example.com/").create(RestaurantApi.class);

        btnAdd.setOnClickListener(v -> createMenuItem());
    }

    private void createMenuItem() {
        String name = etName.getText().toString().trim();
        String desc = etDescription.getText().toString().trim();
        double price = 0.0;
        try { price = Double.parseDouble(etPrice.getText().toString().trim()); } catch (NumberFormatException e) { /* ignore */ }

        if (name.isEmpty()) {
            Toast.makeText(this, "Name required", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        // Using the correct constructor with a default category
        MenuItem item = new MenuItem(0, name, desc, price, "Uncategorized", true);
        api.createMenuItem(item).enqueue(new Callback<MenuItem>() {
            @Override
            public void onResponse(Call<MenuItem> call, Response<MenuItem> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Toast.makeText(StaffManageMenuActivity.this, "Menu item created", Toast.LENGTH_SHORT).show();
                    etName.setText(""); etDescription.setText(""); etPrice.setText("");
                } else {
                    Toast.makeText(StaffManageMenuActivity.this, "Failed: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MenuItem> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(StaffManageMenuActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}