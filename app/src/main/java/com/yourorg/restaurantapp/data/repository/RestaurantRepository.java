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
            // Recommended
            addMenuItemToMemory(new MenuItem("Chef's Special Steak", "Premium cut, served with roasted vegetables.", 35.00, "Recommended", true, "Beef, Vegetables", "None"));
            addMenuItemToMemory(new MenuItem("Truffle Pasta", "Fettuccine with a rich truffle cream sauce.", 28.00, "Recommended", true, "Pasta, Cream, Truffle Oil", "Gluten, Dairy"));
            addMenuItemToMemory(new MenuItem("Avocado Toast", "Smashed avocado on toasted artisan bread.", 12.00, "Recommended", true, "Avocado, Bread, Chili Flakes", "Gluten"));
            addMenuItemToMemory(new MenuItem("Burger & Fries Combo", "Classic beef burger with crispy fries.", 15.00, "Deals", true, "Beef, Bun, Potatoes", "Gluten"));
            addMenuItemToMemory(new MenuItem("Family Pizza Deal", "Two large pizzas for the price of one.", 30.00, "Deals", true, "Pizza Dough, Cheese, Toppings", "Gluten, Dairy"));
            addMenuItemToMemory(new MenuItem("Appetizer Sampler", "A selection of our finest starters.", 20.00, "Deals", true, "Assorted Appetizers", "Varies"));
            addMenuItemToMemory(new MenuItem("Ribeye Steak", "Juicy, perfectly grilled ribeye.", 30.00, "Meat", true, "Beef", "None"));
            addMenuItemToMemory(new MenuItem("Pork Belly Bites", "Crispy pork belly with a sweet glaze.", 24.00, "Meat", true, "Pork Belly, Glaze", "None"));
            addMenuItemToMemory(new MenuItem("Lamb Chops", "Tender grilled lamb chops.", 32.00, "Meat", true, "Lamb Chops", "None"));
            addMenuItemToMemory(new MenuItem("Chicken Skewers", "Marinated grilled chicken skewers.", 18.50, "Meat", true, "Chicken, Marinade", "None"));
            addMenuItemToMemory(new MenuItem("BBQ Ribs", "Slow-cooked pork ribs with BBQ sauce.", 27.00, "Meat", true, "Pork Ribs, BBQ Sauce", "None"));
            addMenuItemToMemory(new MenuItem("Beef Wellington", "Tender beef fillet in puff pastry.", 45.00, "Meat", true, "Beef Fillet, Puff Pastry, Mushrooms", "Gluten, Dairy"));
            addMenuItemToMemory(new MenuItem("Grilled Salmon", "Flaky salmon fillet with lemon butter sauce.", 22.50, "Fish", true, "Salmon, Lemon, Butter", "Fish, Dairy"));
            addMenuItemToMemory(new MenuItem("Shrimp Scampi", "Shrimp sautéed in garlic butter sauce with linguine.", 30.00, "Seafood", true, "Shrimp, Garlic, Butter, Linguine", "Shellfish, Gluten, Dairy"));
            addMenuItemToMemory(new MenuItem("Fish and Chips", "Battered cod with thick-cut fries.", 19.50, "Fish", true, "Cod, Flour, Potatoes", "Gluten, Fish"));
            addMenuItemToMemory(new MenuItem("Seared Tuna Steak", "Sesame-crusted tuna, seared rare.", 27.00, "Fish", true, "Tuna, Sesame Seeds", "Sesame"));
            addMenuItemToMemory(new MenuItem("Lobster Bisque", "Rich and creamy lobster soup.", 18.00, "Seafood", true, "Lobster, Cream, Cognac", "Shellfish, Dairy"));
            addMenuItemToMemory(new MenuItem("Mushroom Risotto", "Creamy Arborio rice with wild mushrooms.", 19.00, "Vegetarian", true, "Rice, Mushrooms, Parmesan", "Dairy"));
            addMenuItemToMemory(new MenuItem("Vegetable Stir-fry", "Mixed seasonal vegetables with soy ginger sauce.", 17.00, "Vegetarian", true, "Mixed Vegetables, Soy Sauce, Ginger", "Soy"));
            addMenuItemToMemory(new MenuItem("Caprese Salad", "Fresh mozzarella, tomatoes, basil, balsamic glaze.", 14.00, "Salad", true, "Mozzarella, Tomatoes, Basil", "Dairy"));
            addMenuItemToMemory(new MenuItem("Spinach and Artichoke Dip", "Creamy dip with tortilla chips.", 13.50, "Appetizer", true, "Spinach, Artichoke Hearts, Cream Cheese", "Dairy"));
            addMenuItemToMemory(new MenuItem("Lentil Soup", "Hearty and nutritious lentil soup.", 8.00, "Vegetarian", true, "Lentils, Vegetables", "None"));
            addMenuItemToMemory(new MenuItem("Quinoa Salad", "Fluffy quinoa with chopped vegetables.", 15.00, "Salad", true, "Quinoa, Mixed Vegetables, Lemon Vinaigrette", "None"));
            addMenuItemToMemory(new MenuItem("Eggplant Parmesan", "Breaded eggplant layered with marinara and cheese.", 18.00, "Vegetarian", true, "Eggplant, Marinara Sauce, Mozzarella, Parmesan", "Gluten, Dairy, Eggs"));
            addMenuItemToMemory(new MenuItem("Vegetable Curry", "Mixed vegetables in a fragrant curry sauce.", 16.00, "Vegetarian", true, "Mixed Vegetables, Coconut Milk, Curry Spices", "None"));
            addMenuItemToMemory(new MenuItem("Chocolate Lava Cake", "Warm chocolate cake with a molten center.", 9.00, "Dessert", true, "Chocolate, Flour, Eggs, Sugar", "Gluten, Dairy, Eggs"));
            addMenuItemToMemory(new MenuItem("New York Cheesecake", "Rich and creamy classic cheesecake.", 8.50, "Dessert", true, "Cream Cheese, Graham Crackers, Eggs", "Gluten, Dairy, Eggs"));
            addMenuItemToMemory(new MenuItem("Tiramisu", "Coffee-soaked ladyfingers, mascarpone, cocoa powder.", 9.50, "Dessert", true, "Ladyfingers, Mascarpone, Coffee, Cocoa", "Gluten, Dairy, Eggs"));
            addMenuItemToMemory(new MenuItem("Apple Crumble", "Warm baked apples with a buttery crumble topping.", 7.00, "Dessert", true, "Apples, Flour, Butter, Sugar", "Gluten, Dairy, Eggs"));
            addMenuItemToMemory(new MenuItem("Fruit Sorbet", "Refreshing dairy-free sorbet.", 6.50, "Dessert", true, "Fruit Puree, Sugar", "None"));
            addMenuItemToMemory(new MenuItem("Brownie Sundae", "Warm brownie with vanilla ice cream and chocolate sauce.", 10.00, "Dessert", true, "Brownie, Ice Cream, Chocolate Sauce", "Gluten, Dairy, Eggs"));
            addMenuItemToMemory(new MenuItem("Key Lime Pie", "Tangy key lime filling in a graham cracker crust.", 8.00, "Dessert", true, "Key Lime, Graham Crackers, Cream", "Gluten, Dairy, Eggs"));
            addMenuItemToMemory(new MenuItem("Pecan Pie", "Sweet pecan filling in a flaky crust.", 7.50, "Dessert", true, "Pecans, Pie Crust, Corn Syrup", "Gluten, Dairy, Eggs"));
            addMenuItemToMemory(new MenuItem("Crème brûlée", "Classic custard dessert with a caramelized sugar topping.", 9.00, "Dessert", true, "Cream, Eggs, Sugar, Vanilla", "Dairy, Eggs"));
            addMenuItemToMemory(new MenuItem("Iced Coffee", "Chilled coffee with cream and ice.", 4.50, "Drinks", true, "Coffee, Ice, Cream", "Dairy"));
            addMenuItemToMemory(new MenuItem("Fresh Orange Juice", "100% pure squeezed orange juice.", 3.50, "Drinks", true, "Oranges", "None"));
            addMenuItemToMemory(new MenuItem("Sparkling Water", "Crisp, refreshing carbonated water.", 2.00, "Drinks", true, "Water", "None"));
            addMenuItemToMemory(new MenuItem("Lemonade", "Homemade sweet and tart lemonade.", 4.00, "Drinks", true, "Lemon, Sugar, Water", "None"));
            addMenuItemToMemory(new MenuItem("Soda (Coke)", "Classic Coca-Cola.", 3.00, "Drinks", true, "Carbonated Water, Sugar, Caffeine", "None"));
            addMenuItemToMemory(new MenuItem("Espresso", "Strong, concentrated coffee.", 3.00, "Drinks", true, "Coffee Beans", "None"));
            addMenuItemToMemory(new MenuItem("Cappuccino", "Espresso with steamed milk and foam.", 4.50, "Drinks", true, "Espresso, Milk, Foam", "Dairy"));
            addMenuItemToMemory(new MenuItem("Tea (Assorted)", "Selection of fine teas.", 3.00, "Drinks", true, "Tea Leaves, Water", "None"));
            addMenuItemToMemory(new MenuItem("Green Tea", "Refreshing and healthy green tea.", 3.00, "Drinks", true, "Green Tea Leaves, Water", "None"));
            addMenuItemToMemory(new MenuItem("Smoothie (Berry Blast)", "A blend of mixed berries.", 5.50, "Drinks", true, "Mixed Berries, Yogurt", "Dairy"));
            addMenuItemToMemory(new MenuItem("French Fries", "Crispy, golden fries.", 4.00, "Side", true, "Potatoes, Oil", "None"));
            addMenuItemToMemory(new MenuItem("Garlic Mashed Potatoes", "Creamy mashed potatoes with garlic.", 5.00, "Side", true, "Potatoes, Butter, Milk, Garlic", "Dairy"));
            addMenuItemToMemory(new MenuItem("Steamed Vegetables", "Fresh, seasonal vegetables.", 6.00, "Side", true, "Mixed Vegetables", "None"));
            addMenuItemToMemory(new MenuItem("Side Salad", "Mixed greens with your choice of dressing.", 5.50, "Side", true, "Mixed Greens, Dressing", "Varies"));
            addMenuItemToMemory(new MenuItem("Onion Rings", "Golden fried onion rings.", 5.50, "Side", true, "Onions, Flour, Oil", "Gluten"));
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
    public void fetchMenuFromServer(Callback<List<MenuItem>> cb) { api.getMenu().enqueue(cb); }
    public void createMenuItemRemote(MenuItem item, Callback<MenuItem> cb) { api.createMenuItem(item).enqueue(cb); }
    public void updateMenuItemRemote(int id, MenuItem item, Callback<MenuItem> cb) { api.updateMenuItem(id, item).enqueue(cb); }
    public void deleteMenuItemRemote(int id, Callback<Void> cb) { api.deleteMenuItem(id).enqueue(cb); }
    public void fetchReservationsRemote(Callback<List<Reservation>> cb) { api.getReservations().enqueue(cb); }
    public void updateReservationRemote(int id, Reservation r, Callback<Reservation> cb) { api.updateReservation(id, r).enqueue(cb); }
    public void deleteReservationRemote(int id, Callback<Void> cb) { api.deleteReservation(id).enqueue(cb); }

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