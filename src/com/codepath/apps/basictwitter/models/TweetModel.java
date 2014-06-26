package com.codepath.apps.basictwitter.models;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ForeignKeyAction;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

/*
 * This is a temporary, sample model that demonstrates the basic structure
 * of a SQLite persisted Model object. Check out the ActiveAndroid wiki for more details:
 * https://github.com/pardom/ActiveAndroid/wiki/Creating-your-database-model
 * 
 */
@Table(name = "items")
public class TweetModel extends Model {
    // This is how you avoid duplicates
    @Column(name = "remote_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public int remoteId;
    
	// Define table fields
	@Column(name = "name")
	private String name;
	
    @Column(name = "User", onUpdate = ForeignKeyAction.CASCADE, onDelete = ForeignKeyAction.CASCADE)
    public UserModel user;
    
    @Column(name = "body")
    public String body;
    
    @Column(name = "uid")
    public long uid;
    
    @Column(name = "created_at")
    public String createdAt;
	
	public TweetModel() {
		super();
	}
	
	// Parse model from JSON
	public TweetModel(JSONObject object){
		super();

		try {
			this.name = object.getString("title");
			this.body = object.getString("text");     
			this.uid = object.getLong("id");
			this.createdAt = object.getString("created_at");
			this.user = new UserModel(object.getJSONObject("user"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	// Getters
	public String getName() {
		return name;
	}
	
	// Record Finders
	public static TweetModel byId(long id) {
	   return new Select().from(TweetModel.class).where("id = ?", id).executeSingle();
	}
	
	public static List<TweetModel> recentItems() {
      return new Select().from(TweetModel.class).orderBy("id DESC").limit("300").execute();
	}
}
