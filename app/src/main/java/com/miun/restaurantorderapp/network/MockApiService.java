package com.miun.restaurantorderapp.network;

import android.os.Handler;
import android.os.Looper;
import com.miun.restaurantorderapp.models.MenuItem;
import com.miun.restaurantorderapp.models.OrderBundle;
import com.miun.restaurantorderapp.models.OrderStatusResponse;
import java.util.List;

/**
 * Mock API Service for testing without backend server.
 * Simulates network delays and returns dummy data.
 *
 * USAGE: Replace ApiService with MockApiService in your test code
 */
public class MockApiService {

    private static final int NETWORK_DELAY_MS = 1000;  // Simulate 1 second network delay

    /**
     * Fetch menu (returns dummy data after simulated delay)
     */
    public void fetchMenu(ApiCallback<List<MenuItem>> callback) {
        simulateNetworkDelay(() -> {
            // Simulate success
            List<MenuItem> menu = DummyDataProvider.getDummyMenu();
            callback.onSuccess(menu);

            // Uncomment to test error handling:
            // callback.onError("Simulated network error");
        });
    }

    /**
     * Send order (returns dummy order ID after simulated delay)
     */
    public void sendOrder(OrderBundle orderBundle, ApiCallback<OrderBundle> callback) {
        simulateNetworkDelay(() -> {
            // Create response with order ID
            OrderStatusResponse response = DummyDataProvider.createDummyOrderResponse();

            // Copy the order bundle and add the ID
            orderBundle.setId(response.getId());
            orderBundle.setDone(false);

            callback.onSuccess(orderBundle);

            // Uncomment to test error handling:
            // callback.onError("Failed to send order");
        });
    }

    /**
     * Check order status (simulates cooking time)
     * Will return done=true after 5 calls (5 seconds)
     */
    public void checkOrderStatus(Long orderId, ApiCallback<OrderStatusResponse> callback) {
        simulateNetworkDelay(() -> {
            OrderStatusResponse response = DummyDataProvider.checkDummyOrderStatus(orderId);
            callback.onSuccess(response);

            // Uncomment to test error handling:
            // callback.onError("Failed to check status");
        });
    }

    /**
     * Create group (returns dummy group ID)
     */
    public void createGroup(ApiCallback<Long> callback) {
        simulateNetworkDelay(() -> {
            Long groupId = DummyDataProvider.createDummyGroupId();
            callback.onSuccess(groupId);
        });
    }

    /**
     * Fetch all orders for a group (for checkout)
     */
    public void fetchGroupOrders(Long groupId, ApiCallback<List<OrderBundle>> callback) {
        simulateNetworkDelay(() -> {
            List<OrderBundle> orders = DummyDataProvider.getDummyOrdersForGroup(groupId);
            callback.onSuccess(orders);
        });
    }

    /**
     * Delete group (simulates success)
     */
    public void deleteGroup(Long groupId, ApiCallback<Void> callback) {
        simulateNetworkDelay(() -> {
            callback.onSuccess(null);
        });
    }

    /**
     * Test connection
     */
    public void testConnection(ApiCallback<String> callback) {
        simulateNetworkDelay(() -> {
            callback.onSuccess("✅ Mock API Connection OK!");
        });
    }

    /**
     * Helper method to simulate network delay
     */
    private void simulateNetworkDelay(Runnable action) {
        new Handler(Looper.getMainLooper()).postDelayed(action, NETWORK_DELAY_MS);
    }
}