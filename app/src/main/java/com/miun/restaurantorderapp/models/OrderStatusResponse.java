package com.miun.restaurantorderapp.models;

import com.google.gson.annotations.SerializedName;

public class OrderStatusResponse {
    @SerializedName("orderId")
    private Long orderId;

    @SerializedName("isComplete")
    private boolean isDone;

    public Long getId() {
        return orderId;
    }

    public void setId(Long orderId) {
        this.orderId = orderId;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setComplete(boolean complete) {
        isDone = complete;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}