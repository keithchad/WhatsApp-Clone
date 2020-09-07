package com.chad.whatsappclone.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.chad.whatsappclone.R;
import com.chad.whatsappclone.databinding.ActivityChatsBinding;

public class ChatsActivity extends AppCompatActivity {

    ActivityChatsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chats);

        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");
        String userID = intent.getStringExtra("userID");
        String userProfile = intent.getStringExtra("userProfile");

        if(userID != null) {
            binding.chatUsername.setText(userName);
            Glide.with(this).load(userProfile).into(binding.imageProfile);
        }

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}