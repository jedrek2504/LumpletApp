package com.example.lumplet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registerButton;

    private Authentication auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        auth = new Authentication(this);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        loginButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                // Poinformuj użytkownika o konieczności wprowadzenia wszystkich danych
                Toast.makeText(LoginActivity.this, "Proszę wprowadzić e-mail i hasło.", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signIn(email, password, new Authentication.FirebaseAuthCompleteListener() {
                @Override
                public void onComplete(FirebaseUser user) {
                    // Logowanie powiodło się, można przekierować użytkownika do głównej aktywności
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }

                @Override
                public void onError(Exception exception) {
                    // Wystąpił błąd podczas logowania, poinformuj użytkownika
                    Toast.makeText(LoginActivity.this, "Błąd logowania: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        registerButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                // Poinformuj użytkownika o konieczności wprowadzenia wszystkich danych
                Toast.makeText(LoginActivity.this, "Proszę wprowadzić e-mail i hasło.", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signUp(email, password, new Authentication.FirebaseAuthCompleteListener() {
                @Override
                public void onComplete(FirebaseUser user) {
                    // Rejestracja powiodła się, można przekierować użytkownika do głównej aktywności
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }

                @Override
                public void onError(Exception exception) {
                    // Wystąpił błąd podczas rejestracji, poinformuj użytkownika
                    Toast.makeText(LoginActivity.this, "Błąd rejestracji: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
