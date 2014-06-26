package com.codepath.apps.basictwitter.models;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Tweet implements Serializable {
    private String body;
    private long uid;
    private String createdAt;
    private User user;
    
    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
            tweet.body = jsonObject.getString("text");     
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return tweet;
    }

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray json) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>();
        for (int i = 0; i < json.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = json.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
            
            Tweet tweet = Tweet.fromJSON(tweetJson);
            if (tweet != null) {
                tweets.add(tweet);
            }
        }
        return tweets;
    }
    
    @Override
    public String toString() {
        return getBody() + " - " + getUser().getScreenName();
    }

}
