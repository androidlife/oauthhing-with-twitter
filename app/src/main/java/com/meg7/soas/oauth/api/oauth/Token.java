package com.meg7.soas.oauth.api.oauth;

/**
 * Created by laaptu on 4/8/16.
 */
public class Token {

    public String token, tokenSecret;

    public Token(String token, String tokenSecret) {
        this.token = token;
        this.tokenSecret = tokenSecret;
    }
}
