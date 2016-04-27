package com.meg7.soas.oauth.api.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.meg7.soas.oauth.model.Tweet;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by laaptu on 4/26/16.
 */
public class UserTimeLineTweetAdapter extends TypeAdapter<ArrayList<Tweet>> {

    private static final Type USER_TIMELINE_TYPE_TOKEN =
            new TypeToken<ArrayList<Tweet>>() {
            }.getType();


    public static boolean adapts(TypeToken<?> type) {
        return USER_TIMELINE_TYPE_TOKEN.equals(type.getType());
    }

    @Override
    public void write(JsonWriter out, ArrayList<Tweet> value) throws IOException {

    }

    @Override
    public ArrayList<Tweet> read(JsonReader in) {
        ArrayList<Tweet> tweets = new ArrayList<>();
        Timber.d("Read tweets ");

        try {
            in.beginArray();
            Timber.d("Begin Array");
            while (in.hasNext()) {
                in.beginObject();
                Tweet tweet = new Tweet();
                while (in.hasNext()) {
                    switch (in.nextName()) {
                        case "created_at":
                            tweet.createdAt = in.nextString();
                            break;
                        case "text":
                            tweet.text = in.nextString();
                            break;
                        case "user":
                            in.beginObject();
                            while (in.hasNext()) {
                                switch (in.nextName()) {
                                    case "profile_image_url":
                                        tweet.imageUrl = in.nextString();
                                        break;
                                    default:
                                        in.skipValue();
                                }
                            }
                            in.endObject();
                            break;
                        default:
                            in.skipValue();
                    }
                }
                tweets.add(tweet);
                in.endObject();
            }
            in.endArray();
            Timber.d("End Array");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tweets;
    }
}
