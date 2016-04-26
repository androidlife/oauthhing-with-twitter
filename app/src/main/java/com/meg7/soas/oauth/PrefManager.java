package com.meg7.soas.oauth;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by laaptu on 4/25/16.
 */
public class PrefManager {
    private static PrefManager prefManager;
    private SharedPreferences sharedPreferences;

    private static final String REQUEST_TOKEN = "requestToken";
    private static final String AUTHORIZE_TOKEN = "authorizeToken";
    private static final String AUTH_TOKEN = "authToken";
    private static final String REFRESH_TOKEN = "refreshToken";
    private static final String AUTH_TOKEN_SECRET = "authTokenSecret";

    private static final String SHARED_PREF_NAME = "OAuth";

    private PrefManager() {
        sharedPreferences = OauthApplication.getContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public static PrefManager getInstance() {
        if (prefManager == null)
            prefManager = new PrefManager();
        return prefManager;
    }

    public void addAuthToken(String authToken, String authTokenSecret) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AUTH_TOKEN, authToken);
        editor.putString(AUTH_TOKEN_SECRET, authTokenSecret);
        editor.apply();
    }

    public boolean isUserLoggedIn() {
        //
        return sharedPreferences.getString(AUTH_TOKEN, null) != null;
    }
}
