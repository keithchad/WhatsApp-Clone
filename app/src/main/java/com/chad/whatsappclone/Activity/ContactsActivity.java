package com.chad.whatsappclone.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.Toast;

import com.chad.whatsappclone.Adapter.ContactsAdapter;
import com.chad.whatsappclone.Model.User;
import com.chad.whatsappclone.R;
import com.chad.whatsappclone.databinding.ActivityContactsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    private ActivityContactsBinding binding;

    private List<User> list = new ArrayList<>();

    private ContactsAdapter contactsAdapter;

    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contacts);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if(firebaseUser != null) {
            getContactList();
        }

    }

    private void getContactList() {

        firebaseFirestore.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                 for(QueryDocumentSnapshot snapshots : queryDocumentSnapshots) {
                     Toast.makeText(ContactsActivity.this, snapshots.toString(), Toast.LENGTH_SHORT).show();

                     String userID = snapshots.getString("userID");
                     String username = snapshots.getString("userName");
                     String imageUrl = snapshots.getString("imageProfile");
                     String about = snapshots.getString("about");

                     User users = new User();
                     users.setUserID(userID);
                     users.setAbout(about);
                     users.setUserName(username);
                     users.setImageProfile(imageUrl);

                     if(userID != null && !userID.equals(firebaseUser.getUid())) {
                         list.add(users);
                     }

                 }
                 contactsAdapter = new ContactsAdapter(list, ContactsActivity.this);
                 binding.recyclerView.setAdapter(contactsAdapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ContactsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}