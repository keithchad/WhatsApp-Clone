package com.chad.whatsappclone.Constants;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.chad.whatsappclone.Interface.OnReadChatCallBack;
import com.chad.whatsappclone.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jsibbold.zoomage.ZoomageView;

public class DialogReviewSendImage {

    private Context context;
    private Dialog dialog;
    private Bitmap bitmap;
    ZoomageView zoomageView;
    FloatingActionButton floatingActionButton;

    public DialogReviewSendImage(Context context, Bitmap bitmap) {
        this.context = context;
        this.bitmap = bitmap;
        this.dialog = new Dialog(context);
        initialize();
    }

    private void initialize() {

        dialog.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        dialog.setContentView(R.layout.activity_review_image_send);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(layoutParams);

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
