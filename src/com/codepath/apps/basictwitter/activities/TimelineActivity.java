package com.codepath.apps.basictwitter.activities;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.TwitterClient;
import com.codepath.apps.basictwitter.fragments.HomeTimelineFragment;
import com.codepath.apps.basictwitter.fragments.MentionsTimelineFragment;
import com.codepath.apps.basictwitter.fragments.TweetsListFragment;
import com.codepath.apps.basictwitter.fragments.TweetsListFragment.OnTweetClickedListener;
import com.codepath.apps.basictwitter.listeners.FragmentTabListener;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends FragmentActivity implements OnTweetClickedListener {
    private User thisUser;
    private TwitterClient client;
    int maxId;
    private TweetsListFragment homeFragment;
    
    public static final int COMPOSE_ACTIVITY_REQUEST_CODE = 1;
    public static final int TWEET_DETAIL_ACTIVITY_REQUEST_CODE = 2;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        setupTabs();
        client = TwitterApplication.getRestClient();
        client.getVerifyCredentials(new JsonHttpResponseHandler() {
          @Override
          public void onSuccess(JSONObject json) {
              thisUser = new User(json);
              thisUser.save();
              getActionBar().setTitle("@" + thisUser.getScreenName());
          }
        });
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    
    private void setupTabs() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);

        Tab homeTab = actionBar
            .newTab()
            .setText("Home")
            .setTag("HomeTimelineFragment")
            .setTabListener(
                new FragmentTabListener<HomeTimelineFragment>(R.id.flContainer, this, "home",
                                HomeTimelineFragment.class));

        actionBar.addTab(homeTab);
        actionBar.selectTab(homeTab);

        Tab mentionsTab = actionBar
            .newTab()
            .setText("Mentions")
            .setTag("MentionsTimelineFragment")
            .setTabListener(
                new FragmentTabListener<MentionsTimelineFragment>(R.id.flContainer, this, "mentions",
                        MentionsTimelineFragment.class));

        actionBar.addTab(mentionsTab);
    }
    
    public void onCompose(MenuItem mi) {       
        if (thisUser != null) {
            Intent i = new Intent(this, ComposeActivity.class);
            i.putExtra("user", thisUser);
            startActivityForResult(i, COMPOSE_ACTIVITY_REQUEST_CODE);
        }
    }
    
    public void onProfileView(MenuItem mi) {
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("user", thisUser);
        startActivity(i);
    }

    @Override
    public void onTweetClicked(Tweet tweet) {
        Intent i = new Intent(this, TweetDetailActivity.class);
        i.putExtra("tweet", tweet);
        startActivityForResult(i, TWEET_DETAIL_ACTIVITY_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == COMPOSE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                ((TweetsListFragment) getSupportFragmentManager().findFragmentByTag("home")).refreshTimeline();
            }
        } else if (requestCode == TWEET_DETAIL_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                ((TweetsListFragment) getSupportFragmentManager().findFragmentByTag("home")).refreshTimeline();
            }
        }
    }
}
