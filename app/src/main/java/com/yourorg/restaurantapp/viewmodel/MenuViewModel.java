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

    // Private helper to populate 3 dishes per category
    private void populateDefaults() {
        // --- Recommended ---
        repository.insertMenuItemLocal(new MenuItemEntity("Chef's Special Steak", "Premium cut with secret sauce.", 35.00, "Recommended", true, "Beef, Secret Sauce, Potatoes", "None"), null);
        repository.insertMenuItemLocal(new MenuItemEntity("Truffle Pasta", "Creamy pasta with black truffle.", 28.00, "Recommended", true, "Pasta, Cream, Truffle, Parmesan", "Gluten, Dairy"), null);
        repository.insertMenuItemLocal(new MenuItemEntity("Lobster Bisque", "Rich and creamy lobster soup.", 18.00, "Recommended", true, "Lobster, Cream, Brandy, Stock", "Shellfish, Dairy"), null);

        // --- Deals ---
        repository.insertMenuItemLocal(new MenuItemEntity("Burger & Fries", "Classic burger with a side of fries.", 15.00, "Deals", true, "Beef, Bun, Lettuce, Potato", "Gluten"), null);
        repository.insertMenuItemLocal(new MenuItemEntity("Pizza Combo", "Two slices of pizza and a drink.", 12.00, "Deals", true, "Dough, Cheese, Tomato Sauce", "Gluten, Dairy"), null);
        repository.insertMenuItemLocal(new MenuItemEntity("Lunch Special", "Soup of the day and half sandwich.", 10.00, "Deals", true, "Varies", "Varies"), null);

        // --- Meat ---
        repository.insertMenuItemLocal(new MenuItemEntity("Ribeye Steak", "Juicy ribeye cooked to perfection.", 29.99, "Meat", true, "Beef, Salt, Pepper", "None"), null);
        repository.insertMenuItemLocal(new MenuItemEntity("Lamb Chops", "Grilled lamb chops with mint jelly.", 32.00, "Meat", true, "Lamb, Mint, Herbs", "None"), null);
        repository.insertMenuItemLocal(new MenuItemEntity("BBQ Ribs", "Slow-cooked pork ribs with BBQ sauce.", 24.50, "Meat", true, "Pork, BBQ Sauce", "None"), null);

        // --- Fish ---
        repository.insertMenuItemLocal(new MenuItemEntity("Grilled Salmon", "Fresh salmon with lemon butter.", 22.50, "Fish", true, "Salmon, Butter, Lemon", "Fish, Dairy"), null);
        repository.insertMenuItemLocal(new MenuItemEntity("Fish & Chips", "Battered cod with chunky chips.", 18.00, "Fish", true, "Cod, Flour, Beer, Potato", "Fish, Gluten"), null);
        repository.insertMenuItemLocal(new MenuItemEntity("Seared Tuna", "Sesame crusted tuna steak.", 26.00, "Fish", true, "Tuna, Sesame Seeds, Soy Sauce", "Fish, Sesame, Soy"), null);

        // --- Vegetarian ---
        repository.insertMenuItemLocal(new MenuItemEntity("Vegetable Stir-fry", "Fresh seasonal vegetables in soy sauce.", 16.00, "Vegetarian", true, "Broccoli, Peppers, Carrots, Soy Sauce", "Soy"), null);
        repository.insertMenuItemLocal(new MenuItemEntity("Mushroom Risotto", "Creamy rice with wild mushrooms.", 19.00, "Vegetarian", true, "Rice, Mushrooms, Cream, Parmesan", "Dairy"), null);
        repository.insertMenuItemLocal(new MenuItemEntity("Spinach Lasagna", "Layers of pasta, spinach, and ricotta.", 17.50, "Vegetarian", true, "Pasta, Spinach, Ricotta, Tomato Sauce", "Gluten, Dairy"), this::loadMenuFromDatabase);
    }

    public void populateDefaultDishes(Runnable onComplete) {
        populateDefaults();
        if (onComplete != null) onComplete.run();
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