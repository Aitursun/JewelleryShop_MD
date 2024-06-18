package com.example.final_project.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.airbnb.lottie.LottieAnimationView;
import com.example.final_project.R;

public class SplashScreenActivity extends Activity {
    private static final int SPLASH_DISPLAY_LENGTH = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        LottieAnimationView animationView = findViewById(R.id.lottie_animation_view);
        animationView.setAnimation(R.raw.splash);
        animationView.playAnimation();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}

