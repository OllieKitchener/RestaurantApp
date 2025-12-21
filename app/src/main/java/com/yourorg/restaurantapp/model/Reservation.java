package com.yourorg.restaurantapp.model;

import com.google.gson.annotations.SerializedName;

public class Reservation {
    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("phone")
    public String phone;

    @SerializedName("partySize")
    public int partySize;

    @SerializedName("dateTime")
    public String dateTime; // ISO 8601 string recommended

    @SerializedName("status")
    public String status; // e.g., "pending", "confirmed", "cancelled"

    @SerializedName("notes")
    public String notes;

    public Reservation() { }

    public Reservation(int id, String name, String phone, int partySize, String dateTime, String status, String notes) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.partySize = partySize;
        this.dateTime = dateTime;
        this.status = status;
        this.notes = notes;
    }
}