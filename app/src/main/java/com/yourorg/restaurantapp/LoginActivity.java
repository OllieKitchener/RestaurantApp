package com.yourorg.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText; // Added for completeness, though not strictly used for login logic here
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // --- CRITICAL FIX: Ensure staff status is false on customer login screen ---
        SharedBookingData.isStaffLoggedIn = false;

        Button loginButton = findViewById(R.id.loginButton);
        TextView staffLoginLink = findViewById(R.id.staffLoginLink);

        if (loginButton != null) {
            loginButton.setOnClickListener(v -> {
                // For now, any login attempt goes to Guest Home
                // You would add actual username/password validation here
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, GuestHomeActivity.class));
                finish(); 
            });
        }

        if (staffLoginLink != null) {
            staffLoginLink.setOnClickListener(v -> {
                startActivity(new Intent(this, StaffLoginActivity.class));
            });
        }
    }
}