package com.yourorg.restaurantapp.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.yourorg.restaurantapp.data.repository.RestaurantRepository;
import com.yourorg.restaurantapp.data.local.entities.ReservationEntity;
import com.yourorg.restaurantapp.model.Reservation;

import java.util.List;
import java.util.stream.Collectors;

public class ReservationViewModel extends AndroidViewModel {
    private final RestaurantRepository repository;
    // This now holds ReservationEntity to match the database response
    public final MutableLiveData<List<ReservationEntity>> reservationsLiveData = new MutableLiveData<>();
    public final MutableLiveData<String> error = new MutableLiveData<>();

    public ReservationViewModel(@NonNull Application application) {
        super(application);
        repository = new RestaurantRepository(application, "");
    }

    // Method to save a reservation to the local database
    public void createReservationLocal(ReservationEntity reservation) {
        repository.insertReservationLocal(reservation, null);
    }

    // Correctly loads reservations from the local database
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
        repository.shutdown(); // Shut down the background thread pool
    }
}