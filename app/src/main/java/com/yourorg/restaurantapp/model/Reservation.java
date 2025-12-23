package com.yourorg.restaurantapp.model;

import com.google.gson.annotations.SerializedName;

public class Reservation {
    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    // Removed phone and notes to match the simplified entity

    @SerializedName("partySize")
    public int partySize;

    @SerializedName("dateTime")
    public String dateTime;

    @SerializedName("status")
    public String status;

    public Reservation() { }

    // Updated constructor
    public Reservation(int id, String name, int partySize, String dateTime, String status) {
        this.id = id;
        this.name = name;
        this.partySize = partySize;
        this.dateTime = dateTime;
        this.status = status;
    }
}