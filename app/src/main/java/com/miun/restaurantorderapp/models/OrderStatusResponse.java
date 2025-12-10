package com.miun.restaurantorderapp.models;

import com.google.gson.annotations.SerializedName;

public class OrderStatusResponse {
    @SerializedName("orderId")
    private Long orderId;

    @SerializedName("isComplete")
    private boolean isComplete;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}