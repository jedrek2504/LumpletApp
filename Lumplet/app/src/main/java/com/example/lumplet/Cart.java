package com.example.lumplet;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private static final Cart instance = new Cart();
    private final List<Item> items;

    private Cart() {
        items = new ArrayList<>();
    }

    public static Cart getInstance() {
        return instance;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public List<Item> getItems() {
        return items;
    }
}