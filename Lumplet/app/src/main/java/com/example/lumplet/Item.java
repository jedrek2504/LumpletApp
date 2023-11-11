package com.example.lumplet;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Item implements Parcelable {
    private String itemId;
    private String name;
    private double price;
    private String category;
    private String description;

    // Konstruktor z ID
    public Item(String itemId, String name, double price, String category, String description) {
        this.itemId = itemId;
        this.name = name;
        this.price = price;
        this.category = category;
        this.description = description;
    }

    // Konstruktor bez ID (Firestore generuje)
    public Item(String name, double price, String category, String description) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.description = description;
    }

    protected Item(Parcel in) {
        itemId = in.readString();
        name = in.readString();
        price = in.readDouble();
        category = in.readString();
        description = in.readString();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Nadpisanie toString aby produkty były poprawnie wyświetlane w koszyku
    @Override
    public String toString() {
        return getName() + " - " + String.format("$%.2f", getPrice());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(itemId);
        dest.writeString(name);
        dest.writeDouble(price);
        dest.writeString(category);
        dest.writeString(description);
    }
}
