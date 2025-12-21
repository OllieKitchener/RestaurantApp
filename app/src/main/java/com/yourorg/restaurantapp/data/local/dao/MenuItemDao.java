package com.yourorg.restaurantapp.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.yourorg.restaurantapp.data.local.entities.MenuItemEntity;

import java.util.List;

@Dao
public interface MenuItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MenuItemEntity menuItem);

    @Query("SELECT * FROM menu_items")
    List<MenuItemEntity> getAll();

    @Query("SELECT DISTINCT category FROM menu_items ORDER BY category ASC")
    List<String> getDistinctCategories();
}
