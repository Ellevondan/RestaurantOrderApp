package com.miun.restaurantorderapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miun.restaurantorderapp.models.MenuItem;
import com.miun.restaurantorderapp.models.OrderBundle;
import com.miun.restaurantorderapp.network.ApiCallback;
import com.miun.restaurantorderapp.network.MockApiService;
import com.miun.restaurantorderapp.network.ApiService;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CheckOutActivity extends AppCompatActivity {

    public static final String EXTRA_GROUP_ID = "GROUP_ID";
    public static final String EXTRA_TABLE_NUMBER = "TABLE_NUMBER";

    private TextView tableNumberText;
    private RecyclerView orderItemsRecyclerView;
    private TextView totalAmount;
    private Button buttonBack;
    private Button buttonConfirmPayment;

    private final List<OrderItem> orderItems = new ArrayList<>();
    private OrderItemsAdapter adapter;

    private ApiService api;
    private long groupId = -1;

    private final Map<Long, MenuItem> menuById = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_check_out);

        initializeViews();
        setupRecyclerView();
        setupButtonListeners();

        // Prevent confirming payment before data is loaded
        buttonConfirmPayment.setEnabled(false);

        api = new ApiService();

        groupId = getIntent().getLongExtra(EXTRA_GROUP_ID, -1);
        int tableNumber = getIntent().getIntExtra(EXTRA_TABLE_NUMBER, -1);

        tableNumberText.setText(tableNumber > 0 ? ("Table #" + tableNumber) : "Table");

        if (groupId < 0) {
            Toast.makeText(this, "Missing groupId - cannot load checkout", Toast.LENGTH_SHORT).show();
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

    private void initializeViews() {
        tableNumberText = findViewById(R.id.tableNumberText);
        orderItemsRecyclerView = findViewById(R.id.orderItemsRecyclerView);
        totalAmount = findViewById(R.id.totalAmount);
        buttonBack = findViewById(R.id.buttonBack);
        buttonConfirmPayment = findViewById(R.id.buttonConfirmPayment);
    }

    private void setupRecyclerView() {
        adapter = new OrderItemsAdapter(orderItems);
        orderItemsRecyclerView.setAdapter(adapter);
        orderItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void fetchMenuThenOrders() {
        api.fetchMenu(new ApiCallback<>() {
            @Override
            public void onSuccess(List<MenuItem> result) {
                menuById.clear();
                for (MenuItem item : result) {
                    menuById.put(item.getId(), item);
                }
                fetchOrders();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(CheckOutActivity.this, "Failed to load menu: " + error, Toast.LENGTH_SHORT).show();
                // keep confirm disabled; no prices without menu
            }
        });
    }

    private void fetchOrders() {
        api.fetchGroupOrders(groupId, new ApiCallback<List<OrderBundle>>() {
            @Override
            public void onSuccess(List<OrderBundle> result) {

                // ================================
                // TEMPORARY WORKAROUND:
                // Backend does NOT filter by groupId.
                // We therefore fetch ALL orders and
                // filter them here in the app.
                // ================================
                List<OrderBundle> filteredByGroup = new ArrayList<>();
                for (OrderBundle bundle : result) {
                    if (bundle.getGroupId() == groupId) {
                        filteredByGroup.add(bundle);
                    }
                }

                Map<Long, OrderItem> grouped = new HashMap<>();

                for (OrderBundle bundle : filteredByGroup) {
                    List<com.miun.restaurantorderapp.models.ModifiedItem> items = bundle.getOrders();
                    if (items == null || items.isEmpty()) continue;

                    for (com.miun.restaurantorderapp.models.ModifiedItem mi : items) {
                        if (mi == null) continue;

                        Log.d("CHECKOUT",
                                "name=" + mi.getName()
                                        + " price=" + mi.getPrice()
                                        + " originalId=" + mi.getOriginalId()
                                        + " qty=" + mi.getQuantity()
                        );


                        Long menuId = mi.getOriginalId(); // must map to MenuItem.id via JSON "originalID"
                        String name = (mi.getName() != null) ? mi.getName() : "Unknown";
                        int qty = (mi.getQuantity() != null) ? mi.getQuantity() : 0;
                        Log.d("CHECKOUT",
                                "name:" + name
                        );
                        Double price = (mi.getPrice() != null) ? mi.getPrice() : 0;
                        MenuItem menuItem = (menuId != null) ? menuById.get(menuId) : null;
                        double unitPrice =
                                (menuItem != null && menuItem.getPrice() != null)
                                        ? menuItem.getPrice()
                                        : 0.0;

                        Long key = (menuId != null) ? menuId : (long) name.hashCode();

                        OrderItem existing = grouped.get(key);
                        if (existing == null) {
                            grouped.put(key, new OrderItem(name, qty, price));
                        } else {
                            existing.addQuantity(qty);
                            existing.setPriceIfMissing(unitPrice);
                        }
                    }
                }

                orderItems.clear();
                orderItems.addAll(grouped.values());
                adapter.notifyDataSetChanged();
                updateTotals();

                buttonConfirmPayment.setEnabled(true);

            }

            @Override
            public void onError(String error) {
                Toast.makeText(CheckOutActivity.this,
                        "Failed to load orders: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTotals() {
        double total = 0.0;
        for (OrderItem item : orderItems) {
            total += item.getPrice() * item.getQuantity();
        }
        totalAmount.setText(String.format(Locale.getDefault(), "%.0f SEK", total));
    }

    private void setupButtonListeners() {
        buttonBack.setOnClickListener(view -> finish());

        buttonConfirmPayment.setOnClickListener(view -> api.deleteGroup(groupId, new ApiCallback<>() {
            @Override
            public void onSuccess(Void result) {
                Toast.makeText(CheckOutActivity.this, "Payment Confirmed.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CheckOutActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(CheckOutActivity.this, "Failed to confirm payment: " + error, Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private static class OrderItem {
        private final String name;
        private int quantity;
        private double price;

        public OrderItem(String name, int quantity, double price) {
            this.name = name;
            this.quantity = quantity;
            this.price = price;
        }

        public String getName() { return name; }
        public int getQuantity() { return quantity; }
        public double getPrice() { return price; }

        public void addQuantity(int add) { this.quantity += add; }

        public void setPriceIfMissing(double p) {
            if (this.price == 0.0 && p != 0.0) this.price = p;
        }
    }

    private static class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.OrderItemViewHolder> {

        private final List<OrderItem> items;

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

            Log.d("CHECKOUT",
                    "nameeeeeee:" + item.getName()
            );
            Log.d("CHECKOUT",
                    "priceeeee:" + item.getPrice()
            );

            holder.itemName.setText(item.getName());

            holder.itemQuantity.setText(String.format(Locale.getDefault(), "Qty: %d", item.getQuantity()));
            holder.itemPrice.setText(String.format(Locale.getDefault(), "%.0f SEK", item.getPrice() * item.getQuantity()));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class OrderItemViewHolder extends RecyclerView.ViewHolder {
            TextView itemName, itemQuantity, itemPrice;

            public OrderItemViewHolder(@NonNull View itemView) {
                super(itemView);
                itemName = itemView.findViewById(R.id.itemName);
                itemQuantity = itemView.findViewById(R.id.itemQuantity);
                itemPrice = itemView.findViewById(R.id.itemPrice);
            }
        }
    }
}
