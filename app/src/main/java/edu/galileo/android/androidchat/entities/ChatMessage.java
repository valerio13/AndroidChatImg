package edu.galileo.android.androidchat.entities;

import android.media.Image;
import android.widget.ImageView;

import com.google.firebase.database.Exclude;

/**
 * Created by avalo.
 */
public class ChatMessage {
    private String msg;
    private String sender;
    private String img;
    @Exclude
    private boolean sentByMe;

    public ChatMessage() {
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
        checkMessage();

    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public boolean isSentByMe() {
        return sentByMe;
    }

    public void setSentByMe(boolean sentByMe) {
        this.sentByMe = sentByMe;
    }

    @Override
    public boolean equals(Object object) {
        boolean equal = false;

        if(object instanceof  ChatMessage){
            ChatMessage msg = (ChatMessage) object;
            equal = this.sender.equals(msg.getSender()) && this.msg.equals(msg.getMsg()) && this.sentByMe == msg.isSentByMe();
        }

        return equal;
    }

    //Nuevo
    public void setImg(String img) {
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    private void checkMessage()
    {
        String s = msg.substring(0,4);
        if(s.contentEquals("img-")){
            String sUrl = msg.substring(4, msg.length());
            img = sUrl;
//            msg = "";
        }
    }

}
