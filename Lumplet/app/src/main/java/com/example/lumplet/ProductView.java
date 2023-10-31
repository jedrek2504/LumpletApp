package com.example.lumplet;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ProductView extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_view);

        // Pobierz dane produktu z Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String productName = extras.getString("productName");
            String productDescription = extras.getString("productDescription");
            double productPrice = extras.getDouble("productPrice");
            String productCategory = extras.getString("productCategory");
            // Możesz dodać kod do pobrania zdjęcia produktu, jeśli go używasz

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
        }
    }
}
