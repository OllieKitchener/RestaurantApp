package com.yourorg.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.example.myapplication.R;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout emailInputLayout, passwordInputLayout;
    private TextInputEditText emailEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Initialize views
        emailInputLayout = findViewById(R.id.emailInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        MaterialButton signInButton = findViewById(R.id.signInButton);
        MaterialButton staffLoginButton = findViewById(R.id.staffLoginButton);

        signInButton.setOnClickListener(v -> attemptCustomerLogin());

        staffLoginButton.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, StaffLoginActivity.class));
        });
    }

    private void attemptCustomerLogin() {
        emailInputLayout.setError(null);
        passwordInputLayout.setError(null);

        String email = emailEditText.getText() != null ? emailEditText.getText().toString().trim() : "";
        String password = passwordEditText.getText() != null ? passwordEditText.getText().toString().trim() : "";

        boolean hasError = false;

        // Use Android's built-in email pattern validator
        if (email.isEmpty()) {
            emailInputLayout.setError("Email is required");
            hasError = true;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInputLayout.setError("Please enter a valid email address");
            hasError = true;
        }

        if (password.isEmpty()) {
            passwordInputLayout.setError("Password is required");
            hasError = true;
        }

        if (hasError) {
            return;
        }

        // Set login state to customer
        SharedBookingData.isStaffLoggedIn = false;
        
        // If validation passes, proceed directly to the home screen
        startActivity(new Intent(LoginActivity.this, GuestHomeActivity.class));
        finish();
    }
}