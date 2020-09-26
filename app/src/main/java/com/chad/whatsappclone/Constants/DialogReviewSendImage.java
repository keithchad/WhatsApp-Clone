package com.chad.whatsappclone.Constants;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

import com.chad.whatsappclone.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jsibbold.zoomage.ZoomageView;

import java.util.Objects;

public class DialogReviewSendImage {

    private Dialog dialog;
    private Bitmap bitmap;
    ZoomageView zoomageView;
    FloatingActionButton floatingActionButton;

    public DialogReviewSendImage(Context context, Bitmap bitmap) {
        this.bitmap = bitmap;
        this.dialog = new Dialog(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            initialize();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initialize() {

        dialog.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        dialog.setContentView(R.layout.activity_review_image_send);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.setCancelable(true);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            layoutParams.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        }
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        Objects.requireNonNull(dialog.getWindow()).setAttributes(layoutParams);

        zoomageView = dialog.findViewById(R.id.zoom_image);
        floatingActionButton = dialog.findViewById(R.id.send_button);

    }

    public void show(final OnCallBack onCallBack) {
        dialog.show();
        zoomageView.setImageBitmap(bitmap);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCallBack.onButtonSendClick();
                dialog.dismiss();
            }
        });

    }

    public interface OnCallBack {
        void onButtonSendClick();
    }


}
