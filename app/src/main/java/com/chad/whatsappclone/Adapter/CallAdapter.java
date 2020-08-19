package com.chad.whatsappclone.Adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.whatsappclone.Model.Call;
import com.chad.whatsappclone.Model.Chat;
import com.chad.whatsappclone.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CallAdapter extends RecyclerView.Adapter<CallAdapter.Holder>  {

    private List<Call> list;
    private Context context;

    public CallAdapter(List<Call> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.call_item, parent, false);
        return new Holder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        Call callList = list.get(position);

        holder.tvName.setText(callList.getUserName());
        holder.tvDate.setText(callList.getDate());

        if(callList.getCallType().equals("missed")) {
            holder.arrow.setImageDrawable(context.getDrawable(R.drawable.ic_arrow_downward));
            holder.arrow.getDrawable().setTint(context.getResources().getColor(android.R.color.holo_red_dark));
        }else if (callList.getCallType().equals("income")){
            holder.arrow.setImageDrawable(context.getDrawable(R.drawable.ic_arrow_downward));
            holder.arrow.getDrawable().setTint(context.getResources().getColor(android.R.color.holo_green_dark));
        }else {
            holder.arrow.setImageDrawable(context.getDrawable(R.drawable.ic_arrow_upward));
            holder.arrow.getDrawable().setTint(context.getResources().getColor(android.R.color.holo_green_dark));
        }
            Glide.with(context).load(callList.getUrlProfile()).into(holder.profile);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private TextView tvName, tvDate;
        private CircleImageView profile;
        private ImageView arrow;

        public Holder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            tvDate = itemView.findViewById(R.id.tv_date);
            profile = itemView.findViewById(R.id.profile_image);
            arrow = itemView.findViewById(R.id.arrow_image);

        }
    }

}
