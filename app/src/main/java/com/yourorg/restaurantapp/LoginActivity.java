package com.yourorg.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        App.setStaffLoggedIn(false);

        EditText emailEditText = findViewById(R.id.username);
        EditText passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);
        TextView staffLoginLink = findViewById(R.id.staffLoginLink);
        TextView registerLink = findViewById(R.id.registerLink);

        if (loginButton != null) {
            loginButton.setOnClickListener(v -> {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "Please enter email and password.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!email.contains("@") || !email.contains(".")) {
                    Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (UserSession.validateUser(email, password)) {
                    startActivity(new Intent(this, GuestHomeActivity.class));
                    finish(); 
                } else {
                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (staffLoginLink != null) {
            staffLoginLink.setOnClickListener(v -> {
                App.setStaffLoggedIn(false);
                startActivity(new Intent(this, StaffLoginActivity.class));
            });
        }

        if (registerLink != null) {
            registerLink.setOnClickListener(v -> {
                startActivity(new Intent(this, RegisterActivity.class));
            });
        }
    }
}