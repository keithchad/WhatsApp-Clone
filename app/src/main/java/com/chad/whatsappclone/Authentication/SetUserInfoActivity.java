package com.chad.whatsappclone.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.chad.whatsappclone.Activity.MainActivity;
import com.chad.whatsappclone.Model.User;
import com.chad.whatsappclone.R;
import com.chad.whatsappclone.databinding.ActivitySetUserInfoBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;


public class SetUserInfoActivity extends AppCompatActivity {

    private ActivitySetUserInfoBinding binding;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_user_info);

        initialize();
        nextButtonClicked();

    }

    private void initialize() {
        progressDialog = new ProgressDialog(this);
    }

    private void nextButtonClicked() {

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if(TextUtils.isEmpty(Objects.requireNonNull(binding.edittextName.getText()).toString())) {
                        Toast.makeText(SetUserInfoActivity.this, "Name Field Cannot be empty", Toast.LENGTH_SHORT).show();
                    }else {
                        doUpdate();
                    }
                }

            }
        });

        binding.imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SetUserInfoActivity.this, "Still in development", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void doUpdate() {
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        if(firebaseUser != null) {
            String userID = firebaseUser.getUid();
            String username = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                username = Objects.requireNonNull(binding.edittextName.getText()).toString();
            }
            User users = new User(userID,username,firebaseUser.getPhoneNumber(),"","","Hey there! I am using WhatsApp");

            firebaseFirestore.collection("Users").document(firebaseUser.getUid()).set(users)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            Toast.makeText(SetUserInfoActivity.this, "Update Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SetUserInfoActivity.this, MainActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SetUserInfoActivity.this, "Update failed!", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            progressDialog.dismiss();
            Toast.makeText(this, "You need to login First", Toast.LENGTH_SHORT).show();
        }

    }
}