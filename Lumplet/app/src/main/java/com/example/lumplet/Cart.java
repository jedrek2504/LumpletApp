package com.example.lumplet;

import java.util.ArrayList;
import java.util.List;

// Singleton reprezentujący koszyk
public class Cart {
    private static final Cart instance = new Cart();
    private final List<Item> items;

    private Cart() {
        items = new ArrayList<>();
    }

    public static Cart getInstance() {
        return instance;
    }

    public boolean addItem(Item item) {
        for (Item existingItem : items) {
            if (existingItem.getItemId().equals(item.getItemId())) {
                return false;
            }
        }
        items.add(item);
        return true;
    }


    public List<Item> getItems() {
        return items;
    }

    public void removeItem(Item item) {
        items.remove(item);
    }
}
