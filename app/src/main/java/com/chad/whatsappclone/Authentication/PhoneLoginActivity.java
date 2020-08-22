package com.chad.whatsappclone.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.chad.whatsappclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;

public class PhoneLoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String mVerification;

    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);

        mAuth = FirebaseAuth.getInstance();
    }
}