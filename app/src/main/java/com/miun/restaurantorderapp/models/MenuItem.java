package com.miun.restaurantorderapp.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class MenuItem {
    @SerializedName("id")
    private Long id;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("price")
    private Double price;

    @SerializedName("allergens")
    private String allergens;

    @SerializedName("isMeat")
    private Boolean isMeat;

    @SerializedName("isVegan")
    private Boolean isVegan;

    @SerializedName("isAppetizer")
    private Boolean isAppetizer;

    @SerializedName("isDessert")
    private Boolean isDessert;

    @SerializedName("isHuvud")
    private Boolean isHuvud;

    @SerializedName("isGlutenFree")
    private Boolean isGlutenFree;

    @SerializedName("activeTime")
    private Integer activeTime;

    @SerializedName("waitingTime")
    private Integer waitingTime;

    @SerializedName("totalTime")
    private Integer totalTime;

    @SerializedName("canSubstitute")
    private Boolean canSubstitute;

    @SerializedName("carteAttributesId")
    private Integer carteAttributesId;

    // Basic getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public Double getPrice() { return price; }
    public String getAllergens() { return allergens; }

    // Flag getters
    public Boolean getIsAppetizer() { return isAppetizer; }
    public Boolean getIsDessert() { return isDessert; }
    public Boolean getIsHuvud() { return isHuvud; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(Double price) { this.price = price; }
    public void setAllergens(String allergens) { this.allergens = allergens; }

    public void setIsAppetizer(Boolean appetizer) { isAppetizer = appetizer; }
    public void setIsDessert(Boolean dessert) { isDessert = dessert; }
    public void setIsHuvud(Boolean huvud) { isHuvud = huvud; }

    public void setIsMeat(Boolean meat) { isMeat = meat; }
    public void setIsVegan(Boolean vegan) { isVegan = vegan; }
    public void setIsGlutenFree(Boolean glutenFree) { isGlutenFree = glutenFree; }

    public void setActiveTime(Integer activeTime) { this.activeTime = activeTime; }
    public void setWaitingTime(Integer waitingTime) { this.waitingTime = waitingTime; }
    public void setTotalTime(Integer totalTime) { this.totalTime = totalTime; }

    public void setCanSubstitute(Boolean canSubstitute) { this.canSubstitute = canSubstitute; }
    public void setCarteAttributesId(Integer carteAttributesId) { this.carteAttributesId = carteAttributesId; }

    // Helper: "Gluten, Dairy" -> ["Gluten","Dairy"]
    public List<String> getAllergensList() {
        if (allergens == null || allergens.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(allergens.split(",\\s*"));
    }
}
