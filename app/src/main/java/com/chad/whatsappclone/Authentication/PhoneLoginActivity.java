package com.chad.whatsappclone.Authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.chad.whatsappclone.Model.User;
import com.chad.whatsappclone.R;
import com.chad.whatsappclone.databinding.ActivityPhoneLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class PhoneLoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "PhoneLoginActivity";
    private ActivityPhoneLoginBinding binding;
    String[] country = { "Kenya", "Uganda", "Tanzania", "Japan", "USA", "India", "UK", "Australia", "Other"};

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firestore;
    private String mVerification;


    private ProgressDialog progressDialog;
    private Button btnNext;

    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_phone_login);

        Spinner spin = findViewById(R.id.country_spinner);
        btnNext = findViewById(R.id.next_btn);
        progressDialog = new ProgressDialog(this);
        spin.setOnItemSelectedListener(this);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, country);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(arrayAdapter);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.nextBtn.getText().toString().equals("Next")) {
                    String phoneNumber = "+"+binding.edittextCountryCode.getText().toString()+binding.edittextPhonenumber.getText().toString();
                    startPhoneNumberVerification(phoneNumber);
                }else {
                    progressDialog.setMessage("Verifying...");
                    progressDialog.show();
                    verifyPhoneNumberWithCode(mVerification, binding.edittextCode.getText().toString());
                }
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
              //  Log.d(TAG, "signInWithCredential:Success");
                Toast.makeText(PhoneLoginActivity.this, "Verified!", Toast.LENGTH_SHORT).show();
                signInWithPhoneAuthCredential(phoneAuthCredential);
                progressDialog.dismiss();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(PhoneLoginActivity.this, "Verification Failed!Please try again Later ", Toast.LENGTH_SHORT).show();
               // Log.d(TAG, "onVerificationFailed:Success: "+e.getMessage());
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                //Log.d(TAG, "onCodeSent:" + verificationId);
                mVerification = verificationId;
                mResendToken = token;

                binding.nextBtn.setText("Confirm");
                progressDialog.dismiss();

            }
        };
    }

    private void startPhoneNumberVerification(String phoneNumber) {

        progressDialog.setMessage("Sending Code to: " +phoneNumber);
        progressDialog.show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        //Phone Number to verify
                60,              //Timeout Duration
                TimeUnit.SECONDS,   //Unit of Timeout
                this,        //Activity
                mCallbacks);
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(PhoneLoginActivity.this, "Signed In", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            FirebaseUser user = task.getResult().getUser();
                            startActivity(new Intent(PhoneLoginActivity.this, SetUserInfoActivity.class));
//                            if (user != null) {
//                                String userID = user.getUid();
//                                User users = new User(userID,"",user.getPhoneNumber(),"","","");
//
//                                firestore.collection("Users").document("UserInfo").collection(userID)
//                                        .add(users).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                    @Override
//                                    public void onSuccess(DocumentReference documentReference) {
//                                         startActivity(new Intent(PhoneLoginActivity.this, SetUserInfoActivity.class));
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Toast.makeText(PhoneLoginActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//
//                            }else {
//                                Toast.makeText(PhoneLoginActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
//                            }
                        } else {
                            progressDialog.dismiss();
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                               // Log.d(TAG, "signInWithCredential:failure");
                                Toast.makeText(PhoneLoginActivity.this, "The Code Entered is incorrect!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), country[position], Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}