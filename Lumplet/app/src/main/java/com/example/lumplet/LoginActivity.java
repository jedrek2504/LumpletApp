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

    private Button googleLoginButton;

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
        googleLoginButton = findViewById(R.id.googleLoginButton);

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

        googleLoginButton.setOnClickListener(view -> signInWithGoogle());
    }

    private void signInWithGoogle() {
        Intent signInIntent = auth.getGoogleSignInClient().getSignInIntent();
        googleSignInLauncher.launch(signInIntent);
    }

    private final ActivityResultLauncher<Intent> googleSignInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    handleSignInResult(intent);
                }
            }
    );

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
