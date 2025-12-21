package com.yourorg.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Collections;

public class NotificationsActivity extends AppCompatActivity {

    public static final ArrayList<String> notificationMessages = new ArrayList<>();

    private LinearLayout notificationsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);

        Button backButton = findViewById(R.id.backButton);
        notificationsContainer = findViewById(R.id.notificationsContainer);
        MaterialButton homeButton = findViewById(R.id.homeButton);
        MaterialButton notificationsButton = findViewById(R.id.notificationsButton);
        MaterialButton settingsButton = findViewById(R.id.settingsButton);

        backButton.setOnClickListener(v -> finish());
        
        homeButton.setOnClickListener(v -> {
            if (SharedBookingData.isStaffLoggedIn) {
                startActivity(new Intent(this, StaffHomeActivity.class));
            } else {
                startActivity(new Intent(this, GuestHomeActivity.class));
            }
        });

        notificationsButton.setOnClickListener(v -> { /* Already here */ });
        settingsButton.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));

        displayNotifications();
    }

    private void displayNotifications() {
        notificationsContainer.removeAllViews();

        for (int i = notificationMessages.size() - 1; i >= 0; i--) {
            String message = notificationMessages.get(i);
            MaterialButton notificationView = new MaterialButton(this, null, com.google.android.material.R.attr.materialCardViewStyle);
            notificationView.setText(message);
            notificationView.setTextAlignment(TextView.TEXT_ALIGNMENT_TEXT_START);
            notificationView.setCornerRadius(16);
            
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 24);
            notificationView.setLayoutParams(params);

            notificationView.setOnClickListener(v -> {
                Intent intent = new Intent(NotificationsActivity.this, ReservationDetailsActivity.class);
                intent.putExtra("RESERVATION_DETAILS", message);
                startActivity(intent);
            });

            notificationsContainer.addView(notificationView);
        }
    }
}