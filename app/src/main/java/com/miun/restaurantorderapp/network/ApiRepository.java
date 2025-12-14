package com.miun.restaurantorderapp.network;

import com.miun.restaurantorderapp.models.OrderBundle;
import com.miun.restaurantorderapp.models.OrderStatusResponse;

import java.util.List;

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

    //wrappers
    public void createGroup(ApiCallback<Long> callback) {
        apiService.createGroup(callback);
    }

    public void fetchGroupOrders(Long groupId, ApiCallback<List<OrderBundle>> callback) {
        apiService.fetchGroupOrders(groupId, callback);
    }

    public void deleteGroup(Long groupId, ApiCallback<Void> callback) {
        apiService.deleteGroup(groupId, callback);
    }



    // TODO: implement checkOrderStatus, fetch, etc
}
