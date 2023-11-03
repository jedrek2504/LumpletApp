package com.example.lumplet;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Authentication {

    private FirebaseAuth mAuth;

    public Authentication() {
        mAuth = FirebaseAuth.getInstance();
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public void signUp(String email, String password, FirebaseAuthCompleteListener listener) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Rejestracja się powiodła
                        listener.onComplete(task.getResult().getUser());
                    } else {
                        // Wystąpił błąd
                        listener.onError(task.getException());
                    }
                });
    }

    public void signIn(String email, String password, FirebaseAuthCompleteListener listener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Logowanie się powiodło
                        listener.onComplete(task.getResult().getUser());
                    } else {
                        // Wystąpił błąd
                        listener.onError(task.getException());
                    }
                });
    }

    public void signOut() {
        mAuth.signOut();
    }

    public interface FirebaseAuthCompleteListener {
        void onComplete(FirebaseUser user);
        void onError(Exception exception);
    }
}
