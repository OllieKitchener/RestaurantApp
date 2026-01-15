package com.yourorg.restaurantapp.data.local.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "reservations")
public class ReservationEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public int partySize;
    public String dateTime;
    public String status;

    public ReservationEntity(String name, int partySize, String dateTime) {
        this.name = name;
        this.partySize = partySize;
        this.dateTime = dateTime;
        this.status = "Booked";
    }

    public com.yourorg.restaurantapp.model.Reservation toModel() {
        return new com.yourorg.restaurantapp.model.Reservation(id, name, partySize, dateTime, status);
    }
}