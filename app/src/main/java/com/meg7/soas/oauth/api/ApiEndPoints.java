package com.meg7.soas.oauth.api;

/**
 */
public class ApiEndPoints {
    //Right now this won't be used as the urls are directly embedded to the ApiService
    public static final String BASE_URL = "https://api.twitter.com/";

    // if app doesn't have access token, first generate request_token through this
    public static final String REQUEST_TOKEN_URL = "https://api.twitter.com/oauth/request_token";
    /**
     * after request token is generated,generate authorization token ,using request token
     * You can use both AUTHENTICATE_URL and AUTHORIZE_URL. Right now we are using AUTHENTICATE_URL,
     * so that if the user has already authorized the app, the user doesn't have to authorize again i.e.
     * the user won't be presented with authorize screen next time and immediately taken back to
     * callback url
     */
    public static final String AUTHENTICATE_URL = "https://api.twitter.com/oauth/authenticate";
    public static final String AUTHORIZE_URL = "https://api.twitter.com/oauth/authorize";
    //once authorization token is generated , use it along with access token secret to generate
    // access_token and access_token_secret
    public static final String ACCESS_TOKEN_URL = "https://api.twitter.com/oauth/access_token";

    //Your apps consumer key and secret
    public static final String TWITTER_CONSUMER_KEY = "VXfSSEjW3F7LdMk37gJ4cX9Hz";
    public static final String TWITTER_CONSUMER_SECRET = "cdaLN6trKFqH9pUyeYD18OHcAOlE3NekzhuzkEMrOyLfnmlohD";

    //callback url where you are presented with
    public static final String CALLBACK_URL = "http://www.lftechnology.com";

    //API for posting data
    public static final String TWEET_POST_URL = "https://api.twitter.com/1.1/statuses/update.json";
    //API for getting user info
    public static final String TWITTER_GET_USER_INFO = "https://api.twitter.com/1.1/users/show.json";
    //API for getting user timeline
    public static final String TWITTER_GET_USER_TIMELINE = "https://api.twitter.com/1.1/statuses/user_timeline.json";

}
