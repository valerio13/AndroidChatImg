package edu.galileo.android.androidchat.addMultimedia;

import edu.galileo.android.androidchat.addMultimedia.events.AddMultimediaEvent;

/**
 * Created by avalo.
 */
public interface AddMultimediaPresenter {
    void onShow();
    void onDestroy();

    void addContact(String email);
    void onEventMainThread(AddMultimediaEvent event);
}
