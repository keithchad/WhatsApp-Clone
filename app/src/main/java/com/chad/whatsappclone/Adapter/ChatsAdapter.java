package com.chad.whatsappclone.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.whatsappclone.Manager.AudioService;
import com.chad.whatsappclone.Model.Chats;
import com.chad.whatsappclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder> {

    private List<Chats> list;
    private Context context;
    public static  final int MSG_TYPE_LEFT = 0;
    public static  final int MSG_TYPE_RIGHT = 1;
    private ImageButton tmpPlay;
    private AudioService audioService;

    public ChatsAdapter(List<Chats> list, Context context) {
        this.list = list;
        this.context = context;
        this.audioService = new AudioService(context);
    }

    public void setList(List<Chats> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_LEFT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new ViewHolder(view);
        }else {
            View view1 = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new ViewHolder(view1);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textMessage;
        private LinearLayout textLayout;
        private LinearLayout voiceLayout;
        private ImageView chatImage;
        private ViewHolder tmpHolder;

        private ImageButton playButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textview_textmessage);
            textLayout = itemView.findViewById(R.id.text_layout);
            voiceLayout = itemView.findViewById(R.id.voiceLayout);
            chatImage = itemView.findViewById(R.id.chat_image);

            playButton = itemView.findViewById(R.id.btn_play_chat);
        }

        void bind(final Chats chats) {
            switch (chats.getType()) {
                case "TEXT" :
                    textLayout.setVisibility(View.VISIBLE);
                    voiceLayout.setVisibility(View.GONE);
                    chatImage.setVisibility(View.GONE);

                    textMessage.setText(chats.getTextMessage());
                    break;
                case "IMAGE" :
                    textLayout.setVisibility(View.GONE);
                    voiceLayout.setVisibility(View.GONE);
                    chatImage.setVisibility(View.VISIBLE);

                    Glide.with(context).load(chats.getUrl()).into(chatImage);
                    break;

                case "VOICE" :
                    textLayout.setVisibility(View.GONE);
                    chatImage.setVisibility(View.GONE);
                    voiceLayout.setVisibility(View.VISIBLE);

                   voiceLayout.setOnClickListener(new View.OnClickListener() {
                       @SuppressLint("UseCompatLoadingForDrawables")
                       @Override
                       public void onClick(View v) {

                           if (tmpPlay != null) {
                               tmpPlay.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_pause));
                           }
                            playButton.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_pause));

                            audioService.playAudioFromUrl(chats.getUrl(), new AudioService.OnPlayCallBack() {
                                @Override
                                public void onFinished() {
                                    playButton.setImageDrawable(context.getResources().getDrawable(R.drawable.play_circle));
                                }
                            });

                            tmpPlay = playButton;
                       }
                   });
                    break;
            }

        }

    }

    @Override
    public int getItemViewType(int position) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        if(list.get(position).getSender().equals(firebaseUser.getUid())) {
            return  MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }
}
