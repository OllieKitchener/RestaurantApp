package com.yourorg.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;

public class StaffLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_login);

        EditText usernameEditText = findViewById(R.id.username);
        EditText passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);
        TextView customerLoginLink = findViewById(R.id.customerLoginLink);

        if (loginButton != null) {
            loginButton.setOnClickListener(v -> {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if ("staff".equals(username) && "password".equals(password)) {
                    // Toast.makeText(this, "Staff Login Successful!", Toast.LENGTH_SHORT).show(); // Toast removed as requested previously
                    
                    // CRITICAL FIX: Use global App.setStaffLoggedIn(true)
                    App.setStaffLoggedIn(true); 
                    
                    startActivity(new Intent(this, StaffHomeActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Invalid Staff ID or Password", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (customerLoginLink != null) {
            customerLoginLink.setOnClickListener(v -> {
                // CRITICAL FIX: Reset staff status to false when navigating back to customer login
                App.setStaffLoggedIn(false);
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });
        }
    }
}