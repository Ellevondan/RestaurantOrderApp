package com.miun.restaurantorderapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.miun.restaurantorderapp.models.MenuItem;
import com.miun.restaurantorderapp.models.ModifiedItem;
import com.miun.restaurantorderapp.models.OrderBundle;
import com.miun.restaurantorderapp.network.ApiCallback;
import com.miun.restaurantorderapp.network.MockApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderActivity extends AppCompatActivity {

    private int tableNumber;
    private long groupId;

    private ArrayList<MenuItem> menuItems = new ArrayList<>();
    private ArrayList<ModifiedItem> selectedItems = new ArrayList<>();

    private MockApiService apiService;

    // Order summary UI (right panel)
    private LinearLayout orderItemsContainer;
    private TextView tvTotal;
    private TextView tvEmptyOrder;

    // Fragment container overlay
    private FrameLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        apiService = new MockApiService();

        // --- Read intent extras ---
        Intent intent = getIntent();
        tableNumber = intent.getIntExtra("TABLE_NUMBER", -1);
        groupId = intent.getLongExtra("GROUP_ID", -1L);

        // --- Find UI ---
        orderItemsContainer = findViewById(R.id.orderItemsContainer);
        tvTotal = findViewById(R.id.tvTotal);
        tvEmptyOrder = findViewById(R.id.tvEmptyOrder);

        fragmentContainer = findViewById(R.id.fragmentContainer);
        if (fragmentContainer != null) {
            fragmentContainer.setVisibility(View.GONE); // start hidden
        }

        // Hide/show fragment overlay automatically based on backstack
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            if (fragmentContainer == null) return;
            boolean hasOverlay = getSupportFragmentManager().getBackStackEntryCount() > 0;
            fragmentContainer.setVisibility(hasOverlay ? View.VISIBLE : View.GONE);
        });

        // --- Receive ModifiedItem from fragment ---
        getSupportFragmentManager().setFragmentResultListener(
                CustomizationFragment.REQUEST_KEY,
                this,
                (requestKey, bundle) -> {
                    ModifiedItem item = bundle.getParcelable(CustomizationFragment.RESULT_MODIFIED_ITEM);
                    if (item == null) return;

                    selectedItems.add(item);
                    Toast.makeText(
                            OrderActivity.this,
                            "Added: " + item.getQuantity() + "x " + item.getName() +
                                    ((item.getSelectedAllergens() != null && !item.getSelectedAllergens().isEmpty())
                                            ? " (" + item.getSelectedAllergens() + ")" : ""),
                            Toast.LENGTH_SHORT
                    ).show();

                    updateOrderSummaryUI();
                }
        );

        // --- Buttons ---
        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> finish());

        Button buttonSendOrder = findViewById(R.id.buttonSendOrder);
        buttonSendOrder.setOnClickListener(v -> sendOrder());

        Button buttonNext = findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(v -> {
            Intent checkout = new Intent(OrderActivity.this, CheckOutActivity.class);
            checkout.putExtra(CheckOutActivity.EXTRA_GROUP_ID, groupId);
            checkout.putExtra(CheckOutActivity.EXTRA_TABLE_NUMBER, tableNumber);
            startActivity(checkout);
        });

        // --- Load menu from API and bind to buttons ---
        apiService.fetchMenu(new ApiCallback<List<MenuItem>>() {
            @Override
            public void onSuccess(List<MenuItem> menu) {
                if (menu == null || menu.isEmpty()) {
                    Toast.makeText(OrderActivity.this, "No menu items available.", Toast.LENGTH_SHORT).show();
                    disableAllDishButtons();
                    return;
                }
                menuItems = new ArrayList<>(menu);
                bindMenuToButtons(menuItems);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(OrderActivity.this, "Failed to load menu: " + error, Toast.LENGTH_SHORT).show();
                disableAllDishButtons();
            }
        });

        updateOrderSummaryUI();
    }

    private void sendOrder() {
        if (groupId <= 0) {
            Toast.makeText(this, "Missing/invalid GROUP_ID.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tableNumber <= 0) {
            Toast.makeText(this, "Missing/invalid table number.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedItems.isEmpty()) {
            Toast.makeText(this, "Order list is empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        OrderBundle bundle = new OrderBundle(groupId, new ArrayList<>(selectedItems));
        bundle.setTableNumber(tableNumber);

        apiService.sendOrder(bundle, new ApiCallback<OrderBundle>() {
            @Override
            public void onSuccess(OrderBundle result) {
                Toast.makeText(OrderActivity.this, "Order sent!", Toast.LENGTH_SHORT).show();
                selectedItems.clear();
                updateOrderSummaryUI();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(OrderActivity.this, "Failed to send order: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ---------------- Menu -> Buttons ----------------

    private void bindMenuToButtons(List<MenuItem> menu) {
        // Categorize using flags from MenuItem
        List<MenuItem> appetizers = new ArrayList<>();
        List<MenuItem> mains = new ArrayList<>();
        List<MenuItem> desserts = new ArrayList<>();
        List<MenuItem> drinks = new ArrayList<>();

        for (MenuItem mi : menu) {
            if (mi == null) continue;
            if (Boolean.TRUE.equals(mi.getIsAppetizer())) appetizers.add(mi);
            else if (Boolean.TRUE.equals(mi.getIsHuvud())) mains.add(mi);
            else if (Boolean.TRUE.equals(mi.getIsDessert())) desserts.add(mi);
            else drinks.add(mi); // fallback
        }

        int[] appetizerIds = {
                R.id.btnAppetizer1, R.id.btnAppetizer2, R.id.btnAppetizer3,
                R.id.btnAppetizer4, R.id.btnAppetizer5, R.id.btnAppetizer6
        };

        int[] mainIds = {
                R.id.btnMain1, R.id.btnMain2, R.id.btnMain3,
                R.id.btnMain4, R.id.btnMain5, R.id.btnMain6,
                R.id.btnMain7, R.id.btnMain8, R.id.btnMain9
        };

        int[] dessertIds = {
                R.id.btnDessert1, R.id.btnDessert2, R.id.btnDessert3,
                R.id.btnDessert4, R.id.btnDessert5, R.id.btnDessert6
        };

        int[] drinkIds = {
                R.id.btnDrink1, R.id.btnDrink2, R.id.btnDrink3,
                R.id.btnDrink4, R.id.btnDrink5, R.id.btnDrink6
        };

        bindButtons(appetizerIds, appetizers);
        bindButtons(mainIds, mains);
        bindButtons(dessertIds, desserts);
        bindButtons(drinkIds, drinks);
    }

    private void bindButtons(int[] buttonIds, List<MenuItem> items) {
        for (int i = 0; i < buttonIds.length; i++) {
            Button b = findViewById(buttonIds[i]);
            if (b == null) continue;

            if (i < items.size()) {
                MenuItem mi = items.get(i);

                b.setEnabled(true);
                b.setAlpha(1f);
                b.setTag(mi);

                String name = (mi.getName() != null) ? mi.getName() : "";
                double price = (mi.getPrice() != null) ? mi.getPrice() : 0.0;
                b.setText(String.format(Locale.getDefault(), "%s\n%.0f SEK", name, price));

                b.setOnClickListener(v -> {
                    Object tag = v.getTag();
                    if (tag instanceof MenuItem) {
                        openCustomizationFragment((MenuItem) tag);
                    }
                });

            } else {
                b.setEnabled(false);
                b.setAlpha(0.35f);
                b.setTag(null);
                b.setText("—");
                b.setOnClickListener(null);
            }
        }
    }

    private void disableAllDishButtons() {
        int[] dishButtonIds = {
                R.id.btnAppetizer1, R.id.btnAppetizer2, R.id.btnAppetizer3,
                R.id.btnAppetizer4, R.id.btnAppetizer5, R.id.btnAppetizer6,
                R.id.btnMain1, R.id.btnMain2, R.id.btnMain3,
                R.id.btnMain4, R.id.btnMain5, R.id.btnMain6,
                R.id.btnMain7, R.id.btnMain8, R.id.btnMain9,
                R.id.btnDessert1, R.id.btnDessert2, R.id.btnDessert3,
                R.id.btnDessert4, R.id.btnDessert5, R.id.btnDessert6,
                R.id.btnDrink1, R.id.btnDrink2, R.id.btnDrink3,
                R.id.btnDrink4, R.id.btnDrink5, R.id.btnDrink6
        };

        for (int id : dishButtonIds) {
            Button b = findViewById(id);
            if (b == null) continue;
            b.setEnabled(false);
            b.setAlpha(0.35f);
        }
    }

    private void openCustomizationFragment(MenuItem hit) {
        if (hit == null) return;

        Long id = hit.getId();
        String name = (hit.getName() != null) ? hit.getName() : "";
        Double price = hit.getPrice();

        if (id == null || name.isEmpty()) {
            Toast.makeText(this, "Menu item data missing.", Toast.LENGTH_SHORT).show();
            return;
        }

        CustomizationFragment fragment = new CustomizationFragment();
        Bundle args = new Bundle();
        args.putLong(CustomizationFragment.ARG_MENU_ID, id);
        args.putString(CustomizationFragment.ARG_MENU_NAME, name);
        args.putDouble(CustomizationFragment.ARG_MENU_PRICE, (price != null) ? price : 0.0);
        fragment.setArguments(args);

        if (fragmentContainer != null) fragmentContainer.setVisibility(View.VISIBLE);

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.fragmentContainer, fragment);
        tx.addToBackStack("customization");
        tx.commit();
    }

    // ---------------- Order summary UI ----------------

    private void updateOrderSummaryUI() {
        if (tvTotal != null) {
            double total = 0.0;
            for (ModifiedItem mi : selectedItems) {
                if (mi == null) continue;
                int q = (mi.getQuantity() != null) ? mi.getQuantity() : 0;
                double p = (mi.getPrice() != null) ? mi.getPrice() : 0.0;
                total += q * p;
            }
            tvTotal.setText(String.format(Locale.getDefault(), "Total: %.0f SEK", total));
        }

        if (orderItemsContainer == null) return;

        // Remove all dynamic children but keep tvEmptyOrder
        for (int i = orderItemsContainer.getChildCount() - 1; i >= 0; i--) {
            View child = orderItemsContainer.getChildAt(i);
            if (child != null && child.getId() != R.id.tvEmptyOrder) {
                orderItemsContainer.removeViewAt(i);
            }
        }

        if (tvEmptyOrder != null) {
            tvEmptyOrder.setVisibility(selectedItems.isEmpty() ? View.VISIBLE : View.GONE);
        }

        if (selectedItems.isEmpty()) return;

        for (ModifiedItem item : selectedItems) {
            if (item == null) continue;

            int q = (item.getQuantity() != null) ? item.getQuantity() : 1;
            String name = (item.getName() != null) ? item.getName() : "";
            double p = (item.getPrice() != null) ? item.getPrice() : 0.0;

            String allergens = item.getSelectedAllergens();
            String comments = item.getComments();

            String line = String.format(Locale.getDefault(),
                    "%dx %s  %.0f SEK", q, name, (q * p));

            if (allergens != null && !allergens.isEmpty()) {
                line += "\nAllergens: " + allergens;
            }
            if (comments != null && !comments.isEmpty()) {
                line += "\nNote: " + comments;
            }

            TextView tv = new TextView(this);
            tv.setText(line);
            tv.setTextSize(14f);
            tv.setPadding(0, 10, 0, 10);

            orderItemsContainer.addView(tv);
        }
    }
}
