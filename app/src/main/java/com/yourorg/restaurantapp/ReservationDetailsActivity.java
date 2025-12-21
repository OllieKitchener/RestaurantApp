package com.yourorg.restaurantapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class ReservationDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_details);

        TextView detailsText = findViewById(R.id.reservationDetailsText);
        Button backButton = findViewById(R.id.backButton);

        String reservationDetails = getIntent().getStringExtra("RESERVATION_DETAILS");

        if (reservationDetails != null) {
            detailsText.setText(reservationDetails);
        } else {
            detailsText.setText("No details found.");
        }

        backButton.setOnClickListener(v -> finish());
    }
}