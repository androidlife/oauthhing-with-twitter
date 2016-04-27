package com.meg7.soas.oauth.api.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.meg7.soas.oauth.model.Tweet;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by laaptu on 4/26/16.
 */
public class UserTimeLineTweetAdapter extends TypeAdapter<ArrayList<Tweet>> {

    private static final TypeToken<ArrayList> USER_TIMELINE_TYPE_TOKEN = TypeToken.get(ArrayList.class);

    public static boolean adapts(TypeToken<?> type) {
        return USER_TIMELINE_TYPE_TOKEN.equals(type);
    }

    @Override
    public void write(JsonWriter out, ArrayList<Tweet> value) throws IOException {

    }

    @Override
    public ArrayList<Tweet> read(JsonReader in) {
        ArrayList<Tweet> tweets = new ArrayList<>();

        try {
            in.beginArray();
            while (in.hasNext()) {
                in.beginObject();
                while (in.hasNext()) {
                    switch (in.nextName()) {
                        case "created_at":
                            tweets.add(new Tweet(in.nextString()));
                            in.endObject();
                            break;
                    }
                }
                in.endObject();
            }
            in.endArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tweets;
    }
}
