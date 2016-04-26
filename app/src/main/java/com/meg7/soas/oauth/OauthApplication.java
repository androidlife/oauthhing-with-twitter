package com.meg7.soas.oauth;

import android.app.Application;
import android.content.Context;

import timber.log.Timber;

/**
 * Created by laaptu on 4/8/16.
 */
public class OauthApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        context = this;
    }

    private static Context context;

    public static Context getContext() {
        return context;
    }
}
