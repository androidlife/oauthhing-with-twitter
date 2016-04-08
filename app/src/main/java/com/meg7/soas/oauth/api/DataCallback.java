package com.meg7.soas.oauth.api;

/**
 */

public interface DataCallback<T> {
    void onResponse(T response);
    void onFailure(String error);
    //not implemented right now
    void cancel();
}
