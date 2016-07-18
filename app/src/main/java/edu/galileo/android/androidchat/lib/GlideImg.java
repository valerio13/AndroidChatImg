package edu.galileo.android.androidchat.lib;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by Vale on 16/07/2016.
 */

public class GlideImg implements ImageLoader {
    private RequestManager glideRequestManager;

    public GlideImg(Context context) {
        this.glideRequestManager = Glide.with(context); //glideRequestManager;
    }

    @Override
    public void load(ImageView imageView, String URL) {

        glideRequestManager
                .load(URL)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .override(600, 400)
                .into(imageView);
    }
}
