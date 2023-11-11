package com.example.lumplet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ItemList extends AppCompatActivity {

    private FirebaseFirestore db;
    private ListView listView;
    TextView kategoriaTextView;
    private final ArrayList<Item> snkrsList = new ArrayList<>();
    private final ArrayList<Item> clothesList = new ArrayList<>();
    private final ArrayList<Item> accessoriesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list);
        kategoriaTextView = findViewById(R.id.kategoriaTextView);
        db = FirebaseFirestore.getInstance();
        listView = findViewById(R.id.listView);

        SeekBar seekBarPrice = findViewById(R.id.seekBarPrice);
        TextView seekBarValueText = findViewById(R.id.seekBarValueText);
        Button filterButton = findViewById(R.id.filterNameBut);
        EditText filterNameText = findViewById(R.id.filterNameText);


        seekBarPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Ustawienie kroku wartości seekbar na 100
                int stepSize = 100;
                progress = (progress / stepSize) * stepSize;
                seekBar.setProgress(progress);

                // Ustawienie odpowiedniej wartości nad seekBar
                seekBarValueText.setText(String.valueOf(progress));
            }

            // Wymagane nadpisujące metody przez seekBar
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        filterButton.setOnClickListener(v -> {
            String filterName = filterNameText.getText().toString().toLowerCase();
            double filterPrice = Double.parseDouble(seekBarValueText.getText().toString());

            ArrayList<Item> filteredList = new ArrayList<>();
            ArrayList<Item> targetList;

            // Wybieramy odpowiednią liste w zależności od kategorii
            String currentCategory = kategoriaTextView.getText().toString();
            if (currentCategory.equalsIgnoreCase("Obuwie")) {
                targetList = snkrsList;
            } else if (currentCategory.equalsIgnoreCase("Ubrania")) {
                targetList = clothesList;
            } else if (currentCategory.equalsIgnoreCase("Akcesoria")) {
                targetList = accessoriesList;
            } else {
                targetList = new ArrayList<>(); // Empty list if no category matches
            }

            // Filtrowanie listy zadanymi wartościami filterName i filterPrice
            for (Item item : targetList) {
                if (item.getName().toLowerCase().contains(filterName) && item.getPrice() <= filterPrice) {
                    filteredList.add(item);
                }
            }

            // Dodajemy to do adaptera i aktualizujemy liste
            ProductAdapter adapter = new ProductAdapter(ItemList.this, filteredList);
            listView.setAdapter(adapter);
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Pobierz wybrany produkt z listy na podstawie pozycji
            Item selectedItem = (Item) listView.getItemAtPosition(position);

            // Przejdź do widoku produktu (ProductViewActivity)
            Intent productViewIntent = new Intent(ItemList.this, ProductView.class);
            productViewIntent.putExtra("itemID", selectedItem.getItemId());
            productViewIntent.putExtra("productName", selectedItem.getName());
            productViewIntent.putExtra("productDescription", selectedItem.getDescription());
            productViewIntent.putExtra("productPrice", selectedItem.getPrice());
            productViewIntent.putExtra("productCategory", selectedItem.getCategory());
            // Tutaj możesz przekazać więcej informacji, takie jak zdjęcia, jeśli potrzebujesz

            startActivity(productViewIntent);
        });

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

        snkrsList.clear();
        clothesList.clear();
        accessoriesList.clear();

        db.collection("items")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String id = document.getId(); // Pobierz id generowane przez Firebase
                            String name = document.getString("name");
                            double price = document.getDouble("price");
                            String category = document.getString("category");
                            String description = document.getString("description");

                            Item item = new Item(id, name, price, category, description);
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

                        // Przekaż odpowiednią listę do adaptera i zaktualizuj ListView
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