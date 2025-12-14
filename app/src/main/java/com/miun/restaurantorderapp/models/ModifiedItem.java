package com.miun.restaurantorderapp.models;

import com.google.gson.annotations.SerializedName;

public class ModifiedItem {
    @SerializedName("id")
    private Long id;

    @SerializedName("originalID")
    private Long originalId;  // MenuItem ID

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

    private transient Double price;

    // Getters o setters

    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() { return id; }

    public void setOriginalId(Long originalId) { this.originalId = originalId; }
    public Long getOriginalId() { return originalId; }

    public void setName(String name) { this.name = name; }
    public String getName() { return name; }


    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Integer getQuantity() { return quantity; }

    public void setPrice(Double price) { this.price = price; }
    public Double getPrice() { return price; }



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