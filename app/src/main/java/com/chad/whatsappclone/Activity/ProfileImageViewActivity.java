package com.chad.whatsappclone.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.chad.whatsappclone.Constants.Constants;
import com.chad.whatsappclone.R;
import com.chad.whatsappclone.databinding.ActivityProfileImageViewBinding;

public class ProfileImageViewActivity extends AppCompatActivity {

    ActivityProfileImageViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_image_view);

        initialize();
    }

    private void initialize() {
        binding.zoomImage.setImageBitmap(Constants.IMAGE_BITMAP);
    }
}