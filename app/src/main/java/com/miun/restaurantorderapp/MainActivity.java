package com.miun.restaurantorderapp;
import com.miun.restaurantorderapp.network.MockApiService;
import com.miun.restaurantorderapp.network.ApiService;
import com.miun.restaurantorderapp.network.ApiCallback;

import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Button;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.List;
import java.util.Map;
import java.util.HashMap;


/**
 * MainActivity - Table Selection Screen
 *
 * Purpose: Allows servers to select which table (1-12) they want to actively work on.
 * This is the entry point of the application.
 *
 * Backend Flow:
 * 1. On table selection: Check if table has an active group (open tab)
 * 2. If active group exists: Navigate to OrderActivity with existing group ID
 * 3. If no active group: Create new group → Store table-group mapping → Navigate to OrderActivity
 * 4. Fetch all menu items (CarteMenu with MenuItems) from Payara server
 *
 * Flow: Table Selection (this screen) -> Order Placement Screen
 */
public class MainActivity extends AppCompatActivity {

    private ApiService apiService;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "TableGroupPrefs";
    private static final String TABLE_GROUP_PREFIX = "table_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        apiService = new ApiService();
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up click listeners for all 12 table buttons
        setupTableButtons();
    }

    /**
     * Set up click listeners for all 12 table buttons
     */
    private void setupTableButtons() {
        int[] tableButtonIds = {
                R.id.btnTable1, R.id.btnTable2, R.id.btnTable3, R.id.btnTable4,
                R.id.btnTable5, R.id.btnTable6, R.id.btnTable7, R.id.btnTable8,
                R.id.btnTable9, R.id.btnTable10, R.id.btnTable11, R.id.btnTable12
        };

        for (int i = 0; i < tableButtonIds.length; i++) {
            final int tableNumber = i + 1; // Table numbers 1-12
            Button tableButton = findViewById(tableButtonIds[i]);
            tableButton.setOnClickListener(view -> {
                handleTableSelection(tableNumber);
            });
        }
    }

    /**
     * Handle table selection - check for existing active group or create new one
     * @param tableNumber The selected table number (1-12)
     */
    private void handleTableSelection(int tableNumber) {
        // Check if table has an active group ID stored
        long existingGroupId = getTableGroupId(tableNumber);

        if (existingGroupId != -1) {
            // Table has an active group, verify it's still valid and open
            verifyAndOpenGroup(tableNumber, existingGroupId);
        } else {
            // No active group, create a new one
            createNewGroupForTable(tableNumber);
        }
    }

    /**
     * Verify that the stored group ID is still valid and has an open tab
     * @param tableNumber The table number
     * @param groupId The stored group ID
     */
    private void verifyAndOpenGroup(int tableNumber, long groupId) {
        // TODO: Add API call to verify group is still active/tab is open
        // For now, we'll assume it's valid and open the order activity
        // You may want to add: apiService.checkGroupStatus(groupId, callback)

        Toast.makeText(this, "Öppnar befintlig grupp för bord " + tableNumber, Toast.LENGTH_SHORT).show();
        openOrderActivity(tableNumber, groupId);
    }

    /**
     * Create a new group for the selected table
     * @param tableNumber The selected table number (1-12)
     */
    private void createNewGroupForTable(int tableNumber) {
        Toast.makeText(this, "Skapar grupp...", Toast.LENGTH_SHORT).show();

        apiService.createGroup(new ApiCallback<Long>() {
            @Override
            public void onSuccess(Long groupId) {
                // Store the table-group mapping
                saveTableGroupId(tableNumber, groupId);

                // Navigate to OrderActivity
                openOrderActivity(tableNumber, groupId);
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(MainActivity.this, "Fel vid skapande av grupp: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Navigate to OrderActivity with the table number and group ID
     * @param tableNumber The selected table number (1-12)
     * @param groupId The group ID for this table
     */
    private void openOrderActivity(int tableNumber, long groupId) {
        Intent intent = new Intent(MainActivity.this, OrderActivity.class);
        intent.putExtra("TABLE_NUMBER", tableNumber);
        intent.putExtra("GROUP_ID", groupId);
        startActivity(intent);
    }

    /**
     * Save the table-group ID mapping to SharedPreferences
     * @param tableNumber The table number
     * @param groupId The group ID to associate with this table
     */
    private void saveTableGroupId(int tableNumber, long groupId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(TABLE_GROUP_PREFIX + tableNumber, groupId);
        editor.apply();
    }

    /**
     * Get the stored group ID for a table
     * @param tableNumber The table number
     * @return The group ID, or -1 if no active group exists
     */
    private long getTableGroupId(int tableNumber) {
        return sharedPreferences.getLong(TABLE_GROUP_PREFIX + tableNumber, -1);
    }

    /**
     * Clear the table-group mapping (call this when a tab is closed)
     * This should be called from OrderActivity when the tab is paid/closed
     * @param tableNumber The table number to clear
     */
    public static void clearTableGroup(SharedPreferences prefs, int tableNumber) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(TABLE_GROUP_PREFIX + tableNumber);
        editor.apply();
    }
}