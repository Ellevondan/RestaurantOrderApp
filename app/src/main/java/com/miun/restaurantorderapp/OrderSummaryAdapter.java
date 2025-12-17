package com.miun.restaurantorderapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miun.restaurantorderapp.models.ModifiedItem;

import java.util.List;
import java.util.Locale;

public class OrderSummaryAdapter extends RecyclerView.Adapter<OrderSummaryAdapter.ViewHolder> {

    private final List<ModifiedItem> selectedItems;
    private final OnItemRemovedListener removeListener;

    public interface OnItemRemovedListener {
        void onItemRemoved(ModifiedItem item);
    }

    public OrderSummaryAdapter(List<ModifiedItem> selectedItems, OnItemRemovedListener listener) {
        this.selectedItems = selectedItems;
        this.removeListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_summary, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(selectedItems.get(position), removeListener);
    }

    @Override
    public int getItemCount() {
        return selectedItems != null ? selectedItems.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView itemNameTextView;
        private final TextView itemQuantityTextView;
        private final TextView itemPriceTextView;
        private final ImageButton buttonRemoveItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
            itemQuantityTextView = itemView.findViewById(R.id.itemQuantityTextView);
            itemPriceTextView = itemView.findViewById(R.id.itemPriceTextView);
            buttonRemoveItem = itemView.findViewById(R.id.buttonRemoveItem);
        }

        public void bind(final ModifiedItem item, OnItemRemovedListener removeListener) {
            String name = item.getName() != null ? item.getName() : "";
            int qty = item.getQuantity() != null ? item.getQuantity() : 1;
            double unitPrice = item.getPrice() != null ? item.getPrice() : 0.0;

            itemQuantityTextView.setText(String.format(Locale.getDefault(), "x%d", qty));
            itemNameTextView.setText(name);
            itemPriceTextView.setText(String.format(Locale.getDefault(), "%.2f kr", unitPrice));


            buttonRemoveItem.setOnClickListener(v -> {
                if (removeListener != null) {
                    removeListener.onItemRemoved(item);
                }
            });
        }
    }
}
