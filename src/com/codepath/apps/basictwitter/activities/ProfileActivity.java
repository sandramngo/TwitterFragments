package com.codepath.apps.basictwitter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.fragments.TweetsListFragment.OnTweetClickedListener;
import com.codepath.apps.basictwitter.fragments.UserTimelineFragment;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends FragmentActivity implements OnTweetClickedListener {
    
    User thisUser;
    ImageView ivProfileViewImage;
    TextView tvName;
    TextView tvTagline;
    TextView tvFollowers;
    TextView tvFollowing;

    public static final int COMPOSE_ACTIVITY_REQUEST_CODE = 1;
    public static final int TWEET_DETAIL_ACTIVITY_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        
        thisUser = (User) getIntent().getSerializableExtra("user");
        ivProfileViewImage = (ImageView) findViewById(R.id.ivProfileViewImage);
        tvName = (TextView) findViewById(R.id.tvName);
        tvTagline = (TextView) findViewById(R.id.tvTagline);
        tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        
        populateProfileHeader();
        getActionBar().setTitle("@" + thisUser.getScreenName());
        
     // Within the activity
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        UserTimelineFragment fragment = UserTimelineFragment.newInstance(thisUser);
        ft.replace(R.id.flUserContainer, fragment);
        ft.commit();
    }
    
    public void populateProfileHeader() {
        tvName.setText(thisUser.getName());
        tvTagline.setText(thisUser.getTagline());
        ivProfileViewImage.setImageResource(android.R.color.transparent);
        ImageLoader loader = ImageLoader.getInstance();
        loader.displayImage(thisUser.getProfileImageUrl(), ivProfileViewImage);   
        tvFollowers.setText(thisUser.getFollowers() + " Followers");
        tvFollowing.setText(thisUser.getFollowing() + " Following");
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }
    
    public void onCompose(MenuItem mi) {       
        if (thisUser != null) {
            Intent i = new Intent(this, ComposeActivity.class);
            i.putExtra("user", thisUser);
            startActivityForResult(i, COMPOSE_ACTIVITY_REQUEST_CODE);
        }
    }
    
    @Override
    public void onTweetClicked(Tweet tweet) {
        Intent i = new Intent(this, TweetDetailActivity.class);
        i.putExtra("tweet", tweet);
        startActivityForResult(i, TWEET_DETAIL_ACTIVITY_REQUEST_CODE);
    }
    
}
