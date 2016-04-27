package com.meg7.soas.oauth.api.networklibs;

import com.meg7.soas.oauth.api.ApiEndPoints;
import com.meg7.soas.oauth.model.UserInfo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

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


    @GET(ApiEndPoints.TWITTER_GET_USER_INFO)
    Call<ResponseBody> getUserInfo(
            @Header("Authorization") String authorization,
            @Query("screen_name") String screenName
    );


    @GET(ApiEndPoints.TWITTER_GET_USER_INFO)
    Call<UserInfo> getUserInfos(
            @Header("Authorization") String authorization,
            @Query("screen_name") String screenName
    );

    @GET(ApiEndPoints.TWITTER_GET_USER_TIMELINE)
    Call<ResponseBody> getUserTimeline(
            @Header("Authorization") String authorization,
            @Query("screen_name") String screenName,
            @Query("count") int totalTweets
    );

}
