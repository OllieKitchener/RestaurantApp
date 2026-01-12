package com.yourorg.restaurantapp;

import java.util.HashMap;
import java.util.Map;

public class UserSession {
    // In-memory user store. Data is lost when the app is fully terminated.
    private static final Map<String, String> registeredUsers = new HashMap<>();

    // Pre-load a default user for convenience
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
        if (registeredUsers.containsKey(email)) {
            return false; // Email already exists
        }
        registeredUsers.put(email, password);
        return true;
    }

    /**
     * Validates a user's login credentials.
     * @param email The user's email.
     * @param password The user's password.
     * @return True if the email exists and the password matches, false otherwise.
     */
    public static boolean validateUser(String email, String password) {
        if (!registeredUsers.containsKey(email)) {
            return false; // User not found
        }
        return registeredUsers.get(email).equals(password);
    }
}