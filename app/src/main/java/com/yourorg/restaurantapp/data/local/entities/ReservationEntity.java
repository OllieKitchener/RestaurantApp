package com.yourorg.restaurantapp.data.local.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "reservations")
public class ReservationEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String phone;
    public int partySize;
    public String dateTime;
    public String status;
    public String notes;

    // No-argument constructor required by Room
    public ReservationEntity() { }

    // Full constructor for loading from the database
    public ReservationEntity(int id, String name, String phone, int partySize, String dateTime, String status, String notes) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.partySize = partySize;
        this.dateTime = dateTime;
        this.status = status;
        this.notes = notes;
    }

    // Constructor for creating a new reservation from the BookInActivity screen
    public ReservationEntity(String name, int partySize, String dateTime) {
        this.name = name;
        this.partySize = partySize;
        this.dateTime = dateTime;
        this.status = "Booked"; // Default status
    }

    public com.yourorg.restaurantapp.model.Reservation toModel() {
        return new com.yourorg.restaurantapp.model.Reservation(id, name, phone, partySize, dateTime, status, notes);
    }
}