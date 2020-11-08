package com.chad.whatsappclone.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.chad.whatsappclone.Adapter.StatusAdapter;
import com.chad.whatsappclone.Model.Status;
import com.chad.whatsappclone.R;
import com.chad.whatsappclone.databinding.FragmentStatusBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class StatusFragment extends Fragment {

    public StatusFragment() { }

    FragmentStatusBinding binding;
    private List<Status> list;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_status, container, false);

        initialize();
        getProfile();
        getStatus();

        return  binding.getRoot();
    }

    private void initialize() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(layoutManager);

        list = new ArrayList<>();
        StatusAdapter statusAdapter = new StatusAdapter(getContext(), list, getActivity());
        binding.recyclerView.setAdapter(statusAdapter);
    }

    private void getProfile() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        if (firebaseUser != null) {
            firebaseFirestore.collection("Users").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    String imageProfile = String.valueOf(documentSnapshot.get("imageProfile"));

                    if (getContext() != null) {
                        Glide.with(getContext()).load(imageProfile).into(binding.profileImage);
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void getStatus() {

        list.add(new Status("https://i.pinimg.com/originals/4d/1c/65/4d1c65f5478142a46a2cf46fd19ad55a.jpg","https://www.capitalfm.co.ke/thesauce/files/2019/01/cardi-b-tongue.jpg","Justin Beiber", "20 minutes ago"));
        list.add(new Status("https://www.capitalfm.co.ke/thesauce/files/2019/01/cardi-b-tongue.jpg","https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSt2Md9_Ov_3NBbpn5qAN4lGhG8RdPwqWyYvw&usqp=CAU","Jon Doe Beiber", "Yesterday, 3:25 PM"));
        list.add(new Status("https://i.insider.com/5c3e320e5241470201598c93?width=600&format=jpeg&auto=webp","https://i.pinimg.com/originals/4d/1c/65/4d1c65f5478142a46a2cf46fd19ad55a.jpg","Justin Beiber", "20 minutes ago"));
        list.add(new Status("https://i.pinimg.com/originals/4d/1c/65/4d1c65f5478142a46a2cf46fd19ad55a.jpg","https://i.insider.com/5c3e320e5241470201598c93?width=600&format=jpeg&auto=webp","Justin Beiber", "20 minutes ago"));

    }

}