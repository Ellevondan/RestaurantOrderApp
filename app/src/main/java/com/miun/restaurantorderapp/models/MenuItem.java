package com.miun.restaurantorderapp.models;

/**
 * MenuItem - Model class representing a menu item
 *
 * This matches the backend MenuItem entity from Payara server.
 * Received from server as part of CarteMenu.
 *
 * Backend entity fields:
 * - Long id
 * - String name
 * - String description
 * - Double price
 * - (potentially more fields - check with backend team)
 */
public class MenuItem {

    // TODO: Add fields matching backend MenuItem entity
    // - private Long id;
    // - private String name;
    // - private String description;
    // - private Double price;
    // - Any other fields from backend (check MenuItem.java on server)

    // TODO: Add constructors
    // - Empty constructor for JSON deserialization
    // - Constructor with all fields

    // TODO: Add getters and setters for all fields
    // - getId() / setId()
    // - getName() / setName()
    // - getDescription() / setDescription()
    // - getPrice() / setPrice()

    // TODO: Override toString() for debugging
    // - Return formatted string with item details

    // TODO: Consider overriding equals() and hashCode()
    // - Based on id field for proper comparison in lists
}
