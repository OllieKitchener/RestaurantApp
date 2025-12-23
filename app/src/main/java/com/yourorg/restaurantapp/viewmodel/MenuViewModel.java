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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuViewModel extends AndroidViewModel {
    private final RestaurantRepository repository;
    public final MutableLiveData<List<MenuItem>> menuLiveData = new MutableLiveData<>();
    public final MutableLiveData<String> error = new MutableLiveData<>();

    public MenuViewModel(@NonNull Application application) {
        super(application);
        repository = new RestaurantRepository(application, "https://api.example.com/");
    }

    // Method to load menu from remote server
    public void loadMenu() {
        repository.fetchMenuFromServer(new Callback<List<MenuItem>>() {
            @Override
            public void onResponse(Call<List<MenuItem>> call, Response<List<MenuItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    menuLiveData.postValue(response.body());
                } else {
                    error.postValue("Failed to load menu: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<MenuItem>> call, Throwable t) {
                error.postValue(t.getMessage());
            }
        });
    }

    // Method to load menu from local database
    public void loadMenuFromDatabase() {
        repository.getAllMenuLocal(menuItemEntities -> {
            if (menuItemEntities != null) {
                List<MenuItem> menuItems = new ArrayList<>();
                for (MenuItemEntity entity : menuItemEntities) {
                    menuItems.add(entity.toModel());
                }
                menuLiveData.postValue(menuItems);
            } else {
                error.postValue("Failed to load menu from database.");
            }
        });
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