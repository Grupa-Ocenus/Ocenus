package com.example.ocenus;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
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
import androidx.constraintlayout.widget.ConstraintLayout;
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

        updatePasswordVisibilityIcon();
        setLoginEditTextStyle();
        setEditTextStyle();

        ConstraintLayout constraintLayout = findViewById(R.id.main);
        if (isDarkThemeEnabled()) {
            constraintLayout.setBackgroundResource(R.drawable.tlo_ocenusowskie_noc);
        } else {
            constraintLayout.setBackgroundResource(R.drawable.tlo_ocenusowskie);
        }

        showPasswordIcon.setOnClickListener(v -> {
            togglePasswordVisibility(); // Wywołaj metodę do przełączania widoczności hasła
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

            if (login.length() > 40) {
                Toast.makeText(Rejestracja.this, "Login nie może przekraczać 40 znaków!", Toast.LENGTH_SHORT).show();
                return; // Przerwij proces rejestracji
            }

            if (password.length() < 6) {
                Toast.makeText(Rejestracja.this, "Hasło musi mieć co najmniej 6 znaków!", Toast.LENGTH_SHORT).show();
                return; // Przerwij proces rejestracji
            }

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

    private void setLoginEditTextStyle() {
        EditText signupEditText = findViewById(R.id.signup_login);
        int personIconResId = isDarkThemeEnabled() ? R.drawable.baseline_person_24_night : R.drawable.baseline_person_24;
        signupEditText.setCompoundDrawablesWithIntrinsicBounds(personIconResId, 0, 0, 0);

        if (isDarkThemeEnabled()) {
            signupEditText.setBackgroundResource(R.drawable.ocenus_ramka_noc);
            signupEditText.setTextColor(Color.WHITE);
        } else {
            signupEditText.setBackgroundResource(R.drawable.ocenus_ramka);
            signupEditText.setTextColor(Color.BLACK);
        }
    }

    private void setEditTextStyle() {
        EditText signupPassword = findViewById(R.id.signup_password);
        int personIconResId = isDarkThemeEnabled() ? R.drawable.baseline_lock_24_night : R.drawable.baseline_lock_24;
        signupPassword.setCompoundDrawablesWithIntrinsicBounds(personIconResId, 0, 0, 0);

        if (isDarkThemeEnabled()) {
            signupPassword.setBackgroundResource(R.drawable.ocenus_ramka_noc);
            signupPassword.setTextColor(Color.WHITE);
        } else {
            signupPassword.setBackgroundResource(R.drawable.ocenus_ramka);
            signupPassword.setTextColor(Color.BLACK);
        }
    }

    private boolean isDarkThemeEnabled() {
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }

    public void showSettingsDialog(View view) {
        UstawieniaGlowne.showSettingsDialog(this);
    }

    private void togglePasswordVisibility() {
        if (passwordVisible) {
            signupPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            signupPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
        passwordVisible = !passwordVisible;
        signupPassword.setSelection(signupPassword.length());
        signupPassword.setTypeface(ResourcesCompat.getFont(this, R.font.asap));
        updatePasswordVisibilityIcon();
    }

    private void updatePasswordVisibilityIcon() {

        boolean isDarkTheme = isDarkThemeEnabled();

        if (passwordVisible) {
            showPasswordIcon.setImageResource(isDarkTheme ? R.drawable.baseline_visibility_off_24_night : R.drawable.baseline_visibility_off_24);
        } else {
            showPasswordIcon.setImageResource(isDarkTheme ? R.drawable.baseline_visibility_24_night : R.drawable.baseline_visibility_24);
        }
    }
}