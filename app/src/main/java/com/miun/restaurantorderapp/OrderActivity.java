package com.miun.restaurantorderapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.content.Intent;

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
public class OrderActivity extends AppCompatActivity {

    // TODO: Add class variables
    // - Variable to store the selected table number (from Intent)
    // - List<MenuItem> to store menu items (from Intent or loaded from cache)
    // - List<OrderBundle> to store current pending orders for this table
    // - Reference to RecyclerView/GridView for menu items display
    // - Reference to order summary RecyclerView (right side, static display)
    // - Reference to price TextView
    // - Reference to submit order button
    // - String groupId (retrieved from SharedPreferences)
    // - Handler/Timer for polling order status from server
    // - Reference to ApiService for server communication

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        // Back button - navigate to MainActivity
        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(view -> {
            finish(); // Goes back to MainActivity
        });

        // Send Order button - submit current order to server
        Button buttonSendOrder = findViewById(R.id.buttonSendOrder);
        buttonSendOrder.setOnClickListener(view -> {
            // TODO: Implement order submission to server
            // - Collect all items from order summary
            // - Create OrderBundle objects for each item
            // - Send to Payara server via API
            // - Show confirmation or error message
            // For now, just show a placeholder message
            android.widget.Toast.makeText(this, "Send Order - Not yet implemented", android.widget.Toast.LENGTH_SHORT).show();
        });

        // Close Tab button - navigate to CheckOutActivity
        Button buttonNext = findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(view -> {
            Intent intent = new Intent(OrderActivity.this, CheckOutActivity.class);
            startActivity(intent);
        });

        // Set up dish buttons to open customization fragment
        setupDishButtons();

        // TODO: Get data from Intent and SharedPreferences
        // - Retrieve the selected table number from Intent
        // - Retrieve menu items from Intent (or load from cache)
        // - Get group ID from SharedPreferences
        // - Display table number in UI (e.g., "Table 5")

        // TODO: Initialize UI components
        // - Find references to all views (menu grid, order summary, total, buttons)
        // - Set up RecyclerView/GridView for menu items on left/center
        // - Set up RecyclerView for order summary on right side (static)
        // - Set up adapter for menu items with click listeners

        // TODO: Start polling for order status updates
        // - Use Handler.postDelayed() or ScheduledExecutorService
        // - Poll server every X seconds for order bundles with this group ID
        // - Update UI when order status changes (e.g., ready for pickup)
        // - Filter out completed orders or show status indicators

        // TODO: Implement menu item selection
        // - When menu item is clicked, show quantity/customization dialog
        // - Add item to pending order list
        // - Update order summary display on right side
        // - Calculate and update total price
    }

    /**
     * Set up click listeners for all dish buttons
     */
    private void setupDishButtons() {
        int[] dishButtonIds = {
            // Appetizers
            R.id.btnAppetizer1, R.id.btnAppetizer2, R.id.btnAppetizer3,
            R.id.btnAppetizer4, R.id.btnAppetizer5, R.id.btnAppetizer6,
            // Mains
            R.id.btnMain1, R.id.btnMain2, R.id.btnMain3,
            R.id.btnMain4, R.id.btnMain5, R.id.btnMain6,
            R.id.btnMain7, R.id.btnMain8, R.id.btnMain9,
            // Desserts
            R.id.btnDessert1, R.id.btnDessert2, R.id.btnDessert3,
            R.id.btnDessert4, R.id.btnDessert5, R.id.btnDessert6,
            // Drinks
            R.id.btnDrink1, R.id.btnDrink2, R.id.btnDrink3,
            R.id.btnDrink4, R.id.btnDrink5, R.id.btnDrink6
        };

        for (int dishButtonId : dishButtonIds) {
            Button dishButton = findViewById(dishButtonId);
            dishButton.setOnClickListener(v -> {
                String dishName = ((Button) v).getText().toString();
                openCustomizationFragment(dishName);
            });
        }
    }

    /**
     * Open the customization fragment with the selected dish
     * @param dishName The name of the selected dish
     */
    private void openCustomizationFragment(String dishName) {
        Fragment fragment = new CustomizationFragment();

        // TODO: Pass dish information to fragment via Bundle
        // Bundle args = new Bundle();
        // args.putString("DISH_NAME", dishName);
        // fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // TODO: Stop polling when activity is destroyed
        // - Cancel Handler callbacks or shutdown ExecutorService
    }

    // TODO: Add order submission methods
    // - Method to submit single order bundle to server
    //   * Include: group ID, table number, menu item, quantity, category (appetizer/main/dessert/rushed)
    //   * Send POST request to Payara server
    //   * Handle success/error responses
    // - Method to validate order before submission

    // TODO: Add polling methods
    // - Method to poll server for order bundles by group ID
    // - Method to update UI based on order status
    // - Method to check if orders are ready for pickup

    // TODO: Add UI update methods
    // - Method to add item to order summary
    // - Method to remove item from order summary
    // - Method to update order summary display
    // - Method to calculate and update total price
    // - Method to clear current pending orders

    // TODO: Add end-of-meal methods
    // - Method to fetch all order bundles for this group
    // - Method to calculate final bill (sum all order prices)
    // - Method to display final bill to user
    // - Option to mark group as completed/paid
}