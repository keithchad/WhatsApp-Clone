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
//        list.add(new Status(R.drawable.testimonial1 ,R.drawable.testimonial1, "Hope", "Yesterday, 3:25 PM"));
//        list.add(new Status(R.drawable.testimonial2 ,R.drawable.testimonial1, "Hope Michaelson", "Today, 1:07PM"));
//        list.add(new Status(R.drawable.testimonial3 ,R.drawable.testimonial1, "Velma", "20 Minutes ago"));
//        list.add(new Status(R.drawable.testimonial4 ,R.drawable.testimonial1, "Mivel Michaelson", "Today 12:02AM"));
//        list.add(new Status(R.drawable.testimonial6 ,R.drawable.testimonial1, "Ian", "40 Minutes ago"));
//        list.add(new Status(R.drawable.thumb1, R.drawable.testimonial1, "Tevin", "20 Minutes ago"));
//        list.add(new Status(R.drawable.thumb2, R.drawable.testimonial1, "Tash Michaelson", "20 Minutes ago"));
//        list.add(new Status(R.drawable.thumb3, R.drawable.testimonial1, "Femi", "20 Minutes ago"));
//        list.add(new Status(R.drawable.thumb4, R.drawable.testimonial1, "John Doe", "20 Minutes ago"));
//        list.add(new Status(R.drawable.thumb5, R.drawable.testimonial1, "Jane Michaelson", "20 Minutes ago"));
//        list.add(new Status(R.drawable.thumb6, R.drawable.testimonial1, "Erick", "20 Minutes ago"));

        list.add(new Status("https://i.pinimg.com/originals/4d/1c/65/4d1c65f5478142a46a2cf46fd19ad55a.jpg","https://www.capitalfm.co.ke/thesauce/files/2019/01/cardi-b-tongue.jpg","Justin Beiber", "20 minutes ago"));
        list.add(new Status("https://www.capitalfm.co.ke/thesauce/files/2019/01/cardi-b-tongue.jpg","https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSt2Md9_Ov_3NBbpn5qAN4lGhG8RdPwqWyYvw&usqp=CAU","Jon Doe Beiber", "Yesterday, 3:25 PM"));
        list.add(new Status("https://i.insider.com/5c3e320e5241470201598c93?width=600&format=jpeg&auto=webp","https://i.pinimg.com/originals/4d/1c/65/4d1c65f5478142a46a2cf46fd19ad55a.jpg","Justin Beiber", "20 minutes ago"));
        list.add(new Status("https://i.pinimg.com/originals/4d/1c/65/4d1c65f5478142a46a2cf46fd19ad55a.jpg","https://i.insider.com/5c3e320e5241470201598c93?width=600&format=jpeg&auto=webp","Justin Beiber", "20 minutes ago"));

//        list.add(new Call("11","Erick Doe","August 22, 3:25 AM","https://www.capitalfm.co.ke/thesauce/files/2019/01/cardi-b-tongue.jpg","missed"));
//        list.add(new Call("11","Josh Doe","August 30, 2:52 AM","data:image/jpeg;base64,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","missed"));
//        list.add(new Call("11","Sam Doe","August 1, 9:25 AM","https://www.capitalfm.co.ke/thesauce/files/2019/01/cardi-b-tongue.jpg","income"));
//        list.add(new Call("11","Sheila Doe","August 8, 11:57 AM","data:image/jpeg;base64,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","missed"));
//        list.add(new Call("11","Sarah Doe","August 2, 2:45 PM!","https://i.insider.com/5c3e320e5241470201598c93?width=600&format=jpeg&auto=webp","income"));
//        list.add(new Call("11","Lilly Doe","August 10, 6:55 AM","https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSt2Md9_Ov_3NBbpn5qAN4lGhG8RdPwqWyYvw&usqp=CAU","income"));
//        list.add(new Call("11","Emmy Doe","August 5, 7:35 PM","data:image/jpeg;base64,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","missed"));
//        list.add(new Call("11","Sharon Doe","August 31, 3:15 AM","data:image/jpeg;base64,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","income"));
//
    }

}