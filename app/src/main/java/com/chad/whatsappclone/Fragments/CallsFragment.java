package com.chad.whatsappclone.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.whatsappclone.Adapter.CallAdapter;
import com.chad.whatsappclone.Adapter.ChatAdapter;
import com.chad.whatsappclone.Model.Call;
import com.chad.whatsappclone.Model.Chat;
import com.chad.whatsappclone.R;

import java.util.ArrayList;
import java.util.List;

public class CallsFragment extends Fragment {
    
    public CallsFragment() {
        // Required empty public constructor
    }


    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calls, container, false);


        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Call> list = new ArrayList<>();

        list.add(new Call("11","John Doe","August 13, 8:54 AM","","income"));
        list.add(new Call("11","Jane Doe","August 15, 4:35 AM","","missed"));
        list.add(new Call("11","Erick Doe","August 22, 3:25 AM","","missed"));
        list.add(new Call("11","Josh Doe","August 30, 2:52 AM","","missed"));
        list.add(new Call("11","Sam Doe","August 1, 9:25 AM","","income"));
        list.add(new Call("11","Sheila Doe","August 8, 11:57 AM","","missed"));
        list.add(new Call("11","Sarah Doe","August 2, 2:45 PM!","","income"));
        list.add(new Call("11","Lilly Doe","August 10, 6:55 AM","","income"));
        list.add(new Call("11","Emmy Doe","August 5, 7:35 PM","","missed"));
        list.add(new Call("11","Sharon Doe","August 31, 3:15 AM","","income"));

        recyclerView.setAdapter(new CallAdapter(list, getContext()));

        return view;
    }


}