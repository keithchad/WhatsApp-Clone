package com.chad.whatsappclone.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.whatsappclone.Adapter.ChatAdapter;
import com.chad.whatsappclone.Model.Chat;
import com.chad.whatsappclone.R;

import java.util.ArrayList;
import java.util.List;


public class ChatsFragment extends Fragment {

    public ChatsFragment() {
        // Required empty public constructor
    }

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)  {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Chat> list = new ArrayList<>();

        list.add(new Chat("11","John Doe","Hello Homie!","20/08/2020",""));
        list.add(new Chat("11","Jane Doe","Hello Homie!","20/08/2020",""));
        list.add(new Chat("11","Erick Doe","Hello Homie!","20/08/2020",""));
        list.add(new Chat("11","Josh Doe","Hello Homie!","20/08/2020",""));
        list.add(new Chat("11","Sam Doe","Hello Homie!","20/08/2020",""));
        list.add(new Chat("11","Sheila Doe","Hello Homie!","20/08/2020",""));
        list.add(new Chat("11","Sarah Doe","Hello Homie!","20/08/2020",""));
        list.add(new Chat("11","Lilly Doe","Hello Homie!","20/08/2020",""));
        list.add(new Chat("11","Emmy Doe","Hello Homie!","20/08/2020",""));
        list.add(new Chat("11","Sharon Doe","Hello Homie!","20/08/2020",""));

        recyclerView.setAdapter(new ChatAdapter(list, getContext()));

        return view;
    }

}