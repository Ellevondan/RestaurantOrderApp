package com.miun.restaurantorderapp.models;

import com.google.gson.annotations.SerializedName;

public class ModifiedItem {
    @SerializedName("id")
    private Long id;  // MenuItem ID

    @SerializedName("name")
    private String name;

    @SerializedName("quantity")
    private Integer quantity;

    @SerializedName("selectedAllergens")
    private String selectedAllergens;  // "Gluten,Dairy"

    @SerializedName("specialInstructions")
    private String specialInstructions;

    @SerializedName("comments")
    private String comments;

    @SerializedName("orderedAt")
    private String orderedAt;  // "2025-12-11T12:34:56"

    // Getters o setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setSelectedAllergens(String selectedAllergens) {
        this.selectedAllergens = selectedAllergens;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setOrderedAt(String orderedAt) {
        this.orderedAt = orderedAt;
    }

}