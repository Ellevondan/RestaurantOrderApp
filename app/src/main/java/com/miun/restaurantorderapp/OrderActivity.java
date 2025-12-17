package com.miun.restaurantorderapp;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;

import static androidx.fragment.app.FragmentManager.TAG;

import android.nfc.Tag;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.content.Intent;


import com.miun.restaurantorderapp.models.MenuItem;
import com.miun.restaurantorderapp.models.ModifiedItem;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import com.miun.restaurantorderapp.network.ApiCallback;
import com.miun.restaurantorderapp.network.MockApiService;
import com.miun.restaurantorderapp.models.OrderBundle;
import com.miun.restaurantorderapp.network.PollingService;

import android.widget.Toast;
import java.util.ArrayList;
import android.content.SharedPreferences;



/**
 * OrderActivity - Order Placement Screen
 *
 * Purpose: Displays menu items for the server to create orders for the selected table.
 * Layout: Menu items grid/list on the left/center, order summary on the right side (static).
 *
 * Backend Flow:
 * 1. Display menu items fetched from MainActivity
 * 2. Server adds items to order → Send OrderBundle to Payara server with category (appetizer/main/dessert/rushed)
 * 3. Poll server for order bundle status updates using stored group ID
 * 4. When meal ends → Fetch all order bundles for this group and sum prices
 *
 * Flow: Table Selection Screen -> Order Placement (this screen) -> Submit orders to server
 */
public class OrderActivity extends AppCompatActivity implements CustomizationFragment.CustomizationListener {

    // CLASS VARIABLES
    private MockApiService apiService;
    private Long groupId;
    private int tableNumber;
    private List<MenuItem> menuItems;
    private List<ModifiedItem> selectedItems;
    private Button buttonSendOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        // 1. Init API service
        apiService = new MockApiService();

        // 2. Hämta Intent data
        tableNumber = getIntent().getIntExtra("TABLE_NUMBER", -1);
        groupId = getIntent().getLongExtra("GROUP_ID", -1);

        // 3. Init listor
        menuItems = new ArrayList<>();
        selectedItems = new ArrayList<>();

        // 4. Init UI
        buttonSendOrder = findViewById(R.id.buttonSendOrder);
        setupButtons();

        // 5. Hämta meny (SIST!)
        fetchMenu();
    }
    @Override
    public void onItemCustomized(ModifiedItem item) {
        selectedItems.add(item);
        Toast.makeText(this,
                "Added: " + item.getName() + " (Total: " + selectedItems.size() + ")",
                LENGTH_SHORT).show();
        // updateUI(); // Om du har en metod för att uppdatera UI
    }
    private void fetchMenu() {
        apiService.fetchMenu(new ApiCallback<List<MenuItem>>() {
            @Override
            public void onSuccess(List<MenuItem> menu) {
                menuItems = new ArrayList<>(menu);
                setupDishButtons();  // ← Nu finns menuItems!
            }

            @Override
            public void onError(String error) {
                Toast.makeText(OrderActivity.this,
                        "Failed to load menu", LENGTH_SHORT).show();
            }
        });
    }

    private void setupButtons() {

        // Back button
        findViewById(R.id.buttonBack).setOnClickListener(v -> finish());

        // Send order button
        buttonSendOrder.setOnClickListener(v -> sendOrder());

        // Checkout button
        findViewById(R.id.buttonNext).setOnClickListener(v -> {
            Intent intent = new Intent(OrderActivity.this, CheckOutActivity.class);
            intent.putExtra("GROUP_ID", groupId);
            intent.putExtra("TABLE_NUMBER", tableNumber);
            startActivity(intent);
        });
    }

    private void sendOrder() {
        if (selectedItems.isEmpty()) {
            Toast.makeText(this, "No items selected!", LENGTH_SHORT).show();
            return;
        }

        OrderBundle bundle = new OrderBundle();
        bundle.setGroupID(groupId);
        bundle.setOrders(new ArrayList<>(selectedItems));

        apiService.sendOrder(bundle, new ApiCallback<OrderBundle>() {
            @Override
            public void onSuccess(OrderBundle result) {
                Toast.makeText(OrderActivity.this,
                        "Order sent! ID: " + result.getId(), LENGTH_SHORT).show();

                // Starta polling
                startPollingService(result.getId());

                // Töm listan
                selectedItems.clear();
                // updateUI();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(OrderActivity.this,
                        "Failed: " + error, LENGTH_LONG).show();
            }
        });
    }
    public static final String TAG = "FragmentManager";
    private void startPollingService(Long orderId) {
        Intent intent = new Intent(this, PollingService.class);
        intent.putExtra("orderId", orderId);
        startService(intent);
        Log.d(TAG, "Started PollingService for order: " + orderId);
    }

    private void setupDishButtons() {
        // Sortera menuItems efter typ
        List<MenuItem> appetizers = new ArrayList<>();
        List<MenuItem> mains = new ArrayList<>();
        List<MenuItem> desserts = new ArrayList<>();
        List<MenuItem> drinks = new ArrayList<>();

        for (MenuItem item : menuItems) {
            if (item.getIsAppetizer() != null && item.getIsAppetizer()) {
                appetizers.add(item);
            } else if (item.getIsHuvud() != null && item.getIsHuvud()) {
                mains.add(item);
            } else if (item.getIsDessert() != null && item.getIsDessert()) {
                desserts.add(item);
            } else {
                drinks.add(item);  // Allt annat = dryck
            }
        }

        // Appetizer buttons
        int[] appetizerButtonIds = {
                R.id.btnAppetizer1, R.id.btnAppetizer2, R.id.btnAppetizer3,
                R.id.btnAppetizer4, R.id.btnAppetizer5, R.id.btnAppetizer6
        };
        setupCategoryButtons(appetizerButtonIds, appetizers);

        // Main buttons
        int[] mainButtonIds = {
                R.id.btnMain1, R.id.btnMain2, R.id.btnMain3,
                R.id.btnMain4, R.id.btnMain5, R.id.btnMain6,
                R.id.btnMain7, R.id.btnMain8, R.id.btnMain9
        };
        setupCategoryButtons(mainButtonIds, mains);

        // Dessert buttons
        int[] dessertButtonIds = {
                R.id.btnDessert1, R.id.btnDessert2, R.id.btnDessert3,
                R.id.btnDessert4, R.id.btnDessert5, R.id.btnDessert6
        };
        setupCategoryButtons(dessertButtonIds, desserts);

        // Drink buttons
        int[] drinkButtonIds = {
                R.id.btnDrink1, R.id.btnDrink2, R.id.btnDrink3,
                R.id.btnDrink4, R.id.btnDrink5, R.id.btnDrink6
        };
        setupCategoryButtons(drinkButtonIds, drinks);
    }

    private void setupCategoryButtons(int[] buttonIds, List<MenuItem> items) {
        for (int i = 0; i < buttonIds.length; i++) {
            Button button = findViewById(buttonIds[i]);

            if (i < items.size()) {
                // Finns rätt för denna knapp
                MenuItem menuItem = items.get(i);
                button.setText(menuItem.getName());
                button.setVisibility(View.VISIBLE);
                button.setOnClickListener(v -> {
                    Log.d("OrderActivity", "Clicked: " + menuItem.getName());
                    openCustomizationFragment(menuItem);
                });
            } else {
                // Ingen rätt, göm knappen
                button.setVisibility(View.GONE);
            }
        }
    }

    private void openCustomizationFragment(MenuItem menuItem) {
        CustomizationFragment fragment = CustomizationFragment.newInstance(menuItem);
        fragment.show(getSupportFragmentManager(), "customize");
    }
}