package com.yourorg.restaurantapp;

import java.util.HashMap;
import java.util.Map;

public class UserSession {
    // In-memory user store. Data is lost when the app is fully terminated.
    private static final Map<String, String> registeredUsers = new HashMap<>();

    // Pre-load a default user for convenience (stored in lowercase)
    static {
        registeredUsers.put("customer@test.com", "password");
    }

    /**
     * Attempts to register a new user.
     * @param email The user's email.
     * @param password The user's password.
     * @return True if registration is successful (email not already taken), false otherwise.
     */
    public static boolean registerUser(String email, String password) {
        if (email == null || password == null) return false;
        
        String normalizedEmail = email.trim().toLowerCase();
        
        if (registeredUsers.containsKey(normalizedEmail)) {
            return false; // Email already exists
        }
        registeredUsers.put(normalizedEmail, password);
        return true;
    }

    /**
     * Validates a user's login credentials.
     * @param email The user's email.
     * @param password The user's password.
     * @return True if the email exists and the password matches, false otherwise.
     */
    public static boolean validateUser(String email, String password) {
        if (email == null || password == null) return false;

        String normalizedEmail = email.trim().toLowerCase();

        if (!registeredUsers.containsKey(normalizedEmail)) {
            return false; // User not found
        }
        return registeredUsers.get(normalizedEmail).equals(password);
    }
}