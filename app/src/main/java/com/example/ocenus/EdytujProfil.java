package com.example.ocenus;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.ClosedSubscriberGroupInfo;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class EdytujProfil extends AppCompatActivity {
    private Intent intent;
    String imie;
    String nazwisko;
    String login;
    EditText imieEdit;
    EditText nazwiskoEdit;
    Button confirmButton;
    FirebaseDatabase database;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edycja_profilu);
        intent = getIntent();
        imieEdit = findViewById(R.id.profileImie);
        nazwiskoEdit = findViewById(R.id.profileNazwisko);
        confirmButton = findViewById(R.id.confirmButton);

        confirmButton.setOnClickListener(view -> {
            saveChanges();
        });
        login = intent.getStringExtra("login");

        DatabaseReference reference = FirebaseDatabase.getInstance("https://ocenus-8f95e-default-rtdb.firebaseio.com/").getReference("users");
        Query checkUserDatabase = reference.orderByChild("login").equalTo(login);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    imie = snapshot.child(login).child("dane").child("name").getValue(String.class);
                    nazwisko = snapshot.child(login).child("dane").child("surname").getValue(String.class);
                    imieEdit.setText(imie);
                    nazwiskoEdit.setText(nazwisko);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void saveChanges(){
        String userName = imieEdit.getText().toString().trim();
        String userSurname = nazwiskoEdit.getText().toString().trim();

        database = FirebaseDatabase.getInstance("https://ocenus-8f95e-default-rtdb.firebaseio.com/");
        reference = database.getReference("users").child(login).child("dane");
        Map<String,Object> values = new HashMap<>();
        values.put("name",userName);
        values.put("surname",userSurname);
        reference.updateChildren(values);

        Toast.makeText(EdytujProfil.this, "Dane zmienione!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(EdytujProfil.this, Profil.class);
        intent.putExtra("login", login);
        intent.putExtra("name", userName);
        intent.putExtra("surname", userSurname);
        startActivity(intent);
    }

}
