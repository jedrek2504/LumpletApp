package com.example.lumplet;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registerButton;

    // Komponent do logowania za pomocą Google
    private Button googleLoginButton;

    // Obiekt klasy Authentication do obsługi autentykacji
    private Authentication auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        // Inicjalizacja obiektu autentykacji
        auth = new Authentication(this);

        // Przypisanie komponentów widoku do zmiennych
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        googleLoginButton = findViewById(R.id.googleLoginButton);

        // Listener dla przycisku logowania
        loginButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Proszę wprowadzić e-mail i hasło.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Logowanie użytkownika
            auth.signIn(email, password, new Authentication.FirebaseAuthCompleteListener() {
                @Override
                public void onComplete(FirebaseUser user) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }

                @Override
                public void onError(Exception exception) {
                    Toast.makeText(LoginActivity.this, "Błąd logowania: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Listener dla przycisku rejestracji
        registerButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Proszę wprowadzić e-mail i hasło.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Rejestracja użytkownika
            auth.signUp(email, password, new Authentication.FirebaseAuthCompleteListener() {
                @Override
                public void onComplete(FirebaseUser user) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }

                @Override
                public void onError(Exception exception) {
                    Toast.makeText(LoginActivity.this, "Błąd rejestracji: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Listener dla przycisku logowania za pomocą Google
        googleLoginButton.setOnClickListener(view -> signInWithGoogle());
    }

    // Funkcja do logowania za pomocą Google
    private void signInWithGoogle() {
        Intent signInIntent = auth.getGoogleSignInClient().getSignInIntent();
        googleSignInLauncher.launch(signInIntent);
    }

    // Listener reagujący na wynik próby logowania przez Google
    private final ActivityResultLauncher<Intent> googleSignInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    handleSignInResult(intent);
                }
            }
    );

    // Funkcja obsługująca wynik próby logowania za pomocą Google
    private void handleSignInResult(Intent data) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            auth.firebaseAuthWithGoogle(account, new Authentication.FirebaseAuthCompleteListener() {
                @Override
                public void onComplete(FirebaseUser user) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }

                @Override
                public void onError(Exception exception) {
                    Toast.makeText(LoginActivity.this, "Błąd logowania przez Google: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (ApiException e) {
            Toast.makeText(LoginActivity.this, "Błąd logowania przez Google: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}