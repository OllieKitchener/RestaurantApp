package com.yourorg.restaurantapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.yourorg.restaurantapp.data.repository.RestaurantRepository;
import com.yourorg.restaurantapp.model.Reservation;
import com.yourorg.restaurantapp.data.local.entities.ReservationEntity;

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
        repository = new RestaurantRepository(application, "https://api.example.com/");
    }

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

    // Restoring the missing local database method
    public void createReservationLocal(ReservationEntity reservation) {
        repository.insertReservationLocal(reservation, null);
    }

    public void loadReservationsFromDatabase() {
        repository.getAllReservationsLocal(reservations -> {
             // This part seems to be missing the conversion to the Model, which might be a future issue
        });
    }
}