package com.example.ocenus;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Profil extends AppCompatActivity {

    TextView profileName, profileSurname,profileLogin;
    Button editProfile;
    Button goToMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profil);

        profileName = findViewById(R.id.profileImie);
        profileSurname = findViewById(R.id.profileNazwisko);
        profileLogin = findViewById(R.id.profileLogowanie);
        editProfile = findViewById(R.id.editButton);
        goToMain = findViewById(R.id.goToMain);

        editProfile.setOnClickListener(view -> goToEdit());
        goToMain.setOnClickListener(view -> goToMain());

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
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    private void goToMain(){
        Intent intent = new Intent(Profil.this, StronaGlowna.class);

        intent.putExtra("login",profileLogin.getText());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}