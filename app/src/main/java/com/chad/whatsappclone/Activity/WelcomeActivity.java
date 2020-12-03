package com.chad.whatsappclone.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.chad.whatsappclone.Authentication.PhoneLoginActivity;
import com.chad.whatsappclone.R;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initialize();
    }

    private void initialize() {
        Button agreeButton = findViewById(R.id.agreeButton);
        agreeButton.setOnClickListener(v -> {
            startActivity(new Intent(WelcomeActivity.this, PhoneLoginActivity.class));
            finish();
        });
    }

}