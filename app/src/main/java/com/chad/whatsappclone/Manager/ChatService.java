package com.chad.whatsappclone.Manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.chad.whatsappclone.Interface.OnReadChatCallBack;
import com.chad.whatsappclone.Model.Chats;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
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

                    if (chats!= null) {
                       try {
                           if (chats.getSender().equals(firebaseUser.getUid()) && chats.getReceiver().equals(receiverID)
                                   || chats.getReceiver().equals(firebaseUser.getUid()) && chats.getSender().equals(receiverID)
                           ) {
                               list.add(chats);
                               Log.e(TAG, "onDataChange : Username :"+chats.getTextMessage());
                           }
                       } catch (Exception e) {
                           e.printStackTrace();
                       }
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

        reference.child("Chats").push().setValue(chats)
                .addOnSuccessListener(aVoid -> Log.d("SEND", "Success")).
                addOnFailureListener(e -> Log.d("SEND","Failed: "+e.getMessage()));

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

        reference.child("Chats").push().setValue(chats).addOnSuccessListener(aVoid -> Log.d("SEND", "Success")).addOnFailureListener(e -> Log.d("SEND","Failed: "+e.getMessage()));

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

    public void sendVoice(String audioPath){
        final Uri uriAudio = Uri.fromFile(new File(audioPath));
        final StorageReference audioRef = FirebaseStorage.getInstance().getReference().child("Chats/VoiceMessages/" + System.currentTimeMillis());
        audioRef.putFile(uriAudio).addOnSuccessListener(audioSnapshot -> {
            Task<Uri> urlTask = audioSnapshot.getStorage().getDownloadUrl();
            while (!urlTask.isSuccessful()) ;
            Uri downloadUrl = urlTask.getResult();
            String voiceUrl = String.valueOf(downloadUrl);

            Chats chats = new Chats(
                    getCurrentDate(),
                    "",
                    "VOICE",
                    firebaseUser.getUid(),
                    receiverID,
                    voiceUrl
            );

            reference.child("Chats").push().setValue(chats)
                    .addOnSuccessListener(aVoid -> Log.d("Send", "onSuccess: "))
                    .addOnFailureListener(e -> Log.d("Send", "onFailure: "+e.getMessage()));

            //Add to ChatList
            DatabaseReference chatRef1 = FirebaseDatabase.getInstance().getReference("ChatList").child(firebaseUser.getUid()).child(receiverID);
            chatRef1.child("chatid").setValue(receiverID);

            //
            DatabaseReference chatRef2 = FirebaseDatabase.getInstance().getReference("ChatList").child(receiverID).child(firebaseUser.getUid());
            chatRef2.child("chatid").setValue(firebaseUser.getUid());
        });
    }

}
