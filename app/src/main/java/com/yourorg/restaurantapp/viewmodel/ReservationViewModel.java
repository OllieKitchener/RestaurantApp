package com.yourorg.restaurantapp.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.yourorg.restaurantapp.data.repository.RestaurantRepository;
import com.yourorg.restaurantapp.model.Reservation;
import com.yourorg.restaurantapp.data.local.entities.ReservationEntity;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Callback;

public class ReservationViewModel extends AndroidViewModel {
    private final RestaurantRepository repository;
    public final MutableLiveData<List<Reservation>> reservationsLiveData = new MutableLiveData<>();
    public final MutableLiveData<String> error = new MutableLiveData<>();
    private boolean isCleared = false;

    public ReservationViewModel(@NonNull Application application) {
        super(application);
        repository = RestaurantRepository.getInstance(application.getApplicationContext());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        isCleared = true;
    }

    public void createReservationLocal(ReservationEntity reservation, Runnable onComplete) {
        repository.insertReservationLocal(reservation, () -> {
            if (isCleared) return; // CRITICAL FIX
            loadReservationsFromDatabase();
            if (onComplete != null) onComplete.run();
        }); 
    }

    public void createReservationLocal(ReservationEntity reservation) {
        createReservationLocal(reservation, null);
    }

    public void loadReservationsFromDatabase() {
        repository.getAllReservationsLocal(entities -> {
            if (isCleared) return; // CRITICAL FIX
            List<Reservation> reservations = new ArrayList<>();
            if (entities != null) {
                for (ReservationEntity entity : entities) reservations.add(entity.toModel());
            }
            reservationsLiveData.postValue(reservations);
        });
    }
    
    // Remote method kept for compile-time safety
    public void createReservationRemote(Reservation reservation, Callback<Reservation> callback) {
        repository.createReservationRemote(reservation, callback);
    }
}