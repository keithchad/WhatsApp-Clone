package com.chad.whatsappclone.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.whatsappclone.Model.ChatList;
import com.chad.whatsappclone.R;

import java.util.Objects;

public class DialogViewUser {
    private Context context;

    public DialogViewUser(Context context, ChatList chatlist) {
        this.context = context;
        initialize(chatlist);
    }
    public void initialize(ChatList chatlist){

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_ACTION_BAR); // before
        dialog.setContentView(R.layout.dialog_view_user);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        ImageButton btnChat, btnCall, btnVideoCall, btnInfo;
        ImageView profile;
        TextView userName;

        btnChat = dialog.findViewById(R.id.chatButton);
        btnCall = dialog.findViewById(R.id.callButton);
        btnVideoCall = dialog.findViewById(R.id.videoButton);
        btnInfo = dialog.findViewById(R.id.infoButton);

        profile = dialog.findViewById(R.id.imageProfile);
        userName = dialog.findViewById(R.id.textUsername);

        userName.setText(chatlist.getUserName());
        Glide.with(context).load(chatlist.getUrlProfile()).into(profile);

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Chats Clicked",Toast.LENGTH_SHORT).show();
            }
        });
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Call Clicked",Toast.LENGTH_SHORT).show();
            }
        });
        btnVideoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Video Call Clicked",Toast.LENGTH_SHORT).show();
            }
        });
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Info Clicked",Toast.LENGTH_SHORT).show();
            }
        });


        dialog.show();
    }
}
