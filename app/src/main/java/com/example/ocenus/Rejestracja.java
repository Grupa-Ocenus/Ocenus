package com.example.ocenus;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class Rejestracja extends AppCompatActivity {
    EditText signupLogin, signupPassword;

    Button signupButton;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejestracja);
        signupLogin = findViewById(R.id.signup_login);
        signupPassword = findViewById(R.id.signup_password);

        signupButton = findViewById(R.id.signup_button);
        signupButton.setOnClickListener(view -> {

            String login = signupLogin.getText().toString();
            String password = signupPassword.getText().toString();

            if (!login.isEmpty() && !password.isEmpty()) {
            database = FirebaseDatabase.getInstance("https://ocenus-8f95e-default-rtdb.firebaseio.com/");
            reference = database.getReference("users");
            String hashedPassword = BCrypt.withDefaults().hashToString(12,password.toCharArray());
            Uzytkownik uzytkownik = new Uzytkownik(login, hashedPassword,new DaneUzytkownika(" "," "));
            reference.child(login).setValue(uzytkownik);
            Toast.makeText(Rejestracja.this, "You have signup successfully!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Rejestracja.this, Logowanie.class);
            startActivity(intent);
            }
            else
            {
                Toast.makeText(Rejestracja.this, "Podaj login i hasło by móc się zarejestrować", Toast.LENGTH_SHORT).show();
            }

        });

    }
}