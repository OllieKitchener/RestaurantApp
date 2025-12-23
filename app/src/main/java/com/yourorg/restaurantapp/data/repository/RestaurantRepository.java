package com.yourorg.restaurantapp.data.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.yourorg.restaurantapp.api.ApiClient;
import com.yourorg.restaurantapp.api.RestaurantApi;
import com.yourorg.restaurantapp.data.local.AppDatabase;
import com.yourorg.restaurantapp.data.local.entities.MenuItemEntity;
import com.yourorg.restaurantapp.data.local.entities.ReservationEntity;
import com.yourorg.restaurantapp.model.MenuItem;
import com.yourorg.restaurantapp.model.Reservation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantRepository {
    private final RestaurantApi api;
    private final AppDatabase db;
    private final ExecutorService executor = Executors.newFixedThreadPool(3);
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public RestaurantRepository(Context context, String baseUrl) {
        api = ApiClient.getClient(baseUrl).create(RestaurantApi.class);
        db = AppDatabase.getInstance(context);
    }

    // Added the missing shutdown() method
    public void shutdown() {
        executor.shutdown();
    }

    // Remote API calls - examples
    public void fetchMenuFromServer(Callback<List<MenuItem>> callback) {
        api.getMenu().enqueue(callback);
    }

    public void createMenuItemRemote(MenuItem item, Callback<MenuItem> callback) {
        api.createMenuItem(item).enqueue(callback);
    }

    public void updateMenuItemRemote(int id, MenuItem item, Callback<MenuItem> callback) {
        api.updateMenuItem(id, item).enqueue(callback);
    }

    public void deleteMenuItemRemote(int id, Callback<Void> callback) {
        api.deleteMenuItem(id).enqueue(callback);
    }

    public void fetchReservationsRemote(Callback<List<Reservation>> callback) {
        api.getReservations().enqueue(callback);
    }

    public void createReservationRemote(Reservation r, Callback<Reservation> callback) {
        api.createReservation(r).enqueue(callback);
    }

    public void updateReservationRemote(int id, Reservation r, Callback<Reservation> callback) {
        api.updateReservation(id, r).enqueue(callback);
    }

    public void deleteReservationRemote(int id, Callback<Void> callback) {
        api.deleteReservation(id).enqueue(callback);
    }

    // Local DB operations
    public void insertMenuItemLocal(final MenuItemEntity entity, final Runnable onComplete) {
        executor.execute(() -> {
            db.menuItemDao().insert(entity);
            if (onComplete != null) mainHandler.post(onComplete);
        });
    }

    public void getAllMenuLocal(final java.util.function.Consumer<List<MenuItemEntity>> consumer) {
        executor.execute(() -> {
            List<MenuItemEntity> items = db.menuItemDao().getAll();
            if (consumer != null) mainHandler.post(() -> consumer.accept(items));
        });
    }

    public void insertReservationLocal(final ReservationEntity entity, final Runnable onComplete) {
        executor.execute(() -> {
            db.reservationDao().insert(entity);
            if (onComplete != null) mainHandler.post(onComplete);
        });
    }

    public void getAllReservationsLocal(final java.util.function.Consumer<List<ReservationEntity>> consumer) {
        executor.execute(() -> {
            List<ReservationEntity> items = db.reservationDao().getAll();
            if (consumer != null) mainHandler.post(() -> consumer.accept(items));
        });
    }
}