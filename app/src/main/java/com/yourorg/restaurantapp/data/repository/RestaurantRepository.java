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
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import retrofit2.Call;
import retrofit2.Callback;

public class RestaurantRepository {
    private static volatile RestaurantRepository INSTANCE;
    private final RestaurantApi api;
    private final List<MenuItem> menuItemsInMemory = Collections.synchronizedList(new ArrayList<>());
    private final List<Reservation> reservationsInMemory = Collections.synchronizedList(new ArrayList<>());
    private int nextMenuItemId = 1;
    private int nextReservationId = 1;
    private final ExecutorService executor = Executors.newFixedThreadPool(3);
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private RestaurantRepository(Context context) {
        api = ApiClient.getClient("https://localhost/").create(RestaurantApi.class);
        initInMemoryMenuData();
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

    // CRITICAL FIX: This now ONLY resets the menu, leaving reservations intact.
    public void resetMenuData() {
        executor.execute(() -> {
            synchronized (menuItemsInMemory) {
                menuItemsInMemory.clear();
                nextMenuItemId = 1;
                initInMemoryMenuData();
            }
        });
    }

    private void initInMemoryMenuData() {
        if (menuItemsInMemory.isEmpty()) {
            addMenuItemToMemory(new MenuItem("Chef's Special Steak", "Premium cut.", 35.00, "Recommended", true, "Beef", "None"));
            addMenuItemToMemory(new MenuItem("Truffle Pasta", "Creamy pasta.", 28.00, "Recommended", true, "Pasta", "Gluten"));
            addMenuItemToMemory(new MenuItem("Burger & Fries", "Classic combo.", 15.00, "Deals", true, "Beef, Bun", "Gluten"));
            addMenuItemToMemory(new MenuItem("Ribeye Steak", "Juicy ribeye.", 29.99, "Meat", true, "Beef", "None"));
            addMenuItemToMemory(new MenuItem("Grilled Salmon", "Fresh salmon.", 22.50, "Fish", true, "Salmon", "Fish"));
            addMenuItemToMemory(new MenuItem("Mushroom Risotto", "Creamy rice.", 19.00, "Vegetarian", true, "Rice", "Dairy"));
        }
    }

    private void addMenuItemToMemory(MenuItem item) {
        if (item.id == 0) item.id = nextMenuItemId++;
        menuItemsInMemory.add(item);
    }
    
    private void addReservationToMemory(Reservation reservation) {
        if (reservation.id == 0) reservation.id = nextReservationId++;
        reservationsInMemory.add(reservation);
    }

    public void shutdown() { executor.shutdown(); }

    public void createReservationRemote(Reservation r, Callback<Reservation> cb) { api.createReservation(r).enqueue(cb); }
    // Other remote methods...

    public void insertMenuItemLocal(MenuItemEntity entity, Runnable onComplete) {
        executor.execute(() -> {
            addMenuItemToMemory(entity.toModel());
            if (onComplete != null) mainHandler.post(onComplete);
        });
    }

    public void getAllMenuLocal(Consumer<List<MenuItemEntity>> consumer) {
        executor.execute(() -> {
            List<MenuItemEntity> entities = new ArrayList<>();
            synchronized (menuItemsInMemory) {
                for (MenuItem m : menuItemsInMemory) entities.add(MenuItemEntity.fromModel(m));
            }
            mainHandler.post(() -> consumer.accept(entities));
        });
    }

    public void deleteAllMenuItemsLocal(Runnable onComplete) {
        executor.execute(() -> {
            synchronized (menuItemsInMemory) {
                menuItemsInMemory.clear();
                nextMenuItemId = 1;
            }
            if (onComplete != null) mainHandler.post(onComplete);
        });
    }

    public void deleteMenuItemLocal(int id, Runnable onComplete) {
        executor.execute(() -> {
            synchronized (menuItemsInMemory) {
                menuItemsInMemory.removeIf(item -> item.id == id);
            }
            if (onComplete != null) mainHandler.post(onComplete);
        });
    }

    public void getDistinctCategoriesLocal(Consumer<List<String>> consumer) {
        executor.execute(() -> {
            List<String> categories = new ArrayList<>();
            synchronized (menuItemsInMemory) {
                for(MenuItem m : menuItemsInMemory) {
                    if(m.category != null && !categories.contains(m.category)) categories.add(m.category);
                }
            }
            Collections.sort(categories, (s1, s2) -> {
                if ("Recommended".equals(s1)) return -1; if ("Recommended".equals(s2)) return 1;
                if ("Deals".equals(s1)) return -1; if ("Deals".equals(s2)) return 1;
                return s1.compareTo(s2);
            });
            mainHandler.post(() -> consumer.accept(categories));
        });
    }

    public void insertReservationLocal(ReservationEntity entity, Runnable onComplete) {
        executor.execute(() -> {
            addReservationToMemory(entity.toModel());
            if (onComplete != null) mainHandler.post(onComplete);
        });
    }

    public void getAllReservationsLocal(Consumer<List<ReservationEntity>> consumer) {
        executor.execute(() -> {
            List<ReservationEntity> entities = new ArrayList<>();
            synchronized (reservationsInMemory) {
                for (Reservation r : reservationsInMemory) entities.add(new ReservationEntity(r.name, r.partySize, r.dateTime));
            }
            mainHandler.post(() -> consumer.accept(entities));
        });
    }
}