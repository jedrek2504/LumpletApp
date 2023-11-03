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
        TextView userEmail = findViewById(R.id.userEmail);

        // Jeśli użytkownik nie jest zalogowany wyświetl ikone do logowania
        if (auth.getCurrentUser() == null) {
            iconUser.setVisibility(View.VISIBLE);
            userEmail.setVisibility(View.INVISIBLE);
        } else {
            iconUser.setVisibility(View.INVISIBLE);
            userEmail.setVisibility(View.VISIBLE);
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
    }
}
