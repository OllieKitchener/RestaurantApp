package com.yourorg.restaurantapp.data.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.yourorg.restaurantapp.api.ApiClient;
import com.yourorg.restaurantapp.api.RestaurantApi;
import com.yourorg.restaurantapp.model.MenuItem;
import com.yourorg.restaurantapp.model.Reservation;
import com.yourorg.restaurantapp.data.local.entities.ReservationEntity;
import com.yourorg.restaurantapp.data.local.entities.MenuItemEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;

public class RestaurantRepository {
    // --- Singleton Pattern ---
    private static volatile RestaurantRepository INSTANCE;
    
    private final RestaurantApi api;
    
    // --- In-Memory Data Storage ---
    private final List<MenuItem> menuItemsInMemory = Collections.synchronizedList(new ArrayList<>());
    private final List<Reservation> reservationsInMemory = Collections.synchronizedList(new ArrayList<>());
    private int nextMenuItemId = 1;
    private int nextReservationId = 1;
    
    private final ExecutorService executor = Executors.newFixedThreadPool(3);
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private RestaurantRepository(Context context) {
        api = ApiClient.getClient("https://localhost/").create(RestaurantApi.class);
        if (menuItemsInMemory.isEmpty()) {
            initInMemoryMenuData();
        }
    }

    public static RestaurantRepository getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (RestaurantRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RestaurantRepository(context.getApplicationContext());
                }
            }
        }
        return INSTANCE;
    }

    private void initInMemoryMenuData() {
        addMenuItemToMemory(new MenuItem("Chef's Special Steak", "Premium cut with secret sauce.", 35.00, "Recommended", true, "Beef, Secret Sauce, Potatoes", "None"));
        addMenuItemToMemory(new MenuItem("Truffle Pasta", "Creamy pasta with black truffle.", 28.00, "Recommended", true, "Pasta, Cream, Truffle, Parmesan", "Gluten, Dairy"));
        addMenuItemToMemory(new MenuItem("Burger & Fries", "Classic burger with a side of fries.", 15.00, "Deals", true, "Beef, Bun, Lettuce, Potato", "Gluten"));
        addMenuItemToMemory(new MenuItem("Ribeye Steak", "Juicy ribeye cooked to perfection.", 29.99, "Meat", true, "Beef, Salt, Pepper", "None"));
        addMenuItemToMemory(new MenuItem("Grilled Salmon", "Fresh salmon with lemon butter.", 22.50, "Fish", true, "Salmon, Butter, Lemon", "Fish, Dairy"));
        addMenuItemToMemory(new MenuItem("Mushroom Risotto", "Creamy rice with wild mushrooms.", 19.00, "Vegetarian", true, "Rice, Mushrooms, Cream, Parmesan", "Dairy"));
    }

    private void addMenuItemToMemory(MenuItem item) {
        if (item.id == 0) {
            item.id = nextMenuItemId++;
        }
        menuItemsInMemory.add(item);
    }

    private void addReservationToMemory(Reservation reservation) {
        if (reservation.id == 0) {
            reservation.id = nextReservationId++;
        }
        reservationsInMemory.add(reservation);
    }

    public void shutdown() {
        executor.shutdown();
    }
    
    // --- RESTORED: Remote API calls for Reservations ---
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

    public void insertMenuItemLocal(final MenuItemEntity entity, final Runnable onComplete) {
        executor.execute(() -> {
            addMenuItemToMemory(entity.toModel());
            if (onComplete != null) mainHandler.post(onComplete);
        });
    }

    public void getAllMenuLocal(final Consumer<List<MenuItemEntity>> consumer) {
        executor.execute(() -> {
            List<MenuItemEntity> entities = new ArrayList<>();
            for(MenuItem m : new ArrayList<>(menuItemsInMemory)) {
                entities.add(MenuItemEntity.fromModel(m));
            }
            if (consumer != null) mainHandler.post(() -> consumer.accept(entities));
        });
    }

    public void deleteAllMenuItemsLocal(final Runnable onComplete) {
        executor.execute(() -> {
            menuItemsInMemory.clear();
            nextMenuItemId = 1;
            if (onComplete != null) mainHandler.post(onComplete);
        });
    }

    public void deleteMenuItemLocal(int id, final Runnable onComplete) {
        executor.execute(() -> {
            menuItemsInMemory.removeIf(item -> item.id == id);
            reassignMenuItemIds(); 
            if (onComplete != null) mainHandler.post(onComplete);
        });
    }

    private void reassignMenuItemIds() {
        int currentId = 1;
        for (MenuItem item : menuItemsInMemory) {
            item.id = currentId++;
        }
        nextMenuItemId = currentId;
    }

    public void getDistinctCategoriesLocal(final Consumer<List<String>> consumer) {
        executor.execute(() -> {
            List<String> categories = new ArrayList<>();
            for(MenuItem m : menuItemsInMemory) {
                if(m.category != null && !categories.contains(m.category)) {
                    categories.add(m.category);
                }
            }
            Collections.sort(categories);
            if (consumer != null) mainHandler.post(() -> consumer.accept(categories));
        });
    }

    public void insertReservationLocal(final ReservationEntity entity, final Runnable onComplete) {
        executor.execute(() -> {
            addReservationToMemory(entity.toModel());
            if (onComplete != null) mainHandler.post(onComplete);
        });
    }

    public void getAllReservationsLocal(final Consumer<List<ReservationEntity>> consumer) {
        executor.execute(() -> {
            List<ReservationEntity> entities = new ArrayList<>();
            for(Reservation r : new ArrayList<>(reservationsInMemory)) {
                entities.add(new ReservationEntity(r.name, r.partySize, r.dateTime)); 
            }
            if (consumer != null) mainHandler.post(() -> consumer.accept(entities));
        });
    }
}