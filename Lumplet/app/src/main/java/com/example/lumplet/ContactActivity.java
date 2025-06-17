package com.example.lumplet;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class ContactActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        TextView contactInfo = findViewById(R.id.contactInfo);
        contactInfo.setText("Skontaktuj się z nami:\n\nEmail: kontakt@lumplet.com\nTelefon: +48 123 456 789\nAdres: al. Politechniki 1, 00-000 Łódź");
    }
}
