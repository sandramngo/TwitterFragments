package com.codepath.apps.basictwitter.fragments;

import org.json.JSONArray;

import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;

import com.codepath.apps.basictwitter.R;
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
//        pb.setVisibility(ProgressBar.VISIBLE);
        showProgressBar();
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONArray json) {
                // run a background job and once complete
    //            pb.setVisibility(ProgressBar.INVISIBLE);
                hideProgressBar();
                if (page == 1) {
                    clearTweets();
                }
                addAll(Tweet.fromJSONArray(json));
            }
            @Override
            public void onFailure(Throwable e, String s) {
                Log.d("debug", e.toString());
                Log.d("debug", s.toString());
                // run a background job and once complete
                pb.setVisibility(ProgressBar.INVISIBLE);
            }
        }, maxId);
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
