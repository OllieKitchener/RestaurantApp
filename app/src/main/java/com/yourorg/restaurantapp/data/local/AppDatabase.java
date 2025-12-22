package com.yourorg.restaurantapp.data.local;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.annotation.NonNull;

import com.yourorg.restaurantapp.data.local.dao.MenuItemDao;
import com.yourorg.restaurantapp.data.local.dao.ReservationDao;
import com.yourorg.restaurantapp.data.local.entities.MenuItemEntity;
import com.yourorg.restaurantapp.data.local.entities.ReservationEntity;

import java.util.concurrent.Executors;

// Restoring MenuItemEntity to the entities list and incrementing version
@Database(entities = {MenuItemEntity.class, ReservationEntity.class}, version = 5, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    // Restoring the abstract menuItemDao() method
    public abstract MenuItemDao menuItemDao();
    public abstract ReservationDao reservationDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "restaurant_db")
                            .addCallback(roomCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Executors.newSingleThreadExecutor().execute(() -> {
                MenuItemDao dao = INSTANCE.menuItemDao();
                // Pre-populate with sample data
                dao.insert(new MenuItemEntity("Ribeye Steak", "Juicy ribeye steak.", 25.99, "Meat", true));
                dao.insert(new MenuItemEntity("Lamb Chops", "Grilled lamb chops.", 22.50, "Meat", true));
                dao.insert(new MenuItemEntity("Salmon Fillet", "Pan-seared salmon.", 20.00, "Fish", true));
            });
        }
    };
}
