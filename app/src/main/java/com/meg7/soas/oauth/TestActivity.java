package com.meg7.soas.oauth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.meg7.soas.oauth.api.ApiEndPoints;
import com.meg7.soas.oauth.api.DataCallback;
import com.meg7.soas.oauth.api.DataManager;
import com.meg7.soas.oauth.api.oauth.OAuthHelper;
import com.meg7.soas.oauth.api.oauth.Token;

import timber.log.Timber;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        requestTokenGeneration();
//        stringParseTest();
    }

    private void stringParseTest(){
        String requestResponse ="oauth_token=EgPCLwAAAAAAucbNAAABU_U09ks&oauth_token_secret=FAOMbVS18VzQhlmnen4gEnMjUiIOhBf7&oauth_callback_confirmed=true";
        Timber.d("Request Token = %s",OAuthHelper.extract(requestResponse,OAuthHelper.TOKEN_REGEX));
        Timber.d("Request Secret = %s",OAuthHelper.extract(requestResponse,OAuthHelper.SECRET_REGEX));
    }

    private void requestTokenGeneration() {
        String authorizationHeader = OAuthHelper.generateRequestTokenHeader(ApiEndPoints.TWITTER_CONSUMER_KEY,
                ApiEndPoints.TWITTER_CONSUMER_SECRET, ApiEndPoints.CALLBACK_URL);
        Timber.d("AuthorizationHeader =%s", authorizationHeader);
        DataManager.getInstance().getRequestToken(new DataCallback<Token>() {
            @Override
            public void onResponse(Token token) {
                beginUserAuthentication(token);
            }

            @Override
            public void onFailure(String error) {

            }

            @Override
            public void cancel() {

            }
        }, authorizationHeader);
    }


    private void beginUserAuthentication(Token token) {
        String authenticationUrl = OAuthHelper.generateAuthorizationUrl(token.token);
        Intent oauthLoginIntent = OAuthLoginActivity.launchActivity(this, authenticationUrl, ApiEndPoints.CALLBACK_URL);
        startActivityForResult(oauthLoginIntent,10);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == Activity.RESULT_OK && data != null) {
            String authorizationToken = data.getStringExtra(OAuthLoginActivity.AUTHORIZE_TOKEN);
            String authorizationVerifier = data.getStringExtra(OAuthLoginActivity.AUTHORIZE_VERIFIER);
        }
    }
}
