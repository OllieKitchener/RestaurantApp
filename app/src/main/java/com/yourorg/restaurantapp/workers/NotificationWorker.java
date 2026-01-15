package com.yourorg.restaurantapp.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.yourorg.restaurantapp.util.NotificationHelper;

public class NotificationWorker extends Worker {
    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        NotificationHelper helper = new NotificationHelper(getApplicationContext());
        helper.showNotification(1001, "Reminder", "You have upcoming reservations");
        return Result.success();
    }
}