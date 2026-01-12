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
        // Using placeholder URL as we are now fully in-memory
        repository = new RestaurantRepository(application, "https://localhost/");
    }

    public void loadMenuFromDatabase() {
        repository.getAllMenuLocal(entities -> {
            if (entities != null) {
                if (entities.isEmpty()) {
                    populateDefaults();
                } else {
                    List<MenuItem> menuItems = new ArrayList<>();
                    for (MenuItemEntity entity : entities) {
                        menuItems.add(entity.toModel());
                    }
                    menuLiveData.postValue(menuItems);
                }
            } else {
                error.postValue("Failed to load menu.");
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

    // Private helper for initial population if empty
    private void populateDefaults() {
        repository.insertMenuItemLocal(new MenuItemEntity("Ribeye Steak", "Juicy steak, served with seasonal vegetables.", 25.99, "Meat", true, "Beef, Salt, Pepper", "None"), null);
        repository.insertMenuItemLocal(new MenuItemEntity("Grilled Salmon", "Fresh Atlantic salmon with lemon butter sauce.", 22.50, "Fish", true, "Salmon, Lemon, Butter", "Fish"), null);
        repository.insertMenuItemLocal(new MenuItemEntity("Margherita Pizza", "Simple and delicious cheese pizza, fresh basil.", 15.50, "Pizza", true, "Dough, Tomato, Mozzarella, Basil", "Gluten, Dairy"), null);
        repository.insertMenuItemLocal(new MenuItemEntity("Vegetable Stir-fry", "A mix of fresh, seasonal vegetables with a savory sauce.", 16.50, "Vegetarian", true, "Mixed Vegetables, Soy Sauce", "Soy, Gluten"), null);
        repository.insertMenuItemLocal(new MenuItemEntity("Chocolate Lava Cake", "Warm chocolate cake with a molten center, vanilla ice cream.", 9.00, "Dessert", true, "Chocolate, Flour, Eggs, Cream", "Gluten, Dairy, Eggs"), this::loadMenuFromDatabase);
    }

    // Public method for staff to trigger population
    public void populateDefaultDishes(Runnable onComplete) {
        repository.insertMenuItemLocal(new MenuItemEntity("Classic Burger", "Beef patty, lettuce, tomato, onion, pickles.", 12.00, "Meat", true, "Beef, Bun, Lettuce, Tomato, Pickles, Onion", "Gluten"), null);
        repository.insertMenuItemLocal(new MenuItemEntity("Tuna Steak", "Seared tuna with a sesame crust, wasabi mayo.", 24.00, "Fish", true, "Tuna, Sesame, Wasabi Mayo", "Fish, Sesame"), null);
        repository.insertMenuItemLocal(new MenuItemEntity("Penne Arrabbiata", "Spicy tomato sauce with garlic and chili.", 17.00, "Pasta", true, "Penne Pasta, Tomato Sauce, Garlic, Chili", "Gluten"), null);
        repository.insertMenuItemLocal(new MenuItemEntity("Greek Salad", "Cucumbers, tomatoes, olives, feta cheese.", 13.50, "Salad", true, "Cucumber, Tomato, Olives, Feta", "Dairy"), null);
        repository.insertMenuItemLocal(new MenuItemEntity("Tiramisu", "Coffee-soaked ladyfingers, mascarpone, cocoa.", 9.50, "Dessert", true, "Coffee, Ladyfingers, Mascarpone, Cocoa", "Gluten, Dairy, Eggs"), onComplete);
        
        loadMenuFromDatabase();
    }

    public void addMenuItem(MenuItemEntity item, Runnable onComplete) {
        repository.insertMenuItemLocal(item, onComplete);
    }

    public void clearAllMenuItems(Runnable onComplete) {
        repository.deleteAllMenuItemsLocal(onComplete);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        repository.shutdown();
    }
}