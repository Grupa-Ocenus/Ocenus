package com.example.ocenus;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class UstawieniaGlowne {

    public static void showSettingsDialog(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_ustawienia_glowne, null);

        SharedPreferences sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setTitle("Ustawienia");

        Switch notificationSwitch = view.findViewById(R.id.notificationSwitch);
        Switch musicSwitch = view.findViewById(R.id.musicSwitch);
        TextView notificationStatus = view.findViewById(R.id.notificationStatus);
        TextView musicStatus = view.findViewById(R.id.musicStatus);
        ImageView notificationIcon = view.findViewById(R.id.notificationIcon);
        ImageView musicIcon = view.findViewById(R.id.musicIcon);

        // Przypisanie stanu przełączników na podstawie danych z SharedPreferences
        boolean notificationChecked = sharedPreferences.getBoolean("notificationSwitch", false);
        boolean musicChecked = sharedPreferences.getBoolean("musicSwitch", false);
        notificationSwitch.setChecked(notificationChecked);
        musicSwitch.setChecked(musicChecked);

        // Aktualizacja tekstu stanu powiadomień na podstawie pozycji suwaka
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("notificationSwitch", isChecked);
            editor.apply();

            // Aktualizacja tekstu w zależności od pozycji suwaka
            if (isChecked) {
                notificationStatus.setText("Włączone");
            } else {
                notificationStatus.setText("Wyłączone");
            }
        });

        // Aktualizacja tekstu stanu muzyki na podstawie pozycji suwaka
        musicSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("musicSwitch", isChecked);
            editor.apply();

            // Aktualizacja tekstu w zależności od pozycji suwaka
            if (isChecked) {
                musicStatus.setText("Włączona");
            } else {
                musicStatus.setText("Wyłączona");
            }
        });

        // Ustawienie tekstu stanu powiadomień na podstawie zapisanej wartości
        if (notificationChecked) {
            notificationStatus.setText("Włączony");
        } else {
            notificationStatus.setText("Wyłączony");
        }

        // Ustawienie tekstu stanu muzyki na podstawie zapisanej wartości
        if (musicChecked) {
            musicStatus.setText("Włączona");
        } else {
            musicStatus.setText("Wyłączona");
        }

        int notificationIconResId = isDarkThemeEnabled() ? R.drawable.baseline_notifications_24_night : R.drawable.baseline_notifications_24;
        notificationIcon.setImageResource(notificationIconResId);

        // Ustawienie ikony muzyki na podstawie motywu
        int musicIconResId = isDarkThemeEnabled() ? R.drawable.baseline_music_note_24_night : R.drawable.baseline_music_note_24;
        musicIcon.setImageResource(musicIconResId);

        builder.setPositiveButton("OK", (dialog, which) -> {
            // Tu możesz dodać dodatkową logikę po naciśnięciu przycisku OK
        });

        builder.setNegativeButton("Anuluj", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }
    private static boolean isDarkThemeEnabled() {
        int nightModeFlags = Resources.getSystem().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }
}
