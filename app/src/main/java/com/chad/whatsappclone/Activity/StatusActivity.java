package com.chad.whatsappclone.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
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
    private ImageView statusImageView, imageProfile, backButton;
    private TextView textUsername, textTimeAgo;

    private TextView seen_number;

    private List<String> images;
    private List<String> storyIds;
    String userId;

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
                    return false;
                case MotionEvent.ACTION_UP:
                    long now = System.currentTimeMillis();
                    storiesProgressView.resume();
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

        statusImageView = findViewById(R.id.statusImage);
        imageProfile = findViewById(R.id.imageProfile);
        backButton = findViewById(R.id.backButton);

        textUsername = findViewById(R.id.textUsername);
        textTimeAgo = findViewById(R.id.textTimeAgo);

        String profileImage = getIntent().getStringExtra("profileImage");
        String username = getIntent().getStringExtra("username");
        String statusImage = getIntent().getStringExtra("statusImage");

        textUsername.setText(username);
        Glide.with(this).load(profileImage).into(imageProfile);
        Glide.with(this).load(statusImage).into(statusImageView);

//        LinearLayout read_seen = findViewById(R.id.read_seen);
//        seen_number = findViewById(R.id.seen_number);
//        ImageView story_delete = findViewById(R.id.story_delete);
//
//        read_seen.setVisibility(View.GONE);
//        story_delete.setVisibility(View.GONE);
//
//        userId = getIntent().getStringExtra("userid");
//
//        if(userId.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
//            read_seen.setVisibility(View.VISIBLE);
//            story_delete.setVisibility(View.VISIBLE);
//        }
//
//        getStories(userId);
//        userInfo(userId);
//
//        View reverse = findViewById(R.id.reverse);
//        reverse.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                storiesProgressView.reverse();
//            }
//        });
//
//        reverse.setOnTouchListener(onTouchListener);
//
//        View skip = findViewById(R.id.skip);
//        skip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                storiesProgressView.skip();
//            }
//        });
//
//        skip.setOnTouchListener(onTouchListener);
//
//        read_seen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(StoryActivity.this, FollowersActivity.class);
//                intent.putExtra("id", userId);
//                intent.putExtra("storyid", storyIds.get(counter));
//                intent.putExtra("title", "views");
//                startActivity(intent);
//
//            }
//        });
//
//        story_delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Story")
//                        .child(userId).child(storyIds.get(counter));
//
//                reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(StoryActivity.this, "Deleted!", Toast.LENGTH_SHORT).show();
//                            finish();
//                        }
//                    }
//                });
//            }
//        });
//
//
//    }
//
//    @Override
//    public void onNext() {
//        Glide.with(getApplicationContext()).load(images.get(++counter)).into(image);
//
//        addView(storyIds.get(counter));
//        seenNumber(storyIds.get(counter));
//
//    }
//
//    @Override
//    public void onPrev() {
//
//        if ((counter -1) < 0) return;
//        Glide.with(getApplicationContext()).load(images.get(--counter)).into(image);
//        seenNumber(storyIds.get(counter));
//
//    }
//
//    @Override
//    public void onComplete() {
//        finish();
//    }
//
//    @Override
//    protected void onDestroy() {
//        storiesProgressView.destroy();
//        super.onDestroy();
//    }
//
//    @Override
//    protected void onPause() {
//        storiesProgressView.pause();
//        super.onPause();
//    }
//
//    @Override
//    protected void onResume() {
//        storiesProgressView.resume();
//        super.onResume();
//    }
//
//    private  void getStories(String userid) {
//        images = new ArrayList<>();
//        storyIds = new ArrayList<>();
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Story")
//                .child(userid);
//        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                images.clear();
//                storyIds.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Story story = snapshot.getValue(Story.class);
//                    long timecurrent = System.currentTimeMillis();
//                    if (timecurrent > story.getTimestart() && timecurrent < story.getTimeend()) {
//                        images.add(story.getImageurl());
//                        storyIds.add(story.getStoryid());
//                    }
//                }
//
//                storiesProgressView.setStoriesCount(images.size());
//                storiesProgressView.setStoryDuration(5000L);
//                storiesProgressView.setStoriesListener(StoryActivity.this);
//                storiesProgressView.startStories(counter);
//
//                Glide.with(getApplicationContext()).load(images.get(counter)).into(image);
//
//                addView(storyIds.get(counter));
//                seenNumber(storyIds.get(counter));
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//
//    private void userInfo(String userid) {
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users")
//                .child(userid);
//        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                User user = dataSnapshot.getValue(User.class);
//                if (user != null) {
//                    Glide.with(getApplicationContext()).load(user.getImageurl()).into(story_photo);
//                    story_username.setText(user.getUsername());
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//
//    private void addView(String storyid) {
//        FirebaseDatabase.getInstance().getReference("Story").child(userId)
//                .child(storyid).child("views").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
//    }
//
//    private void seenNumber(String storyId) {
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Story")
//                .child(userId).child(storyId).child("views");
//        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                seen_number.setText(""+dataSnapshot.getChildrenCount());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
    }
}