package edu.galileo.android.androidchat.chat;


public interface ChatInteractor {
    void sendMessage(String msg);
    void setRecipient(String recipient);

    void subscribe();
    void unsubscribe();
    void destroyListener();

    //Nuevo
    void execute(String path);
}
