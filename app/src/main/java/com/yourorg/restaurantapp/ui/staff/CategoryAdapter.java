package com.yourorg.restaurantapp.ui.staff;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.function.Consumer;

public class CategoryAdapter extends ListAdapter<String, CategoryAdapter.CategoryViewHolder> {

    private final Consumer<String> onCategoryClicked;

    public CategoryAdapter(Consumer<String> onCategoryClicked) {
        super(DIFF_CALLBACK);
        this.onCategoryClicked = onCategoryClicked;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new CategoryViewHolder(view, onCategoryClicked);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView categoryName;
        private String currentCategory;

        public CategoryViewHolder(@NonNull View itemView, Consumer<String> onCategoryClicked) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.category_name);
            itemView.setOnClickListener(v -> {
                if (currentCategory != null) {
                    onCategoryClicked.accept(currentCategory);
                }
            });
        }

        public void bind(String category) {
            currentCategory = category;
            categoryName.setText(category);
        }
    }

    private static final DiffUtil.ItemCallback<String> DIFF_CALLBACK = new DiffUtil.ItemCallback<String>() {
        @Override
        public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return oldItem.equals(newItem);
        }
    };
}