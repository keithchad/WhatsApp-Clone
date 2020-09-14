package com.chad.whatsappclone.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.whatsappclone.Adapter.ChatsAdapter;
import com.chad.whatsappclone.Constants.DialogReviewSendImage;
import com.chad.whatsappclone.Interface.OnReadChatCallBack;
import com.chad.whatsappclone.Manager.ChatService;
import com.chad.whatsappclone.Model.Chats;
import com.chad.whatsappclone.R;
import com.chad.whatsappclone.Service.FirebaseService;
import com.chad.whatsappclone.databinding.ActivityChatsBinding;

import java.util.ArrayList;
import java.util.List;

public class ChatsActivity extends AppCompatActivity {

    ActivityChatsBinding binding;
//    private FirebaseUser firebaseUser;
//    private DatabaseReference reference;
    private String userProfile;
    private String userName;
    private String about;
    private String receiverID;
    private boolean isActionShown = false;

    private ChatsAdapter chatsAdapter;
    private ChatService chatService;

    private static int REQUEST_CODE_GALLERY = 111;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chats);

        initialize();
        initBtnClick();
        readChats();

    }

    private void initialize() {
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        reference = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        receiverID = intent.getStringExtra("userID");
        userProfile = intent.getStringExtra("userProfile");
        about = intent.getStringExtra("about");

        chatService = new ChatService(this, receiverID);

        if(receiverID != null) {
            binding.chatUsername.setText(userName);
            if (userProfile != null) {
                if(userProfile.equals("")) {
                    binding.imageProfile.setImageResource(R.drawable.profile_image);
                }else {
                    Glide.with(this).load(userProfile).into(binding.imageProfile);
                }
            }
        }

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.messageEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(binding.messageEdittext.getText().toString())) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        binding.sendButton.setImageDrawable(getDrawable(R.drawable.ic_voice));
                    }
                }else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        binding.sendButton.setImageDrawable(getDrawable(R.drawable.ic_send));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        List<Chats> list = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        binding.recyclerView.setLayoutManager(layoutManager);
        chatsAdapter = new ChatsAdapter(list,this);
        binding.recyclerView.setAdapter(chatsAdapter);
    }

    private void readChats() {
        chatService.readChatData(new OnReadChatCallBack() {
            @Override
            public void onReadSuccess(List<Chats> list) {
                chatsAdapter.setList(list);
            }

            @Override
            public void onReadFailed() {
                Toast.makeText(ChatsActivity.this, "Read Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initBtnClick() {
        binding.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(binding.messageEdittext.getText().toString())) {
                    chatService.sendTextMessage(binding.messageEdittext.getText().toString());

                    binding.messageEdittext.setText("");

                }
            }
        });

        binding.imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatsActivity.this, UserProfileActivity.class)
                .putExtra("userID", receiverID)
                .putExtra("imageProfile", userProfile)
                .putExtra("userName", userName)
                .putExtra("about", about));
            }
        });

        binding.usernameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatsActivity.this, UserProfileActivity.class)
                        .putExtra("userID", receiverID)
                        .putExtra("imageProfile", userProfile)
                        .putExtra("userName", userName)
                        .putExtra("about", about));
            }
        });

        binding.fileAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isActionShown) {
                    binding.layoutActions.setVisibility(View.GONE);
                    isActionShown = false;
                }else {
                    binding.layoutActions.setVisibility(View.VISIBLE);
                    isActionShown = true;
                }
            }
        });

        binding.gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

    }

    private void openGallery() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select image"), REQUEST_CODE_GALLERY);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            //uploadToFirebase();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                reviewImage(bitmap);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
//
    }

    private void reviewImage(Bitmap bitmap) {
        new DialogReviewSendImage(ChatsActivity.this, bitmap).show(new DialogReviewSendImage.OnCallBack() {
            @Override
            public void onButtonSendClick() {
                 final ProgressDialog progressDialog = new ProgressDialog(ChatsActivity.this);
                 progressDialog.setMessage("Sending Image!");
                if(imageUri != null) {
                    new FirebaseService(ChatsActivity.this).uploadImageToFirebaseStorage(imageUri, new FirebaseService.onCallBack() {
                        @Override
                        public void onUploadSuccess(String imageUrl) {
                            chatService.sendImage(imageUrl);
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onUploadFailed(Exception e) {

                        }
                    });
                }
            }
        });
    }


//    private void sendTextMessage(String text) {
//
//        Date date = Calendar.getInstance().getTime();
//        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
//        String today = formatter.format(date);
//
//        Calendar currentDateTime = Calendar.getInstance();
//        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
//        String currentTime = dateFormat.format(currentDateTime.getTime());
//
//        Chats chats = new Chats(
//                today+", "+currentTime,
//                text,
//                "TEXT",
//                firebaseUser.getUid(),
//                receiverID
//        );
//
//        reference.child("Chats").push().setValue(chats).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Log.d("SEND", "Success");
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d("SEND","Failed: "+e.getMessage());
//            }
//        });
//
//        //Add to ChatList
//        DatabaseReference chatReference = FirebaseDatabase.getInstance().getReference("ChatList").child(firebaseUser.getUid()).child(receiverID);
//        chatReference.child("chatid").setValue(receiverID);
//
//        DatabaseReference chatReference1 = FirebaseDatabase.getInstance().getReference("ChatList").child(receiverID).child(firebaseUser.getUid());
//        chatReference1.child("chatid").setValue(firebaseUser.getUid());
//
//
//    }
//
//    private void readChats() {
//        try {
//            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//            reference.child("Chats").addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    list.clear();
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        Chats chats = snapshot.getValue(Chats.class);
//                        if (chats != null && chats.getSender().equals(firebaseUser.getUid()) && chats.getReceiver().equals(receiverID)
//                            || chats.getReceiver().equals(firebaseUser.getUid()) && chats.getSender().equals(receiverID)
//                        ) {
//                            list.add(chats);
//                            Log.e(TAG, "onDataChange : Username :"+chats.getTextMessage());
//                        }
//                    }
//                    if (chatsAdapter != null) {
//                        chatsAdapter.notifyDataSetChanged();
//                    } else {
//                        chatsAdapter = new ChatsAdapter(list, ChatsActivity.this);
//                        binding.recyclerView.setAdapter(chatsAdapter);
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    Toast.makeText(ChatsActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
//                }
//            });
//        }catch (Exception e) {
//            Toast.makeText(ChatsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
//        }
//    }
}