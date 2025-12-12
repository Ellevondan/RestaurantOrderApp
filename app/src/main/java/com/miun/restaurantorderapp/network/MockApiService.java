package com.miun.restaurantorderapp.network;

import android.os.Handler;
import android.os.Looper;
import com.miun.restaurantorderapp.models.MenuItem;
import com.miun.restaurantorderapp.models.OrderBundle;
import com.miun.restaurantorderapp.models.OrderStatusResponse;
import java.util.List;

/**
 * Mock API Service for testing without backend server.
 * Uses ApiCallback<T> for async responses.
 */
public class MockApiService {

    private static final int NETWORK_DELAY_MS = 1000;  // Simulate 1 second network delay

    /**
     * Fetch menu (returns dummy data after simulated delay)
     */
    public void fetchMenu(ApiCallback<List<MenuItem>> callback) {
        simulateNetworkDelay(() -> {
            try {
                List<MenuItem> menu = DummyDataProvider.getDummyMenu();
                callback.onSuccess(menu);
            } catch (Exception e) {
                callback.onError("Failed to fetch menu: " + e.getMessage());
            }
        });
    }

    /**
     * Send order (returns dummy order bundle with ID after simulated delay)
     */
    public void sendOrder(OrderBundle orderBundle, ApiCallback<OrderBundle> callback) {
        simulateNetworkDelay(() -> {
            try {
                OrderStatusResponse response = DummyDataProvider.createDummyOrderResponse();
                orderBundle.setId(response.getId());
                orderBundle.setDone(false);
                callback.onSuccess(orderBundle);
            } catch (Exception e) {
                callback.onError("Failed to send order: " + e.getMessage());
            }
        });
    }

    /**
     * Check order status (simulates cooking time)
     */
    public void checkOrderStatus(Long orderId, ApiCallback<OrderStatusResponse> callback) {
        simulateNetworkDelay(() -> {
            try {
                OrderStatusResponse response = DummyDataProvider.checkDummyOrderStatus(orderId);
                callback.onSuccess(response);
            } catch (Exception e) {
                callback.onError("Failed to check order status: " + e.getMessage());
            }
        });
    }

    /**
     * Create group (returns dummy group ID)
     */
    public void createGroup(ApiCallback<Long> callback) {
        simulateNetworkDelay(() -> {
            try {
                Long groupId = DummyDataProvider.createDummyGroupId();
                callback.onSuccess(groupId);
            } catch (Exception e) {
                callback.onError("Failed to create group: " + e.getMessage());
            }
        });
    }

    /**
     * Fetch all orders for a group (for checkout)
     */
    public void fetchGroupOrders(Long groupId, ApiCallback<List<OrderBundle>> callback) {
        simulateNetworkDelay(() -> {
            try {
                List<OrderBundle> orders = DummyDataProvider.getDummyOrdersForGroup(groupId);
                callback.onSuccess(orders);
            } catch (Exception e) {
                callback.onError("Failed to fetch group orders: " + e.getMessage());
            }
        });
    }

    /**
     * Delete group (simulates success)
     */
    public void deleteGroup(Long groupId, ApiCallback<Void> callback) {
        simulateNetworkDelay(() -> {
            try {
                callback.onSuccess(null);
            } catch (Exception e) {
                callback.onError("Failed to delete group: " + e.getMessage());
            }
        });
    }

    /**
     * Test connection
     */
    public void testConnection(ApiCallback<String> callback) {
        simulateNetworkDelay(() -> {
            try {
                callback.onSuccess("✅ Mock API Connection OK!");
            } catch (Exception e) {
                callback.onError("Connection failed: " + e.getMessage());
            }
        });
    }

    /**
     * Helper method to simulate network delay
     */
    private void simulateNetworkDelay(Runnable action) {
        new Handler(Looper.getMainLooper()).postDelayed(action, NETWORK_DELAY_MS);
    }
}
