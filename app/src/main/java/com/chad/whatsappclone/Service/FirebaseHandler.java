package com.chad.whatsappclone.Service;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHandler extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //Enable Disk Persistence
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
