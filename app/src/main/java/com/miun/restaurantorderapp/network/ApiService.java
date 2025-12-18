package com.miun.restaurantorderapp.network;

import com.miun.restaurantorderapp.models.CarteMenu;
import com.miun.restaurantorderapp.models.MenuItem;
import com.miun.restaurantorderapp.models.OrderBundle;
import com.miun.restaurantorderapp.models.OrderStatusResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
 *
 * TODO: Coordinate with backend team for exact API endpoints and request/response formats
 */
public class ApiService {

    private static final String TAG = "ApiService";
    private static final List<Long> activeGroupIds = new ArrayList<>(); // TODO implement this in create/get group
    private final RestaurantApiService api;

    public ApiService() {
        this.api = ApiClient.getRestaurantApiService();
    }

    // 1) Hämta meny
    public void fetchMenu(ApiCallback<List<MenuItem>> callback) {
        api.getMenus().enqueue(new Callback<List<MenuItem>>() {
            @Override
            public void onResponse(Call<List<MenuItem>> call, Response<List<MenuItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Menu request failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<MenuItem>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    // 2) Skicka order
    public void sendOrder(OrderBundle orderBundle, ApiCallback<Long> callback) {
        api.sendOrder(orderBundle).enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Send order failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    // 7) Kolla status på en order (används av repository/polling)
    public void checkOrderStatus(Long orderId, ApiCallback<OrderStatusResponse> callback) {
        api.checkOrderStatus(orderId).enqueue(new Callback<OrderStatusResponse>() {
            @Override
            public void onResponse(Call<OrderStatusResponse> call, Response<OrderStatusResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Status request failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<OrderStatusResponse> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void testConnection(ApiCallback<String> callback) {
        api.testConnection().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void createGroup(ApiCallback<Long> callback) {
        api.createGroup().enqueue(new retrofit2.Callback<Long>() {
            @Override
            public void onResponse(retrofit2.Call<Long> call,
                                   retrofit2.Response<Long> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Skicka tillbaka groupID till den som anropade
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Create group failed: " + response.code());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Long> call, Throwable t) {
                callback.onError("Network error (createGroup): " + t.getMessage());
            }
        });
    }


    public void fetchGroupOrders(Long groupId, ApiCallback<List<OrderBundle>> callback) {
        api.fetchGroupOrders(groupId).enqueue(new Callback<List<OrderBundle>>() {
            @Override
            public void onResponse(Call<List<OrderBundle>> call,
                                   Response<List<OrderBundle>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Fetch group orders failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<OrderBundle>> call, Throwable t) {
                callback.onError("Network error (fetchGroupOrders): " + t.getMessage());
            }
        });
    }

    public void deleteGroup(Long groupId, ApiCallback<Void> callback) {
        api.deleteGroup(groupId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null); // inget body, bara OK
                } else {
                    callback.onError("Delete group failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError("Network error (deleteGroup): " + t.getMessage());
            }
        });
    }

    /**
     * Hämta alla aktiva group IDs från servern
     */
    public void getGroupIds(ApiCallback<List<Long>> callback) {
        api.getActiveGroupIds().enqueue(new Callback<List<Long>>() {
            @Override
            public void onResponse(Call<List<Long>> call, Response<List<Long>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Uppdatera lokal lista
                    activeGroupIds.clear();
                    activeGroupIds.addAll(response.body());
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Fetch group IDs failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Long>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    /**
     * Hämta lokalt cachade group IDs (utan att kontakta servern)
     */
    public List<Long> getCachedGroupIds() {
        return new ArrayList<>(activeGroupIds);
    }

    /**
     * Lägg till ett group ID i listan (när createGroup anropas)
     */
    public void addGroupId(Long groupId) {
        if (!activeGroupIds.contains(groupId)) {
            activeGroupIds.add(groupId);
        }
    }

    /**
     * Ta bort ett group ID från listan (när deleteGroup anropas)
     */
    public void removeGroupId(Long groupId) {
        activeGroupIds.remove(groupId);
    }

}



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

