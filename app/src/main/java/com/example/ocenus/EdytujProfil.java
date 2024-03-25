package com.example.ocenus;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.ClosedSubscriberGroupInfo;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.webkit.MimeTypeMap;
import android.net.Uri;
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

    //private FloatingActionButton uploadButton;

    Button ConfirmButton;
    private ImageView uploadImage;
    private Uri imageUri;

    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Obrazy");

    final private StorageReference storageReference = FirebaseStorage.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edycja_profilu);
        intent = getIntent();
        imieEdit = findViewById(R.id.profileImie);
        nazwiskoEdit = findViewById(R.id.profileNazwisko);
        confirmButton = findViewById(R.id.confirmButton);
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

        uploadImage = findViewById(R.id.uploadImage);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        imageUri = data.getData();
                        uploadImage.setImageURI(imageUri);}
                        else
                        {Toast.makeText(EdytujProfil.this, "Nie wybrano obrazu", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
        );

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent();
                photoPicker.setAction(Intent.ACTION_GET_CONTENT);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageUri != null) {
                    uploadToFirebase(imageUri);
                } else {
                    Toast.makeText(EdytujProfil.this, "Wybierz obraz", Toast.LENGTH_SHORT).show();
                }
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


    private void uploadToFirebase(Uri uri)
        {
            final StorageReference imageReference = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(uri));

            imageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uzytkownik uzytkownik =  new Uzytkownik(uri.toString());
                            String key = databaseReference.push().getKey();
                            databaseReference.child(key).setValue(uzytkownik);
                            Toast.makeText(EdytujProfil.this, "Udane", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EdytujProfil.this, Profil.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            });
        }


    private String getFileExtension(Uri fileUri){
            ContentResolver contentResolver = getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            return mime.getExtensionFromMimeType(contentResolver.getType(fileUri));
    }

}
