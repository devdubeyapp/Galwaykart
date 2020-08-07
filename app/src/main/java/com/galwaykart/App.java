package com.galwaykart;

/**
 * Created by AnkeshKumar on 6/21/2018.
 */

import android.app.Application;

//import androidx.security.crypto.EncryptedSharedPreferences;

import com.freshchat.consumer.sdk.Freshchat;
import com.galwaykart.essentialClass.CustomImageLoaderFreshChat;
import com.google.firebase.analytics.FirebaseAnalytics;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class App extends Application {
    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    public void onCreate() {
        super.onCreate();





            Freshchat.setImageLoader(new CustomImageLoaderFreshChat());
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

            Realm.init(this);
            RealmConfiguration config = new RealmConfiguration.Builder()
                    .name("gkart.realm")
                    .schemaVersion(3)
                    .deleteRealmIfMigrationNeeded()
                    .build();
            Realm.setDefaultConfiguration(config);



    }
}