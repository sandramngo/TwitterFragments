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
        
//        lvTweets.setOnScrollListener(new EndlessScrollListener() {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount) {
//                if (aTweets.getCount() > 0) {
//                    Tweet lastTweet = aTweets.getItem(aTweets.getCount() - 1);
//                    
//                    customLoadMoreDataFromApi(page, lastTweet.getUid() - 1); 
//                }
//            }
//         });
//        
//        lvTweets.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                // Your code to refresh the list contents
//                // Make sure you call listView.onRefreshComplete()
//                // once the loading is done. This can be done from here or any
//                // place such as when the network request has completed successfully.
//                fetchTimelineAsync(0);
//            }
//        });
//        
//        lvTweets.setOnItemClickListener(this);
//        
//        client.getVerifyCredentials(new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(JSONObject json) {
//                thisUser = User.fromJSON(json);
//            }
//        });
    }
    
    public void populateTimeline(final int page, long maxId) {
        client.getMentionsTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONArray json) {
                if (page == 1) {
                    clearTweets();
                }
                addAll(Tweet.fromJSONArray(json));
            }
            @Override
            public void onFailure(Throwable e, String s) {
                Log.d("debug", e.toString());
                Log.d("debug", s.toString());
            }
        });
    }
    
    public void customLoadMoreDataFromApi(int page, long maxId) {
        populateTimeline(page, maxId);
    }
    
    public void fetchTimelineAsync(int page) {
        clearTweets();
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            public void onSuccess(JSONArray json) {
                addAll(Tweet.fromJSONArray(json));
               // lvTweets.onRefreshComplete();
            }

            public void onFailure(Throwable e) {
                Log.d("DEBUG", "Fetch timeline error: " + e.toString());
            }
        }, 0);
    }
}
