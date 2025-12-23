// The following document contains full Java source files for the skeleton Android app described.
// Each file is separated by a header comment of the form: // --- File: <path> ---
// Copy each file into your Android Studio project under the corresponding package/folder.

// --- File: app/src/main/java/com/yourorg/restaurantapp/api/ApiClient.java ---
package com.yourorg.restaurantapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

// --- File: app/src/main/java/com/yourorg/restaurantapp/api/RestaurantApi.java ---
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

// --- File: app/src/main/java/com/yourorg/restaurantapp/model/MenuItem.java ---
package com.yourorg.restaurantapp.model;

import com.google.gson.annotations.SerializedName;

public class MenuItem {
    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("description")
    public String description;

    @SerializedName("price")
    public double price;

    @SerializedName("available")
    public boolean available;

    public MenuItem() { }

    public MenuItem(int id, String name, String description, double price, boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.available = available;
    }

    // getters and setters (optional)
}

// --- File: app/src/main/java/com/yourorg/restaurantapp/model/Reservation.java ---
package com.yourorg.restaurantapp.model;

import com.google.gson.annotations.SerializedName;

public class Reservation {
    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("phone")
    public String phone;

    @SerializedName("partySize")
    public int partySize;

    @SerializedName("dateTime")
    public String dateTime; // ISO 8601 string recommended

    @SerializedName("status")
    public String status; // e.g., "pending", "confirmed", "cancelled"

    @SerializedName("notes")
    public String notes;

    public Reservation() { }

    public Reservation(int id, String name, String phone, int partySize, String dateTime, String status, String notes) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.partySize = partySize;
        this.dateTime = dateTime;
        this.status = status;
        this.notes = notes;
    }
}

// --- File: app/src/main/java/com/yourorg/restaurantapp/data/local/entities/MenuItemEntity.java ---
package com.yourorg.restaurantapp.data.local.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "menu_items")
public class MenuItemEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String description;
    public double price;
    public boolean available;

    public MenuItemEntity() { }

    public MenuItemEntity(int id, String name, String description, double price, boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.available = available;
    }

    public static MenuItemEntity fromModel(com.yourorg.restaurantapp.model.MenuItem m) {
        return new MenuItemEntity(m.id, m.name, m.description, m.price, m.available);
    }

    public com.yourorg.restaurantapp.model.MenuItem toModel() {
        return new com.yourorg.restaurantapp.model.MenuItem(id, name, description, price, available);
    }
}

// --- File: app/src/main/java/com/yourorg/restaurantapp/data/local/entities/ReservationEntity.java ---
package com.yourorg.restaurantapp.data.local.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "reservations")
public class ReservationEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String phone;
    public int partySize;
    public String dateTime;
    public String status;
    public String notes;

    public ReservationEntity() { }

    public ReservationEntity(int id, String name, String phone, int partySize, String dateTime, String status, String notes) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.partySize = partySize;
        this.dateTime = dateTime;
        this.status = status;
        this.notes = notes;
    }

    public static ReservationEntity fromModel(com.yourorg.restaurantapp.model.Reservation r) {
        return new ReservationEntity(r.id, r.name, r.phone, r.partySize, r.dateTime, r.status, r.notes);
    }

    public com.yourorg.restaurantapp.model.Reservation toModel() {
        return new com.yourorg.restaurantapp.model.Reservation(id, name, phone, partySize, dateTime, status, notes);
    }
}

// --- File: app/src/main/java/com/yourorg/restaurantapp/data/local/dao/MenuItemDao.java ---
package com.yourorg.restaurantapp.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.yourorg.restaurantapp.data.local.entities.MenuItemEntity;

import java.util.List;

@Dao
public interface MenuItemDao {
    @Query("SELECT * FROM menu_items")
    List<MenuItemEntity> getAll();

    @Insert
    long insert(MenuItemEntity item);

    @Update
    int update(MenuItemEntity item);

    @Delete
    int delete(MenuItemEntity item);

    @Query("DELETE FROM menu_items")
    void deleteAll();
}

// --- File: app/src/main/java/com/yourorg/restaurantapp/data/local/dao/ReservationDao.java ---
package com.yourorg.restaurantapp.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.yourorg.restaurantapp.data.local.entities.ReservationEntity;

import java.util.List;

@Dao
public interface ReservationDao {
    @Query("SELECT * FROM reservations")
    List<ReservationEntity> getAll();

    @Insert
    long insert(ReservationEntity reservation);

    @Update
    int update(ReservationEntity reservation);

    @Delete
    int delete(ReservationEntity reservation);

    @Query("DELETE FROM reservations")
    void deleteAll();
}

// --- File: app/src/main/java/com/yourorg/restaurantapp/data/local/AppDatabase.java ---
package com.yourorg.restaurantapp.data.local;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.yourorg.restaurantapp.data.local.entities.MenuItemEntity;
import com.yourorg.restaurantapp.data.local.entities.ReservationEntity;
import com.yourorg.restaurantapp.data.local.dao.MenuItemDao;
import com.yourorg.restaurantapp.data.local.dao.ReservationDao;

@Database(entities = {MenuItemEntity.class, ReservationEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract MenuItemDao menuItemDao();
    public abstract ReservationDao reservationDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "restaurant_db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

// --- File: app/src/main/java/com/yourorg/restaurantapp/data/repository/RestaurantRepository.java ---
package com.yourorg.restaurantapp.data.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.yourorg.restaurantapp.api.ApiClient;
import com.yourorg.restaurantapp.api.RestaurantApi;
import com.yourorg.restaurantapp.data.local.AppDatabase;
import com.yourorg.restaurantapp.data.local.entities.MenuItemEntity;
import com.yourorg.restaurantapp.data.local.entities.ReservationEntity;
import com.yourorg.restaurantapp.model.MenuItem;
import com.yourorg.restaurantapp.model.Reservation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantRepository {
    private final RestaurantApi api;
    private final AppDatabase db;
    private final ExecutorService executor = Executors.newFixedThreadPool(3);
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public RestaurantRepository(Context context, String baseUrl) {
        api = ApiClient.getClient(baseUrl).create(RestaurantApi.class);
        db = AppDatabase.getInstance(context);
    }

    // Remote API calls - examples
    public void fetchMenuFromServer(Callback<List<MenuItem>> callback) {
        api.getMenu().enqueue(callback);
    }

    public void createMenuItemRemote(MenuItem item, Callback<MenuItem> callback) {
        api.createMenuItem(item).enqueue(callback);
    }

    public void updateMenuItemRemote(int id, MenuItem item, Callback<MenuItem> callback) {
        api.updateMenuItem(id, item).enqueue(callback);
    }

    public void deleteMenuItemRemote(int id, Callback<Void> callback) {
        api.deleteMenuItem(id).enqueue(callback);
    }

    public void fetchReservationsRemote(Callback<List<Reservation>> callback) {
        api.getReservations().enqueue(callback);
    }

    public void createReservationRemote(Reservation r, Callback<Reservation> callback) {
        api.createReservation(r).enqueue(callback);
    }

    public void updateReservationRemote(int id, Reservation r, Callback<Reservation> callback) {
        api.updateReservation(id, r).enqueue(callback);
    }

    public void deleteReservationRemote(int id, Callback<Void> callback) {
        api.deleteReservation(id).enqueue(callback);
    }

    // Local DB operations
    public void insertMenuItemLocal(final MenuItemEntity entity, final Runnable onComplete) {
        executor.execute(() -> {
            db.menuItemDao().insert(entity);
            if (onComplete != null) mainHandler.post(onComplete);
        });
    }

    public void getAllMenuLocal(final java.util.function.Consumer<List<MenuItemEntity>> consumer) {
        executor.execute(() -> {
            List<MenuItemEntity> items = db.menuItemDao().getAll();
            if (consumer != null) mainHandler.post(() -> consumer.accept(items));
        });
    }

    public void insertReservationLocal(final ReservationEntity entity, final Runnable onComplete) {
        executor.execute(() -> {
            db.reservationDao().insert(entity);
            if (onComplete != null) mainHandler.post(onComplete);
        });
    }

    public void getAllReservationsLocal(final java.util.function.Consumer<List<ReservationEntity>> consumer) {
        executor.execute(() -> {
            List<ReservationEntity> items = db.reservationDao().getAll();
            if (consumer != null) mainHandler.post(() -> consumer.accept(items));
        });
    }
}

// --- File: app/src/main/java/com/yourorg/restaurantapp/viewmodel/MenuViewModel.java ---
package com.yourorg.restaurantapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.yourorg.restaurantapp.data.repository.RestaurantRepository;
import com.yourorg.restaurantapp.model.MenuItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuViewModel extends AndroidViewModel {
    private final RestaurantRepository repository;
    public final MutableLiveData<List<MenuItem>> menuLiveData = new MutableLiveData<>();
    public final MutableLiveData<String> error = new MutableLiveData<>();

    public MenuViewModel(@NonNull Application application) {
        super(application);
        // TODO: replace with your API base URL or expose via config
        repository = new RestaurantRepository(application, "https://api.example.com/");
    }

    public void loadMenu() {
        repository.fetchMenuFromServer(new Callback<List<MenuItem>>() {
            @Override
            public void onResponse(Call<List<MenuItem>> call, Response<List<MenuItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    menuLiveData.postValue(response.body());
                } else {
                    error.postValue("Failed to load menu: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<MenuItem>> call, Throwable t) {
                error.postValue(t.getMessage());
            }
        });
    }
}

// --- File: app/src/main/java/com/yourorg/restaurantapp/viewmodel/ReservationViewModel.java ---
package com.yourorg.restaurantapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.yourorg.restaurantapp.data.repository.RestaurantRepository;
import com.yourorg.restaurantapp.model.Reservation;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationViewModel extends AndroidViewModel {
    private final RestaurantRepository repository;
    public final MutableLiveData<List<Reservation>> reservationsLiveData = new MutableLiveData<>();
    public final MutableLiveData<String> error = new MutableLiveData<>();

    public ReservationViewModel(@NonNull Application application) {
        super(application);
        repository = new RestaurantRepository(application, "https://api.example.com/");
    }

    public void loadReservations() {
        repository.fetchReservationsRemote(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    reservationsLiveData.postValue(response.body());
                } else {
                    error.postValue("Failed to load reservations: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Reservation>> call, Throwable t) {
                error.postValue(t.getMessage());
            }
        });
    }

    public void createReservation(Reservation reservation, Callback<Reservation> callback) {
        repository.createReservationRemote(reservation, callback);
    }
}

// --- File: app/src/main/java/com/yourorg/restaurantapp/ui/guest/GuestMenuActivity.java ---
package com.yourorg.restaurantapp.ui.guest;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yourorg.restaurantapp.R;
import com.yourorg.restaurantapp.model.MenuItem;
import com.yourorg.restaurantapp.viewmodel.MenuViewModel;

import java.util.List;

public class GuestMenuActivity extends AppCompatActivity {
    private MenuViewModel viewModel;
    private RecyclerView recyclerView;
    private MenuAdapter adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_menu);

        recyclerView = findViewById(R.id.recycler_view_menu);
        progressBar = findViewById(R.id.progress_bar);

        adapter = new MenuAdapter(item -> onMenuItemClicked(item));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(MenuViewModel.class);
        viewModel.menuLiveData.observe(this, this::onMenuLoaded);
        viewModel.error.observe(this, s -> Toast.makeText(this, s, Toast.LENGTH_LONG).show());

        progressBar.setVisibility(View.VISIBLE);
        viewModel.loadMenu();
    }

    private void onMenuLoaded(List<MenuItem> items) {
        progressBar.setVisibility(View.GONE);
        adapter.submitList(items);
    }

    private void onMenuItemClicked(MenuItem item) {
        // TODO: open Menu Details / Reservation screen
        Toast.makeText(this, "Clicked: " + item.name, Toast.LENGTH_SHORT).show();
    }
}

// --- File: app/src/main/java/com/yourorg/restaurantapp/ui/guest/MenuAdapter.java ---
package com.yourorg.restaurantapp.ui.guest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yourorg.restaurantapp.R;
import com.yourorg.restaurantapp.model.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private List<MenuItem> items = new ArrayList<>();
    private final OnItemClickListener listener;

    public interface OnItemClickListener { void onItemClick(MenuItem item); }

    public MenuAdapter(OnItemClickListener listener) { this.listener = listener; }

    public void submitList(List<MenuItem> newItems) {
        items = newItems == null ? new ArrayList<>() : newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_simple, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuItem item = items.get(position);
        holder.title.setText(item.name);
        holder.subtitle.setText(String.format("%.2f", item.price) + "");
        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount() { return items.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, subtitle;
        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text1);
            subtitle = itemView.findViewById(R.id.text2);
        }
    }
}

// --- File: app/src/main/java/com/yourorg/restaurantapp/ui/staff/StaffManageMenuActivity.java ---
package com.yourorg.restaurantapp.ui.staff;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yourorg.restaurantapp.R;
import com.yourorg.restaurantapp.api.RestaurantApi;
import com.yourorg.restaurantapp.api.ApiClient;
import com.yourorg.restaurantapp.model.MenuItem;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffManageMenuActivity extends AppCompatActivity {
    private EditText etName, etDescription, etPrice;
    private Button btnAdd;
    private ProgressBar progressBar;

    private RestaurantApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_manage_menu);

        etName = findViewById(R.id.et_name);
        etDescription = findViewById(R.id.et_description);
        etPrice = findViewById(R.id.et_price);
        btnAdd = findViewById(R.id.btn_add);
        progressBar = findViewById(R.id.progress_bar);

        api = ApiClient.getClient("https://api.example.com/").create(RestaurantApi.class);

        btnAdd.setOnClickListener(v -> createMenuItem());
    }

    private void createMenuItem() {
        String name = etName.getText().toString().trim();
        String desc = etDescription.getText().toString().trim();
        double price = 0.0;
        try { price = Double.parseDouble(etPrice.getText().toString().trim()); } catch (NumberFormatException e) { /* ignore */ }

        if (name.isEmpty()) {
            Toast.makeText(this, "Name required", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        MenuItem item = new MenuItem(0, name, desc, price, true);
        api.createMenuItem(item).enqueue(new Callback<MenuItem>() {
            @Override
            public void onResponse(Call<MenuItem> call, Response<MenuItem> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Toast.makeText(StaffManageMenuActivity.this, "Menu item created", Toast.LENGTH_SHORT).show();
                    etName.setText(""); etDescription.setText(""); etPrice.setText("");
                } else {
                    Toast.makeText(StaffManageMenuActivity.this, "Failed: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MenuItem> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(StaffManageMenuActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}

// --- File: app/src/main/java/com/yourorg/restaurantapp/util/NotificationHelper.java ---
package com.yourorg.restaurantapp.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHelper {
    private static final String CHANNEL_ID = "restaurant_channel";
    private final Context context;
    private final NotificationManager manager;

    public NotificationHelper(Context context) {
        this.context = context;
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createChannel();
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Restaurant Notifications";
            String description = "Notifications for reservations and menu updates";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            manager.createNotificationChannel(channel);
        }
    }

    public void showNotification(int id, String title, String text) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true);
        manager.notify(id, builder.build());
    }
}

// --- File: app/src/main/java/com/yourorg/restaurantapp/workers/NotificationWorker.java ---
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
        // Example check: you could read preferences and call API here
        NotificationHelper helper = new NotificationHelper(getApplicationContext());
        helper.showNotification(1001, "Reminder", "You have upcoming reservations");
        return Result.success();
    }
}

// --- File: app/src/main/java/com/yourorg/restaurantapp/App.java ---
package com.yourorg.restaurantapp;

import android.app.Application;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize any global components here if needed
    }
}

// --- End of document ---
