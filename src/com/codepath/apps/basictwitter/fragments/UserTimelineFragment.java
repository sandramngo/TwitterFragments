package com.codepath.apps.basictwitter.fragments;

import org.json.JSONArray;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.TwitterClient;
import com.codepath.apps.basictwitter.listeners.EndlessScrollListener;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;


public class UserTimelineFragment extends TweetsListFragment {
    
    private TwitterClient client;
    private User user;
    
    public static UserTimelineFragment newInstance(User user) {
        UserTimelineFragment fragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        user = (User) getArguments().getSerializable("user");
        populateTimeline(1, 0);
    }
    
    @Override
    public void populateTimeline(final int page, long maxId) {
        Log.d("debug", "User populate page " + page);
        showProgressBar();
        if (isNetworkAvailable()) {
            client.getUserTimeline(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONArray json) {
                    hideProgressBar();
                    if (page == 1) {
                        //clearTweets();
                    }
                    addAll(Tweet.fromJSONArray(json));
                }
                @Override
                public void onFailure(Throwable e, String s) {
                    Log.d("debug", e.toString());
                    Log.d("debug", s.toString());
                    hideProgressBar();
                }
            }, user.getUid() + "", maxId);
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
        client.getUserTimeline(new JsonHttpResponseHandler() {
            public void onSuccess(JSONArray json) {
                addAll(Tweet.fromJSONArray(json));
                onRefreshComplete();
            }

            public void onFailure(Throwable e) {
                Log.d("DEBUG", "Fetch timeline error: " + e.toString());
            }
        }, user.getUid() + "", 0);
    }

}
