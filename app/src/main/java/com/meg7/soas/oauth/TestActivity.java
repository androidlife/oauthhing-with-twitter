package com.meg7.soas.oauth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.meg7.soas.oauth.api.ApiEndPoints;
import com.meg7.soas.oauth.api.DataCallback;
import com.meg7.soas.oauth.api.DataManager;
import com.meg7.soas.oauth.api.oauth.OAuthHelper;
import com.wordpress.laaptu.oauth.R;

import timber.log.Timber;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        requestTokenGeneration();
    }

    private void requestTokenGeneration() {
        String authorizationHeader = OAuthHelper.generateRequestTokenHeader(ApiEndPoints.TWITTER_CONSUMER_KEY,
                ApiEndPoints.TWITTER_CONSUMER_SECRET, ApiEndPoints.CALLBACK_URL);
        Timber.d("AuthorizationHeader =%s",authorizationHeader);
        DataManager.getInstance().getRequestToken(new DataCallback<String>() {
            @Override
            public void onResponse(String response) {

            }

            @Override
            public void onFailure(String error) {

            }

            @Override
            public void cancel() {

            }
        },authorizationHeader);
    }
}
