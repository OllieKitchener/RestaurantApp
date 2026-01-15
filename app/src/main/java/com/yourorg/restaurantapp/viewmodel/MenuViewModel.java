package com.yourorg.restaurantapp.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.yourorg.restaurantapp.data.repository.RestaurantRepository;
import com.yourorg.restaurantapp.data.local.entities.MenuItemEntity;
import com.yourorg.restaurantapp.model.MenuItem;
import java.util.ArrayList;
import java.util.List;

public class MenuViewModel extends AndroidViewModel {
    private final RestaurantRepository repository;
    public final MutableLiveData<List<MenuItem>> menuLiveData = new MutableLiveData<>();
    public final MutableLiveData<List<String>> categoriesLiveData = new MutableLiveData<>();
    public final MutableLiveData<String> error = new MutableLiveData<>();
    private boolean isCleared = false;

    public MenuViewModel(@NonNull Application application) {
        super(application);
        repository = RestaurantRepository.getInstance(application.getApplicationContext());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        isCleared = true;
    }

    public void loadMenuFromDatabase() {
        repository.getAllMenuLocal(entities -> {
            if (isCleared) return;
            List<MenuItem> menuItems = new ArrayList<>();
            if (entities != null) {
                for (MenuItemEntity entity : entities) menuItems.add(entity.toModel());
            }
            menuLiveData.postValue(menuItems);
        });
    }

    public void loadCategoriesFromDatabase() {
        repository.getDistinctCategoriesLocal(categories -> {
            if (isCleared) return;
            if (categories != null) {
                categoriesLiveData.postValue(categories);
            }
        });
    }

    // RESTORED: This method was accidentally removed.
    public void populateDefaultDishes(Runnable onComplete) {
        repository.insertMenuItemLocal(new MenuItemEntity("Sample Dish 1", "Desc 1", 10.99, "Deals", true, "", ""), null);
        repository.insertMenuItemLocal(new MenuItemEntity("Sample Dish 2", "Desc 2", 12.99, "Meat", true, "", ""), () -> {
            if (isCleared) return;
            loadMenuFromDatabase(); // Refresh list after adding
            if (onComplete != null) onComplete.run();
        });
    }
    
    public void deleteMenuItem(MenuItem item, Runnable onComplete) {
        repository.deleteMenuItemLocal(item.id, () -> {
            if (isCleared) return;
            loadMenuFromDatabase();
            if (onComplete != null) onComplete.run();
        });
    }

    public void addMenuItem(MenuItemEntity item, Runnable onComplete) {
        repository.insertMenuItemLocal(item, () -> {
            if (isCleared) return;
            loadMenuFromDatabase(); 
            if (onComplete != null) onComplete.run();
        });
    }

    public void clearAllMenuItems(Runnable onComplete) {
        repository.deleteAllMenuItemsLocal(() -> {
            if (isCleared) return;
            loadMenuFromDatabase();
            if (onComplete != null) onComplete.run();
        });
    }
}