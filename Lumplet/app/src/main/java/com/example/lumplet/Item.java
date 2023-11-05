package com.example.lumplet;

import androidx.annotation.NonNull;

public class Item {
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
}
