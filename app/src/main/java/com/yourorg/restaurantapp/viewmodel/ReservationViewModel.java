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
        // Using placeholder URL as we are now fully in-memory
        repository = new RestaurantRepository(application, "https://localhost/");
    }

    // Remote call (kept for compatibility, though likely unused now)
    public void loadReservations() {
        repository.fetchReservationsRemote(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    reservationsLiveData.postValue(response.body());
                } else {
                    error.postValue("Failed to load reservations: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Reservation>> call, Throwable t) {
                error.postValue(t.getMessage());
            }
        });
    }

    public void createReservation(Reservation reservation, Callback<Reservation> callback) {
        repository.createReservationRemote(reservation, callback);
    }

    // Local in-memory operations
    public void createReservationLocal(ReservationEntity reservation) {
        repository.insertReservationLocal(reservation, null);
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