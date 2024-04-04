package com.example.ocenus;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Profil extends AppCompatActivity {

    TextView profileName, profileSurname,profileLogin;
    Button editProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profil);

        profileName = findViewById(R.id.profileImie);
        profileSurname = findViewById(R.id.profileNazwisko);
        profileLogin = findViewById(R.id.profileLogowanie);
        editProfile = findViewById(R.id.editButton);

        editProfile.setOnClickListener(view -> {
            goToEdit();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        showAllUserData();
    }
    public void showAllUserData(){
        Intent intent = getIntent();
        String nameUser = intent.getStringExtra("name");
        String surnameUser = intent.getStringExtra("surname");
        String loginUser = intent.getStringExtra("login");

        if(nameUser != null && surnameUser != null && loginUser != null) {
            profileName.setText(nameUser);
            profileSurname.setText(surnameUser);
            profileLogin.setText(loginUser);
        }
    }

    private void goToEdit(){
        Intent intent = new Intent(Profil.this, EdytujProfil.class);

        intent.putExtra("login",profileLogin.getText());
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finishAffinity();

    }
}