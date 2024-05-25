package com.example.ocenus;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BladFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BladFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public BladFragment() {
        // Required empty public constructor
    }

    public static BladFragment newInstance(String param1, String param2) {
        BladFragment fragment = new BladFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blad, container, false);
    }

    public static void showAboutDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Zgłoś błąd");
        builder.setIcon(isDarkThemeEnabled(context) ? R.drawable.baseline_bug_report_24_night : R.drawable.baseline_bug_report_24);

        // Utwórz instancję EditText i ustaw jego właściwości
        EditText wyslijBlad = new EditText(context);
        wyslijBlad.setHint("Opisz błąd związany z aplikacją");
        wyslijBlad.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        wyslijBlad.setGravity(Gravity.TOP | Gravity.START);
        wyslijBlad.setPadding(16, 16, 16, 16);

        // Ustaw maksymalną długość na 1000 znaków
        wyslijBlad.setFilters(new InputFilter[] {new InputFilter.LengthFilter(1000)});

        int customBackgroundColor = ContextCompat.getColor(context, R.color.Noc);

        if (isDarkThemeEnabled(context)) {
            // Ustaw domyślny kolor tekstu dla motywu ciemnego
            wyslijBlad.setBackgroundColor(customBackgroundColor);
            wyslijBlad.setTextColor(ContextCompat.getColor(context, android.R.color.white));
        } else {
            // Ustaw domyślny kolor tekstu dla motywu jasnego
            wyslijBlad.setBackgroundColor(ContextCompat.getColor(context, android.R.color.background_light));
            wyslijBlad.setTextColor(ContextCompat.getColor(context, android.R.color.black));
        }

        // Ustaw ramkę dla EditText
        wyslijBlad.setBackground(ContextCompat.getDrawable(context, R.drawable.edittext_border));

        // Ustaw rozmiar EditText
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                800 // Ustaw wysokość EditText na 800 pikseli
        );
        layoutParams.gravity = Gravity.CENTER;
        wyslijBlad.setLayoutParams(layoutParams);

        // Stwórz TextView do wyświetlania liczby znaków
        TextView counterTextView = new TextView(context);
        counterTextView.setText("0/1000");
        counterTextView.setTextColor(isDarkThemeEnabled(context) ? Color.WHITE : Color.BLACK);
        LinearLayout.LayoutParams counterLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        counterLayoutParams.gravity = Gravity.START;  // Ustawienie licznika po lewej stronie
        counterTextView.setLayoutParams(counterLayoutParams);

        // Stwórz układ linearLayout aby umieścić EditText i licznik
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL); // Ustaw wyśrodkowanie poziome

        // Dodaj pustą przestrzeń między tytułem a polem EditText
        layout.setPadding(40, 50, 40, 0); // Ustaw dolny padding na 32 piksele

        layout.addView(wyslijBlad);

        LinearLayout.LayoutParams spaceParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                50 // Ustaw wysokość przestrzeni na 50 pikseli
        );
        layout.addView(new Space(context), spaceParams);

        layout.addView(counterTextView);

        // Dodaj dodatkową przestrzeń między licznikiem a przyciskiem "Anuluj"
        LinearLayout.LayoutParams spaceParams2 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT // Ustaw wysokość przestrzeni na zawartość
        );
        layout.addView(new Space(context), spaceParams2);

        // Dodaj layout do dialogu
        builder.setView(layout);

        builder.setPositiveButton("Wyślij", (dialog, which) -> {
            String errorDescription = wyslijBlad.getText().toString();
            // Handle the error description, e.g., send it to a server or log it
            Toast.makeText(context, "Pomyślnie wysłano!", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Anuluj", (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(dialogInterface -> {
            // Aktualizuj licznik znaków za każdym razem, gdy zawartość pola EditText się zmieni
            wyslijBlad.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @SuppressLint("SetTextI18n")
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // Aktualizuj tekst licznika
                    int currentLength = s.length();
                    counterTextView.setText(currentLength + "/1000");
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        });

        alertDialog.show();
    }

    private static boolean isDarkThemeEnabled(Context context) {
        int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }
}