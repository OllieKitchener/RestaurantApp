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

    public MenuViewModel(@NonNull Application application) {
        super(application);
        // Use the singleton instance of the repository
        repository = RestaurantRepository.getInstance(application);
    }

    public void loadMenuFromDatabase() {
        repository.getAllMenuLocal(entities -> {
            if (entities != null) {
                List<MenuItem> menuItems = new ArrayList<>();
                for (MenuItemEntity entity : entities) {
                    menuItems.add(entity.toModel());
                }
                menuLiveData.postValue(menuItems);
            } else {
                error.postValue("Failed to load menu from repository.");
            }
        });
    }

    public void loadCategoriesFromDatabase() {
        repository.getDistinctCategoriesLocal(categories -> {
            if (categories != null) {
                categoriesLiveData.postValue(categories);
            } else {
                error.postValue("Failed to load categories.");
            }
        });
    }

    public void populateDefaultDishes(Runnable onComplete) {
        repository.insertMenuItemLocal(new MenuItemEntity("Spicy Chicken Wings", "Crispy wings with hot sauce.", 11.00, "Deals", true, "Chicken, Hot Sauce", "None"), null);
        repository.insertMenuItemLocal(new MenuItemEntity("Vegan Buddha Bowl", "Quinoa, roasted veggies, and tahini dressing.", 16.00, "Vegetarian", true, "Quinoa, Mixed Veg, Tahini", "Sesame"), null);
        repository.insertMenuItemLocal(new MenuItemEntity("Kids Pasta", "Simple pasta with tomato sauce.", 8.00, "Deals", true, "Pasta, Tomato Sauce", "Gluten"), onComplete); 
        
        loadMenuFromDatabase();
    }

    public void addMenuItem(MenuItemEntity item, Runnable onComplete) {
        repository.insertMenuItemLocal(item, onComplete);
        loadMenuFromDatabase();
    }

    public void clearAllMenuItems(Runnable onComplete) {
        repository.deleteAllMenuItemsLocal(() -> {
            loadMenuFromDatabase();
            if (onComplete != null) {
                onComplete.run();
            }
        });
    }

    public void deleteMenuItem(MenuItem item, Runnable onComplete) {
        repository.deleteMenuItemLocal(item.id, () -> {
            loadMenuFromDatabase();
            if (onComplete != null) {
                onComplete.run();
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        repository.shutdown();
    }
}