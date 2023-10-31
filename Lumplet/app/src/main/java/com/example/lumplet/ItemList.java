package com.example.lumplet;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ItemList extends AppCompatActivity {

    private FirebaseFirestore db;
    private ListView listView;
    TextView kategoriaTextView;
    private final ArrayList<Item> snkrsList = new ArrayList<>();
    private final ArrayList<Item> clothesList = new ArrayList<>();
    private final ArrayList<Item> accessoriesList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list);
        kategoriaTextView = findViewById(R.id.kategoriaTextView);
        db = FirebaseFirestore.getInstance();
        listView = findViewById(R.id.listView);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String kategoria = extras.getString("kategoria");
            if (kategoria != null) {
                kategoriaTextView.setText(kategoria);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        db.collection("items")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String name = document.getString("name");
                            double price = document.getDouble("price");
                            String category = document.getString("category");
                            String description = document.getString("description");

                            Item item = new Item(name, price, category, description);
                            if (category != null) {
                                switch (category) {
                                    case "sneakers":
                                        snkrsList.add(item);
                                        break;
                                    case "cloth":
                                        clothesList.add(item);
                                        break;
                                    case "accessory":
                                        accessoriesList.add(item);
                                        break;
                                }
                            }
                        }

                        // Przekaz odpowiednią listę do adaptera i zaktualizuj ListView
                        String currentCategory = kategoriaTextView.getText().toString();
                        if (currentCategory.equalsIgnoreCase("Obuwie")) {
                            ProductAdapter adapter = new ProductAdapter(ItemList.this, snkrsList);
                            listView.setAdapter(adapter);
                        } else if (currentCategory.equalsIgnoreCase("Ubrania")) {
                            ProductAdapter adapter = new ProductAdapter(ItemList.this, clothesList);
                            listView.setAdapter(adapter);
                        } else if (currentCategory.equalsIgnoreCase("Akcesoria")) {
                            ProductAdapter adapter = new ProductAdapter(ItemList.this, accessoriesList);
                            listView.setAdapter(adapter);
                        }
                    } else {
                        throw new IllegalArgumentException("Błąd w przekazaniu listy");
                    }
                });
    }
}
