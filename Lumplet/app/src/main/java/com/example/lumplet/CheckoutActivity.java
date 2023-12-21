package com.example.lumplet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText streetEditText;
    private EditText zipcodeEditText;
    private EditText cityEditText;
    private double totalCartValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        totalCartValue = getIntent().getDoubleExtra("totalCartValue", 0.0);

        ArrayList<Item> cartItems = getIntent().getParcelableArrayListExtra("cartItems");

        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        streetEditText = findViewById(R.id.streetEditText);
        zipcodeEditText = findViewById(R.id.zipcodeEditText);
        cityEditText = findViewById(R.id.cityEditText);
        Button confirmOrderButton = findViewById(R.id.confirmOrderButton);

        confirmOrderButton.setOnClickListener(v -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                confirmOrder(user, cartItems);
            } else {
                Toast.makeText(CheckoutActivity.this, "Użytkownik nie jest zalogowany. Zaloguj się, aby złożyć zamówienie.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void confirmOrder(FirebaseUser user, List<Item> cartItems) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        String orderDate = dateFormat.format(new Date());
        String userId = user.getUid();
        String street = streetEditText.getText().toString();
        String zipcode = zipcodeEditText.getText().toString();
        String city = cityEditText.getText().toString();
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ordersCollection = db.collection("orders");

        Map<String, Object> orderData = new HashMap<>();
        orderData.put("userId", userId);
        orderData.put("orderDate", orderDate);
        orderData.put("totalAmount", totalCartValue);
        orderData.put("firstName", firstName);
        orderData.put("lastName", lastName);

        Map<String, Object> shippingAddress = new HashMap<>();
        shippingAddress.put("street", street);
        shippingAddress.put("zipcode", zipcode);
        shippingAddress.put("city", city);
        orderData.put("shippingAddress", shippingAddress);

        List<String> productIds = new ArrayList<>();
        for (Item item : cartItems) {
            productIds.add(item.getItemId());
        }
        orderData.put("productIds", productIds);

        ((CollectionReference) ordersCollection).add(orderData)
                .addOnSuccessListener(documentReference -> {
                    // Zamówienie zostało pomyślnie dodane
                    String orderId = documentReference.getId();
                    moveItemsToArchive(productIds);
                    Toast.makeText(CheckoutActivity.this, "Zamówienie o numerze: " + orderId + " zostało pomyślnie dodane.", Toast.LENGTH_SHORT).show();
                    Intent mainIntent = new Intent(CheckoutActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.w("CheckoutActivity", "Błąd podczas dodawania zamówienia do bazy danych", e);
                    Toast.makeText(CheckoutActivity.this, "Wystąpił błąd podczas składania zamówienia.", Toast.LENGTH_SHORT).show();
                });
    }

    private void moveItemsToArchive(List<String> productIds) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        for (String itemId : productIds) {
            DocumentReference itemRef = db.collection("items").document(itemId);

            itemRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        Map<String, Object> itemData = document.getData();

                        // Dodaj produkt do kolekcji "archive"
                        assert itemData != null;
                        db.collection("itemsArchive")
                                .document(itemId) // Użyj itemId jako ID dokumentu w kolekcji "archive"
                                .set(itemData) // Ustaw dane produktu z "items" w "archive"
                                .addOnSuccessListener(aVoid -> {
                                    Log.d("CheckoutActivity", "Produkt o numerze " + itemId + " został dodany do archiwum.");
                                })
                                .addOnFailureListener(e -> {
                                    Log.w("CheckoutActivity", "Błąd podczas dodawania produktu do archiwum", e);
                                });
                    } else {
                        Log.d("CheckoutActivity", "Brak dokumentu produktu o numerze: " + itemId);
                    }

                    itemRef.delete()
                            .addOnSuccessListener(aVoid -> {
                                Log.d("CheckoutActivity", "Produkt o numerze " + itemId + " został usunięty z kolekcji 'items'.");
                            })
                            .addOnFailureListener(e -> {
                                Log.w("CheckoutActivity", "Błąd podczas usuwania produktu z kolekcji 'items'", e);
                            });
                } else {
                    Log.w("CheckoutActivity", "Błąd podczas pobierania dokumentu produktu", task.getException());
                }
            });
        }
    }


}