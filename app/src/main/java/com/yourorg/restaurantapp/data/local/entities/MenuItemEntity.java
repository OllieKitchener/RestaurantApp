package com.yourorg.restaurantapp.data.local.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.yourorg.restaurantapp.model.MenuItem;

// Annotations kept for structural integrity, but Room DB is bypassed for in-memory storage
@Entity(tableName = "menu_items")
public class MenuItemEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String description;
    public double price;
    public String category;
    public boolean available;
    public String ingredients;
    public String allergyInfo;

    public MenuItemEntity() { }

    // Full constructor including ID
    public MenuItemEntity(int id, String name, String description, double price, String category, boolean available, String ingredients, String allergyInfo) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.available = available;
        this.ingredients = (ingredients != null) ? ingredients : "";
        this.allergyInfo = (allergyInfo != null) ? allergyInfo : "";
    }

    // Constructor without ID
    public MenuItemEntity(String name, String description, double price, String category, boolean available, String ingredients, String allergyInfo) {
        this(0, name, description, price, category, available, ingredients, allergyInfo);
    }

    // Helper method to convert Model -> Entity
    public static MenuItemEntity fromModel(MenuItem m) {
        return new MenuItemEntity(m.id, m.name, m.description, m.price, m.category, m.available, m.ingredients, m.allergyInfo);
    }

    // Helper method to convert Entity -> Model
    public MenuItem toModel() {
        return new MenuItem(id, name, description, price, category, available, ingredients, allergyInfo);
    }
}