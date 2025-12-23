package com.yourorg.restaurantapp.model;

import com.google.gson.annotations.SerializedName;

public class MenuItem {
    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("description")
    public String description;

    @SerializedName("price")
    public double price;

    // Adding the missing 'category' field
    @SerializedName("category")
    public String category;

    @SerializedName("available")
    public boolean available;

    public MenuItem() { }

    // Full constructor including the category
    public MenuItem(int id, String name, String description, double price, String category, boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.available = available;
    }
}