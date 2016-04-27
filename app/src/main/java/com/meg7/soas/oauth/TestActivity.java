package com.meg7.soas.oauth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.meg7.soas.oauth.api.ApiEndPoints;
import com.meg7.soas.oauth.api.DataCallback;
import com.meg7.soas.oauth.api.DataManager;
import com.meg7.soas.oauth.api.networklibs.RetrofitManager;
import com.meg7.soas.oauth.api.oauth.OAuthHelper;
import com.meg7.soas.oauth.api.oauth.Token;
import com.meg7.soas.oauth.model.Tweet;
import com.meg7.soas.oauth.model.UserInfo;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
//        requestTokenGeneration();
//        stringParseTest();
//        postStatusTest();
        //hashTest("Hello");
//        getUserInfoTest();
        getUserHomeTimelineTest();
    }


    private void hashTest(String value) {
        Timber.d("MD5 hash of %s = %s", value, OAuthHelper.generateMD5Hash(value));
        Timber.d("SHA1 hash of  %s = %s", value, OAuthHelper.generateSHA1Hash(value));
        Timber.d("SHA256 hash of  %s = %s", value, OAuthHelper.generateSHA256Hash(value));
    }

    String accToken = "34595673-GFl1Rtut7Ogi1mLRClxM3teBWHnytyauAzp3oMjva";
    String accTokenSecret = "5n9lrFtNKmPqpxwpzIX5x3Q541JrF0PLICqW5mPAbqPbC";
    String userId = "34595673";
    String screenName = "laaptu9";

    private void postStatusTest() {
        Token accessToken = new Token(accToken, accTokenSecret);
        String status = "Hello";
        String authorizationHeader =
                OAuthHelper.generateStatusPostHeaderString(
                        accessToken, ApiEndPoints.TWITTER_CONSUMER_KEY, ApiEndPoints.TWITTER_CONSUMER_SECRET,
                        status
                );
        Timber.d("Authorization Header = %s", authorizationHeader);
        RetrofitManager.getApiService().postTweet(authorizationHeader, status, true).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response != null && response.body() != null) {
                    try {
                        String responseString = response.body().string();
                        Timber.d("Response String =%s", responseString);
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Timber.e("Error making post");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Timber.e("Error making post");

            }
        });

    }

    private void getUserHomeTimelineTest() {
        String header = OAuthHelper.generateUserTimelineHeaderString(new Token(accToken, accTokenSecret),
                ApiEndPoints.TWITTER_CONSUMER_KEY, ApiEndPoints.TWITTER_CONSUMER_SECRET, screenName, 10);
        Timber.d("Authorization header = %s", header);
        RetrofitManager.getApiService().getUserTimelines(header, screenName, 10).enqueue(new Callback<ArrayList<Tweet>>() {
            @Override
            public void onResponse(Call<ArrayList<Tweet>> call, Response<ArrayList<Tweet>> response) {
                if (response != null && response.body() != null) {
                    try {
                        ArrayList<Tweet> tweets = response.body();
                        if (tweets != null && tweets.size() > 0) {
                            for (Tweet tweet : tweets)
                                Timber.d("created at =%s", tweet.createdAt);
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Timber.d("Unable to fetch the user timeline");

                }
            }

            @Override
            public void onFailure(Call<ArrayList<Tweet>> call, Throwable t) {
                Timber.d("Unable to fetch the user timeline");
            }
        });
    }

    private void getUserInfoTest() {
        String header = OAuthHelper.generateUserInfoHeaderString(new Token(accToken, accTokenSecret), ApiEndPoints.TWITTER_CONSUMER_KEY, ApiEndPoints.TWITTER_CONSUMER_SECRET, userId, screenName);
        Timber.d("Authorization Header = %s", header);
//        RetrofitManager.getApiService().getUserInfo(header, screenName).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response != null && response.body() != null) {
//                    try {
//                        String responseString = response.body().string();
//                        Timber.d("Response String = %s", responseString);
//                        return;
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                Timber.e("Error getting user info");
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Timber.e("Error getting User Info");
//            }
//        });

        RetrofitManager.getApiService().getUserInfos(header, screenName).enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (response != null && response.body() != null) {
                    try {
                        UserInfo userInfo = response.body();
                        Timber.d("Response String = %s", userInfo.fullName);
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Timber.e("Error getting user info");
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                Timber.e("Error getting User Info");
            }
        });

    }

    private void stringParseTest() {
        String requestResponse = "oauth_token=EgPCLwAAAAAAucbNAAABU_U09ks&oauth_token_secret=FAOMbVS18VzQhlmnen4gEnMjUiIOhBf7&oauth_callback_confirmed=true";
        Timber.d("Request Token = %s", OAuthHelper.extract(requestResponse, OAuthHelper.TOKEN_REGEX));
        Timber.d("Request Secret = %s", OAuthHelper.extract(requestResponse, OAuthHelper.SECRET_REGEX));
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


    Token requestToken;
    String oauthVerifier;

    private void beginUserAuthentication(Token token) {
        requestToken = token;
        String authenticationUrl = OAuthHelper.generateAuthorizationUrl(token.token);
        Intent oauthLoginIntent = OAuthLoginActivity.launchActivity(this, authenticationUrl, ApiEndPoints.CALLBACK_URL);
        startActivityForResult(oauthLoginIntent, 10);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == Activity.RESULT_OK && data != null) {
            String authorizationToken = data.getStringExtra(OAuthLoginActivity.AUTHORIZE_TOKEN);
            String authorizationVerifier = data.getStringExtra(OAuthLoginActivity.AUTHORIZE_VERIFIER);
            requestAccessToken(authorizationVerifier);
        }
    }

    private void requestAccessToken(String oauthVerifier) {
        this.oauthVerifier = oauthVerifier;
        String authorizationHeader = OAuthHelper.generateAccessToken(requestToken, oauthVerifier, ApiEndPoints.TWITTER_CONSUMER_KEY, ApiEndPoints.TWITTER_CONSUMER_SECRET);
        Timber.d("Authorization Header for access token = %s", authorizationHeader);
        DataManager.getInstance().getAccessToken(new DataCallback<Token>() {
            @Override
            public void onResponse(Token response) {

            }

            @Override
            public void onFailure(String error) {

            }

            @Override
            public void cancel() {

            }
        }, authorizationHeader);
    }
}
