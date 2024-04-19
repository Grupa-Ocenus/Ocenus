package com.example.ocenus;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

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

    CheckBox regulaminCheckBox;

    ImageView showPasswordIcon;
    boolean passwordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejestracja);
        signupLogin = findViewById(R.id.signup_login);
        signupPassword = findViewById(R.id.signup_password);
        registerRedirectText = findViewById(R.id.registerRedirectText);
        signupButton = findViewById(R.id.signup_button);
        showPasswordIcon = findViewById(R.id.showPasswordIcon);
        regulaminCheckBox = findViewById(R.id.regulaminBox);

        showPasswordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility(); // Wywołaj metodę do przełączania widoczności hasła
            }
        });


        registerRedirectText.setOnClickListener(view -> {
            Intent intent = new Intent(Rejestracja.this, Logowanie.class);
            startActivity(intent);
        });

        signupButton.setOnClickListener(view -> {

            if (!regulaminCheckBox.isChecked()) {
                Toast.makeText(Rejestracja.this, "Zaakceptuj regulamin przed rejestracją!", Toast.LENGTH_SHORT).show();
                return; // Przerwij proces rejestracji
            }
            String login = signupLogin.getText().toString();
            String password = signupPassword.getText().toString();

            if (!login.isEmpty() && !password.isEmpty()) {
                database = FirebaseDatabase.getInstance("https://ocenus-8f95e-default-rtdb.firebaseio.com/");
                reference = database.getReference("users");

                // Sprawdz czy login istnieje w bazie danych
                reference.child(login).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Uzytkownik o podanym loginie istnieje
                            Toast.makeText(Rejestracja.this, "Nazwa użytkownika zajęta.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Login jest dostepny, przejscie do rejestracji
                            String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
                            Uzytkownik uzytkownik = new Uzytkownik(login, hashedPassword, new DaneUzytkownika(" ", " "));
                            reference.child(login).setValue(uzytkownik);
                            Toast.makeText(Rejestracja.this, "Rejestracja przebiegła pomyślnie!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Rejestracja.this, Profil.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle errors, if any
                        Toast.makeText(Rejestracja.this, "Błąd bazy danych. Spróbuj ponownie później.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(Rejestracja.this, "Podaj login i hasło by móc się zarejestrować", Toast.LENGTH_SHORT).show();
            }
        });


    }
    private void togglePasswordVisibility() {
        if (passwordVisible) {
            // Jeśli hasło jest aktualnie widoczne, ukryj je
            signupPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            showPasswordIcon.setImageResource(R.drawable.baseline_visibility_24); // Zmień ikonę, aby pokazać ukryte hasło
        } else {
            // Jeśli hasło jest aktualnie ukryte, pokaż je
            signupPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            showPasswordIcon.setImageResource(R.drawable.baseline_visibility_off_24); // Zmień ikonę, aby pokazać widoczne hasło
        }
        passwordVisible = !passwordVisible; // Przełącz flagę widoczności hasła
        signupPassword.setSelection(signupPassword.length()); // Ustaw pozycję kursora na koniec tekstu
        signupPassword.setTypeface(ResourcesCompat.getFont(this, R.font.asap));
    }
}