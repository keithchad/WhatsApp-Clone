package com.chad.whatsappclone.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.chad.whatsappclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initialize();
    }

    private void initialize() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        delay(firebaseUser);
    }

    private void delay(FirebaseUser firebaseUser) {

        if(firebaseUser != null) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Intent mainIntent = new Intent(SplashActivity.this,
                        MainActivity.class);
                startActivity(mainIntent);
                finish();
            }, 3000);
        }

        else {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Intent mainIntent = new Intent(SplashActivity.this,
                        WelcomeActivity.class);
                startActivity(mainIntent);
                finish();
            }, 3000);
        }
    }

}