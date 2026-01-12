package com.yourorg.restaurantapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.myapplication.R;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Intent;

import com.yourorg.restaurantapp.model.Reservation;
import com.yourorg.restaurantapp.viewmodel.ReservationViewModel;

import java.util.Collections;
import java.util.List;

public class ReservationsActivity extends AppCompatActivity {

    private ReservationViewModel reservationViewModel;
    private LinearLayout reservationsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservations1);

        reservationsContainer = findViewById(R.id.reservationsContainer);
        reservationViewModel = new ViewModelProvider(this).get(ReservationViewModel.class);

        // Observer for reservations LiveData
        reservationViewModel.reservationsLiveData.observe(this, this::displayReservations);

        // Data loading is handled in onResume()

        // --- Back Button Navigation ---
        Button backButton = findViewById(R.id.backButton);
        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }

        // --- Bottom Nav Bar Logic ---
        Button homeButton = findViewById(R.id.homeButton);
        if(homeButton != null) homeButton.setOnClickListener(v -> startActivity(new Intent(this, GuestHomeActivity.class)));

        Button notificationsButton = findViewById(R.id.notificationsButton);
        if(notificationsButton != null) notificationsButton.setOnClickListener(v -> startActivity(new Intent(this, NotificationsActivity.class)));

        Button settingsButton = findViewById(R.id.settingsButton);
        if(settingsButton != null) settingsButton.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // CRITICAL FIX: Load reservations every time the activity is shown
        reservationViewModel.loadReservationsFromDatabase();
    }

    private void displayReservations(List<Reservation> reservations) {
        if (reservationsContainer == null) return;
        reservationsContainer.removeAllViews();

        if (reservations == null || reservations.isEmpty()) {
            TextView textView = new TextView(this);
            textView.setText("No reservations found.");
            textView.setTextSize(18);
            reservationsContainer.addView(textView);
        } else {
            // Display most recent reservations first
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
    }
}