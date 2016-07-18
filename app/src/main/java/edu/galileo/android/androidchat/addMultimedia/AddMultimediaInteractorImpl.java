package edu.galileo.android.androidchat.addMultimedia;

/**
 * Created by avalo.
 */
public class AddMultimediaInteractorImpl implements AddMultimediaInteractor {
    AddMultimediaRepository repository;

    public AddMultimediaInteractorImpl() {
        this.repository = new AddMultimediaRepositoryImpl();
    }

    @Override
    public void execute(String email) {
        repository.addContact(email);
    }
}
