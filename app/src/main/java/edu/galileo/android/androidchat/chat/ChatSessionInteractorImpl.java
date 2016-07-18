package edu.galileo.android.androidchat.chat;


public class ChatSessionInteractorImpl implements ChatSessionInteractor {
    ChatRepository repository;

    public ChatSessionInteractorImpl() {
        this.repository = new ChatRepositoryImpl();
    }

    @Override
    public void changeConnectionStatus(boolean online) {
        repository.changeConnectionStatus(online);
    }


    //Nuevo
    @Override
    public void execute(String path) {
        repository.uploadPhoto(path);
    }
}
