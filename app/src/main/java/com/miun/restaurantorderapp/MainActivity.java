package com.miun.restaurantorderapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * MainActivity - Table Selection Screen
 *
 * Purpose: Allows servers to select which table (1-12) they want to actively work on.
 * This is the entry point of the application.
 *
 * Backend Flow:
 * 1. On first launch: Request server to create new group → Store group ID locally (SharedPreferences)
 * 2. Fetch all menu items (CarteMenu with MenuItems) from Payara server
 * 3. Server selects table → Navigate to OrderActivity (table selection is local only)
 *
 * Flow: Table Selection (this screen) -> Order Placement Screen
 */
public class MainActivity extends AppCompatActivity {

    // TODO: Add class variables
    // - Array or List to store table buttons (1-12)
    // - Variable to track currently selected table number
    // - Reference to ApiService for server communication
    // - SharedPreferences to store group ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // TODO: Check if group ID exists in SharedPreferences
        // - If no group ID exists: Make API call to create new group
        // - Server returns group ID → Store in SharedPreferences
        // - This group ID will be used for all orders from this device

        // TODO: Fetch menu items from Payara server
        // - API call to get all CarteMenu objects with their MenuItems
        // - Store menu items locally (in memory or cache) for OrderActivity to use
        // - Handle loading state and errors

        // TODO: Initialize UI components
        // - Find references to table buttons (1-12) from layout
        // - Set up click listeners for each table button
        // - Show loading indicator while fetching menu items

        // TODO: Implement table selection logic
        // - When a table button is clicked, store the selected table number
        // - Start OrderActivity and pass:
        //   * Selected table number
        //   * Menu items (or load from cache in OrderActivity)
    }

    // TODO: Add helper methods
    // - Method to create new group on server (returns group ID)
    // - Method to fetch menu items from server
    // - Method to handle table button clicks
    // - Method to navigate to OrderActivity with selected table number
    // - Method to get/set group ID in SharedPreferences
}