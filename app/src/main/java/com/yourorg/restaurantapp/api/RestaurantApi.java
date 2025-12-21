package com.yourorg.restaurantapp.api;

import com.yourorg.restaurantapp.model.MenuItem;
import com.yourorg.restaurantapp.model.Reservation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RestaurantApi {
    @GET("menu")
    Call<List<MenuItem>> getMenu();

    @POST("menu")
    Call<MenuItem> createMenuItem(@Body MenuItem item);

    @PUT("menu/{id}")
    Call<MenuItem> updateMenuItem(@Path("id") int id, @Body MenuItem item);

    @DELETE("menu/{id}")
    Call<Void> deleteMenuItem(@Path("id") int id);

    @GET("reservations")
    Call<List<Reservation>> getReservations();

    @POST("reservations")
    Call<Reservation> createReservation(@Body Reservation reservation);

    @PUT("reservations/{id}")
    Call<Reservation> updateReservation(@Path("id") int id, @Body Reservation reservation);

    @DELETE("reservations/{id}")
    Call<Void> deleteReservation(@Path("id") int id);
}