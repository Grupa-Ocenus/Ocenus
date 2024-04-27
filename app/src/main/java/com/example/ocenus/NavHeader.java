package com.example.ocenus;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;

public class NavHeader extends AppCompatActivity {

    private TextView imie;
    private TextView nazwisko;

    private ImageView zdjecie;

    private DatabaseReference mDatabase;
    String login;
    private Intent intent;

    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_header);

        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance("https://ocenus-8f95e-default-rtdb.firebaseio.com/").getReference("users");

        // Find TextViews
        imieTextView = findViewById(R.id.ImieId);
        nazwiskoTextView = findViewById(R.id.NazwiskoId);

        // Retrieve and display user's name and surname
        DatabaseReference userRef = mDatabase.child(login).child("dane"); // Replace "userId" with the actual ID of the user
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue(String.class);
                String surname = dataSnapshot.child("surname").getValue(String.class);

                // Set retrieved data to TextViews
                imieTextView.setText(name);
                nazwiskoTextView.setText(surname);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }

     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_header);

        imie = findViewById(R.id.ImieId);
        nazwisko = findViewById(R.id.NazwiskoId);
        zdjecie = findViewById(R.id.profiloweImageView);

        showAllUserData();

    }

    private void showAllUserData() {

        Intent intent = getIntent();
        String Imie_osoby = intent.getStringExtra("name");
        String Nazwisko_osoby = intent.getStringExtra("username");

        imie.setText(Imie_osoby);
        imie.setText(Nazwisko_osoby);
    }

}