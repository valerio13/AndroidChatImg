package edu.galileo.android.androidchat.chat;


import com.cloudinary.Cloudinary;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.io.File;

import javax.inject.Singleton;

import edu.galileo.android.androidchat.chat.events.ChatEvent;
import edu.galileo.android.androidchat.domain.FirebaseHelper;
import edu.galileo.android.androidchat.entities.ChatMessage;
import edu.galileo.android.androidchat.lib.CloudinaryImageStorage;
import edu.galileo.android.androidchat.lib.EventBus;
import edu.galileo.android.androidchat.lib.GreenRobotEventBus;
import edu.galileo.android.androidchat.entities.Photo;
import edu.galileo.android.androidchat.lib.ImageStorageFinishedListener;
import edu.galileo.android.androidchat.lib.ImageStorage;


public class ChatRepositoryImpl implements ChatRepository {
    private String recipient;
    private EventBus eventBus;
    private FirebaseHelper helper;
    private ChildEventListener chatEventListener;
    private ImageStorage cloudinaryImageStorage;

    public ChatRepositoryImpl() {
        this.eventBus = GreenRobotEventBus.getInstance();
        this.helper = FirebaseHelper.getInstance();
    }

    @Override
    public void sendMessage(String msg) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(helper.getAuthUserEmail());
        chatMessage.setMsg(msg);

        DatabaseReference chatsReference = helper.getChatsReference(recipient);
        chatsReference.push().setValue(chatMessage);
    }

    @Override
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    @Override
    public void subscribe() {
        if(chatEventListener == null){
            chatEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                    String msgSender = chatMessage.getSender();

                    chatMessage.setSentByMe(msgSender.equals(helper.getAuthUserEmail()));

                    ChatEvent chatEvent = new ChatEvent();
                    chatEvent.setMessage(chatMessage);
                    eventBus.post(chatEvent);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
        }

        helper.getChatsReference(recipient).addChildEventListener(chatEventListener);
    }

    @Override
    public void unsubscribe() {
        if(chatEventListener != null){
            helper.getChatsReference(recipient).removeEventListener(chatEventListener);
        }
    }

    @Override
    public void destroyListener() {
        chatEventListener = null;
    }

    @Override
    public void changeConnectionStatus(boolean online) {
        helper.changeUserConnectionStatus(online);
    }


    //Nuevo
    @Override
    public void uploadPhoto(String path) {
        //Nuevo
        DatabaseReference chatsReference = helper.getChatsReference(recipient);
        final String newPhotoId = chatsReference.push().getKey();        //final String newPhotoId = firebaseAPI.create();

        cloudinaryImageStorage = new CloudinaryImageStorage();
        //Fin de nuevo


        final Photo photo = new Photo();
        photo.setId(newPhotoId);
        photo.setEmail(helper.getAuthUserEmail());
//        if(location != null){
//            photo.setLatitude(location.getLatitude());
//            photo.setLongitude(location.getLongitude());
//        }
        post(ChatEvent.UPLOAD_INIT);
        ImageStorageFinishedListener listener = new ImageStorageFinishedListener() {
            @Override
            public void onSuccess() {
                String url = cloudinaryImageStorage.getImageUrl(newPhotoId);
                photo.setUrl(url);
//                helper.update(photo);
                //Nuevo
                sendMessage("img-" + url);
                //fin de nuevo

                post(ChatEvent.UPLOAD_COMPLETE);
            }

            @Override
            public void onError(String error) {
                post(ChatEvent.UPLOAD_ERROR, error);
            }
        };

        cloudinaryImageStorage.upload(new File(path), newPhotoId, listener);
    }

    private void post(int type){
        post(type, null);
    }

    private void post(int type, String error){
        ChatEvent event = new ChatEvent();
        event.setType(type);
        event.setError(error);
        eventBus.post(event);
    }
}
