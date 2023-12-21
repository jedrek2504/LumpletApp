package com.example.lumplet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private double totalCartValue = 0.0; // Całkowita wartość koszyka
    private CartAdapter itemsAdapter; // Zmiana na CartAdapter
    private TextView totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);

        ListView cartItemsList = findViewById(R.id.cartItemsList);
        totalPrice = findViewById(R.id.totalPrice);
        Button proceedToCheckout = findViewById(R.id.proceedToCheckout);

        List<Item> cartItems = Cart.getInstance().getItems();

        itemsAdapter = new CartAdapter(this, cartItems);
        cartItemsList.setAdapter(itemsAdapter);
        calculateTotal(cartItems);
        totalPrice.setText(String.format("Total: $%.2f", totalCartValue));


        proceedToCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cartItems.isEmpty()) {
                    // Tylko jeśli koszyk zawiera przedmioty
                    Intent checkoutIntent = new Intent(CartActivity.this, CheckoutActivity.class);
                    checkoutIntent.putExtra("totalCartValue", totalCartValue);
                    checkoutIntent.putParcelableArrayListExtra("cartItems", new ArrayList<>(cartItems));
                    startActivity(checkoutIntent);
                } else {
                    // Informuj użytkownika, że koszyk jest pusty
                    Toast.makeText(CartActivity.this, "Your cart is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void updateTotalPrice() {
        calculateTotal(Cart.getInstance().getItems());
    }

    private void calculateTotal(List<Item> items) {
        totalCartValue = 0.0;
        for (Item item : items) {
            totalCartValue += item.getPrice();
        }
        totalPrice.setText(String.format("Total: $%.2f", totalCartValue));
    }
}
