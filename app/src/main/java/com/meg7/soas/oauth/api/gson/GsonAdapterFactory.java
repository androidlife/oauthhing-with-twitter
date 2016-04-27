package com.meg7.soas.oauth.api.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * Created by laaptu on 4/26/16.
 */
public class GsonAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (UserInfoAdapter.adapts(type))
            return (TypeAdapter<T>) new UserInfoAdapter();
        if (UserTimeLineTweetAdapter.adapts(type))
            return (TypeAdapter<T>) new UserTimeLineTweetAdapter();
        return null;
    }
}
