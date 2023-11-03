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

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    public Authentication(Context context) {
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("621222884241-t4bif25qvt70m5kamvl8693sh8ljflco.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
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

    public GoogleSignInClient getGoogleSignInClient() {
        return mGoogleSignInClient;
    }

    public void firebaseAuthWithGoogle(GoogleSignInAccount acct, FirebaseAuthCompleteListener listener) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onComplete(mAuth.getCurrentUser());
                    } else {
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
