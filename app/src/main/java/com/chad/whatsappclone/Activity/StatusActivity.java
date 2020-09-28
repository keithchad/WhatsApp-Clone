package com.chad.whatsappclone.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.whatsappclone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import jp.shts.android.storiesprogressview.StoriesProgressView;

public class StatusActivity extends AppCompatActivity {

    int counter = 0;
    long pressTime = 0L;
    long limit = 500L;

    private StoriesProgressView storiesProgressView;
    private ImageView imageProfile;
    private ImageView backButton;
    private ImageView deleteButton;
    private TextView textUsername, textTimeAgo;

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pressTime = System.currentTimeMillis();
                    storiesProgressView.pause();
                    imageProfile.setVisibility(View.INVISIBLE);
                    textUsername.setVisibility(View.INVISIBLE);
                    textTimeAgo.setVisibility(View.INVISIBLE);
                    backButton.setVisibility(View.INVISIBLE);
                    deleteButton.setVisibility(View.INVISIBLE);
                    return false;
                case MotionEvent.ACTION_UP:
                    long now = System.currentTimeMillis();
                    storiesProgressView.resume();
                    imageProfile.setVisibility(View.VISIBLE);
                    textUsername.setVisibility(View.VISIBLE);
                    textTimeAgo.setVisibility(View.VISIBLE);
                    backButton.setVisibility(View.VISIBLE);
                    deleteButton.setVisibility(View.VISIBLE);
                    return limit < now - pressTime;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        storiesProgressView = findViewById(R.id.status_bar);

        ImageView statusImageView = findViewById(R.id.statusImage);
        imageProfile = findViewById(R.id.imageProfile);
        backButton = findViewById(R.id.backButton);
        deleteButton = findViewById(R.id.deleteButton);


        textUsername = findViewById(R.id.textUsername);
        textTimeAgo = findViewById(R.id.textTimeAgo);

        String username = getIntent().getStringExtra("username");
        String profileImage = getIntent().getStringExtra("profileImage");
        String statusImage = getIntent().getStringExtra("statusImage");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlack));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorBlack));
        }

        textUsername.setText(username);
        Glide.with(this).load(profileImage).into(imageProfile);
        Glide.with(this).load(statusImage).into(statusImageView);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.no_anim, R.anim.slide_down);
            }
        });

        View reverse = findViewById(R.id.reverse);
        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.reverse();
            }
        });

        reverse.setOnTouchListener(onTouchListener);

        View skip = findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.skip();
            }
        });

        skip.setOnTouchListener(onTouchListener);

        storiesProgressView.setStoriesCount(10);
        storiesProgressView.setStoryDuration(5000L);
        storiesProgressView.startStories(counter);
  }
}