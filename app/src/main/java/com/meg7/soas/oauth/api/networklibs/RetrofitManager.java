package com.meg7.soas.oauth.api.networklibs;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.meg7.soas.oauth.api.ApiEndPoints;
import com.meg7.soas.oauth.api.gson.GsonAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 */
public class RetrofitManager {
    static volatile Retrofit retrofit;

    private static ApiService apiService;

    private RetrofitManager() {

    }

    private static Retrofit getRetrofit() {
        if (retrofit == null) {
            synchronized (RetrofitManager.class) {
                if (retrofit == null) {
                    OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
                    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
                    okhttpClientBuilder.addInterceptor(loggingInterceptor);
                    okhttpClientBuilder.connectTimeout(2, TimeUnit.MINUTES);
                    okhttpClientBuilder.readTimeout(2, TimeUnit.MINUTES);
                    Gson gson = new GsonBuilder().registerTypeAdapterFactory(new GsonAdapterFactory()).create();
                    retrofit = new Retrofit.Builder()
                            .baseUrl(ApiEndPoints.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .client(okhttpClientBuilder.build())
                            .build();
                }
            }
        }

        return retrofit;
    }

    public static ApiService getApiService() {
        if (apiService == null) {
            synchronized (RetrofitManager.class) {
                if (apiService == null)
                    apiService = getRetrofit().create(ApiService.class);
            }
        }
        return apiService;
    }
}
