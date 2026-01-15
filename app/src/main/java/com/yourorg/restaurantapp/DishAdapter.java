package com.yourorg.restaurantapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.yourorg.restaurantapp.model.MenuItem;
import com.yourorg.restaurantapp.util.OnItemClickListener;
import java.util.ArrayList;
import java.util.List;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.DishViewHolder> {

    private final List<MenuItem> dishes = new ArrayList<>();
    private final OnItemClickListener<MenuItem> onDishClicked;
    private final OnItemClickListener<MenuItem> onDeleteClicked;

    public DishAdapter(OnItemClickListener<MenuItem> onDishClicked, OnItemClickListener<MenuItem> onDeleteClicked) {
        this.onDishClicked = onDishClicked;
        this.onDeleteClicked = onDeleteClicked;
    }

    public void setDishes(List<MenuItem> newDishes) {
        dishes.clear();
        if (newDishes != null) {
            dishes.addAll(newDishes);
        }
        notifyDataSetChanged();
    }
    
    public void clear() {
        dishes.clear();
        notifyDataSetChanged();
    }

    // CRITICAL FIX: By returning the position, we force RecyclerView to treat every item
    // as a unique view type, effectively disabling view recycling. This prevents crashes
    // related to stale, recycled views.
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dish_item, parent, false);
        return new DishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishViewHolder holder, int position) {
        holder.bind(dishes.get(position), onDishClicked, onDeleteClicked);
    }
    
    @Override
    public int getItemCount() {
        return dishes.size();
    }

    static class DishViewHolder extends RecyclerView.ViewHolder {
        private final Button dishNameButton;
        private final Button deleteButton;

        public DishViewHolder(@NonNull View itemView) {
            super(itemView);
            dishNameButton = itemView.findViewById(R.id.dish_name_button);
            deleteButton = itemView.findViewById(R.id.delete_dish_button);
        }

        void bind(final MenuItem item, final OnItemClickListener<MenuItem> dishListener, final OnItemClickListener<MenuItem> deleteListener) {
            dishNameButton.setText(item.name);
            dishNameButton.setOnClickListener(v -> {
                if (dishListener != null) dishListener.onItemClick(item);
            });

            if (App.isStaffLoggedIn()) {
                deleteButton.setVisibility(View.VISIBLE);
                deleteButton.setOnClickListener(v -> {
                    if (deleteListener != null) deleteListener.onItemClick(item);
                });
            } else {
                deleteButton.setVisibility(View.GONE);
                deleteButton.setOnClickListener(null);
            }
        }
    }
}