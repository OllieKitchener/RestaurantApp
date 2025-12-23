package com.yourorg.restaurantapp.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.yourorg.restaurantapp.data.local.entities.MenuItemEntity;

import java.util.List;

@Dao
public interface MenuItemDao {
    @Query("SELECT * FROM menu_items")
    List<MenuItemEntity> getAll();

    @Insert
    long insert(MenuItemEntity item);

    @Update
    int update(MenuItemEntity item);

    @Delete
    int delete(MenuItemEntity item);

    @Query("DELETE FROM menu_items")
    void deleteAll();
}