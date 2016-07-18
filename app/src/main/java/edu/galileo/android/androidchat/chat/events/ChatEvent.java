package edu.galileo.android.androidchat.chat.events;

import edu.galileo.android.androidchat.entities.ChatMessage;


public class ChatEvent {
    private ChatMessage message;

    //Nuevo
    private int type;
    private String error;
    public static final int UPLOAD_INIT = 10;
    public static final int UPLOAD_COMPLETE = 11;
    public static final int UPLOAD_ERROR = 12;


    public ChatMessage getMessage() {
        return message;
    }

    public void setMessage(ChatMessage message) {
        this.message = message;
    }


    //Nuevo
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
