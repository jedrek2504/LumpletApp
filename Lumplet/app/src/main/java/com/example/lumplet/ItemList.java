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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;


public class ItemList extends AppCompatActivity {

    private FirebaseFirestore db;
    private DocumentReference snkrsDocRef;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list);
        TextView kategoriaTextView = findViewById(R.id.kategoriaTextView);
        db = FirebaseFirestore.getInstance();
        snkrsDocRef = db.collection("items").document("snkrs1");
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

        // Obsługa błędów
        // Aktualizuj listę z danymi produktów
        // Przekaz listę do adaptera i zaktualizuj ListView
        ListenerRegistration snkrsListener = snkrsDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    // Obsługa błędów
                    return;
                }

                if (documentSnapshot.exists()) {
                    String name = documentSnapshot.getString("name");
                    double price = documentSnapshot.getDouble("price");
                    String category = documentSnapshot.getString("category");
                    String description = documentSnapshot.getString("description");

                    // Aktualizuj listę z danymi produktów
                    Item item = new Item(name, price, category, description);
                    ArrayList<Item> itemList = new ArrayList<>();
                    itemList.add(item);

                    // Przekaz listę do adaptera i zaktualizuj ListView
                    ProductAdapter adapter = new ProductAdapter(ItemList.this, itemList);
                    listView.setAdapter(adapter);
                }
            }
        });
    }

}



