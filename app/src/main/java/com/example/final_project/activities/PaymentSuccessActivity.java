package com.example.final_project.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.final_project.R;

public class PaymentSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);

        LottieAnimationView animationView = findViewById(R.id.lottie_animation_view);
        animationView.playAnimation();


        TextView thankYouTextView = findViewById(R.id.thank_you);
        thankYouTextView.setText("Thank you for your purchase!");
    }
}
