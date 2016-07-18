package edu.galileo.android.androidchat.chat;


public interface ChatSessionInteractor {
    void changeConnectionStatus(boolean online);

    //Nuevo
    void execute(String path);
}
