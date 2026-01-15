package com.yourorg.restaurantapp;

import java.util.HashMap;
import java.util.Map;

public class UserSession {
    private static final Map<String, String> registeredUsers = new HashMap<>();

    static {
        registeredUsers.put("customer@test.com", "password");
    }

    public static boolean registerUser(String email, String password) {
        if (email == null || password == null) return false;
        
        String normalizedEmail = email.trim().toLowerCase();
        
        if (registeredUsers.containsKey(normalizedEmail)) {
            return false;
        }
        registeredUsers.put(normalizedEmail, password);
        return true;
    }
    public static boolean validateUser(String email, String password) {
        if (email == null || password == null) return false;

        String normalizedEmail = email.trim().toLowerCase();

        if (!registeredUsers.containsKey(normalizedEmail)) {
            return false;
        }
        return registeredUsers.get(normalizedEmail).equals(password);
    }
}