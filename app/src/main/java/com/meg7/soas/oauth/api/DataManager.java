package com.meg7.soas.oauth.api;

import com.meg7.soas.oauth.api.networklibs.RetrofitManager;
import com.meg7.soas.oauth.api.oauth.OAuthHelper;
import com.meg7.soas.oauth.api.oauth.Token;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * A singleton class ,that directly calls the necessary classes for
 * data fetch. All the app communicates with this through {@link DataCallback}.
 * So the app doesn't need to know what it implements. Right now {@link retrofit2.Retrofit}
 * is implemented, later this class can be modified to handle any other API libraries
 */
public class DataManager {
    static volatile DataManager dataManager;

    private DataManager() {

    }

    public static DataManager getInstance() {
        if (dataManager == null) {
            synchronized (DataManager.class) {
                if (dataManager == null)
                    dataManager = new DataManager();
            }
        }
        return dataManager;
    }

    public void getRequestToken(final DataCallback<Token> dataCallback, String authorizationHeader) {
        RetrofitManager.getApiService().getRequestToken(authorizationHeader).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response != null && response.body() != null) {
                    try {
                        String requestResponse = response.body().string();
                        Timber.d("Request token Response = %s", requestResponse);
                        String requestToken = OAuthHelper.extract(requestResponse, OAuthHelper.TOKEN_REGEX);
                        String requestTokenSecret = OAuthHelper.extract(requestResponse, OAuthHelper.SECRET_REGEX);
                        Timber.d("Request Token = %s ", requestToken);
                        Timber.d("Request token secret = %s ", requestTokenSecret);
                        dataCallback.onResponse(new Token(requestToken, requestTokenSecret));
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                Timber.e("There was error fetching request token");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Timber.e("There was error fetching request token");
            }
        });
    }


    public void getRequestToken(final DataCallbackMain<Token> dataCallback) {
        String authorizationHeader = OAuthHelper.generateRequestTokenHeader(ApiEndPoints.TWITTER_CONSUMER_KEY,
                ApiEndPoints.TWITTER_CONSUMER_SECRET, ApiEndPoints.CALLBACK_URL);
        RetrofitManager.getApiService().getRequestToken(authorizationHeader).enqueue(new Callback<ResponseBody>() {

            private void onResult(Token token) {
                if (token == null) {
                    Timber.e("There was error fetching request token");
                    dataCallback.onFailure("There was error fetching request token");
                } else {
                    dataCallback.onResponse(token);
                }
            }

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response != null && response.body() != null) {
                    try {
                        String requestResponse = response.body().string();
                        Timber.d("Request token Response = %s", requestResponse);
                        String requestToken = OAuthHelper.extract(requestResponse, OAuthHelper.TOKEN_REGEX);
                        String requestTokenSecret = OAuthHelper.extract(requestResponse, OAuthHelper.SECRET_REGEX);
                        Timber.d("Request Token = %s ", requestToken);
                        Timber.d("Request token secret = %s ", requestTokenSecret);
                        onResult(new Token(requestToken, requestTokenSecret));
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    onResult(null);

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                onResult(null);
            }
        });
    }


    public void getAccessToken(final DataCallback<Token> dataCallback, String authorizationHeader) {
        RetrofitManager.getApiService().getAccessToken(authorizationHeader).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response != null && response.body() != null) {
                    try {
                        String requestResponse = response.body().string();
                        Timber.d("Access token Response = %s", requestResponse);
                        String requestToken = OAuthHelper.extract(requestResponse, OAuthHelper.TOKEN_REGEX);
                        String requestTokenSecret = OAuthHelper.extract(requestResponse, OAuthHelper.SECRET_REGEX);
                        Timber.d("Access Token = %s ", requestToken);
                        Timber.d("Access token secret = %s ", requestTokenSecret);
                        dataCallback.onResponse(new Token(requestToken, requestTokenSecret, requestResponse));
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                Timber.e("There was error fetching access token");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Timber.e("There was error fetching access token");
            }
        });

    }


    public void getAccessToken(final DataCallbackMain<Token> dataCallback, Token requestToken, String oauthVerifier) {
        String authorizationHeader = OAuthHelper.generateAccessToken(requestToken, oauthVerifier, ApiEndPoints.TWITTER_CONSUMER_KEY, ApiEndPoints.TWITTER_CONSUMER_SECRET);
        RetrofitManager.getApiService().getAccessToken(authorizationHeader).enqueue(new Callback<ResponseBody>() {

            public void onResult(Token token) {
                if (token == null) {
                    Timber.e("There was error fetching access token");
                    dataCallback.onFailure("There was error fetching access token");
                } else {
                    dataCallback.onResponse(token);
                }
            }

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response != null && response.body() != null) {
                    try {
                        String requestAccessTokenResponse = response.body().string();
                        Timber.d("Access token Response = %s", requestAccessTokenResponse);
                        String accessToken = OAuthHelper.extract(requestAccessTokenResponse, OAuthHelper.TOKEN_REGEX);
                        String accessTokenSecret = OAuthHelper.extract(requestAccessTokenResponse, OAuthHelper.SECRET_REGEX);
                        String userId = OAuthHelper.extract(requestAccessTokenResponse, OAuthHelper.USER_ID_REGEX);
                        String screenName = OAuthHelper.extract(requestAccessTokenResponse, OAuthHelper.SCREEN_NAME_REGEX);
                        Timber.d("Access Token = %s ", accessToken);
                        Timber.d("Access token secret = %s ", accessTokenSecret);
                        Timber.d("UserId = %s", userId);
                        Timber.d("ScreenName = %s", screenName);
                        Token token = new Token(accessToken, accessTokenSecret, requestAccessTokenResponse);
                        token.setUserInfo(userId, screenName);
                        onResult(token);
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                onResult(null);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                onResult(null);
            }
        });

    }
}
