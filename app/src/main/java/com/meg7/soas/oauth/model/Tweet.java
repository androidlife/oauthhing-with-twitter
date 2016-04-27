package com.meg7.soas.oauth.model;

/**
 * Created by laaptu on 4/25/16.
 */
public class Tweet {

    public String createdAt, text, imageUrl;

    public Tweet() {

    }

    public Tweet(String createdAt) {
        this.createdAt = createdAt;
    }
}
