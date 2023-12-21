package com.example.lumplet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.lumplet.Item;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class CartAdapter extends ArrayAdapter<Item> {
    private final CartActivity cartActivity;

    public CartAdapter(Context context, List<Item> items) {
        super(context, 0, items);
        this.cartActivity = (CartActivity) context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cart_item, parent, false);
        }

        TextView itemName = convertView.findViewById(R.id.item_name);
        TextView itemPrice = convertView.findViewById(R.id.item_price);
        ImageView itemImage = convertView.findViewById(R.id.item_image);

        itemName.setText(item.getName());
        itemPrice.setText(String.format("$%.2f", item.getPrice()));

        if (item.getImgUrl() != null && !item.getImgUrl().isEmpty()) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl(item.getImgUrl());

            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                Glide.with(getContext())
                        .load(uri.toString())
                        .into(itemImage);
            }).addOnFailureListener(exception -> {
                itemImage.setVisibility(View.GONE);
            });
        } else {
            itemImage.setImageResource(R.drawable.lumpletlogo);
        }

        Button removeButton = convertView.findViewById(R.id.remove_button);
        removeButton.setOnClickListener(v -> {
            Item itemToRemove = getItem(position);
            remove(itemToRemove);
            Cart.getInstance().removeItem(itemToRemove);
            notifyDataSetChanged();
            cartActivity.updateTotalPrice();
        });

        return convertView;
    }
}