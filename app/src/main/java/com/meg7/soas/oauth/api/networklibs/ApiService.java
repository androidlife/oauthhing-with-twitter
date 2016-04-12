package com.meg7.soas.oauth.api.networklibs;

import com.meg7.soas.oauth.api.ApiEndPoints;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 */
public interface ApiService {

    @POST(ApiEndPoints.REQUEST_TOKEN_URL)
    Call<ResponseBody> getRequestToken(
            @Header("Authorization") String authorization
    );

    @POST(ApiEndPoints.ACCESS_TOKEN_URL)
    Call<ResponseBody> getAccessToken(
            @Header("Authorization") String authorization
    );

    @FormUrlEncoded
    @POST(ApiEndPoints.TWEET_POST_URL)
    Call<ResponseBody> postTweet(
            @Header("Authorization") String authorization,
            @Field("status") String status,
            @Field("include_entities") boolean include
    );

}
