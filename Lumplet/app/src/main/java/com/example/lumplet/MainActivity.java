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
        Button logoutButton = findViewById(R.id.logoutBut);


        // Jeśli użytkownik nie jest zalogowany wyświetl ikone do logowania
        if (auth.getCurrentUser() == null) {
            iconUser.setVisibility(View.VISIBLE);
            userEmail.setVisibility(View.INVISIBLE);
            logoutButton.setVisibility(View.INVISIBLE);
        } else {
            iconUser.setVisibility(View.INVISIBLE);
            userEmail.setVisibility(View.VISIBLE);
            userEmail.setText(auth.getCurrentUser().getEmail());
            logoutButton.setVisibility(View.VISIBLE);
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
            // Call the signOut method from your auth object
            auth.signOut();

            // Refresh the current activity to update the UI
            finish();
            startActivity(getIntent());

            // Optionally, if you want to clear the activity stack, you can add the following flags
            Intent refresh = new Intent(MainActivity.this, MainActivity.class);
            refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(refresh);
        });

    }
}
