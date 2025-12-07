package com.miun.restaurantorderapp.models;

import java.util.List;

/**
 * CarteMenu - Model class representing a menu
 *
 * This matches the backend CarteMenu entity from Payara server.
 * Contains a collection of MenuItems.
 *
 * Backend entity structure:
 * - Long id
 * - String name
 * - List<MenuItem> menuItems (from many-to-many relationship)
 */
public class CarteMenu {

    // TODO: Add fields matching backend CarteMenu entity
    // - private Long id;
    // - private String name;
    // - private List<MenuItem> menuItems;

    // TODO: Add constructors
    // - Empty constructor for JSON deserialization
    // - Constructor with all fields

    // TODO: Add getters and setters
    // - getId() / setId()
    // - getName() / setName()
    // - getMenuItems() / setMenuItems()

    // TODO: Override toString() for debugging

    // TODO: Consider helper methods
    // - Method to get all menu items flattened
    // - Method to filter items by category (if categories are added later)
}
