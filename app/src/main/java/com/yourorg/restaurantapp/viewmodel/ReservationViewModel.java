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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationViewModel extends AndroidViewModel {
    private final RestaurantRepository repository;
    public final MutableLiveData<List<Reservation>> reservationsLiveData = new MutableLiveData<>();
    public final MutableLiveData<String> error = new MutableLiveData<>();

    public ReservationViewModel(@NonNull Application application) {
        super(application);
        // Use the singleton instance of the repository
        repository = RestaurantRepository.getInstance(application);
    }

    // Remote call (kept for compatibility, though likely unused now)
    public void loadReservations() {
        // This is a remote call, which we are not using for local data.
        // Keeping it here for potential future use.
    }

    public void createReservation(Reservation reservation, Callback<Reservation> callback) {
        repository.createReservationRemote(reservation, callback);
    }

    // Local in-memory operations
    public void createReservationLocal(ReservationEntity reservation) {
        repository.insertReservationLocal(reservation, () -> loadReservationsFromDatabase()); // Refresh after insert
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