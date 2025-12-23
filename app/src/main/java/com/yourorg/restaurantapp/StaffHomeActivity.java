package com.yourorg.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;

public class StaffHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_home);

        Button manageMenuButton = findViewById(R.id.manageMenuButton);

        if (manageMenuButton != null) {
            manageMenuButton.setOnClickListener(v -> {
                startActivity(new Intent(this, com.yourorg.restaurantapp.ui.staff.StaffManageMenuActivity.class));
            });
        }
    }
}