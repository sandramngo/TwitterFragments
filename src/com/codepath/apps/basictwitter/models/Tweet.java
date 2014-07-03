package com.codepath.apps.basictwitter.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ClipData.Item;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ForeignKeyAction;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Tweet")
public class Tweet extends Model implements Serializable {
  @Column(name = "User", onUpdate = ForeignKeyAction.CASCADE, onDelete = ForeignKeyAction.CASCADE)
    public User user;
    @Column(name = "body")
    private String body;
    @Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long uid;
    @Column(name = "created_at")
    private String createdAt;
    
    public Tweet() {
        super();
    }
    
    public Tweet(JSONObject jsonObject){
        super();
        try {
            this.body = jsonObject.getString("text");     
            this.uid = jsonObject.getLong("id");
            this.createdAt = jsonObject.getString("created_at");
            this.user = new User(jsonObject.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            
            Tweet tweet = new Tweet(tweetJson);
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
    
    public static List<Tweet> getRecent() {
        // This is how you execute a query
        return new Select()
          .from(Tweet.class)
          .where("uid >= 0")
          .orderBy("uid DESC")
          .execute();
    }

}
