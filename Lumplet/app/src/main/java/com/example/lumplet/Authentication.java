package com.example.lumplet;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Authentication {

    // Klient do logowania przez Google
    private GoogleSignInClient mGoogleSignInClient;

    // Klient do autentykacji Firebase
    private FirebaseAuth mAuth;

    // Konstruktor inicjalizujący klienta do logowania przez Google oraz autentykacji Firebase
    public Authentication(Context context) {
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("621222884241-t4bif25qvt70m5kamvl8693sh8ljflco.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    // Pobranie aktualnie zalogowanego użytkownika
    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    // Rejestracja nowego użytkownika za pomocą e-maila i hasła
    public void signUp(String email, String password, FirebaseAuthCompleteListener listener) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Rejestracja się powiodła
                        listener.onComplete(task.getResult().getUser());
                    } else {
                        // Wystąpił błąd podczas rejestracji
                        listener.onError(task.getException());
                    }
                });
    }

    // Logowanie użytkownika za pomocą e-maila i hasła
    public void signIn(String email, String password, FirebaseAuthCompleteListener listener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Logowanie się powiodło
                        listener.onComplete(task.getResult().getUser());
                    } else {
                        // Wystąpił błąd podczas logowania
                        listener.onError(task.getException());
                    }
                });
    }

    // Pobranie klienta do logowania przez Google
    public GoogleSignInClient getGoogleSignInClient() {
        return mGoogleSignInClient;
    }

    // Logowanie użytkownika przez Google z wykorzystaniem tokena od Google
    public void firebaseAuthWithGoogle(GoogleSignInAccount acct, FirebaseAuthCompleteListener listener) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Logowanie przez Google się powiodło
                        listener.onComplete(mAuth.getCurrentUser());
                    } else {
                        // Wystąpił błąd podczas logowania przez Google
                        listener.onError(task.getException());
                    }
                });
    }

    // Wylogowywanie użytkownika
    public void signOut() {
        mAuth.signOut();
    }

    // Interfejs do obsługi wyników operacji autentykacji
    public interface FirebaseAuthCompleteListener {
        void onComplete(FirebaseUser user);       //    Metoda wywoływana po pomyślnej operacji
        void onError(Exception exception);       // Metoda wywoływana w przypadku wystąpienia błędu
    }
}