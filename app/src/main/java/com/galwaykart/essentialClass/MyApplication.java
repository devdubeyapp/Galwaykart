package com.galwaykart.essentialClass;

import android.app.Application;

import com.freshchat.consumer.sdk.Freshchat;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Freshchat.setImageLoader(new CustomImageLoaderFreshChat());
    }
}
