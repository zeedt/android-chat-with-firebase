package com.example.zeed.edx_firebase_chat.util;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;

/**
 * Created by zeed on 1/20/19.
 */

public class GlideUtil {

    public static void loadImage(View view, String imageUrl, ImageView imageView) {
        RequestManager requestManager = Glide.with(view);
        RequestBuilder requestBuilder = requestManager.load(imageUrl);
        requestBuilder.into(imageView);
    }

}
