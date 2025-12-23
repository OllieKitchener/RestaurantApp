package com.yourorg.restaurantapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.myapplication.R;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Intent;

// Correctly import the UI model, not the database entity
import com.yourorg.restaurantapp.model.Reservation;
import com.yourorg.restaurantapp.viewmodel.ReservationViewModel;

import java.util.Collections;

public class ReservationsActivity extends AppCompatActivity {

    private ReservationViewModel reservationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservations1);

        LinearLayout reservationsContainer = findViewById(R.id.reservationsContainer);
        reservationViewModel = new ViewModelProvider(this).get(ReservationViewModel.class);

        reservationViewModel.reservationsLiveData.observe(this, reservations -> {
            if (reservationsContainer == null) return;
            reservationsContainer.removeAllViews();

            if (reservations == null || reservations.isEmpty()) {
                TextView textView = new TextView(this);
                textView.setText("No reservations found.");
                textView.setTextSize(18);
                reservationsContainer.addView(textView);
            } else {
                // The LiveData provides Reservation objects, not ReservationEntity objects.
                // This loop now uses the correct type.
                Collections.reverse(reservations);
                for (Reservation reservation : reservations) {
                    String summary = "Name: " + reservation.name + "\n" +
                                     "Party Size: " + reservation.partySize + "\n" +
                                     "Date & Time: " + reservation.dateTime;
                    TextView textView = new TextView(this);
                    textView.setText(summary);
                    textView.setTextSize(18);
                    textView.setPadding(0, 8, 0, 8);
                    reservationsContainer.addView(textView);
                }
            }
        });

        // Corrected to call the method that fetches from the remote API as per the ViewModel design
        reservationViewModel.loadReservations();

        // Initialize buttons from the layout
        Button backButton = findViewById(R.id.backButton);
        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }

        Button homeButton = findViewById(R.id.homeButton);
        Button notificationsButton = findViewById(R.id.notificationsButton);
        Button settingsButton = findViewById(R.id.settingsButton);

        if (homeButton != null) {
            homeButton.setOnClickListener(v -> {
                // This logic seems incorrect, there is no isStaffLoggedIn variable.
                // For now, it will just go to the Guest Home.
                startActivity(new Intent(this, GuestHomeActivity.class));
            });
        }

        if (notificationsButton != null) {
            notificationsButton.setOnClickListener(v -> {
                startActivity(new Intent(this, com.yourorg.restaurantapp.NotificationsActivity.class));
            });
        }

        if (settingsButton != null) {
            settingsButton.setOnClickListener(v -> {
                startActivity(new Intent(this, SettingsActivity.class));
            });
        }
    }
}