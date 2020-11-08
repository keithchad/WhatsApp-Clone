package com.chad.whatsappclone.Interface;

import com.chad.whatsappclone.Model.Chats;

import java.util.List;

public interface OnReadChatCallBack {

    void onReadSuccess(List<Chats> list);
    void onReadFailed();

}
