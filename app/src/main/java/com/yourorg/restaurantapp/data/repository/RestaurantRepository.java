package com.yourorg.restaurantapp.data.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.yourorg.restaurantapp.api.ApiClient;
import com.yourorg.restaurantapp.api.RestaurantApi;
import com.yourorg.restaurantapp.data.local.AppDatabase;
import com.yourorg.restaurantapp.data.local.entities.MenuItemEntity;
import com.yourorg.restaurantapp.data.local.entities.ReservationEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RestaurantRepository {
    private final RestaurantApi api;
    private final AppDatabase db;
    private final ExecutorService executor = Executors.newFixedThreadPool(3);
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public RestaurantRepository(Context context, String baseUrl) {
        api = ApiClient.getClient(baseUrl).create(RestaurantApi.class);
        db = AppDatabase.getInstance(context);
    }

    public void shutdown() {
        executor.shutdown();
    }

    // --- Local Menu Item Operations (Restored) ---

    public void insertMenuItemLocal(final MenuItemEntity entity, final Runnable onComplete) {
        executor.execute(() -> {
            db.menuItemDao().insert(entity);
            if (onComplete != null) mainHandler.post(onComplete);
        });
    }

    public void getAllMenuLocal(final java.util.function.Consumer<List<MenuItemEntity>> consumer) {
        executor.execute(() -> {
            List<MenuItemEntity> items = db.menuItemDao().getAll();
            mainHandler.post(() -> consumer.accept(items));
        });
    }

    public void getDistinctCategoriesLocal(final java.util.function.Consumer<List<String>> consumer) {
        executor.execute(() -> {
            List<String> categories = db.menuItemDao().getDistinctCategories();
            mainHandler.post(() -> consumer.accept(categories));
        });
    }

    // --- Local Reservation Operations ---

    public void insertReservationLocal(final ReservationEntity entity, final Runnable onComplete) {
        executor.execute(() -> {
            db.reservationDao().insert(entity);
            if (onComplete != null) mainHandler.post(onComplete);
        });
    }

    public void getAllReservationsLocal(final java.util.function.Consumer<List<ReservationEntity>> consumer) {
        executor.execute(() -> {
            List<ReservationEntity> items = db.reservationDao().getAll();
            mainHandler.post(() -> consumer.accept(items));
        });
    }
}
