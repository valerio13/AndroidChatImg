package edu.galileo.android.androidchat.chat.ui;

import edu.galileo.android.androidchat.entities.ChatMessage;

public interface ChatView {
    void onMessageReceived(ChatMessage msg);

    //Nuevo
    void onUploadInit();

    void onUploadComplete();

    void onUploadError(String error);

}
