package com.yourorg.restaurantapp.data.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.yourorg.restaurantapp.api.ApiClient;
import com.yourorg.restaurantapp.api.RestaurantApi;
import com.yourorg.restaurantapp.model.MenuItem;
import com.yourorg.restaurantapp.model.Reservation;
// Import ReservationEntity just to adapt old calls if needed, though we should transition to model.Reservation
import com.yourorg.restaurantapp.data.local.entities.ReservationEntity;
import com.yourorg.restaurantapp.data.local.entities.MenuItemEntity;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;

public class RestaurantRepository {
    private final RestaurantApi api;
    
    // --- In-Memory Data Storage ---
    private final List<MenuItem> menuItemsInMemory = new ArrayList<>();
    private final List<Reservation> reservationsInMemory = new ArrayList<>();
    
    private final ExecutorService executor = Executors.newFixedThreadPool(3);
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public RestaurantRepository(Context context, String baseUrl) {
        api = ApiClient.getClient(baseUrl).create(RestaurantApi.class);
    }

    public void shutdown() {
        executor.shutdown();
    }

    // --- Remote API calls (Kept as is) ---
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

    // --- Local In-Memory Operations ---

    // 1. Menu Items
    public void insertMenuItemLocal(final MenuItemEntity entity, final Runnable onComplete) {
        executor.execute(() -> {
            // Convert Entity to Model for in-memory storage
            MenuItem item = entity.toModel();
            // Simulate auto-increment ID
            item.id = menuItemsInMemory.size() + 1;
            menuItemsInMemory.add(item);
            
            if (onComplete != null) mainHandler.post(onComplete);
        });
    }
    
    // Overloaded for MenuItem directly if needed
    public void insertMenuItemLocal(final MenuItem item, final Runnable onComplete) {
        executor.execute(() -> {
            item.id = menuItemsInMemory.size() + 1;
            menuItemsInMemory.add(item);
            if (onComplete != null) mainHandler.post(onComplete);
        });
    }

    public void getAllMenuLocal(final java.util.function.Consumer<List<MenuItemEntity>> consumer) {
        executor.execute(() -> {
            // Convert Model back to Entity structure to match ViewModel expectations
            List<MenuItemEntity> entities = new ArrayList<>();
            for(MenuItem m : menuItemsInMemory) {
                // Using the static helper we added earlier or manually mapping
                entities.add(com.yourorg.restaurantapp.data.local.entities.MenuItemEntity.fromModel(m));
            }
            
            if (consumer != null) mainHandler.post(() -> consumer.accept(entities));
        });
    }

    public void deleteAllMenuItemsLocal(final Runnable onComplete) {
        executor.execute(() -> {
            menuItemsInMemory.clear();
            if (onComplete != null) mainHandler.post(onComplete);
        });
    }

    public void getDistinctCategoriesLocal(final java.util.function.Consumer<List<String>> consumer) {
        executor.execute(() -> {
            List<String> categories = new ArrayList<>();
            for(MenuItem m : menuItemsInMemory) {
                if(m.category != null && !categories.contains(m.category)) {
                    categories.add(m.category);
                }
            }
            // Sort them
            java.util.Collections.sort(categories);
            
            if (consumer != null) mainHandler.post(() -> consumer.accept(categories));
        });
    }

    // 2. Reservations
    public void insertReservationLocal(final ReservationEntity entity, final Runnable onComplete) {
        executor.execute(() -> {
            // Convert Entity to Model
            Reservation r = entity.toModel();
            r.id = reservationsInMemory.size() + 1;
            reservationsInMemory.add(r);
            
            if (onComplete != null) mainHandler.post(onComplete);
        });
    }

    public void getAllReservationsLocal(final java.util.function.Consumer<List<ReservationEntity>> consumer) {
        executor.execute(() -> {
            List<ReservationEntity> entities = new ArrayList<>();
            for(Reservation r : reservationsInMemory) {
                // Manually map back to entity for now
                entities.add(new ReservationEntity(r.name, r.partySize, r.dateTime)); 
            }
            if (consumer != null) mainHandler.post(() -> consumer.accept(entities));
        });
    }
}