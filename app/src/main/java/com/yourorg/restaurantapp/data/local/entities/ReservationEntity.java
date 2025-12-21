package com.yourorg.restaurantapp.data.local.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "reservations")
public class ReservationEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public int partySize;
    public String dateTime; // The single source of truth for date and time
    public String status;

    // No-argument constructor required by Room
    public ReservationEntity() { }

    // Simple constructor for creating a new booking from the UI
    public ReservationEntity(String name, int partySize, String dateTime) {
        this.name = name;
        this.partySize = partySize;
        this.dateTime = dateTime;
        this.status = "Booked"; // Default status
    }
}