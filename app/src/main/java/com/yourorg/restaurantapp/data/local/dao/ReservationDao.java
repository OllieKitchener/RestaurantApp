package com.yourorg.restaurantapp.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.yourorg.restaurantapp.data.local.entities.ReservationEntity;

import java.util.List;

@Dao
public interface ReservationDao {
    @Query("SELECT * FROM reservations")
    List<ReservationEntity> getAll();

    @Insert
    long insert(ReservationEntity reservation);

    @Update
    int update(ReservationEntity reservation);

    @Delete
    int delete(ReservationEntity reservation);

    @Query("DELETE FROM reservations")
    void deleteAll();
}