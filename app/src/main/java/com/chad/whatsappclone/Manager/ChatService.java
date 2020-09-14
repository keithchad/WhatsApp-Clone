package com.chad.whatsappclone.Manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.chad.whatsappclone.Activity.ChatsActivity;
import com.chad.whatsappclone.Adapter.ChatsAdapter;
import com.chad.whatsappclone.Interface.OnReadChatCallBack;
import com.chad.whatsappclone.Model.Chats;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChatService {

    private static final String TAG = "ChatService";
    private Context context;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    private String receiverID;

    public ChatService(Context context, String receiverID) {
        this.context = context;
        this.receiverID = receiverID;
    }

    public  void readChatData(final OnReadChatCallBack onReadChatCallBack)  {
        reference.child("Chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Chats> list = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chats chats = snapshot.getValue(Chats.class);
                    if (chats != null && chats.getSender().equals(firebaseUser.getUid()) && chats.getReceiver().equals(receiverID)
                            || chats.getReceiver().equals(firebaseUser.getUid()) && chats.getSender().equals(receiverID)
                    ) {
                        list.add(chats);
                        Log.e(TAG, "onDataChange : Username :"+chats.getTextMessage());
                    }
                }
                onReadChatCallBack.onReadSuccess(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Something went wrong!", Toast.LENGTH_LONG).show();
                onReadChatCallBack.onReadFailed();
            }
        });
    }

    public  void sendTextMessage(String text) {

        Chats chats = new Chats(
                getCurrentDate(),
                text,
                "TEXT",
                firebaseUser.getUid(),
                receiverID,
                ""
        );

        reference.child("Chats").push().setValue(chats).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("SEND", "Success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("SEND","Failed: "+e.getMessage());
            }
        });

        //Add to ChatList
        DatabaseReference chatReference = FirebaseDatabase.getInstance().getReference("ChatList").child(firebaseUser.getUid()).child(receiverID);
        chatReference.child("chatid").setValue(receiverID);

        DatabaseReference chatReference1 = FirebaseDatabase.getInstance().getReference("ChatList").child(receiverID).child(firebaseUser.getUid());
        chatReference1.child("chatid").setValue(firebaseUser.getUid());


    }

    public void sendImage(String imageUrl) {
        Chats chats = new Chats(
                getCurrentDate(),
                "",
                "IMAGE",
                firebaseUser.getUid(),
                receiverID,
                imageUrl
        );

        reference.child("Chats").push().setValue(chats).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("SEND", "Success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("SEND","Failed: "+e.getMessage());
            }
        });

        //Add to ChatList
        DatabaseReference chatReference = FirebaseDatabase.getInstance().getReference("ChatList").child(firebaseUser.getUid()).child(receiverID);
        chatReference.child("chatid").setValue(receiverID);

        DatabaseReference chatReference1 = FirebaseDatabase.getInstance().getReference("ChatList").child(receiverID).child(firebaseUser.getUid());
        chatReference1.child("chatid").setValue(firebaseUser.getUid());
    }

    public String getCurrentDate() {
        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String today = formatter.format(date);

        Calendar currentDateTime = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        String currentTime = dateFormat.format(currentDateTime.getTime());

        return today+", "+currentTime;

    }



}
