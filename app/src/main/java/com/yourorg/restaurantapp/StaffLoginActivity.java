package com.yourorg.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;

public class StaffLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_login);

        Button loginButton = findViewById(R.id.loginButton);
        TextView customerLoginLink = findViewById(R.id.customerLoginLink);

        if (loginButton != null) {
            loginButton.setOnClickListener(v -> {
                // For now, any staff login attempt is successful
                Toast.makeText(this, "Staff Login Successful!", Toast.LENGTH_SHORT).show();
                SharedBookingData.isStaffLoggedIn = true;
                startActivity(new Intent(this, StaffHomeActivity.class));
                finish();
            });
        }

        if (customerLoginLink != null) {
            customerLoginLink.setOnClickListener(v -> {
                SharedBookingData.isStaffLoggedIn = false; // Reset staff login status
                Intent intent = new Intent(this, LoginActivity.class);
                // Clear the activity stack so they can't go back to staff login
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });
        }
    }
}