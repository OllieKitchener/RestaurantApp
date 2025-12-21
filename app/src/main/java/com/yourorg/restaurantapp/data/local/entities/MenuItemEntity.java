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
    public String category;
    public boolean available;

    public MenuItemEntity() { }

    public MenuItemEntity(String name, String description, double price, String category, boolean available) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.available = available;
    }

    public MenuItem toModel() {
        return new MenuItem(id, name, description, price, category, available);
    }
}