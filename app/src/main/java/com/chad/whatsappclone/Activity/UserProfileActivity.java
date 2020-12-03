package com.chad.whatsappclone.Activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.chad.whatsappclone.Constants.Constants;
import com.chad.whatsappclone.R;
import com.chad.whatsappclone.databinding.ActivityUserProfileBinding;

import java.util.Objects;

public class UserProfileActivity extends AppCompatActivity {

    private ActivityUserProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile);

       getIntentMethod();

        initialize();
    }

    private void initialize() {
        binding.imageProfile.setOnClickListener(v -> {
            binding.imageProfile.invalidate();
            Drawable drawable = binding.imageProfile.getDrawable();
            Constants.IMAGE_BITMAP = ((BitmapDrawable)drawable.getCurrent()).getBitmap();
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(UserProfileActivity.this, binding.imageProfile, "image");
            Intent intent = new Intent(UserProfileActivity.this, ProfileImageViewActivity.class);
            startActivity(intent, activityOptionsCompat.toBundle());
        });

        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void getIntentMethod() {

        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");
        String receiverID = intent.getStringExtra("userID");
        String userProfile = intent.getStringExtra("imageProfile");
        String about = intent.getStringExtra("about");


        if(receiverID != null) {
            binding.toolbar.setTitle(userName);
            if (userProfile != null) {
                if(userProfile.equals("")) {
                    binding.imageProfile.setImageResource(R.drawable.profile_image);
                }else {
                    Glide.with(this).load(userProfile).into(binding.imageProfile);
                }
            }
            binding.aboutText.setText(about);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}