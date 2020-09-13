package com.chad.whatsappclone.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.chad.whatsappclone.Constants.Constants;
import com.chad.whatsappclone.R;
import com.chad.whatsappclone.databinding.ActivityUserProfileBinding;

public class UserProfileActivity extends AppCompatActivity {

    private ActivityUserProfileBinding binding;

    private String userProfile;
    private String receiverID;
    private  String about;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile);

        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");
        receiverID = intent.getStringExtra("userID");
        userProfile = intent.getStringExtra("imageProfile");
        about = intent.getStringExtra("about");

        binding.imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.imageProfile.invalidate();
                Drawable drawable = binding.imageProfile.getDrawable();
                Constants.IMAGE_BITMAP = ((BitmapDrawable)drawable.getCurrent()).getBitmap();
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(UserProfileActivity.this, binding.imageProfile, "image");
                Intent intent = new Intent(UserProfileActivity.this, ProfileImageViewActivity.class);
                startActivity(intent, activityOptionsCompat.toBundle());
            }
        });


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
        initToolbar();
    }

    private void initToolbar() {
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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