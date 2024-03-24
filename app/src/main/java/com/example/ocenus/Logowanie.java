package com.example.ocenus;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class Logowanie extends AppCompatActivity {

    
    EditText loginLogin, loginPassword;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logowanie);

        loginLogin = findViewById(R.id.login_login);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
     //   signupRedirectText = findViewById(R.id.signupRedirectText);

        loginButton.setOnClickListener(view -> {
            if (validateLogin() & validatePassword()) {
                checkUser();
            }
        });

        //signupRedirectText.setOnClickListener(view -> {
        //    Intent intent = new Intent(Logowanie.this, Rejestracja.class);
         //   startActivity(intent);
      //  });

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

    public Boolean validatePassword(){
        String val = loginPassword.getText().toString();
        if (val.isEmpty()) {
            loginPassword.setError("Password cannot be empty");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }


    public void checkUser(){
        String userLogin = loginLogin.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance("https://ocenus-8f95e-default-rtdb.firebaseio.com/").getReference("users");
        Query checkUserDatabase = reference.orderByChild("login").equalTo(userLogin);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    loginLogin.setError(null);
                    String passwordFromDB = snapshot.child(userLogin).child("password").getValue(String.class);
                    //String hashedPassword = BCrypt.withDefaults().hashToString(12,userPassword.toCharArray());
                    BCrypt.Result result = BCrypt.verifyer().verify(userPassword.toCharArray(),passwordFromDB);
                    if (result.verified) {
                        loginLogin.setError(null);

                        String loginFromDB = snapshot.child(userLogin).child("login").getValue(String.class);
                        //String usernameFromDB = snapshot.child(userLogin).child("username").getValue(String.class);

                        Intent intent = new Intent(Logowanie.this, Profil.class);

                        intent.putExtra("login", loginFromDB);
                        //intent.putExtra("username", usernameFromDB);
                        intent.putExtra("password", passwordFromDB);

                        startActivity(intent);
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