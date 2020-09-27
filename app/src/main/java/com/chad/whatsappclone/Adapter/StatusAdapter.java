package com.chad.whatsappclone.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.chad.whatsappclone.Activity.StatusActivity;
import com.chad.whatsappclone.Model.Status;
import com.chad.whatsappclone.R;

import java.io.File;
import java.util.List;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.ViewHolder> {

    private Context context;
    private List<Status> list;
    private Activity activity = (Activity) context;
    //private File[] date;

    public StatusAdapter(Context context, List<Status> list, Activity activity) {
        this.context = context;
        this.list = list;
        this.activity = activity;
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

         final Status status = list.get(position);

        Glide.with(context).load(status.getProfileImage()).into(holder.profileImage);
        holder.textUsername.setText(status.getUserName());
        holder.textTimeAgo.setText(status.getTimeAgo());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StatusActivity.class);
                intent.putExtra("username", status.getUserName());
                intent.putExtra("profileImage", status.getProfileImage());
                intent.putExtra("statusImage", status.getStatusImage());
                context.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_up, R.anim.no_anim);

            }
        });

        //holder.textTimeAgo.setText(date[position].lastModified() + "");
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
