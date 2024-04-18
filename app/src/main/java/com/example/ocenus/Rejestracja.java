package com.example.ocenus;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class Rejestracja extends AppCompatActivity {
    EditText signupLogin, signupPassword;

    Button signupButton;
    FirebaseDatabase database;
    DatabaseReference reference;

    TextView registerRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejestracja);
        signupLogin = findViewById(R.id.signup_login);
        signupPassword = findViewById(R.id.signup_password);
        registerRedirectText = findViewById(R.id.registerRedirectText);
        signupButton = findViewById(R.id.signup_button);

        registerRedirectText.setOnClickListener(view -> {
            Intent intent = new Intent(Rejestracja.this, Logowanie.class);
            startActivity(intent);
        });

        signupButton.setOnClickListener(view -> {
            String login = signupLogin.getText().toString();
            String password = signupPassword.getText().toString();

            if (!login.isEmpty() && !password.isEmpty()) {
                database = FirebaseDatabase.getInstance("https://ocenus-8f95e-default-rtdb.firebaseio.com/");
                reference = database.getReference("users");

                // Check if the login already exists
                reference.child(login).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Username already exists, prompt the user to choose a different one
                            Toast.makeText(Rejestracja.this, "Nazwa użytkownika zajęta.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Username is available, proceed with registration
                            String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
                            Uzytkownik uzytkownik = new Uzytkownik(login, hashedPassword, new DaneUzytkownika(" ", " "));
                            reference.child(login).setValue(uzytkownik);
                            Toast.makeText(Rejestracja.this, "You have signed up successfully!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Rejestracja.this, Logowanie.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle errors, if any
                        Toast.makeText(Rejestracja.this, "Database error. Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(Rejestracja.this, "Podaj login i hasło by móc się zarejestrować", Toast.LENGTH_SHORT).show();
            }
        });


    }
}