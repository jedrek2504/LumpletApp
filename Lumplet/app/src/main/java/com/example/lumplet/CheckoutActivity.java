package com.example.lumplet;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CheckoutActivity extends AppCompatActivity {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText streetEditText;
    private EditText zipcodeEditText;
    private EditText cityEditText;
    private double totalCartValue; // Całkowita wartość koszyka

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Odczytaj przekazane dane z CartActivity
        totalCartValue = getIntent().getDoubleExtra("totalCartValue", 0.0);

        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        streetEditText = findViewById(R.id.streetEditText);
        zipcodeEditText = findViewById(R.id.zipcodeEditText);
        cityEditText = findViewById(R.id.cityEditText);
        Button confirmOrderButton = findViewById(R.id.confirmOrderButton);

        confirmOrderButton.setOnClickListener(v -> {
            // Odczytaj dane z pól
            String firstName = firstNameEditText.getText().toString();
            String lastName = lastNameEditText.getText().toString();
            String street = streetEditText.getText().toString();
            String zipcode = zipcodeEditText.getText().toString();
            String city = cityEditText.getText().toString();

            // logika obsługi zamówienia
            // zapisywanie danych do bazy danych, przetwarzanie zamówienia, itp.

            // Wyświetl komunikat potwierdzenia
            Toast.makeText(CheckoutActivity.this, "Zamówienie złożone. Całkowita wartość: $" + totalCartValue, Toast.LENGTH_LONG).show();

            // Zamknij aktywność CheckoutActivity
            finish();
        });
    }
}
