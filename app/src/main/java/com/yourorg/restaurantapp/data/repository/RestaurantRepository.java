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

    // Private constructor for singleton
    private RestaurantRepository(Context context) {
        api = ApiClient.getClient("https://localhost/").create(RestaurantApi.class);
        if (menuItemsInMemory.isEmpty()) {
            initInMemoryMenuData();
        }
    }

    // Public method to get the singleton instance
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
        // --- Recommended ---
        addMenuItemToMemory(new MenuItem("Chef's Special Steak", "Premium cut with secret sauce.", 35.00, "Recommended", true, "Beef, Secret Sauce, Potatoes", "None"));
        addMenuItemToMemory(new MenuItem("Truffle Pasta", "Creamy pasta with black truffle.", 28.00, "Recommended", true, "Pasta, Cream, Truffle, Parmesan", "Gluten, Dairy"));
        addMenuItemToMemory(new MenuItem("Lobster Bisque", "Rich and creamy lobster soup.", 18.00, "Recommended", true, "Lobster, Cream, Brandy, Stock", "Shellfish, Dairy"));

        // --- Deals ---
        addMenuItemToMemory(new MenuItem("Burger & Fries", "Classic burger with a side of fries.", 15.00, "Deals", true, "Beef, Bun, Lettuce, Potato", "Gluten"));
        addMenuItemToMemory(new MenuItem("Pizza Combo", "Two slices of pizza and a drink.", 12.00, "Deals", true, "Dough, Cheese, Tomato Sauce", "Gluten, Dairy"));
        addMenuItemToMemory(new MenuItem("Lunch Special", "Soup of the day and half sandwich.", 10.00, "Deals", true, "Varies", "Varies"));

        // --- Meat ---
        addMenuItemToMemory(new MenuItem("Ribeye Steak", "Juicy ribeye cooked to perfection.", 29.99, "Meat", true, "Beef, Salt, Pepper", "None"));
        addMenuItemToMemory(new MenuItem("Lamb Chops", "Grilled lamb chops with mint jelly.", 32.00, "Meat", true, "Lamb, Mint, Herbs", "None"));
        addMenuItemToMemory(new MenuItem("BBQ Ribs", "Slow-cooked pork ribs with BBQ sauce.", 24.50, "Meat", true, "Pork, BBQ Sauce", "None"));

        // --- Fish ---
        addMenuItemToMemory(new MenuItem("Grilled Salmon", "Fresh salmon with lemon butter.", 22.50, "Fish", true, "Salmon, Butter, Lemon", "Fish, Dairy"));
        addMenuItemToMemory(new MenuItem("Fish & Chips", "Battered cod with chunky chips.", 18.00, "Fish", true, "Cod, Flour, Beer, Potato", "Fish, Gluten"));
        addMenuItemToMemory(new MenuItem("Seared Tuna", "Sesame crusted tuna steak.", 26.00, "Fish", true, "Tuna, Sesame Seeds, Soy Sauce", "Fish, Sesame, Soy"));

        // --- Vegetarian ---
        addMenuItemToMemory(new MenuItem("Vegetable Stir-fry", "Fresh seasonal vegetables in soy sauce.", 16.00, "Vegetarian", true, "Broccoli, Peppers, Carrots, Soy Sauce", "Soy"));
        addMenuItemToMemory(new MenuItem("Mushroom Risotto", "Creamy rice with wild mushrooms.", 19.00, "Vegetarian", true, "Rice, Mushrooms, Cream, Parmesan", "Dairy"));
        addMenuItemToMemory(new MenuItem("Spinach Lasagna", "Layers of pasta, spinach, and ricotta.", 17.50, "Vegetarian", true, "Pasta, Spinach, Ricotta, Tomato Sauce", "Gluten, Dairy"));
        
        // --- Dessert ---
        addMenuItemToMemory(new MenuItem("Chocolate Lava Cake", "Warm chocolate cake with a molten center.", 9.00, "Dessert", true, "Chocolate, Flour, Eggs, Sugar", "Gluten, Dairy, Eggs"));
        addMenuItemToMemory(new MenuItem("Cheesecake", "Rich and creamy New York style cheesecake.", 8.50, "Dessert", true, "Cream Cheese, Graham Crackers", "Dairy, Gluten"));
        addMenuItemToMemory(new MenuItem("Tiramisu", "Coffee-soaked ladyfingers, mascarpone, cocoa.", 9.50, "Dessert", true, "Coffee, Ladyfingers, Mascarpone", "Gluten, Dairy, Eggs"));
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

    // --- Remote API calls (Kept for compile-time safety) ---
    public void fetchMenuFromServer(Callback<List<MenuItem>> callback) { api.getMenu().enqueue(callback); }
    public void createMenuItemRemote(MenuItem item, Callback<MenuItem> callback) { api.createMenuItem(item).enqueue(callback); }
    public void updateMenuItemRemote(int id, MenuItem item, Callback<MenuItem> callback) { api.updateMenuItem(id, item).enqueue(callback); }
    public void deleteMenuItemRemote(int id, Callback<Void> callback) { api.deleteMenuItem(id).enqueue(callback); }
    public void fetchReservationsRemote(Callback<List<Reservation>> callback) { api.getReservations().enqueue(callback); }
    public void createReservationRemote(Reservation r, Callback<Reservation> callback) { api.createReservation(r).enqueue(callback); }
    public void updateReservationRemote(int id, Reservation r, Callback<Reservation> callback) { api.updateReservation(id, r).enqueue(callback); }
    public void deleteReservationRemote(int id, Callback<Void> callback) { api.deleteReservation(id).enqueue(callback); }

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
            synchronized (menuItemsInMemory) {
                for(MenuItem m : menuItemsInMemory) {
                    entities.add(MenuItemEntity.fromModel(m));
                }
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
            synchronized (menuItemsInMemory) {
                menuItemsInMemory.removeIf(item -> item.id == id);
            }
            if (onComplete != null) mainHandler.post(onComplete);
        });
    }

    public void getDistinctCategoriesLocal(final Consumer<List<String>> consumer) {
        executor.execute(() -> {
            List<String> categories = new ArrayList<>();
            synchronized (menuItemsInMemory) {
                for(MenuItem m : menuItemsInMemory) {
                    if(m.category != null && !categories.contains(m.category)) {
                        categories.add(m.category);
                    }
                }
            }
            Collections.sort(categories, (s1, s2) -> {
                if (s1.equals("Recommended")) return -1;
                if (s2.equals("Recommended")) return 1;
                if (s1.equals("Deals")) return -1;
                if (s2.equals("Deals")) return 1;
                return s1.compareTo(s2);
            });
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
            synchronized (reservationsInMemory) {
                for(Reservation r : reservationsInMemory) {
                    entities.add(new ReservationEntity(r.name, r.partySize, r.dateTime)); 
                }
            }
            if (consumer != null) mainHandler.post(() -> consumer.accept(entities));
        });
    }
}