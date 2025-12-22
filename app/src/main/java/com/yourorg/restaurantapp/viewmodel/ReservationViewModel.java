package com.yourorg.restaurantapp.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.yourorg.restaurantapp.data.repository.RestaurantRepository;

public class ReservationViewModel extends AndroidViewModel {
    private final RestaurantRepository repository;
    public final androidx.lifecycle.MutableLiveData<java.util.List<com.yourorg.restaurantapp.data.local.entities.ReservationEntity>> reservationsLiveData = new androidx.lifecycle.MutableLiveData<>();
    public final androidx.lifecycle.MutableLiveData<String> error = new androidx.lifecycle.MutableLiveData<>();

    public ReservationViewModel(@NonNull Application application) {
        super(application);
        repository = new RestaurantRepository(application, "");
    }

    public void createReservationLocal(com.yourorg.restaurantapp.data.local.entities.ReservationEntity reservation) {
        repository.insertReservationLocal(reservation, null);
    }

    public void loadReservationsFromDatabase() {
        repository.getAllReservationsLocal(reservations -> {
            if (reservations != null) {
                reservationsLiveData.postValue(reservations);
            } else {
                error.postValue("Failed to load reservations from database.");
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        repository.shutdown();
    }
}