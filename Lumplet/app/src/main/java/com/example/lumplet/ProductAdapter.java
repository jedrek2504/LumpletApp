package com.example.lumplet;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
        ImageView imageView = convertView.findViewById(R.id.obrazekImageView);

        if (item != null) {
            nazwaTextView.setText(item.getName());
            cenaTextView.setText(String.valueOf(item.getPrice()));

            if (item.getImgUrl() != null && !item.getImgUrl().isEmpty()) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReferenceFromUrl(item.getImgUrl());

                storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    Glide.with(getContext())
                            .load(uri.toString())
                            .into(imageView);
                }).addOnFailureListener(exception -> {
                    imageView.setVisibility(View.GONE);
                });
            } else {
                imageView.setImageResource(R.drawable.lumpletlogo);
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Tworzenie Intent do ProductView
                    Intent intent = new Intent(getContext(), ProductView.class);
                    intent.putExtra("itemID", item.getItemId());
                    intent.putExtra("productName", item.getName());
                    intent.putExtra("productDescription", item.getDescription());
                    intent.putExtra("productPrice", item.getPrice());
                    intent.putExtra("productCategory", item.getCategory());
                    intent.putExtra("imgUrl", item.getImgUrl());
                    getContext().startActivity(intent);
                }
            });
        }

        return convertView;
    }

}
