package com.yourorg.restaurantapp.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.yourorg.restaurantapp.data.repository.RestaurantRepository;
import com.yourorg.restaurantapp.model.Reservation;
import com.yourorg.restaurantapp.data.local.entities.ReservationEntity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationViewModel extends AndroidViewModel {
    private final RestaurantRepository repository;
    public final MutableLiveData<List<Reservation>> reservationsLiveData = new MutableLiveData<>();
    public final MutableLiveData<String> error = new MutableLiveData<>();

    public ReservationViewModel(@NonNull Application application) {
        super(application);
        repository = RestaurantRepository.getInstance(application.getApplicationContext());
    }

    // Remote call (kept for compatibility)
    public void loadReservations() {
        // Unused in local mode
    }

    public void createReservation(Reservation reservation, Callback<Reservation> callback) {
        repository.createReservationRemote(reservation, callback);
    }

    // Local in-memory operations - Now accepts a callback!
    public void createReservationLocal(ReservationEntity reservation, Runnable onComplete) {
        repository.insertReservationLocal(reservation, () -> {
            loadReservationsFromDatabase(); // Refresh internal live data
            if (onComplete != null) {
                onComplete.run();
            }
        }); 
    }

    // Overload for backward compatibility if needed, though we should use the one above
    public void createReservationLocal(ReservationEntity reservation) {
        createReservationLocal(reservation, null);
    }

    public void loadReservationsFromDatabase() {
        repository.getAllReservationsLocal(entities -> {
            if (entities != null) {
                List<Reservation> reservations = new ArrayList<>();
                for (ReservationEntity entity : entities) {
                    reservations.add(entity.toModel());
                }
                reservationsLiveData.postValue(reservations);
            }
        });
    }
}