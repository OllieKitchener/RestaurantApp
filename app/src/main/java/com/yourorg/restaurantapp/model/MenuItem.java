package com.yourorg.restaurantapp.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable; // Explicitly import Serializable

public class MenuItem implements Serializable { // Explicitly implement Serializable
    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("description")
    public String description;

    @SerializedName("price")
    public double price;

    @SerializedName("category")
    public String category;

    @SerializedName("available")
    public boolean available;

    @SerializedName("ingredients")
    public String ingredients;

    @SerializedName("allergyInfo")
    public String allergyInfo;

    // Default no-argument constructor required by some frameworks/libraries
    public MenuItem() { }

    // Constructor for creating a new item without an ID (for inserts where ID is auto-generated)
    public MenuItem(String name, String description, double price, String category, boolean available, String ingredients, String allergyInfo) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.available = available;
        this.ingredients = ingredients; // Ensure new fields are initialized
        this.allergyInfo = allergyInfo; // Ensure new fields are initialized
    }

    // Full constructor including ID (for loading from database or explicit ID setting)
    public MenuItem(int id, String name, String description, double price, String category, boolean available, String ingredients, String allergyInfo) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.available = available;
        this.ingredients = ingredients;
        this.allergyInfo = allergyInfo;
    }
}
