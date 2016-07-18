package edu.galileo.android.androidchat.addMultimedia.events;

/**
 * Created by avalo.
 */
public class AddMultimediaEvent {
    boolean error = false;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
