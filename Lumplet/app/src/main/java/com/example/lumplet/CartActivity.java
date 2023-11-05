package com.example.lumplet;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private double totalCartValue = 0.0;
    private ArrayAdapter<Item> itemsAdapter; // Declare itemsAdapter as a field
    private TextView totalPrice; // Declaration without initialization

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);

        ListView cartItemsList = findViewById(R.id.cartItemsList);
        totalPrice = findViewById(R.id.totalPrice); // Initialization moved inside onCreate
        Button proceedToCheckout = findViewById(R.id.proceedToCheckout);

        // Fetch the items from the Cart singleton
        List<Item> cartItems = Cart.getInstance().getItems();

        itemsAdapter = new ArrayAdapter<>(
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
                checkoutCart();
            }
        });
    }

    private void calculateTotal(List<Item> items) {
        totalCartValue = 0.0;
        for (Item item : items) {
            totalCartValue += item.getPrice();
        }
    }

    private void checkoutCart() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<Item> cartItems = Cart.getInstance().getItems();
        List<Item> itemsToRemove = new ArrayList<>();

        for (Item item : cartItems) {
            String itemId = item.getItemId(); // Ensure you have the ID

            db.collection("items").document(itemId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        // Add the item to the list of items to be removed
                        itemsToRemove.add(item);
                        Log.d("CartActivity", "Item successfully deleted from database: " + itemId);

                        // If all items have been processed, remove them from the cart
                        if (itemsToRemove.size() == cartItems.size()) {
                            cartItems.removeAll(itemsToRemove);
                            itemsAdapter.notifyDataSetChanged(); // Notify the adapter after changes
                            totalPrice.setText("Total: $0.00"); // Update total price
                            Toast.makeText(this, "Checkout Successful", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.w("CartActivity", "Error deleting item from database", e);
                    });
        }

        // If there were no items to begin with, we should still reset the UI
        if (cartItems.isEmpty()) {
            totalPrice.setText("Total: $0.00");
            Toast.makeText(this, "Checkout Successful", Toast.LENGTH_SHORT).show();
        }
    }
}
