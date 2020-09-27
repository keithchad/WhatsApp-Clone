package com.chad.whatsappclone.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.chad.whatsappclone.Model.Status;
import com.chad.whatsappclone.R;

import java.io.File;
import java.util.List;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.ViewHolder> {

    private Context context;
    private List<Status> list;
    //private File[] date;

    public StatusAdapter(Context context, List<Status> list) {
        this.context = context;
        this.list = list;
        //this.date = date;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.status_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

         Status status = list.get(position);

        Glide.with(context).load(status.getProfileImage()).into(holder.profileImage);
        holder.textUsername.setText(status.getUserName());
        holder.textTimeAgo.setText(status.getTimeAgo());

//        holder.textTimeAgo.setText(date[position].lastModified() + "");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView profileImage;
        private TextView textUsername;
        private TextView textTimeAgo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.imageProfile);
            textUsername = itemView.findViewById(R.id.textUsername);
            textTimeAgo = itemView.findViewById(R.id.textTimeAgo);

        }
    }
}
