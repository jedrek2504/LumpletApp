package com.example.lumplet;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ProductView extends AppCompatActivity {
    private Item currentItem;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_view);

        // Find the addToCartButton and set an onClickListener
        Button addToCartButton = findViewById(R.id.addToCartButton);

        // Pobierz dane produktu z Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String productName = extras.getString("productName");
            String productDescription = extras.getString("productDescription");
            double productPrice = extras.getDouble("productPrice");
            String productCategory = extras.getString("productCategory");
            // Możesz dodać kod do pobrania zdjęcia produktu, jeśli go używasz

            currentItem = new Item(productName, productPrice, productCategory, productDescription);

            // Znajdź widoki w układzie XML
            TextView nameTextView = findViewById(R.id.productNameTextView);
            TextView descriptionTextView = findViewById(R.id.productDescriptionTextView);
            TextView priceTextView = findViewById(R.id.productPriceTextView);
            TextView categoryTextView = findViewById(R.id.productCategoryTextView);
            ImageView productImageView = findViewById(R.id.productImageView); // Odpowiedni widok ImageView

            // Wyświetl dane produktu w widoku
            nameTextView.setText(productName);
            descriptionTextView.setText(productDescription);
            priceTextView.setText("Cena: " + productPrice + " PLN");
            categoryTextView.setText("Kategoria: " + productCategory);
            // Możesz również dodać kod do wyświetlenia zdjęcia, jeśli jest dostępne

            addToCartButton.setOnClickListener(view -> {
                if (currentItem != null) {
                    Cart.getInstance().addItem(currentItem);
                    Toast.makeText(ProductView.this, currentItem.getName() + " added to cart!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
