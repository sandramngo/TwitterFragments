package com.codepath.apps.basictwitter;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.activities.ProfileActivity;
import com.codepath.apps.basictwitter.activities.TweetDetailActivity;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetArrayAdapter extends ArrayAdapter<Tweet> {

    public TweetArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, 0, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Tweet tweet = getItem(position);
        View v;
        
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.tweet_item, parent, false);
        } else {
            v = convertView;
        }
        
        ImageView ivProfileImage = (ImageView) v.findViewById(R.id.ivProfileImage);
        TextView tvUserName = (TextView) v.findViewById(R.id.tvUserName);
        TextView tvBody = (TextView) v.findViewById(R.id.tvBody);
        TextView tvUserScreenName = (TextView) v.findViewById(R.id.tvUserScreenName);
        TextView tvTimestamp = (TextView) v.findViewById(R.id.tvTimestamp);
        
        ivProfileImage.setImageResource(android.R.color.transparent);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(tweet.getUser().getProfileImageUrl(), ivProfileImage);
        
        ivProfileImage.setOnClickListener(new OnClickListener() {
           @Override
            public void onClick(View v) {
               onProfileClick(tweet, v);
            } 
        });
        
        tvUserName.setText(tweet.getUser().getName());
        tvUserName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onTweetClick(tweet, v);
            }
        });
        tvUserScreenName.setText("@" + tweet.getUser().getScreenName());
        tvUserScreenName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onTweetClick(tweet, v);
            }
        });
        tvBody.setText(tweet.getBody());
        tvBody.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onTweetClick(tweet, v);
            }
        });
        tvTimestamp.setText(TweetUtils.getRelativeTimeAgo(tweet.getCreatedAt()));
        tvTimestamp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onTweetClick(tweet, v);
            }
        });

        return v;
    }
    
    public void onProfileClick(Tweet tweet, final View v) {
        TwitterApplication.getRestClient().getLookupUser(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONArray json) {
                if (json.length() > 0) {
                    try {
                        JSONObject object = json.getJSONObject(0);
                        User user = User.fromJSON(object);
                        Intent i = new Intent(v.getContext(), ProfileActivity.class);
                        i.putExtra("user", user);
                        v.getContext().startActivity(i); 
                    } catch (JSONException e) {
                        
                    }
                }
            }
        }, tweet.getUser().getScreenName());
    }
    
    public void onTweetClick(Tweet tweet, final View v) {
        Intent i = new Intent(v.getContext(), TweetDetailActivity.class);
        i.putExtra("tweet", tweet);
        v.getContext().startActivity(i);    
    }
}
