package com.codepath.apps.basictwitter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetArrayAdapter extends ArrayAdapter<Tweet> {

    public TweetArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, 0, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);
        View v;
        
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.tweet_item, parent, false);
        } else {
            v = convertView;
        }
        
        ImageView ivProfileImage = (ImageView) v.findViewById(R.id.ivProfileImageCompose);
        TextView tvUserName = (TextView) v.findViewById(R.id.tvUserName);
        TextView tvBody = (TextView) v.findViewById(R.id.tvBody);
        TextView tvUserScreenName = (TextView) v.findViewById(R.id.tvUserScreenName);
        TextView tvTimestamp = (TextView) v.findViewById(R.id.tvTimestamp);
        
        ivProfileImage.setImageResource(android.R.color.transparent);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(tweet.getUser().getProfileImageUrl(), ivProfileImage);
        
        tvUserName.setText(tweet.getUser().getName());
        tvUserScreenName.setText("@" + tweet.getUser().getScreenName());
        tvBody.setText(tweet.getBody());
        tvTimestamp.setText(TweetUtils.getRelativeTimeAgo(tweet.getCreatedAt()));
        return v;
    }
    
    
}
