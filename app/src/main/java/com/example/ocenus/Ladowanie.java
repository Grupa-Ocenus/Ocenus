package com.example.ocenus;

import android.content.res.Configuration;
import android.os.Bundle;
        import androidx.appcompat.app.AppCompatActivity;

public class Ladowanie extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ladowanie);
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
            // Jasny motyw
            getWindow().getDecorView().setBackgroundColor(getResources().getColor(android.R.color.white));
        } else {
            // Ciemny motyw
            getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.motyw_noc));
        }
    }
}