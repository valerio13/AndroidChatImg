package edu.galileo.android.androidchat.lib;

import java.io.File;

/**
 * Created by avalo.
 */
public interface ImageStorage {
    String getImageUrl(String id);
    void upload(File file, String id, ImageStorageFinishedListener listener);
}
