package com.example.lumplet;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private ListView cartItemsList;
    private TextView totalPrice;
    private Button proceedToCheckout;
    private ArrayList<Item> cartItems = new ArrayList<>();
    private double totalCartValue = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);

        cartItemsList = findViewById(R.id.cartItemsList);
        totalPrice = findViewById(R.id.totalPrice);
        proceedToCheckout = findViewById(R.id.proceedToCheckout);

        // This is where you would ideally retrieve the cart items from a cart management system or from an intent
        // For the sake of this example, we're just initializing the cart with a few items.
        // In a real application, you would remove this and initialize your cart from saved state or a database.
        initializeCartWithDummyData();

        ArrayAdapter<Item> itemsAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                cartItems
        );

        cartItemsList.setAdapter(itemsAdapter);

        // Calculate total price and display
        calculateTotal();
        totalPrice.setText(String.format("Total: $%.2f", totalCartValue));

        proceedToCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Here you would add logic to proceed with the checkout process
            }
        });
    }

    private void calculateTotal() {
        totalCartValue = 0.0;
        for (Item item : cartItems) {
            totalCartValue += item.getPrice();
        }
    }

    private void initializeCartWithDummyData() {
        // Here we add dummy items to the cart, you should replace this with real cart management logic
        cartItems.add(new Item("Sneakers", 59.99, "Footwear", "Comfortable running sneakers"));
        cartItems.add(new Item("T-Shirt", 29.99, "Clothing", "Cotton graphic tee"));
        // Update the total price whenever items are added to the cart
        calculateTotal();
    }
}
