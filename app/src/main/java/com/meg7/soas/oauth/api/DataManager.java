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
}
