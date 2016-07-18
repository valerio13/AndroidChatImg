package edu.galileo.android.androidchat.addMultimedia.ui;

/**
 * Created by avalo.
 */
public interface AddMultimediaView {
    void showInput();
    void hideInput();
    void showProgress();
    void hideProgress();

    void mutimediaAdded();
    void multimediaNotAdded();
}
