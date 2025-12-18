package com.miun.restaurantorderapp.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * OrderBundle - Model class representing an order sent to/from the server
 *
 * This represents an order bundle that will be sent to the Payara server
 * and later retrieved by the kitchen app.
 *
 * Fields to include (based on backend requirements):
 * - String groupId (the group this order belongs to)
 * - Integer tableNumber (which table ordered this)
 * - MenuItem menuItem (or just menuItemId)
 * - Integer quantity
 * - OrderCategory category (APPETIZER, MAIN, DESSERT, RUSHED)
 * - OrderStatus status (PENDING, IN_PROGRESS, READY, COMPLETED)
 * - Double activeTime (from backend - for kitchen multitasking)
 * - Double idleTime (from backend - for kitchen multitasking)
 * - Timestamp orderTime
 */
public class OrderBundle {


    @SerializedName("id")
    private Long id;

    @SerializedName("groupID")
    private Long groupID;

    @SerializedName(value = "orders", alternate = {"Orders"})
    private List<ModifiedItem> orders;

    @SerializedName("modifyType")
    private String modifyType;

    @SerializedName("isDone")
    private Boolean isDone;

    public long getGroupId() {
        return groupID;
    }


    // TODO: Add enum for order categories
    public enum OrderCategory {
        APPETIZER,
        MAIN,
        DESSERT,
        RUSHED
    }
    // TODO: Add enum for order status
    public enum OrderStatus {
        PENDING,       // Order has been created but not yet sent to kitchen
        IN_PROGRESS,   // Kitchen is preparing the order
        READY,         // Order is ready to be served
        COMPLETED      // Order has been served / finished
    }


    // TODO: Add fields


    private Integer tableNumber;     // which table ordered this
    private Long menuItemId;         // menu item ID
    private String menuItemName;     // name of the menu item
    private Integer quantity;        // how many of this item
    private OrderCategory category;  // APPETIZER, MAIN, DESSERT, RUSHED
    private OrderStatus status;      // PENDING, IN_PROGRESS, READY, COMPLETED
    private Double price;            // price of this item
    private Double activeTime;       // for kitchen multitasking
    private Double idleTime;         // for kitchen multitasking
    private String orderTime;        // timestamp of the order


    // TODO: Add constructors
    public OrderBundle() {
    }
    public OrderBundle(Long groupID, List<ModifiedItem> orders) {
        this.groupID = groupID;
        this.orders = orders;
        this.isDone = false;  // new orders are not done yet
    }
    public OrderBundle(Long id, Long groupID, List<ModifiedItem> orders, Boolean isDone) {
        this.id = id;
        this.groupID = groupID;
        this.orders = orders;
        this.isDone = isDone;
    }




    // TODO: Add getters and setters for all fields
    // Getters and Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getGroupID() { return groupID; }
    public void setGroupID(Long groupID) { this.groupID = groupID; }

    public List<ModifiedItem> getOrders() { return orders; }
    public void setOrders(List<ModifiedItem> orders) { this.orders = orders; }

    public Boolean getIsDone() { return isDone; }
    public void setIsDone(Boolean isDone) { this.isDone = isDone; }

    public Integer getTableNumber() { return tableNumber; }
    public void setTableNumber(Integer tableNumber) { this.tableNumber = tableNumber; }

    public Long getMenuItemId() { return menuItemId; }
    public void setMenuItemId(Long menuItemId) { this.menuItemId = menuItemId; }

    public String getMenuItemName() { return menuItemName; }
    public void setMenuItemName(String menuItemName) { this.menuItemName = menuItemName; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public OrderCategory getCategory() { return category; }
    public void setCategory(OrderCategory category) { this.category = category; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Double getActiveTime() { return activeTime; }
    public void setActiveTime(Double activeTime) { this.activeTime = activeTime; }

    public Double getIdleTime() { return idleTime; }
    public void setIdleTime(Double idleTime) { this.idleTime = idleTime; }

    public String getOrderTime() { return orderTime; }
    public void setOrderTime(String orderTime) { this.orderTime = orderTime; }

    // TODO: Add helper methods
// Calculate total price of the order bundle
    public double calculateTotalPrice() {
        if (orders == null || orders.isEmpty()) return 0.0;

        double total = 0.0;
        for (ModifiedItem item : orders) {
            if (item != null && item.getQuantity() != null && item.getPrice() != null) {
                total += item.getQuantity() * item.getPrice();
            }
        }
        return total;
    }

    // Check if the order is ready for pickup
    public boolean isReadyForPickup() {
        return status != null && status == OrderStatus.READY;
    }

    // - Method to format order time for display

    // TODO: Override toString() for debugging


}
