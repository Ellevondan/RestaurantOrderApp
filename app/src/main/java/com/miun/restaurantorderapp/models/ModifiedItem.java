package com.miun.restaurantorderapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ModifiedItem implements Parcelable {

    @SerializedName("id")
    private Long id;

    @SerializedName("originalID")
    private Long originalId;   // MenuItem ID

    @SerializedName("name")
    private String name = "";

    @SerializedName("quantity")
    private Integer quantity = 1;

    @SerializedName("selectedAllergens")
    private String selectedAllergens = ""; // e.g. "Gluten,Dairy"

    @SerializedName("specialInstructions")
    private String specialInstructions = "";

    @SerializedName("comments")
    private String comments = "";

    @SerializedName("orderedAt")
    private String orderedAt = ""; // ISO-8601 string

    // Not sent to backend by Gson, but used by app UI
    private transient Double price = 0.0;

    public ModifiedItem() {}

    public ModifiedItem(Long originalId, String name, Double price, Integer quantity, String comments, String selectedAllergens) {
        this.originalId = originalId;
        this.name = safe(name);
        this.price = (price != null) ? price : 0.0;
        this.quantity = (quantity != null && quantity > 0) ? quantity : 1;

        String c = safe(comments);
        this.comments = c;
        this.specialInstructions = c;

        this.selectedAllergens = safe(selectedAllergens);
    }

    private static String safe(String s) {
        return (s == null) ? "" : s.trim();
    }

    // ---------------- Parcelable ----------------
    protected ModifiedItem(Parcel in) {
        id = (in.readByte() == 0) ? null : in.readLong();
        originalId = (in.readByte() == 0) ? null : in.readLong();
        name = safe(in.readString());
        quantity = (in.readByte() == 0) ? 1 : in.readInt();
        selectedAllergens = safe(in.readString());
        specialInstructions = safe(in.readString());
        comments = safe(in.readString());
        orderedAt = safe(in.readString());
        price = (in.readByte() == 0) ? 0.0 : in.readDouble();

        if (quantity == null || quantity <= 0) quantity = 1;
        if (price == null) price = 0.0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) { dest.writeByte((byte) 0); }
        else { dest.writeByte((byte) 1); dest.writeLong(id); }

        if (originalId == null) { dest.writeByte((byte) 0); }
        else { dest.writeByte((byte) 1); dest.writeLong(originalId); }

        dest.writeString(name);

        if (quantity == null) { dest.writeByte((byte) 0); }
        else { dest.writeByte((byte) 1); dest.writeInt(quantity); }

        dest.writeString(selectedAllergens);
        dest.writeString(specialInstructions);
        dest.writeString(comments);
        dest.writeString(orderedAt);

        if (price == null) { dest.writeByte((byte) 0); }
        else { dest.writeByte((byte) 1); dest.writeDouble(price); }
    }

    @Override
    public int describeContents() { return 0; }

    public static final Creator<ModifiedItem> CREATOR = new Creator<ModifiedItem>() {
        @Override public ModifiedItem createFromParcel(Parcel in) { return new ModifiedItem(in); }
        @Override public ModifiedItem[] newArray(int size) { return new ModifiedItem[size]; }
    };

    // ---------------- Getters/Setters ----------------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOriginalId() { return originalId; }
    public void setOriginalId(Long originalId) { this.originalId = originalId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = safe(name); }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = (quantity != null && quantity > 0) ? quantity : 1; }

    public String getSelectedAllergens() { return selectedAllergens; }
    public void setSelectedAllergens(String selectedAllergens) { this.selectedAllergens = safe(selectedAllergens); }

    public String getSpecialInstructions() { return specialInstructions; }
    public void setSpecialInstructions(String specialInstructions) { this.specialInstructions = safe(specialInstructions); }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = safe(comments); }

    public String getOrderedAt() { return orderedAt; }
    public void setOrderedAt(String orderedAt) { this.orderedAt = safe(orderedAt); }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = (price != null) ? price : 0.0; }
}
