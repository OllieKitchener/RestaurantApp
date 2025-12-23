package com.yourorg.restaurantapp.data.local.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.yourorg.restaurantapp.model.MenuItem;

@Entity(tableName = "menu_items")
public class MenuItemEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String description;
    public double price;
    public String category; // Added missing category field for consistency
    public boolean available;

    public MenuItemEntity() { }

    public MenuItemEntity(int id, String name, String description, double price, String category, boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.available = available;
    }

    public static MenuItemEntity fromModel(MenuItem m) {
        return new MenuItemEntity(m.id, m.name, m.description, m.price, m.category, m.available);
    }

    // Corrected to pass the category field to the model's constructor
    public MenuItem toModel() {
        return new MenuItem(id, name, description, price, category, available);
    }
}