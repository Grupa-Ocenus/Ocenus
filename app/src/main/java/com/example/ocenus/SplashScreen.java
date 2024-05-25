package com.example.ocenus;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_TIMER = 2000;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        // Initialize ProgressBar
        progressBar = findViewById(R.id.progressBar);

        // Load animation
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.progress_anim);

        // Apply animation to ProgressBar
        progressBar.startAnimation(animation);

        // Start a delayed action to hide ProgressBar and transition to the next activity
        new Handler().postDelayed(() -> {
            // Hide ProgressBar


            // Start your main activity
            Intent intent = new Intent(SplashScreen.this, Logowanie.class);
            startActivity(intent);
            finish();
        }, SPLASH_TIMER);
    }
}
