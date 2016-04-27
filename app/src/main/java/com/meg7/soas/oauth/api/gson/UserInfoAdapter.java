package com.meg7.soas.oauth.api.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.meg7.soas.oauth.model.UserInfo;

import java.io.IOException;

import timber.log.Timber;

/**
 * Created by laaptu on 4/26/16.
 */
public class UserInfoAdapter extends TypeAdapter<UserInfo> {
    @Override
    public void write(JsonWriter out, UserInfo value) throws IOException {

    }


    private static final TypeToken<UserInfo> USER_INFO_TYPE_TOKEN = TypeToken.get(UserInfo.class);

    public static boolean adapts(TypeToken<?> type) {
        return USER_INFO_TYPE_TOKEN.equals(type);
    }

    @Override
    public UserInfo read(JsonReader in) {
        UserInfo userInfo = new UserInfo();
        try {
            in.beginObject();
            while (in.hasNext()) {
                String tagName = in.nextName();
                Timber.d("TagName = %s", tagName);
                switch (tagName) {
                    case "name":
                        userInfo.fullName = in.nextString();
                        break;
                    case "screen_name":
                        userInfo.screenName = in.nextString();
                        break;
                    case "profile_image_url":
                        userInfo.profileImageUrl = in.nextString();
                        break;
                    case "profile_background_image_url":
                        userInfo.backgroundImageUrl = in.nextString();
                        break;
                    default:
                        in.skipValue();
                }
            }
            in.endObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfo;
    }
}
