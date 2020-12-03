package com.chad.whatsappclone.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.chad.whatsappclone.BuildConfig;
import com.chad.whatsappclone.Constants.Constants;
import com.chad.whatsappclone.R;
import com.chad.whatsappclone.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;

    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;

    private BottomSheetDialog bottomSheetDialogPickImage;
    private BottomSheetDialog bottomSheetDialogEditName;
    private BottomSheetDialog bottomSheetDialogEditAbout;
    private ProgressDialog progressDialog;
    private AlertDialog dialogSignOut;

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        initialize();
        bottomSheetPickAboutUsername();
    }

    private void initialize() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        progressDialog = new ProgressDialog(this);

        if (firebaseUser != null) {
            getInfo();
        }

        binding.imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.imageProfile.invalidate();
                Drawable drawable = binding.imageProfile.getDrawable();
                Constants.IMAGE_BITMAP = ((BitmapDrawable) drawable.getCurrent()).getBitmap();
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(ProfileActivity.this, binding.imageProfile, "image");
                Intent intent = new Intent(ProfileActivity.this, ProfileImageViewActivity.class);
                startActivity(intent, activityOptionsCompat.toBundle());
            }
        });

    }

    private void bottomSheetPickAboutUsername() {

        binding.layoutName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialogEditName = new BottomSheetDialog(
                        ProfileActivity.this, R.style.BottomSheetDialogTheme
                );
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottomsheet_editname, (LinearLayout)findViewById(R.id.bottomSheetContainer));

                final EditText changedUsername = view.findViewById(R.id.edittext_username);

                view.findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(TextUtils.isEmpty(changedUsername.getText().toString())) {
                            Toast.makeText(ProfileActivity.this, "Name can't be Empty!", Toast.LENGTH_SHORT).show();
                        }else {
                            updateName(changedUsername.getText().toString());
                            bottomSheetDialogEditName.dismiss();
                        }
                    }
                });
                view.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialogEditName.dismiss();
                    }
                });
                bottomSheetDialogEditName.setContentView(view);
                bottomSheetDialogEditName.show();
            }
        });

        binding.fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialogPickImage = new BottomSheetDialog(
                        ProfileActivity.this, R.style.BottomSheetDialogTheme
                );
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottomsheet_pickimage, (LinearLayout)findViewById(R.id.bottomSheetContainer));
                view.findViewById(R.id.layout_gallery).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       openGallery();
                        bottomSheetDialogPickImage.dismiss();
                    }
                });
                view.findViewById(R.id.layout_camera).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkCameraPermission();
                        bottomSheetDialogPickImage.dismiss();
                    }
                });
                bottomSheetDialogPickImage.setContentView(view);
                bottomSheetDialogPickImage.show();
            }
        });

        binding.signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogSignOut();
            }
        });

        binding.layoutAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialogEditAbout = new BottomSheetDialog(
                        ProfileActivity.this, R.style.BottomSheetDialogTheme
                );
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottomsheet_about, (LinearLayout)findViewById(R.id.bottomSheetContainer));

                final EditText changedAbout = view.findViewById(R.id.edittext_about);

                view.findViewById(R.id.about_save_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(TextUtils.isEmpty(changedAbout.getText().toString())) {
                            Toast.makeText(ProfileActivity.this, "About can't be Empty!", Toast.LENGTH_SHORT).show();
                        }else {
                            updateAbout(changedAbout.getText().toString());
                            bottomSheetDialogEditAbout.dismiss();
                        }
                        bottomSheetDialogEditAbout.dismiss();
                    }
                });
                view.findViewById(R.id.about_cancel_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialogEditAbout.dismiss();
                    }
                });
                bottomSheetDialogEditAbout.setContentView(view);
                bottomSheetDialogEditAbout.show();
            }
        });

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

    private void updateAbout(String updateAbout) {

        firebaseFirestore.collection("Users").document(firebaseUser.getUid()).update("about", updateAbout).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ProfileActivity.this, "Updated Succefully!", Toast.LENGTH_SHORT).show();
                getInfo();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showDialogSignOut() {

            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_signout,(ViewGroup) findViewById(R.id.layoutSignOutContainer)
            );
            builder.setView(view);

        dialogSignOut = builder.create();
            if(dialogSignOut.getWindow() != null) {
                dialogSignOut.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            view.findViewById(R.id.textSignOut).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(ProfileActivity.this, SplashActivity.class));

                }
            });

            view.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogSignOut.dismiss();
                }
            });

        dialogSignOut.show();

    }

    private void updateName(String changedName) {

        firebaseFirestore.collection("Users").document(firebaseUser.getUid()).update("userName", changedName).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ProfileActivity.this, "Updated Succefully!", Toast.LENGTH_SHORT).show();
                getInfo();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select image"), Constants.REQUEST_CODE_GALLERY);
    }

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

    private void getInfo() {

        firebaseFirestore.collection("Users").document(firebaseUser.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        String userName = String.valueOf(documentSnapshot.get("userName"));
                        String phoneNumber = String.valueOf(documentSnapshot.get("userPhone"));
                        String imageProfile = String.valueOf(documentSnapshot.get("imageProfile"));
                        String about = String.valueOf(documentSnapshot.get("about"));


                        binding.userNameText.setText(userName);
                        binding.phoneNumberText.setText(phoneNumber);
                        binding.aboutText.setText(about);
                        Glide.with(ProfileActivity.this).load(imageProfile).into(binding.imageProfile);


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this, "Failed to retrieve data!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Constants.REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            uploadToFirebase();
        }

        if(requestCode == Constants.REQUEST_CODE_CAMERA) {
            uploadToFirebase();
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
            storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while(!urlTask.isSuccessful());
                    Uri downloadUrl = urlTask.getResult();

                    final String storageDownloadUrl = String.valueOf(downloadUrl);

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("imageProfile", storageDownloadUrl);
                    progressDialog.dismiss();
                    firebaseFirestore.collection("Users").document(firebaseUser.getUid()).update(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ProfileActivity.this, "Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                            getInfo();

                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(ProfileActivity.this, "Upload Failed!", Toast.LENGTH_SHORT).show();

                }
            });

        }

    }
}