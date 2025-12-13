package com.miun.restaurantorderapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Button;
import android.content.Intent;
import android.widget.Toast;

import com.miun.restaurantorderapp.models.ModifiedItem;
import com.miun.restaurantorderapp.models.OrderBundle;
import com.miun.restaurantorderapp.network.ApiCallback;
import com.miun.restaurantorderapp.network.MockApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Locale;

/**
 * CheckOutActivity - Checkout/Close Tab Screen
 *
 * Purpose: Display order summary and total price when closing a group's tab.
 * Shows all ordered items with quantities and prices, calculates subtotal, tax, and total.
 *
 * UI Components (no actual functionality yet, just prepared UI):
 * - Order summary with RecyclerView of ordered items
 * - Subtotal, tax, and total amount displays
 * - Back button and Confirm Payment button
 */
public class CheckOutActivity extends AppCompatActivity {

    public static final String EXTRA_GROUP_ID = "GROUP_ID";
    public static final String EXTRA_TABLE_NUMBER= "TABLE_NUMBER";

    // UI Components
    private TextView tableNumberText;
    private RecyclerView orderItemsRecyclerView;
    private TextView totalAmount;
    private Button buttonBack;
    private Button buttonConfirmPayment;

    // Data (will be populated from Intent in the future)
    //private List<OrderItem> orderItems;
    private final List<OrderItem> orderItems = new ArrayList<>();
    private OrderItemsAdapter adapter;

    private MockApiService api; // sen byter du bara till ApiService
    private long groupId = -1;
    private int tableNumber = -1;

    private final Map<Long, MenuItem> menuById = new Hashmap<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_check_out);

        // Initialize UI components
        initializeViews();

        // Set up RecyclerView
        setupRecyclerView();

        // Populate with sample data for UI demonstration
        //loadSampleData();

        // Set up button listeners
        setupButtonListeners();

        api = new MockApiService();

        groupId = getIntent().getLongExtra(EXTRA_GROUP_ID, -1);
        tableNumber = getIntent().getIntExtra(EXTRA_TABLE_NUMBER, -1);

        tableNumberText.setText(tableNumber > 0 ? ("Table #" + tableNumber) : "Table");

        if (groupId < 0){
            Toast.makeText(this, "Missing groupId - cannot load checkout", Toast. LENGTH_SHORT).show();
            finish();
            return;
        }

        fetchMenuThenOrders();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Initialize all view references
     */
    private void initializeViews() {
        tableNumberText = findViewById(R.id.tableNumberText);
        orderItemsRecyclerView = findViewById(R.id.orderItemsRecyclerView);
        totalAmount = findViewById(R.id.totalAmount);
        buttonBack = findViewById(R.id.buttonBack);
        buttonConfirmPayment = findViewById(R.id.buttonConfirmPayment);
    }

    /**
     * Set up the RecyclerView with adapter and layout manager
     */
    private void setupRecyclerView() {
        adapter = new OrderItemsAdapter(orderItems);
        orderItemsRecyclerView.setAdapter(adapter);
        orderItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Load sample data for UI demonstration
     * TODO: Replace with actual data from Intent/server in the future

    private void loadSampleData() {
        // Sample table number
        tableNumberText.setText("Table #5");

        // Sample order items
        orderItems.add(new OrderItem("Caesar Salad", 2, 12.99));
        orderItems.add(new OrderItem("Grilled Salmon", 1, 24.99));
        orderItems.add(new OrderItem("Pasta Carbonara", 2, 18.99));
        orderItems.add(new OrderItem("Chocolate Cake", 1, 8.99));
        orderItems.add(new OrderItem("Red Wine", 2, 15.99));

        // Update the adapter
        orderItemsRecyclerView.getAdapter().notifyDataSetChanged();

        // Calculate and display totals
        updateTotals();
    }
     */



    /**
     * Calculate and update total amount
     */
    private void updateTotals() {
        double total = 0.0;

        for (OrderItem item : orderItems) {
            total += item.getPrice() * item.getQuantity();
        }

        // Update UI
        totalAmount.setText(String.format("%.0f SEK", total));
    }

    /**
     * Set up button click listeners
     */
    private void setupButtonListeners() {
        buttonBack.setOnClickListener(view -> {
            finish(); // Goes back to OrderActivity
        });

        buttonConfirmPayment.setOnClickListener(view -> {
            // TODO: Implement actual payment confirmation logic
            Toast.makeText(this, "Payment confirmation - Not yet implemented", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Simple OrderItem class to hold item data for display
     * TODO: Replace with actual OrderBundle model when implemented
     */
    private static class OrderItem {
        private String name;
        private int quantity;
        private double price;

        public OrderItem(String name, int quantity, double price) {
            this.name = name;
            this.quantity = quantity;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getPrice() {
            return price;
        }
    }

    /**
     * RecyclerView Adapter for order items
     */
    private static class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.OrderItemViewHolder> {

        private List<OrderItem> items;

        public OrderItemsAdapter(List<OrderItem> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_checkout_order, parent, false);
            return new OrderItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
            OrderItem item = items.get(position);
            holder.itemName.setText(item.getName());
            holder.itemQuantity.setText("Qty: " + item.getQuantity());
            holder.itemPrice.setText(String.format("%.0f SEK", item.getPrice() * item.getQuantity()));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class OrderItemViewHolder extends RecyclerView.ViewHolder {
            TextView itemName;
            TextView itemQuantity;
            TextView itemPrice;

            public OrderItemViewHolder(@NonNull View itemView) {
                super(itemView);
                itemName = itemView.findViewById(R.id.itemName);
                itemQuantity = itemView.findViewById(R.id.itemQuantity);
                itemPrice = itemView.findViewById(R.id.itemPrice);
            }
        }
    }
}