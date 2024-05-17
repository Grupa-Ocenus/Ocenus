package com.example.ocenus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfilFragment extends Fragment {

    // Identyfikator parametrów fragmentu
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // Parametry fragmentu
    private String mParam1;
    private String mParam2;

    // Widok paska dolnego
    private View bottomBar;

    public ProfilFragment() {
        // Wymagany konstruktor publiczny
    }

    // Metoda fabryki do tworzenia nowej instancji fragmentu z określonymi parametrami
    public static ProfilFragment newInstance(String param1, String param2) {
        ProfilFragment fragment = new ProfilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflacja widoku fragmentu
        return inflater.inflate(R.layout.fragment_moj_profil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Znalezienie paska dolnego w widoku fragmentu
        bottomBar = view.findViewById(R.id.bottomAppBar);

        // Ukrycie paska dolnego
        hideBottomBar();
    }

    private void hideBottomBar() {
        // Ustawienie widoczności paska dolnego na "GONE"
        if (bottomBar != null) {
            bottomBar.setVisibility(View.GONE);
        }
    }
}
