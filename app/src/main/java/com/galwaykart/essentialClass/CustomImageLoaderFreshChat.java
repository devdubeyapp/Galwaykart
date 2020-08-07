package com.galwaykart.essentialClass;

import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.freshchat.consumer.sdk.FreshchatImageLoader;
import com.freshchat.consumer.sdk.FreshchatImageLoaderRequest;

import org.jetbrains.annotations.Nullable;

public class CustomImageLoaderFreshChat  implements FreshchatImageLoader {

    @Override
    public void load(@NonNull FreshchatImageLoaderRequest request, @NonNull ImageView imageView) {
        // your code to download image and set to imageView
    }

    @Nullable
    @Override
    public Bitmap get(@NonNull FreshchatImageLoaderRequest request) {
        // code to download and return bitmap image
        return null;
    }

    @Override
    public void fetch(@NonNull FreshchatImageLoaderRequest request) {
        // code to download image
    }
}