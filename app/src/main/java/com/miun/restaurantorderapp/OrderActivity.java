package com.miun.restaurantorderapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

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

        Button customizeButton= findViewById(R.id.selectbutton);
        customizeButton.setOnClickListener(v ->{
            openCustomizationFragment();
        });

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

    private void openCustomizationFragment(){
        Fragment fragment= new CustomizationFragment();

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