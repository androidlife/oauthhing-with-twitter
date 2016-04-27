package com.meg7.soas.oauth;

import android.content.Context;
import android.content.SharedPreferences;

import com.meg7.soas.oauth.api.oauth.Token;

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
    private static final String USER_ID = "userId";
    private static final String SCREEN_NAME = "screenName";
    private static final String USER_PROFILE_IMAGE = "userProfileImage";
    private static final String USER_PROFILE_BG_IMAGE = "userProfileBgImage";

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

    public void storeUserInfo(String userId, String screenName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_ID, userId);
        editor.putString(SCREEN_NAME, screenName);
        editor.apply();
    }

    public Token getAccessToken() {
        if (!isUserLoggedIn())
            return null;
        return new Token(getAuthToken(), getAuthTokenSecret());

    }

    public void storeUserProfileImages(String userProfileImage, String userProfileBgImage) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_PROFILE_IMAGE, userProfileImage);
        editor.putString(USER_PROFILE_BG_IMAGE, userProfileBgImage);
        editor.apply();
    }

    public String getUserProfileImage() {
        return sharedPreferences.getString(USER_PROFILE_IMAGE, null);
    }

    public String getUserScreenName() {
        return sharedPreferences.getString(SCREEN_NAME, null);
    }

    public boolean isUserLoggedIn() {
        return getAuthToken() != null && getAuthTokenSecret() != null;
    }

    private String getAuthToken() {
        return sharedPreferences.getString(AUTH_TOKEN, null);
    }

    private String getAuthTokenSecret() {
        return sharedPreferences.getString(AUTH_TOKEN_SECRET, null);
    }

    public void removeUserCredentials() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
