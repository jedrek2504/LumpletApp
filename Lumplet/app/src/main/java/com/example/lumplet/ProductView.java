package com.example.lumplet;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProductView extends AppCompatActivity {
    private Item currentItem;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_view);

        // Znajdź widoki w układzie XML
        TextView nameTextView = findViewById(R.id.productNameTextView);
        TextView descriptionTextView = findViewById(R.id.productDescriptionTextView);
        TextView priceTextView = findViewById(R.id.productPriceTextView);
        TextView categoryTextView = findViewById(R.id.productCategoryTextView);
        ImageView productImageView = findViewById(R.id.productImageView); // Widok ImageView
        Button addToCartButton = findViewById(R.id.addToCartButton);

        // Pobierz dane produktu z Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String itemId = extras.getString("itemID");
            String productName = extras.getString("productName");
            String productDescription = extras.getString("productDescription");
            double productPrice = extras.getDouble("productPrice");
            String productCategory = extras.getString("productCategory");
            String productImageUrl = extras.getString("imgUrl");
            Log.d("ProductView", "imgUrl: " + productImageUrl);


            currentItem = new Item(itemId, productName, productPrice, productCategory, productDescription, productImageUrl);

            // Wyświetl dane produktu w widoku
            nameTextView.setText(productName);
            descriptionTextView.setText(productDescription);
            priceTextView.setText("Cena: $" + productPrice);
            categoryTextView.setText("Kategoria: " + productCategory);

            // Ładowanie obrazu, jeśli jest dostępny
            if (productImageUrl != null && !productImageUrl.isEmpty()) {
                loadProductImage(productImageUrl, productImageView);
            }

            addToCartButton.setOnClickListener(view -> {
                if (currentItem != null) {
                    Cart.getInstance().addItem(currentItem);
                    Toast.makeText(ProductView.this, currentItem.getName() + " added to cart!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void loadProductImage(String imageUrl, ImageView imageView) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(imageUrl);

        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(this)
                    .load(uri.toString())
                    .into(imageView);
        }).addOnFailureListener(exception -> {
            // Obsługa błędów
            Toast.makeText(this, "Nie udało się załadować obrazu", Toast.LENGTH_SHORT).show();
        });
    }
}
