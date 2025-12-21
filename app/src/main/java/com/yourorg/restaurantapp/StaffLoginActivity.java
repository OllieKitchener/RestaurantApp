package com.yourorg.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.google.android.material.button.MaterialButton;

public class StaffLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_login);

        TextInputEditText emailEditText = findViewById(R.id.staffEmailEditText);
        TextInputEditText passwordEditText = findViewById(R.id.staffPasswordEditText);
        TextInputLayout passwordLayout = findViewById(R.id.staffPasswordInputLayout);
        MaterialButton signInButton = findViewById(R.id.staffSignInButton);
        MaterialButton customerLoginButton = findViewById(R.id.customerLoginButton);

        signInButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // Simple hardcoded check for staff
            if (email.equals("staff@restaurant.com") && password.equals("password123")) {
                // Set login state to staff
                SharedBookingData.isStaffLoggedIn = true;
                // Navigate to the new Staff Home Screen
                startActivity(new Intent(this, StaffHomeActivity.class));
                finish();
            } else {
                passwordLayout.setError("Invalid staff credentials");
            }
        });

        customerLoginButton.setOnClickListener(v -> {
            // Finish this activity to go back to the customer login screen
            finish();
        });
    }
}