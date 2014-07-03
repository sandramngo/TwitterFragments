package com.codepath.apps.basictwitter.fragments;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.TwitterClient;
import com.codepath.apps.basictwitter.activities.TweetDetailActivity;
import com.codepath.apps.basictwitter.listeners.EndlessScrollListener;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class MentionsTimelineFragment extends TweetsListFragment implements OnItemClickListener {
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
        if (isNetworkAvailable()) {
            client.getMentionsTimeline(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONArray json) {
                    hideProgressBar();
                    if (page == 1) {
                        clearTweets();
                    }
                    addAll(Tweet.fromJSONArray(json));
                }
                @Override
                public void onFailure(Throwable e, String s) {
                    hideProgressBar();
                    Log.d("debug", e.toString());
                    Log.d("debug", s.toString());
                }
            }, maxId);
        } else {
            hideProgressBar();
            displayNoConnectionMsg();
        }
    }
    
    @Override
    public void customLoadMoreDataFromApi(int page, long maxId) {
        populateTimeline(page, maxId);
    }
    
    @Override
    public void fetchTimelineAsync(int page) {
        clearTweets();
        client.getMentionsTimeline(new JsonHttpResponseHandler() {
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
