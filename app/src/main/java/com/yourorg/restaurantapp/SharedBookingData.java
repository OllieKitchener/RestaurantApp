package com.yourorg.restaurantapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SharedBookingData {

    public static final List<String> customerNotificationMessages = Collections.synchronizedList(new ArrayList<>());

    public static final List<String> staffNotificationMessages = Collections.synchronizedList(new ArrayList<>());
}
