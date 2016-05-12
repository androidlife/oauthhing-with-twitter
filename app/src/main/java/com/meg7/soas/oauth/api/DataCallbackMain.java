package com.meg7.soas.oauth.api;

/**
 * Created by laaptu on 4/26/16.
 */
public class DataCallbackMain<T> implements DataCallback<T> {

    private DataCallback<T> dataCallback;
    private boolean cancel = false;

    public DataCallbackMain(DataCallback<T> dataCallback) {
        this.dataCallback = dataCallback;
    }


    @Override
    public void onResponse(T response) {
        if (!cancel)
            dataCallback.onResponse(response);


    }

    @Override
    public void onFailure(String error) {
        if (!cancel)
            dataCallback.onFailure(error);

    }

    @Override
    public void cancel() {
        cancel = true;
    }


}
