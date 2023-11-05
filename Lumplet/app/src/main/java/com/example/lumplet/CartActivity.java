package com.example.lumplet;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private double totalCartValue = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);

        ListView cartItemsList = findViewById(R.id.cartItemsList);
        TextView totalPrice = findViewById(R.id.totalPrice);
        Button proceedToCheckout = findViewById(R.id.proceedToCheckout);

        // Fetch the items from the Cart singleton
        List<Item> cartItems = Cart.getInstance().getItems();

        ArrayAdapter<Item> itemsAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                cartItems
        );

        cartItemsList.setAdapter(itemsAdapter);

        // Calculate total price and display
        calculateTotal(cartItems);
        totalPrice.setText(String.format("Total: $%.2f", totalCartValue));

        proceedToCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Here you would add logic to proceed with the checkout process
            }
        });
    }

    private void calculateTotal(List<Item> items) {
        totalCartValue = 0.0;
        for (Item item : items) {
            totalCartValue += item.getPrice();
        }
    }
}