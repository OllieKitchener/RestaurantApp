package com.yourorg.restaurantapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SharedBookingData {
    public static boolean isStaffLoggedIn = false;
    
    // Customer-specific notifications
    public static final List<String> customerNotificationMessages = Collections.synchronizedList(new ArrayList<>());

    // NEW: Staff-specific notifications
    public static final List<String> staffNotificationMessages = Collections.synchronizedList(new ArrayList<>());
}
