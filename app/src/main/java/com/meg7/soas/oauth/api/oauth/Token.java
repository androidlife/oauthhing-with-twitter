package com.meg7.soas.oauth.api.oauth;

/**
 */
public class Token {

    public String token, tokenSecret, rawResponse;
    public String userId, screenName;

    public Token(String token, String tokenSecret) {
        this(token, tokenSecret, null);
    }

    public Token(String token, String tokenSecret, String rawResponse) {
        this.token = token;
        this.tokenSecret = tokenSecret;
        this.rawResponse = rawResponse;
    }

    public void setUserInfo(String userId, String screenName) {
        this.userId = userId;
        this.screenName = screenName;
    }
}
