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

    private double totalCartValue = 0.0; // Całkowita wartość koszyka
    private ArrayAdapter<Item> itemsAdapter;
    private TextView totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);

        ListView cartItemsList = findViewById(R.id.cartItemsList);
        totalPrice = findViewById(R.id.totalPrice);
        Button proceedToCheckout = findViewById(R.id.proceedToCheckout);

        // Pobieramy przedmioty z singletonu Cart
        List<Item> cartItems = Cart.getInstance().getItems();

        // Wyświetlamy z pomocą adaptera przedmioty dodane do koszyka
        itemsAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                cartItems
        );

        cartItemsList.setAdapter(itemsAdapter);

        // Liczymy i wyświetlamy łączną wartość produktów w koszyku
        calculateTotal(cartItems);
        totalPrice.setText(String.format("Total: $%.2f", totalCartValue));

        // Obsługa przycisku "Checkout"
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

    // Funkcja obsługująca usuwanie produktów z koszyka z bazy danych
    private void checkoutCart() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<Item> cartItems = Cart.getInstance().getItems();
        List<Item> itemsToRemove = new ArrayList<>();

        for (Item item : cartItems) {
            String itemId = item.getItemId(); // Pobieramy Id produktu

            db.collection("items").document(itemId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        // Dodajemy przedmiot do listy przedmiotów do usunięcia
                        itemsToRemove.add(item);
                        Log.d("CartActivity", "Item successfully deleted from database: " + itemId);

                        // Jeśli wszystkie przedmioty zostały przeiterowane usuwamy je
                        if (itemsToRemove.size() == cartItems.size()) {
                            cartItems.removeAll(itemsToRemove);
                            itemsAdapter.notifyDataSetChanged(); // Informujemy adapter o zmianach
                            totalPrice.setText("Total: $0.00"); // Ustawiamy całkowitą wartość koszyka na 0
                            Toast.makeText(this, "Checkout Successful", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.w("CartActivity", "Error deleting item from database", e);
                    });
        }

        // Jeśli w koszyku nie ma żadnych przedmiotów to odświeżamy UI
        if (cartItems.isEmpty()) {
            totalPrice.setText("Total: $0.00");
            Toast.makeText(this, "Checkout Successful", Toast.LENGTH_SHORT).show();
        }
    }
}
