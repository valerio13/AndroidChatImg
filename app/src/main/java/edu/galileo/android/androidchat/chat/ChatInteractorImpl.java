package edu.galileo.android.androidchat.chat;

import android.location.Location;


public class ChatInteractorImpl implements ChatInteractor {
    ChatRepository repository;

    public ChatInteractorImpl() {
        this.repository = new ChatRepositoryImpl();
    }

    @Override
    public void sendMessage(String msg) {
        repository.sendMessage(msg);
    }

    @Override
    public void setRecipient(String recipient) {
        repository.setRecipient(recipient);
    }

    @Override
    public void subscribe() {
        repository.subscribe();
    }

    @Override
    public void unsubscribe() {
        repository.unsubscribe();
    }

    @Override
    public void destroyListener() {
        repository.destroyListener();
    }

    //Nuevo
    @Override
    public void execute(String path) {
        repository.uploadPhoto(path);
    }
}