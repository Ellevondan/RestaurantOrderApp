package com.miun.restaurantorderapp.models;

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

    // TODO: Add enum for order categories
    // public enum OrderCategory {
    //     APPETIZER, MAIN, DESSERT, RUSHED
    // }

    // TODO: Add enum for order status
    // public enum OrderStatus {
    //     PENDING, IN_PROGRESS, READY, COMPLETED
    // }

    // TODO: Add fields
    // - private Long id; (from server)
    // - private String groupId;
    // - private Integer tableNumber;
    // - private Long menuItemId; (or MenuItem object)
    // - private String menuItemName; (for display)
    // - private Integer quantity;
    // - private OrderCategory category;
    // - private OrderStatus status;
    // - private Double price;
    // - private Double activeTime; (from backend)
    // - private Double idleTime; (from backend)
    // - private String orderTime; (timestamp)

    // TODO: Add constructors
    // - Empty constructor for JSON deserialization
    // - Constructor for creating new orders (before sending to server)
    // - Constructor with all fields (for received orders)

    // TODO: Add getters and setters for all fields

    // TODO: Add helper methods
    // - Method to calculate total price (quantity * item price)
    // - Method to check if order is ready for pickup
    // - Method to format order time for display

    // TODO: Override toString() for debugging
}
