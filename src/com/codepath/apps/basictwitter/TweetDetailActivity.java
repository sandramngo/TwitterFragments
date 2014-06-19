package com.codepath.apps.basictwitter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetDetailActivity extends Activity {
    private TextView tvUserName;
    private TextView tvUserScreenName;
    private ImageView ivProfileImage;
    private TextView tvBody;
    private TextView tvCreatedAt;
    private Tweet tweet;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);
        
        tvUserName = (TextView) findViewById(R.id.tvUserNameDetail);
        tvUserScreenName = (TextView) findViewById(R.id.tvUserScreenNameDetail);
        tvBody = (TextView) findViewById(R.id.tvBodyDetail);
        tvCreatedAt = (TextView) findViewById(R.id.tvCreatedAt);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImageDetail);
        ivProfileImage.setImageResource(android.R.color.transparent);
        
        this.tweet = (Tweet) getIntent().getSerializableExtra("tweet");
        tvUserName.setText(tweet.getUser().getName());
        tvUserScreenName.setText("@" + tweet.getUser().getScreenName());
        tvBody.setText(tweet.getBody());
        tvCreatedAt.setText(TweetUtils.getRelativeTimeAgo(tweet.getCreatedAt()));
        ImageLoader loader = ImageLoader.getInstance();
        loader.displayImage(tweet.getUser().getProfileImageUrl(), ivProfileImage);
        
    }
}
