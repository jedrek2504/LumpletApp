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
    private TextView kategoriaTextView;
    private EditText filterNameText;
    private TextView seekBarValueText;
    private SeekBar seekBarPrice;

    private final ArrayList<Item> snkrsList = new ArrayList<>();
    private final ArrayList<Item> clothesList = new ArrayList<>();
    private final ArrayList<Item> accessoriesList = new ArrayList<>();

    // Pola do przechowywania stanu filtracji
    private String lastFilterName = "";
    private double lastFilterPrice = 5000; // Załóżmy, że to maksymalna wartość suwaka

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list);


        kategoriaTextView = findViewById(R.id.kategoriaTextView);
        filterNameText = findViewById(R.id.filterNameText);
        seekBarPrice = findViewById(R.id.seekBarPrice);
        seekBarValueText = findViewById(R.id.seekBarValueText);
        Button filterButton = findViewById(R.id.filterNameBut);
        listView = findViewById(R.id.listView);
        db = FirebaseFirestore.getInstance();

        Button clearFilterButton = findViewById(R.id.clearFilterButton);
        clearFilterButton.setOnClickListener(v -> {
            clearFilters();
        });

        seekBarPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int stepSize = 100;
                progress = (progress / stepSize) * stepSize;
                seekBar.setProgress(progress);
                seekBarValueText.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        filterButton.setOnClickListener(v -> {
            lastFilterName = filterNameText.getText().toString();
            lastFilterPrice = Double.parseDouble(seekBarValueText.getText().toString());
            applyFilter();
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Item selectedItem = (Item) listView.getItemAtPosition(position);
            Intent productViewIntent = new Intent(ItemList.this, ProductView.class);
            productViewIntent.putExtra("itemID", selectedItem.getItemId());
            productViewIntent.putExtra("productName", selectedItem.getName());
            productViewIntent.putExtra("productDescription", selectedItem.getDescription());
            productViewIntent.putExtra("productPrice", selectedItem.getPrice());
            productViewIntent.putExtra("productCategory", selectedItem.getCategory());

            startActivity(productViewIntent);
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String kategoria = extras.getString("kategoria");
            kategoriaTextView.setText(kategoria != null ? kategoria : "");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        applyFilter();
    }

    private void applyFilter() {
        ArrayList<Item> filteredList = new ArrayList<>();
        ArrayList<Item> targetList = getCurrentCategoryList();

        for (Item item : targetList) {
            if (item.getName().toLowerCase().contains(lastFilterName.toLowerCase()) && item.getPrice() <= lastFilterPrice) {
                filteredList.add(item);
            }
        }

        ProductAdapter adapter = new ProductAdapter(ItemList.this, filteredList);
        listView.setAdapter(adapter);
    }

    private ArrayList<Item> getCurrentCategoryList() {
        String currentCategory = kategoriaTextView.getText().toString();
        switch (currentCategory) {
            case "Obuwie":
                return snkrsList;
            case "Ubrania":
                return clothesList;
            case "Akcesoria":
                return accessoriesList;
            default:
                return new ArrayList<>();
        }
    }

    private void clearFilters() {
        lastFilterName = "";
        lastFilterPrice = 5000;
        filterNameText.setText("");
        seekBarPrice.setProgress(5000);
        applyFilter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    private void loadData() {
        snkrsList.clear();
        clothesList.clear();
        accessoriesList.clear();

        db.collection("items")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String id = document.getId();
                            String name = document.getString("name");
                            double price = document.getDouble("price");
                            String category = document.getString("category");
                            String description = document.getString("description");
                            String imgUrl = document.getString("imgUrl");

                            Item item = new Item(id, name, price, category, description, imgUrl);
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
                        applyFilter(); // Zastosuj filtrację po załadowaniu danych
                    } else {
                        // Obsługa błędów...
                    }
                });
    }
}
