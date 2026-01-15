package com.yourorg.restaurantapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SharedBookingData {
    // Removed isStaffLoggedIn as it's now managed globally in App.java
    
    // Customer-specific notifications
    public static final List<String> customerNotificationMessages = Collections.synchronizedList(new ArrayList<>());

    // Staff-specific notifications
    public static final List<String> staffNotificationMessages = Collections.synchronizedList(new ArrayList<>());
}
