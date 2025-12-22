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

    public MenuViewModel(@NonNull Application application) {
        super(application);
        repository = new RestaurantRepository(application, "");
    }

    public void loadMenuFromDatabase() {
        repository.getAllMenuLocal(menuItemEntities -> {
            if (menuItemEntities != null) {
                List<MenuItem> menuItems = new ArrayList<>();
                for (MenuItemEntity entity : menuItemEntities) {
                    menuItems.add(new MenuItem(entity.id, entity.name, entity.description, entity.price, entity.category, entity.available));
                }
                menuLiveData.postValue(menuItems);
            }
        });
    }

    public void loadCategoriesFromDatabase() {
        repository.getDistinctCategoriesLocal(categoriesLiveData::postValue);
    }

    public void addMenuItem(MenuItemEntity item) {
        repository.insertMenuItemLocal(item, this::loadMenuFromDatabase);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        repository.shutdown();
    }
}