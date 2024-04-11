package com.example.ocenus;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NavHeader extends AppCompatActivity {

    private TextView imieTextView;
    private TextView nazwiskoTextView;
    private DatabaseReference mDatabase;
    String login;

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
}



