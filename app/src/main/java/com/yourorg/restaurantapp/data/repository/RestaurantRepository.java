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
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        // Initialize with default data so the menu is never empty on start
        initInMemoryData();
    }

    private void initInMemoryData() {
        // --- Recommended ---
        menuItemsInMemory.add(new MenuItem(1, "Chef's Special Steak", "Premium cut with secret sauce.", 35.00, "Recommended", true, "Beef, Secret Sauce, Potatoes", "None"));
        menuItemsInMemory.add(new MenuItem(2, "Truffle Pasta", "Creamy pasta with black truffle.", 28.00, "Recommended", true, "Pasta, Cream, Truffle, Parmesan", "Gluten, Dairy"));
        menuItemsInMemory.add(new MenuItem(3, "Lobster Bisque", "Rich and creamy lobster soup.", 18.00, "Recommended", true, "Lobster, Cream, Brandy, Stock", "Shellfish, Dairy"));

        // --- Deals ---
        menuItemsInMemory.add(new MenuItem(4, "Burger & Fries", "Classic burger with a side of fries.", 15.00, "Deals", true, "Beef, Bun, Lettuce, Potato", "Gluten"));
        menuItemsInMemory.add(new MenuItem(5, "Pizza Combo", "Two slices of pizza and a drink.", 12.00, "Deals", true, "Dough, Cheese, Tomato Sauce", "Gluten, Dairy"));
        menuItemsInMemory.add(new MenuItem(6, "Lunch Special", "Soup of the day and half sandwich.", 10.00, "Deals", true, "Varies", "Varies"));

        // --- Meat ---
        menuItemsInMemory.add(new MenuItem(7, "Ribeye Steak", "Juicy ribeye cooked to perfection.", 29.99, "Meat", true, "Beef, Salt, Pepper", "None"));
        menuItemsInMemory.add(new MenuItem(8, "Lamb Chops", "Grilled lamb chops with mint jelly.", 32.00, "Meat", true, "Lamb, Mint, Herbs", "None"));
        menuItemsInMemory.add(new MenuItem(9, "BBQ Ribs", "Slow-cooked pork ribs with BBQ sauce.", 24.50, "Meat", true, "Pork, BBQ Sauce", "None"));

        // --- Fish ---
        menuItemsInMemory.add(new MenuItem(10, "Grilled Salmon", "Fresh salmon with lemon butter.", 22.50, "Fish", true, "Salmon, Butter, Lemon", "Fish, Dairy"));
        menuItemsInMemory.add(new MenuItem(11, "Fish & Chips", "Battered cod with chunky chips.", 18.00, "Fish", true, "Cod, Flour, Beer, Potato", "Fish, Gluten"));
        menuItemsInMemory.add(new MenuItem(12, "Seared Tuna", "Sesame crusted tuna steak.", 26.00, "Fish", true, "Tuna, Sesame Seeds, Soy Sauce", "Fish, Sesame, Soy"));

        // --- Vegetarian ---
        menuItemsInMemory.add(new MenuItem(13, "Vegetable Stir-fry", "Fresh seasonal vegetables in soy sauce.", 16.00, "Vegetarian", true, "Broccoli, Peppers, Carrots, Soy Sauce", "Soy"));
        menuItemsInMemory.add(new MenuItem(14, "Mushroom Risotto", "Creamy rice with wild mushrooms.", 19.00, "Vegetarian", true, "Rice, Mushrooms, Cream, Parmesan", "Dairy"));
        menuItemsInMemory.add(new MenuItem(15, "Spinach Lasagna", "Layers of pasta, spinach, and ricotta.", 17.50, "Vegetarian", true, "Pasta, Spinach, Ricotta, Tomato Sauce", "Gluten, Dairy"));
        
        // --- Dessert ---
        menuItemsInMemory.add(new MenuItem(16, "Chocolate Lava Cake", "Warm chocolate cake with a molten center.", 9.00, "Dessert", true, "Chocolate, Flour, Eggs, Sugar", "Gluten, Dairy, Eggs"));
        menuItemsInMemory.add(new MenuItem(17, "Cheesecake", "Rich and creamy New York style cheesecake.", 8.50, "Dessert", true, "Cream Cheese, Graham Crackers", "Dairy, Gluten"));
        menuItemsInMemory.add(new MenuItem(18, "Tiramisu", "Coffee-soaked ladyfingers, mascarpone, cocoa.", 9.50, "Dessert", true, "Coffee, Ladyfingers, Mascarpone", "Gluten, Dairy, Eggs"));
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
            MenuItem item = entity.toModel();
            item.id = menuItemsInMemory.size() + 1;
            menuItemsInMemory.add(item);
            if (onComplete != null) mainHandler.post(onComplete);
        });
    }
    
    public void insertMenuItemLocal(final MenuItem item, final Runnable onComplete) {
        executor.execute(() -> {
            item.id = menuItemsInMemory.size() + 1;
            menuItemsInMemory.add(item);
            if (onComplete != null) mainHandler.post(onComplete);
        });
    }

    public void getAllMenuLocal(final java.util.function.Consumer<List<MenuItemEntity>> consumer) {
        executor.execute(() -> {
            List<MenuItemEntity> entities = new ArrayList<>();
            for(MenuItem m : menuItemsInMemory) {
                entities.add(MenuItemEntity.fromModel(m));
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

    public void deleteMenuItemLocal(int id, final Runnable onComplete) {
        executor.execute(() -> {
            menuItemsInMemory.removeIf(item -> item.id == id);
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
            java.util.Collections.sort(categories);
            if (consumer != null) mainHandler.post(() -> consumer.accept(categories));
        });
    }

    // 2. Reservations
    public void insertReservationLocal(final ReservationEntity entity, final Runnable onComplete) {
        executor.execute(() -> {
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
                entities.add(new ReservationEntity(r.name, r.partySize, r.dateTime)); 
            }
            if (consumer != null) mainHandler.post(() -> consumer.accept(entities));
        });
    }
}