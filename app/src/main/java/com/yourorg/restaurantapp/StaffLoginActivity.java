package com.yourorg.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;

public class StaffLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_login);

        Button loginButton = findViewById(R.id.loginButton);

        if (loginButton != null) {
            loginButton.setOnClickListener(v -> {
                // For now, any staff login attempt is successful
                Toast.makeText(this, "Staff Login Successful!", Toast.LENGTH_SHORT).show();
                SharedBookingData.isStaffLoggedIn = true;
                startActivity(new Intent(this, StaffHomeActivity.class));
                finish();
            });
        }
    }
}