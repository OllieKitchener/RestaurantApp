package com.yourorg.restaurantapp.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable; // Explicitly import Serializable

public class MenuItem implements Serializable {
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

    public MenuItem() { }

    public MenuItem(String name, String description, double price, String category, boolean available, String ingredients, String allergyInfo) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.available = available;
        this.ingredients = ingredients;
        this.allergyInfo = allergyInfo;
    }
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
