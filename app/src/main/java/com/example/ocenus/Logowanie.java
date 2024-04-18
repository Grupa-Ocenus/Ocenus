package com.example.ocenus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class Logowanie extends AppCompatActivity {

    EditText loginLogin, loginPassword;
    Button loginButton;
    TextView loginRedirectText;
    CheckBox remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logowanie);

        loginLogin = findViewById(R.id.login_login);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        remember = findViewById(R.id.zapamietajmnie);

        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        String checkbox = preferences.getString("remember", "");
        if (checkbox.equals("true")) {
            String lastLoggedInUser = preferences.getString("lastLoggedInUser", "");
            if (!lastLoggedInUser.isEmpty()) {
                Intent intent = new Intent(Logowanie.this, StronaGlowna.class);
                intent.putExtra("login", lastLoggedInUser); // Pass the login value
                startActivity(intent);
                finish(); // Finish the current activity to prevent going back to login page
            }
        } else if (checkbox.equals("false")) {
            Toast.makeText(this, "Please Sign in", Toast.LENGTH_SHORT).show();
        }

        loginButton.setOnClickListener(view -> {
            if (validateLogin() & validatePassword()) {
                checkUser();
            }
        });

        loginRedirectText.setOnClickListener(view -> {
            Intent intent = new Intent(Logowanie.this, Rejestracja.class);
            startActivity(intent);
        });

        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("remember", b ? "true" : "false");
                editor.apply();
                Toast.makeText(Logowanie.this, b ? "Checked" : "Unchecked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Boolean validateLogin() {
        String val = loginLogin.getText().toString();
        if (val.isEmpty()) {
            loginLogin.setError("Username cannot be empty");
            return false;
        } else {
            loginLogin.setError(null);
            return true;
        }
    }

    public Boolean validatePassword() {
        String val = loginPassword.getText().toString();
        if (val.isEmpty()) {
            loginPassword.setError("Password cannot be empty");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }

    public void checkUser() {
        String userLogin = loginLogin.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance("https://ocenus-8f95e-default-rtdb.firebaseio.com/").getReference("users");
        Query checkUserDatabase = reference.orderByChild("login").equalTo(userLogin);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    loginLogin.setError(null);
                    String passwordFromDB = snapshot.child(userLogin).child("password").getValue(String.class);
                    BCrypt.Result result = BCrypt.verifyer().verify(userPassword.toCharArray(), passwordFromDB);
                    if (result.verified) {
                        loginLogin.setError(null);
                        String loginFromDB = snapshot.child(userLogin).child("login").getValue(String.class);
                        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("lastLoggedInUser", loginFromDB);
                        editor.apply();
                        DaneUzytkownika dane = snapshot.child(userLogin).child("dane").getValue(DaneUzytkownika.class);
                        Intent intent;
                        if (Objects.isNull(dane) || Objects.equals(dane.getName(), " ") || Objects.equals(dane.getSurname(), " ")) {
                            intent = new Intent(Logowanie.this, EdytujProfil.class);
                        } else {
                            intent = new Intent(Logowanie.this, Profil.class);
                            intent.putExtra("name", dane.getName());
                            intent.putExtra("surname", dane.getSurname());
                        }
                        intent.putExtra("login", loginFromDB);
                        startActivity(intent);
                        finish();
                    } else {
                        loginPassword.setError("Invalid Credentials");
                        loginPassword.requestFocus();
                    }
                } else {
                    loginLogin.setError("User does not exist");
                    loginLogin.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
