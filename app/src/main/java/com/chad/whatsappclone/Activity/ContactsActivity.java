package com.chad.whatsappclone.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.chad.whatsappclone.R;
import com.chad.whatsappclone.databinding.ActivityContactsBinding;

public class ContactsActivity extends AppCompatActivity {

    private ActivityContactsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contacts);



    }
}