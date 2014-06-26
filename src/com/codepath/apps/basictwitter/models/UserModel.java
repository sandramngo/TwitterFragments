package com.codepath.apps.basictwitter.models;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.query.Select;

public class UserModel extends Model {
    // This is how you avoid duplicates
    @Column(name = "remote_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public int remoteId;
    
    // Define table fields
    @Column(name = "name")
    private String name;
    
    @Column(name = "uid")
    private long uid;
    
    @Column(name = "screenname")
    private String screenName;
    
    @Column(name = "profile_image_url")
    private String profileImageUrl;
    
    public UserModel() {
        super();
    }
    
    // Parse model from JSON
    public UserModel(JSONObject object){
        super();

        try {
            this.name = object.getString("title");
            this.uid = object.getLong("id");
            this.screenName = object.getString("screen_name");
            this.profileImageUrl = object.getString("profile_image_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    // Getters
    public String getName() {
        return name;
    }
    
    // Record Finders
    public static UserModel byId(long id) {
       return new Select().from(UserModel.class).where("id = ?", id).executeSingle();
    }
    
    public static List<TweetModel> recentItems() {
      return new Select().from(UserModel.class).orderBy("id DESC").limit("300").execute();
    }
}