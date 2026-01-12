package com.yourorg.restaurantapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.yourorg.restaurantapp.model.MenuItem;

import java.util.Objects;
import java.util.function.Consumer;

public class DishAdapter extends ListAdapter<MenuItem, DishAdapter.DishViewHolder> {

    private final Consumer<MenuItem> onDishClicked;
    private final Consumer<MenuItem> onDeleteClicked;

    public DishAdapter(Consumer<MenuItem> onDishClicked, Consumer<MenuItem> onDeleteClicked) {
        super(DIFF_CALLBACK);
        this.onDishClicked = onDishClicked;
        this.onDeleteClicked = onDeleteClicked;
    }

    @NonNull
    @Override
    public DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dish_item, parent, false);
        return new DishViewHolder(view, onDishClicked, onDeleteClicked);
    }

    @Override
    public void onBindViewHolder(@NonNull DishViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class DishViewHolder extends RecyclerView.ViewHolder {
        private final Button dishNameButton;
        private final Button deleteButton;
        private MenuItem currentItem;

        public DishViewHolder(@NonNull View itemView, Consumer<MenuItem> onDishClicked, Consumer<MenuItem> onDeleteClicked) {
            super(itemView);
            dishNameButton = itemView.findViewById(R.id.dish_name_button);
            deleteButton = itemView.findViewById(R.id.delete_dish_button);

            dishNameButton.setOnClickListener(v -> {
                if (currentItem != null && onDishClicked != null) {
                    onDishClicked.accept(currentItem);
                }
            });

            deleteButton.setOnClickListener(v -> {
                if (currentItem != null && onDeleteClicked != null) {
                    onDeleteClicked.accept(currentItem);
                }
            });
        }

        public void bind(MenuItem item) {
            currentItem = item;
            if (item != null) {
                dishNameButton.setText(item.name != null ? item.name : "Unknown Dish");
            }

            // Show delete button only if staff is logged in
            if (SharedBookingData.isStaffLoggedIn) {
                deleteButton.setVisibility(View.VISIBLE);
            } else {
                deleteButton.setVisibility(View.GONE);
            }
        }
    }

    private static final DiffUtil.ItemCallback<MenuItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<MenuItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull MenuItem oldItem, @NonNull MenuItem newItem) {
            // Safe null checks
            if (oldItem == null || newItem == null) return false;
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull MenuItem oldItem, @NonNull MenuItem newItem) {
            // Safe null checks for content
            if (oldItem == null || newItem == null) return false;
            return Objects.equals(oldItem.name, newItem.name) && 
                   Objects.equals(oldItem.description, newItem.description) &&
                   oldItem.price == newItem.price;
        }
    };
}