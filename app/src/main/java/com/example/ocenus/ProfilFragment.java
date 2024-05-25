package com.example.ocenus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfilFragment extends Fragment {

    private String login;
    private View bottomBar;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    public ProfilFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Pobieranie loginu z Intentu aktywności
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            login = intent.getStringExtra("login");
            Log.d("ProfilFragment", "Pobrano login: " + login);
        }

        // Inicjalizacja Firebase
        databaseReference = FirebaseDatabase.getInstance("https://ocenus-8f95e-default-rtdb.firebaseio.com/").getReference("users");
        storageReference = FirebaseStorage.getInstance().getReference();

        hideBottomBar();
        retrieveUserData();
        retrieveUserImage();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Znajdź przycisk edycji
        Button editButton = view.findViewById(R.id.editButton);

        // Dodaj obsługę kliknięcia na przycisk edycji
        editButton.setOnClickListener(v -> {
            // Przekieruj do aktywności EdytujProfil
            Intent intent = new Intent(getActivity(), EdytujProfil.class);
            intent.putExtra("login", login); // Przekaż login do aktywności EdytujProfil
            startActivity(intent);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_moj_profil, container, false);
    }

    private void hideBottomBar() {
        // Ukryj pasek dolny
        if (bottomBar != null) {
            bottomBar.setVisibility(View.GONE);
        }
    }



    private void retrieveUserData() {
        Log.d("ProfilFragment", "Rozpoczęto pobieranie danych użytkownika");

        if (login != null) {

            // Pobierz dane użytkownika z bazy danych
            DatabaseReference userRef = databaseReference.child(login).child("dane");
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String imie = dataSnapshot.child("name").getValue(String.class);
                        String nazwisko = dataSnapshot.child("surname").getValue(String.class);

                        TextView imieTextView = getView().findViewById(R.id.profileImie);
                        TextView nazwiskoTextView = getView().findViewById(R.id.profileNazwisko);
                        TextView loginTextView = getView().findViewById(R.id.profileLogowanie);

                        imieTextView.setText(imie);
                        nazwiskoTextView.setText(nazwisko);
                        loginTextView.setText(login);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    private void retrieveUserImage() {
        DatabaseReference imgRef = FirebaseDatabase.getInstance().getReference("users").child(login).child("obrazy");
        imgRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String zdjecieUrl = childSnapshot.child("imageURL").getValue(String.class);
                        ImageView profiloweImageView = getView().findViewById(R.id.profileImg);
                        Glide.with(requireContext())
                                .load(zdjecieUrl)
                                .circleCrop()
                                .into(profiloweImageView);
                    }
                } else {
                    Toast.makeText(getContext(), "Brak danych o obrazie profilowym", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Błąd pobierania danych użytkownika", Toast.LENGTH_SHORT).show();
            }
        });
    }

}