package com.codepath.apps.basictwitter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class TimelineActivity extends Activity {
    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private ArrayAdapter<Tweet> aTweets;
    private PullToRefreshListView lvTweets;
    private User thisUser;
    
    int maxId;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        client = TwitterApplication.getRestClient();
        populateTimeline(1, 0);
        
        lvTweets = (PullToRefreshListView) findViewById(R.id.lvTweets);
        tweets = new ArrayList<Tweet>();
        aTweets = new TweetArrayAdapter(this, tweets);
        lvTweets.setAdapter(aTweets);
        
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (aTweets.getCount() > 0) {
                    Tweet lastTweet = aTweets.getItem(totalItemsCount -1);
                    
                    customLoadMoreDataFromApi(page, lastTweet.getUid() - 1); 
                }
            }
         });
        
        lvTweets.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list contents
                // Make sure you call listView.onRefreshComplete()
                // once the loading is done. This can be done from here or any
                // place such as when the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        populateTimeline(1, 0);
    }
    
    public void populateTimeline(final int page, long maxId) {
        Log.d("debug", page+"");
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONArray json) {
                if (page == 1) {
                    aTweets.clear();
                }
                aTweets.addAll(Tweet.fromJSONArray(json));
                
             //   Log.d("debug", json.toString());
                
            }
            @Override
            public void onFailure(Throwable e, String s) {
                Log.d("debug", e.toString());
                Log.d("debug", s.toString());
            }
        }, maxId);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    
    public void onCompose(MenuItem mi) {
        client.getVerifyCredentials(new JsonHttpResponseHandler() {
           @Override
            public void onSuccess(JSONObject json) {
               thisUser = User.fromJSON(json);
            } 
        });
        
        if (thisUser != null) {
            Intent i = new Intent(this, ComposeActivity.class);
            i.putExtra("user", thisUser);
            startActivityForResult(i, 5);
        }
    }

    public void customLoadMoreDataFromApi(int page, long maxId) {
        populateTimeline(page, maxId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 5) {
            if (resultCode == RESULT_OK) {
                populateTimeline(1, 0);
            }
        }
    }
    
    public void fetchTimelineAsync(int page) {
        aTweets.clear();
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            public void onSuccess(JSONArray json) {
                aTweets.addAll(Tweet.fromJSONArray(json));
                lvTweets.onRefreshComplete();
            }

            public void onFailure(Throwable e) {
                Log.d("DEBUG", "Fetch timeline error: " + e.toString());
            }
        }, 0);
    }
}
