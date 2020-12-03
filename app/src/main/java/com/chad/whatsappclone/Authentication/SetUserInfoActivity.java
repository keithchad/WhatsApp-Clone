package com.chad.whatsappclone.Authentication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.chad.whatsappclone.Activity.MainActivity;
import com.chad.whatsappclone.BuildConfig;
import com.chad.whatsappclone.Constants.Constants;
import com.chad.whatsappclone.R;
import com.chad.whatsappclone.databinding.ActivitySetUserInfoBinding;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class SetUserInfoActivity extends AppCompatActivity {

    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;

    private ActivitySetUserInfoBinding binding;
    private BottomSheetDialog bottomSheetDialogPickImage;
    private ProgressDialog progressDialog;

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_user_info);

        initialize();
        buttonClicked();

    }

    private void initialize() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        firebaseFirestore.collection("Users").document(firebaseUser.getUid()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        binding.edittextName.setText(Objects.requireNonNull(task.getResult()).getString("userName"));
                        Glide.with(SetUserInfoActivity.this).load(task.getResult()
                                .getString("userName")).into(binding.imageProfile);
                    } else {

                    }
                });
        progressDialog = new ProgressDialog(this);
    }

    private void buttonClicked() {
        binding.nextButton.setOnClickListener(v -> {
            if(TextUtils.isEmpty(Objects.requireNonNull(binding.edittextName.getText()).toString())) {
                Toast.makeText(SetUserInfoActivity.this, "Name Field Cannot be empty", Toast.LENGTH_SHORT).show();
            }else {
                uploadToFirebase();
            }
        });
        binding.imageProfile.setOnClickListener(v -> showBottomSheet());
    }
//    private void doUpdate() {
//        progressDialog.setMessage("Please Wait");
//        progressDialog.show();
//        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//
//
//        if(firebaseUser != null) {
//            String userID = firebaseUser.getUid();
//            String username;
//            username = Objects.requireNonNull(binding.edittextName.getText()).toString();
//            User users = new User(
//                    userID,
//                    username,
//                    firebaseUser.getPhoneNumber(),
//                    "",
//                    "",
//                    "Hey there! I am using WhatsApp");
//
//            firebaseFirestore.collection("Users").document(firebaseUser.getUid()).set(users)
//                    .addOnSuccessListener(aVoid -> {
//                        progressDialog.dismiss();
//                        Toast.makeText(SetUserInfoActivity.this, "Update Successful", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(SetUserInfoActivity.this, MainActivity.class));
//                    }).addOnFailureListener(e -> Toast.makeText(SetUserInfoActivity.this, "Update failed!", Toast.LENGTH_SHORT).show());
//        }else {
//            progressDialog.dismiss();
//            Toast.makeText(this, "You need to login First", Toast.LENGTH_SHORT).show();
//        }
//
//    }
    @SuppressLint("SimpleDateFormat")
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String timeStamp = new SimpleDateFormat("yyyyMMDD_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + ".jpg";

        try {
            File file = File.createTempFile("IMG_" + timeStamp, ".jpg", getExternalFilesDir(Environment.DIRECTORY_PICTURES));
            imageUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intent.putExtra("photoName", imageFileName);
            startActivityForResult(intent, Constants.REQUEST_CODE_CAMERA);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select image"), Constants.REQUEST_CODE_GALLERY);
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    Constants.REQUEST_CODE_CAMERA);
        } else if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constants.REQUEST_CODE_CAMERA);
        } else {
            openCamera();
        }
    }

    private void showBottomSheet() {
        bottomSheetDialogPickImage = new BottomSheetDialog(
                SetUserInfoActivity.this, R.style.BottomSheetDialogTheme
        );
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottomsheet_pickimage, (LinearLayout)findViewById(R.id.bottomSheetContainer));
        view.findViewById(R.id.layout_gallery).setOnClickListener(v13 -> {
            openGallery();
            bottomSheetDialogPickImage.dismiss();
        });
        view.findViewById(R.id.layout_camera).setOnClickListener(v14 -> {
            checkCameraPermission();
            bottomSheetDialogPickImage.dismiss();
        });
        bottomSheetDialogPickImage.setContentView(view);
        bottomSheetDialogPickImage.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Constants.REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Glide.with(SetUserInfoActivity.this).load(imageUri).into(binding.imageProfile);
            //uploadToFirebase();
        }

        if(requestCode == Constants.REQUEST_CODE_CAMERA) {
            //uploadToFirebase();
            Glide.with(SetUserInfoActivity.this).load(imageUri).into(binding.imageProfile);
        }

    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadToFirebase() {

        if(imageUri != null) {
            progressDialog.setMessage("Please Wait");
            progressDialog.show();
            StorageReference storageReference = firebaseStorage.getReference().child("Profile Images/" + System.currentTimeMillis()+"."+getFileExtension(imageUri));
            storageReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!urlTask.isSuccessful());
                Uri downloadUrl = urlTask.getResult();

                final String storageDownloadUrl = String.valueOf(downloadUrl);

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("imageProfile", storageDownloadUrl);
                hashMap.put("userName", Objects.requireNonNull(binding.edittextName.getText()).toString());


                progressDialog.dismiss();
                firebaseFirestore.collection("Users").document(firebaseUser.getUid()).update(hashMap)
                        .addOnSuccessListener(aVoid -> {
                            progressDialog.dismiss();
                            Toast.makeText(SetUserInfoActivity.this, "Update Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SetUserInfoActivity.this, MainActivity.class));
                            finish();
                        });

            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                Toast.makeText(SetUserInfoActivity.this, "Upload Failed!", Toast.LENGTH_SHORT).show();
            });

        }

    }

}