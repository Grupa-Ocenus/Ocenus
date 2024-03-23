package com.example.ocenus;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class Rejestracja extends AppCompatActivity {
    EditText signupEmail, signupPassword;
    TextView loginRedirectText;
    Button signupButton;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejestracja);
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        signupButton = findViewById(R.id.signup_button);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance("https://ocenus-8f95e-default-rtdb.firebaseio.com/");
                reference = database.getReference("users");
                String email = signupEmail.getText().toString();
                String password = signupPassword.getText().toString();
                Helper helperClass = new Helper(email, password);
                reference.child(email).setValue(helperClass);
                Toast.makeText(Rejestracja.this, "You have signup successfully!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Rejestracja.this, Logowanie.class);
                startActivity(intent);
            }
        });
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Rejestracja.this, Logowanie.class);
                startActivity(intent);
            }
        });
    }
}