package com.yourorg.restaurantapp;

import android.app.Application;

public class App extends Application {

    private static boolean isStaffLoggedIn = false;

    public static boolean isStaffLoggedIn() {
        return isStaffLoggedIn;
    }

    public static void setStaffLoggedIn(boolean staffLoggedIn) {
        isStaffLoggedIn = staffLoggedIn;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
