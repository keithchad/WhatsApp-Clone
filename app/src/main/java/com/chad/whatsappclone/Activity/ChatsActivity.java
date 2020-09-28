package com.chad.whatsappclone.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.devlomi.record_view.OnBasketAnimationEnd;
import com.devlomi.record_view.OnRecordListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ChatsActivity extends AppCompatActivity {

    ActivityChatsBinding binding;

    private String userProfile;
    private String userName;
    private String about;
    private String receiverID;
    private boolean isActionShown = false;

    private ChatsAdapter chatsAdapter;
    private ChatService chatService;

    private static int REQUEST_CODE_GALLERY = 111;
    private Uri imageUri;

    //Audio
    private MediaRecorder mediaRecorder;
    private String audio_path;
    private String sTime;
    private static final int REQUEST_CORD_PERMISSION = 332;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chats);

        initialize();
        initBtnClick();
        readChats();
    }

    private void initialize() {
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
        binding.recordButton.setRecordView(binding.recordView);

        binding.recordView.setOnRecordListener(new OnRecordListener() {
            @Override
            public void onStart() {
                //Start Recording..
                if (!checkPermissionFromDevice()) {

                    binding.emojiButton.setVisibility(View.INVISIBLE);
                    binding.fileAttachment.setVisibility(View.INVISIBLE);
                    binding.cameraButton.setVisibility(View.INVISIBLE);
                    binding.messageEdittext.setVisibility(View.INVISIBLE);
                    startRecord();
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (vibrator != null) {
                        vibrator.vibrate(100);
                    }

                } else {
                    requestPermission();
                }


            }
            @Override
            public void onCancel() {

            }

            @Override
            public void onFinish(long recordTime) {
                binding.emojiButton.setVisibility(View.VISIBLE);
                binding.fileAttachment.setVisibility(View.VISIBLE);
                binding.cameraButton.setVisibility(View.VISIBLE);
                binding.messageEdittext.setVisibility(View.VISIBLE);

            }

            @Override
            public void onLessThanSecond() {
                binding.emojiButton.setVisibility(View.VISIBLE);
                binding.fileAttachment.setVisibility(View.VISIBLE);
                binding.cameraButton.setVisibility(View.VISIBLE);
                binding.messageEdittext.setVisibility(View.VISIBLE);
            }
        });

        binding.recordView.setOnBasketAnimationEndListener(new OnBasketAnimationEnd() {
            @Override
            public void onAnimationEnd() {
                binding.emojiButton.setVisibility(View.VISIBLE);
                binding.fileAttachment.setVisibility(View.VISIBLE);
                binding.cameraButton.setVisibility(View.VISIBLE);
                binding.messageEdittext.setVisibility(View.VISIBLE);
            }
        });

        binding.messageEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(binding.messageEdittext.getText().toString())) {
                    binding.sendButton.setVisibility(View.INVISIBLE);
                    binding.recordButton.setVisibility(View.VISIBLE);
                }else {
                   binding.sendButton.setVisibility(View.VISIBLE);
                   binding.recordButton.setVisibility(View.INVISIBLE);
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

    @SuppressLint("DefaultLocale")
    private String getHumanTimeText(long milliseconds) {
        return String.format("%02d",
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
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
                 progressDialog.show();

                 binding.layoutActions.setVisibility(View.GONE);
                 isActionShown = false;
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

    private void startRecord(){
        setUpMediaRecorder();

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(ChatsActivity.this, "Recording Error , Please restart your app ", Toast.LENGTH_LONG).show();
        }

    }

    private void stopRecord(){
        try {
            if (mediaRecorder != null) {
                mediaRecorder.stop();
                mediaRecorder.reset();
                mediaRecorder.release();
                mediaRecorder = null;

                //sendVoice();
                //chatService.sendVoice(audio_path);

            } else {
                Toast.makeText(getApplicationContext(), "Null", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Stop Recording Error :" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void setUpMediaRecorder() {
        String path_save = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + UUID.randomUUID().toString() + "audio_record.m4a";
        audio_path = path_save;

        mediaRecorder = new MediaRecorder();
        try {
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.setOutputFile(path_save);
        } catch (Exception e) {
            Log.d("Chats Activity", "setUpMediaRecord: " + e.getMessage());
        }


    }

    private boolean checkPermissionFromDevice() {
        int write_external_strorage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return write_external_strorage_result == PackageManager.PERMISSION_DENIED || record_audio_result == PackageManager.PERMISSION_DENIED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        }, REQUEST_CORD_PERMISSION);
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