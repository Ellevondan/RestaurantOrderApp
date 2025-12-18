package com.miun.restaurantorderapp.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.Serializable;


public class MenuItem implements java.io.Serializable{
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

    @SerializedName(value = "activeTime", alternate = {"active_time", "activetime"})
    private Integer activeTime;

    @SerializedName(value = "waitingTime", alternate = {"waiting_time", "waitingtime"})
    private Integer waitingTime;

    @SerializedName(value = "totalTime", alternate = {"total_time", "totaltime"})
    private Integer totalTime;

    @SerializedName(value = "canSubstitute", alternate = {"can_substitute", "cansubstitute"})
    private Boolean canSubstitute;
    @SerializedName(value = "carteAttributesId", alternate = {"carte_attributes_id", "carteattributesid"})
    private Integer carteAttributesId;

    // Getters o setters

    public Long getId() {
        return id;
    }

    public String getName(){
        return name;
    }
    public Double getPrice(){
        return price;
    }
    public String getAllergens(){ // ändra
        return allergens;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setActiveTime(Integer activeTime) {
        this.activeTime = activeTime;
    }

    public void setAllergens(String allergens) {
        this.allergens = allergens;
    }

    public void setIsAppetizer(Boolean appetizer) {
        isAppetizer = appetizer;
    }

    public void setCanSubstitute(Boolean canSubstitute) {
        this.canSubstitute = canSubstitute;
    }

    public void setCarteAttributesId(Integer carteAttributesId) {
        this.carteAttributesId = carteAttributesId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIsDessert(Boolean dessert) {
        isDessert = dessert;
    }

    public void setIsGlutenFree(Boolean glutenFree) {
        isGlutenFree = glutenFree;
    }

    public void setIsHuvud(Boolean huvud) {
        isHuvud = huvud;
    }

    public void setIsMeat(Boolean meat) {
        isMeat = meat;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    public void setIsVegan(Boolean vegan) {
        isVegan = vegan;
    }

    public void setWaitingTime(Integer waitingTime) {
        this.waitingTime = waitingTime;
    }

    public Boolean getIsDessert() {
        return isDessert;
    }

    public Boolean getIsAppetizer() {
        return isAppetizer;
    }

    public Boolean getIsHuvud() {
        return isHuvud;
    }

    public String getDescription() {
        return description;
    }

    // Helper method för att konvertera allergen-string till lista
    public List<String> getAllergensList() {
        if (allergens == null || allergens.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(allergens.split(",\\s*"));
    }


}