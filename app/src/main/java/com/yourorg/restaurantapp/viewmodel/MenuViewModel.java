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
                    // Populating defaults from ViewModel if in-memory list is empty on app start.
                    // This is for the in-memory demo. With Room, AppDatabase handles initial populate.
                    populateDefaults();
                } else {
                    List<MenuItem> menuItems = new ArrayList<>();
                    for (MenuItemEntity entity : entities) {
                        menuItems.add(entity.toModel());
                    }
                    menuLiveData.postValue(menuItems);
                }
            } else {
                error.postValue("Failed to load menu from database.");
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

    private void populateDefaults() {
        repository.insertMenuItemLocal(new MenuItemEntity("Ribeye Steak", "Juicy steak, served with seasonal vegetables.", 25.99, "Meat", true, "Beef, Salt, Pepper", "None"), null);
        repository.insertMenuItemLocal(new MenuItemEntity("Grilled Salmon", "Fresh Atlantic salmon with lemon butter sauce.", 22.50, "Fish", true, "Salmon, Lemon, Butter", "Fish"), null);
        repository.insertMenuItemLocal(new MenuItemEntity("Spaghetti Carbonara", "Classic Italian pasta with crispy pancetta.", 18.00, "Pasta", true, "Pasta, Pancetta, Eggs, Parmesan", "Gluten, Dairy, Eggs"), null);
        repository.insertMenuItemLocal(new MenuItemEntity("Margherita Pizza", "Simple and delicious cheese pizza, fresh basil.", 15.50, "Pizza", true, "Dough, Tomato, Mozzarella, Basil", "Gluten, Dairy"), null);
        repository.insertMenuItemLocal(new MenuItemEntity("Vegetable Stir-fry", "A mix of fresh, seasonal vegetables with a savory sauce.", 16.50, "Vegetarian", true, "Mixed Veg, Soy Sauce", "Soy, Gluten"), null);
        repository.insertMenuItemLocal(new MenuItemEntity("Chicken Caesar Salad", "Crisp romaine, grilled chicken, parmesan, croutons.", 14.00, "Salad", true, "Lettuce, Chicken, Croutons, Dressing", "Gluten, Dairy, Eggs"), null);
        repository.insertMenuItemLocal(new MenuItemEntity("Chocolate Lava Cake", "Warm chocolate cake with a molten center, vanilla ice cream.", 9.00, "Dessert", true, "Chocolate, Flour, Sugar, Eggs, Cream", "Gluten, Dairy, Eggs"), null);
        repository.insertMenuItemLocal(new MenuItemEntity("Cheesecake", "Rich and creamy New York style cheesecake.", 8.50, "Dessert", true, "Cream Cheese, Sugar, Graham Crackers", "Dairy, Gluten"), this::loadMenuFromDatabase);
        
        repository.insertMenuItemLocal(new MenuItemEntity("Classic Burger", "Beef patty, lettuce, tomato, onion, pickles.", 12.00, "Meat", true, "Beef, Bun, Salad", "Gluten"), null);
        repository.insertMenuItemLocal(new MenuItemEntity("Tuna Steak", "Seared tuna with a sesame crust, wasabi mayo.", 24.00, "Fish", true, "Tuna, Sesame, Mayo", "Fish, Sesame, Eggs"), null);
        repository.insertMenuItemLocal(new MenuItemEntity("Penne Arrabbiata", "Spicy tomato sauce with garlic and chili.", 17.00, "Pasta", true, "Pasta, Tomato, Chili", "Gluten"), null);
        repository.insertMenuItemLocal(new MenuItemEntity("Greek Salad", "Cucumbers, tomatoes, olives, feta cheese.", 13.50, "Salad", true, "Cucumber, Tomato, Feta, Olives", "Dairy"), null);
        repository.insertMenuItemLocal(new MenuItemEntity("Tiramisu", "Coffee-soaked ladyfingers, mascarpone, cocoa.", 9.50, "Dessert", true, "Coffee, Ladyfingers, Mascarpone", "Gluten, Dairy, Eggs"), null);
    }

    // Public method for staff to trigger population (also using correct constructors)
    public void populateDefaultDishes(Runnable onComplete) {
        repository.insertMenuItemLocal(new MenuItemEntity("Daily Special Burger", "Our chef's special burger creation. Limited time!", 12.50, "Deals", true, "Beef, Special Sauce, Bun", "Gluten, Soy"), null);
        repository.insertMenuItemLocal(new MenuItemEntity("Pasta Primavera (Vegan)", "Seasonal vegetables with light pasta, recommended by our chef.", 17.00, "Recommended", true, "Pasta, Zucchini, Bell Peppers, Cherry Tomatoes, Olive Oil", "Gluten"), null);
        repository.insertMenuItemLocal(new MenuItemEntity("Kids Eat Free (Under 12)", "One free kids meal with every adult entree purchase.", 0.00, "Deals", true, "Chicken Nuggets, Fries", "Gluten, Chicken"), null);
        repository.insertMenuItemLocal(new MenuItemEntity("Chef's Tasting Menu", "A curated selection of our finest dishes.", 45.00, "Recommended", true, "Chef's daily selection of ingredients", "Varies, inquire with staff"), onComplete); // Last insert, triggers provided callback

        loadMenuFromDatabase();
    }

    public void addMenuItem(MenuItemEntity item, Runnable onComplete) {
        repository.insertMenuItemLocal(item, onComplete);
    }

    public void clearAllMenuItems(Runnable onComplete) {
        repository.deleteAllMenuItemsLocal(onComplete);
    }

    // Modified to take a callback and refresh menu
    public void deleteMenuItem(MenuItem item, Runnable onComplete) {
        repository.deleteMenuItemLocal(item.id, () -> {
            loadMenuFromDatabase(); // Refresh menu after deletion
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