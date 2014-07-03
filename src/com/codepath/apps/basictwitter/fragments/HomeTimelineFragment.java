package com.codepath.apps.basictwitter.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.TwitterClient;
import com.codepath.apps.basictwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class HomeTimelineFragment extends TweetsListFragment implements OnItemClickListener {
    private TwitterClient client;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        populateTimeline(1, 0);
    }
    
    @Override
    public void populateTimeline(final int page, long maxId) {
        showProgressBar();
        
        // get recent tweets from db first
        ArrayList<Tweet> recentTweets = (ArrayList) Tweet.getRecent();
        
        // get the newest id
        long sinceId = 1;
        if (recentTweets.size() > 0 && page == 1) {
            Tweet latestTweet = recentTweets.get(0);
            sinceId = latestTweet.getUid();
        }
        
        Log.d("debug", "max_id: " + maxId + " since_id: " + sinceId);
        if (isNetworkAvailable()) {
            // get new tweets
            client.getHomeTimeline(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONArray json) {
                    hideProgressBar();
                    if (page == 1) {
                     //   clearTweets();
                    }
                    addAll(Tweet.fromJSONArray(json));
                }
                @Override
                public void onFailure(Throwable e, String s) {
                    Log.d("debug", e.toString());
                    Log.d("debug", s.toString());
                    pb.setVisibility(ProgressBar.INVISIBLE);
                }
            }, maxId, sinceId);
        } else {
            hideProgressBar();
            displayNoConnectionMsg();
        }
        // add the older tweets 
        if (recentTweets.size() != 0 && page == 1) {
           addAll(recentTweets);
           Log.d("debug", "recent tweet size : " + recentTweets.size());
            
        }
    }
    
    @Override
    public void customLoadMoreDataFromApi(int page, long maxId) {
        populateTimeline(page, maxId);
    }
    
    @Override
    public void fetchTimelineAsync(int page) {
        clearTweets();
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            public void onSuccess(JSONArray json) {
                addAll(Tweet.fromJSONArray(json));
                onRefreshComplete();
            }

            public void onFailure(Throwable e) {
                Log.d("DEBUG", "Fetch timeline error: " + e.toString());
            }
        }, 0);
    }
}
