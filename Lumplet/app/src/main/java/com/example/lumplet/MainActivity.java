package com.example.lumplet;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent; // Dodaj import
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private Authentication auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicjalizacja klasy Authentication
        auth = new Authentication(this);

        Button snkrsButton = findViewById(R.id.snkrsBut);
        Button clothesButton = findViewById(R.id.clothesBut);
        Button accessButton = findViewById(R.id.accessBut);
        ImageView iconUser = findViewById(R.id.iconUser);
        ImageView iconCart = findViewById(R.id.iconCart);
        TextView userEmail = findViewById(R.id.userEmail);
        Button logoutButton = findViewById(R.id.logoutBut);



        // Jeśli użytkownik nie jest zalogowany wyświetl ikone do logowania
        if (auth.getCurrentUser() == null) {
            iconUser.setVisibility(View.VISIBLE);
            userEmail.setVisibility(View.INVISIBLE);
            logoutButton.setVisibility(View.INVISIBLE);
            iconCart.setVisibility(View.INVISIBLE);
        } else {
            iconUser.setVisibility(View.INVISIBLE);
            userEmail.setVisibility(View.VISIBLE);
            iconCart.setVisibility(View.VISIBLE);
            logoutButton.setVisibility(View.VISIBLE);
            userEmail.setText(auth.getCurrentUser().getEmail());
        }

        snkrsButton.setOnClickListener(view -> {
            // Obsługa kliknięcia przycisku "Obuwie"
            // Przekierowanie do sceny "item_list.xml" (ListaPrzedmiotowActivity)
            Intent intent = new Intent(MainActivity.this, ItemList.class);
            Bundle extras = new Bundle();
            extras.putString("kategoria", "Obuwie");
            intent.putExtras(extras);
            startActivity(intent);
        });

        clothesButton.setOnClickListener(view -> {
            // Obsługa kliknięcia przycisku "Ubrania"
            // Przekierowanie do sceny "item_list.xml" (ItemList)
            Intent intent = new Intent(MainActivity.this, ItemList.class);
            Bundle extras = new Bundle();
            extras.putString("kategoria", "Ubrania");
            intent.putExtras(extras);
            startActivity(intent);
        });

        accessButton.setOnClickListener(view -> {
            // Obsługa kliknięcia przycisku "Akcesoria"
            // Przekierowanie do sceny "item_list.xml" (ItemList)
            Intent intent = new Intent(MainActivity.this, ItemList.class);
            Bundle extras = new Bundle();
            extras.putString("kategoria", "Akcesoria");
            intent.putExtras(extras);
            startActivity(intent);
        });

        iconUser.setOnClickListener(view -> {
            // Obsługa kliknięcia logo "ludzika"
            // Przekierowanie do sceny "login_activity.xml" (LoginActivity)
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(view -> {
            // Obsługa kliknięcia przycisku "Wyloguj"
            // Wylogowanie aktualnie zalogowanego użytkownika
            auth.signOut();

            // Zerowanie stosu aktywności i odświeżenie sceny
            Intent refresh = new Intent(MainActivity.this, MainActivity.class);
            refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(refresh);
            finish();
        });

        iconCart.setOnClickListener(view -> {
            // Obsługa kliknięcia logo "koszyka"
            // Przekierowanie do sceny "cart_activity.xml"
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });
    }
}
