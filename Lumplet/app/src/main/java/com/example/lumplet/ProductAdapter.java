package com.example.lumplet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ProductAdapter extends ArrayAdapter<Item> {
    public ProductAdapter(Context context, ArrayList<Item> productList) {
        super(context, 0, productList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Item item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.product_item, parent, false);
        }

        TextView nazwaTextView = convertView.findViewById(R.id.nazwaTextView);
        TextView cenaTextView = convertView.findViewById(R.id.cenaTextView);

        if (item != null) {
            nazwaTextView.setText(item.getName()); // Zaktualizowana metoda getName()
            cenaTextView.setText(String.valueOf(item.getPrice())); // Zaktualizowana metoda getPrice()
        }

        return convertView;
    }
}
