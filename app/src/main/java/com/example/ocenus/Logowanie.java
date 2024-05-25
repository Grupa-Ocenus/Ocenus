package com.example.ocenus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

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
    ImageView showPasswordIcon;
    boolean passwordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logowanie);


        loginLogin = findViewById(R.id.login_login);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        remember = findViewById(R.id.zapamietajmnie);
        showPasswordIcon = findViewById(R.id.showPasswordIcon);

        updatePasswordVisibilityIcon();
        setLoginEditTextStyle();
        setEditTextStyle();

        ConstraintLayout constraintLayout = findViewById(R.id.main);
        if (isDarkThemeEnabled()) {
            constraintLayout.setBackgroundResource(R.drawable.tlo_ocenusowskie_noc);
        } else {
            constraintLayout.setBackgroundResource(R.drawable.tlo_ocenusowskie);
        }

        showPasswordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility(); // Call the method to toggle password visibility
            }
        });

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
            //Toast.makeText(this, "Zaloguj się!", Toast.LENGTH_SHORT).show();
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
                //Toast.makeText(Logowanie.this, b ? "Checked" : "Unchecked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setLoginEditTextStyle() {
        EditText loginEditText = findViewById(R.id.login_login);
        int personIconResId = isDarkThemeEnabled() ? R.drawable.baseline_person_24_night : R.drawable.baseline_person_24;
        loginEditText.setCompoundDrawablesWithIntrinsicBounds(personIconResId, 0, 0, 0);
        if (isDarkThemeEnabled()) {
            loginEditText.setBackgroundResource(R.drawable.ocenus_ramka_noc);
            loginEditText.setTextColor(Color.WHITE);
        } else {
            loginEditText.setBackgroundResource(R.drawable.ocenus_ramka);
            loginEditText.setTextColor(Color.BLACK);
        }
    }

    private void setEditTextStyle() {
        EditText loginPassword = findViewById(R.id.login_password);
        int personIconResId = isDarkThemeEnabled() ? R.drawable.baseline_lock_24_night : R.drawable.baseline_lock_24;
        loginPassword.setCompoundDrawablesWithIntrinsicBounds(personIconResId, 0, 0, 0);

        if (isDarkThemeEnabled()) {
            loginPassword.setBackgroundResource(R.drawable.ocenus_ramka_noc);
            loginPassword.setTextColor(Color.WHITE);
        } else {
            loginPassword.setBackgroundResource(R.drawable.ocenus_ramka);
            loginPassword.setTextColor(Color.BLACK);
        }
    }

    private void togglePasswordVisibility() {
        if (passwordVisible) {
            loginPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            loginPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
        passwordVisible = !passwordVisible;
        loginPassword.setSelection(loginPassword.length());
        loginPassword.setTypeface(ResourcesCompat.getFont(this, R.font.asap));
        updatePasswordVisibilityIcon(); // Aktualizuj ikonę widoczności hasła
    }

    private void updatePasswordVisibilityIcon() {

        boolean isDarkTheme = isDarkThemeEnabled();

        if (passwordVisible) {
            showPasswordIcon.setImageResource(isDarkTheme ? R.drawable.baseline_visibility_off_24_night : R.drawable.baseline_visibility_off_24);
        } else {
            showPasswordIcon.setImageResource(isDarkTheme ? R.drawable.baseline_visibility_24_night : R.drawable.baseline_visibility_24);
        }
    }

        public Boolean validateLogin() {
        String val = loginLogin.getText().toString();
        if (val.isEmpty()) {
            loginLogin.setError("Pole login nie może być puste!");
            return false;
        } else {
            loginLogin.setError(null);
            return true;
        }
    }

    private boolean isDarkThemeEnabled() {
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }

    public Boolean validatePassword() {
        String val = loginPassword.getText().toString();
        if (val.isEmpty()) {
            loginPassword.setError("Pole hasło nie może być puste!");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }

    public void showSettingsDialog(View view) {
        UstawieniaGlowne.showSettingsDialog(this);
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
                            // Jeśli dane użytkownika są puste, to jest to pierwsze logowanie
                            intent = new Intent(Logowanie.this, Profil.class);
                            intent.putExtra("firstLogin", true);
                        } else {
                            // Sprawdź, czy użytkownik ma ustawione imię i nazwisko
                            if (!dane.getName().isEmpty() && !dane.getSurname().isEmpty()) {
                                // Jeśli użytkownik ma imię i nazwisko, przekieruj do strony głównej
                                intent = new Intent(Logowanie.this, StronaGlowna.class);
                            } else {
                                // W przeciwnym razie przekieruj do strony profilu
                                intent = new Intent(Logowanie.this, Profil.class);
                                intent.putExtra("name", dane.getName());
                                intent.putExtra("surname", dane.getSurname());
                            }
                        }
                        intent.putExtra("login", loginFromDB);
                        startActivity(intent);
                        finish();
                    } else {
                        loginPassword.setError("Niepoprawne hasło!");
                        loginPassword.requestFocus();
                    }
                } else {
                    loginLogin.setError("Użytkownik o podanym loginie nie istnieje!");
                    loginLogin.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
