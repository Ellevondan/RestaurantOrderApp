package com.miun.restaurantorderapp.network;

import com.miun.restaurantorderapp.models.CarteMenu;
import com.miun.restaurantorderapp.models.OrderBundle;

import java.util.List;

/**
 * ApiService - Handles all REST API calls to the Payara application server
 *
 * This class manages communication with the backend server for:
 * - Creating groups
 * - Fetching menu items
 * - Submitting orders
 * - Polling order status
 * - Fetching final bill
 *
 * Technology options:
 * - Retrofit (recommended) - Type-safe REST client
 * - Volley - Android's HTTP library
 * - OkHttp - Lower-level HTTP client
 *
 * TODO: Coordinate with backend team for exact API endpoints and request/response formats
 */
public class ApiService {

    // TODO: Add configuration constants
    // - private static final String BASE_URL = "http://your-payara-server:8080/api/";
    // - (Get actual URL from backend team)

    // TODO: Add singleton instance
    // - private static ApiService instance;
    // - public static synchronized ApiService getInstance()

    // TODO: Add HTTP client setup (Retrofit/Volley/OkHttp)
    // - Configure base URL
    // - Add JSON converter (Gson/Jackson)
    // - Add logging interceptor for debugging
    // - Set timeouts

    // TODO: Implement group management methods
    /**
     * Create a new group on the server
     * Called once per device on first launch
     *
     * @return String groupId returned from server
     */
    // public void createGroup(Callback<String> callback)

    // TODO: Implement menu fetching methods
    /**
     * Fetch all menu items from the server
     * Returns list of CarteMenu objects with their MenuItems
     *
     * @return List<CarteMenu> containing all available menu items
     */
    // public void fetchMenuItems(Callback<List<CarteMenu>> callback)

    // TODO: Implement order submission methods
    /**
     * Submit a new order bundle to the server
     *
     * @param orderBundle The order to submit (includes groupId, tableNumber, menuItem, category, etc.)
     * @return Success/failure response
     */
    // public void submitOrder(OrderBundle orderBundle, Callback<OrderBundle> callback)

    /**
     * Submit multiple orders at once
     *
     * @param orderBundles List of orders to submit
     */
    // public void submitOrders(List<OrderBundle> orderBundles, Callback<List<OrderBundle>> callback)

    // TODO: Implement order polling methods
    /**
     * Poll server for order bundles by group ID
     * Used to check status of orders (pending, in_progress, ready, completed)
     *
     * @param groupId The group ID to fetch orders for
     * @return List<OrderBundle> with current status
     */
    // public void pollOrdersByGroup(String groupId, Callback<List<OrderBundle>> callback)

    /**
     * Poll server for orders by table number
     *
     * @param groupId The group ID
     * @param tableNumber The table number
     * @return List<OrderBundle> for this specific table
     */
    // public void pollOrdersByTable(String groupId, int tableNumber, Callback<List<OrderBundle>> callback)

    // TODO: Implement billing methods
    /**
     * Fetch all order bundles for a group to calculate final bill
     *
     * @param groupId The group ID
     * @return List<OrderBundle> with all orders for this group
     */
    // public void fetchAllOrdersForBilling(String groupId, Callback<List<OrderBundle>> callback)

    /**
     * Mark a group as completed/paid (if backend supports this)
     *
     * @param groupId The group ID to mark as completed
     */
    // public void markGroupCompleted(String groupId, Callback<Void> callback)

    // TODO: Add callback interface for async responses
    /**
     * Generic callback interface for API responses
     *
     * @param <T> The type of data expected in response
     */
    // public interface Callback<T> {
    //     void onSuccess(T data);
    //     void onError(String errorMessage);
    // }

    // TODO: Add error handling
    // - Method to handle network errors
    // - Method to handle HTTP error codes
    // - Method to parse error responses from server

    // TODO: Add helper methods
    // - Method to check network connectivity
    // - Method to build request headers
    // - Method to log requests/responses for debugging
}
