package com.galwaykart;

/**
 * Created by AnkeshKumar on 6/21/2018.
 */

import android.app.Application;

import com.bugfender.sdk.Bugfender;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        Bugfender.init(this, "YHcJV0iPp8qJ01ILXoBPMVR5pQXMYrBZ", BuildConfig.DEBUG);
//        Bugfender.enableLogcatLogging();
//        Bugfender.enableUIEventLogging(this);

        //HyperTrack.initialize(this, "pk_5e3e18247df688e19426bc5a31fc8178c9d0c917");
    }
}