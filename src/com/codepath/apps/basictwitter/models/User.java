package com.codepath.apps.basictwitter.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "User")
public class User extends Model implements Serializable {
    @Column(name = "Name")
    private String name;
    @Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long uid;
    @Column(name = "screen_name")
    private String screenName;
    @Column(name = "profile_image_url")
    private String profileImageUrl;
    @Column(name = "tagline")
    private String tagline;
    @Column(name = "followers")
    private int followers;
    @Column(name = "following")
    private int following;
    
    public User() {
        super();
    }
    public User(JSONObject jsonObject) {
        try {
            this.name = jsonObject.getString("name");
            this.uid = jsonObject.getLong("id");
            this.screenName = jsonObject.getString("screen_name");
            this.profileImageUrl = jsonObject.getString("profile_image_url");
            this.tagline = jsonObject.getString("description");
            this.followers = jsonObject.getInt("followers_count");
            this.following = jsonObject.getInt("friends_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }    
    }

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
    
    public String getTagline() {
        return tagline;
    }

    public int getFollowers() {
        return followers;
    }

    public int getFollowing() {
        return following;
    }

}
