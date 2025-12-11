package com.miun.restaurantorderapp.network;

import com.miun.restaurantorderapp.models.OrderStatusResponse;

public class ApiRepository {

    private final ApiService apiService;
    public ApiRepository() {
        this.apiService = new ApiService();
    }


    public void checkOrderStatus(Long currentOrderId, ApiCallback<OrderStatusResponse> apiCallback) {
        apiService.checkOrderStatus(currentOrderId, apiCallback);
    }

    public void testConnection(ApiCallback<String> callback) {
        apiService.testConnection(callback);
    }

    // TODO: implement checkOrderStatus, fetch, etc
}
