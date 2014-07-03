package com.codepath.apps.basictwitter.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.AdapterView.OnItemClickListener;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TweetArrayAdapter;
import com.codepath.apps.basictwitter.listeners.EndlessScrollListener;
import com.codepath.apps.basictwitter.models.Tweet;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class TweetsListFragment extends Fragment implements OnItemClickListener {
    protected ArrayList<Tweet> tweets;
    protected TweetArrayAdapter aTweets;
    protected PullToRefreshListView lvTweets;
    protected ProgressBar pb;
    
    private OnTweetClickedListener listener;
    public interface OnTweetClickedListener {
      public void onTweetClicked(Tweet tweet);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tweets = new ArrayList<Tweet>();
        aTweets = new TweetArrayAdapter(getActivity(), tweets);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        
        // Assign view references
        lvTweets = (PullToRefreshListView) v.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(aTweets);
        lvTweets.setOnItemClickListener(this);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (aTweets.getCount() > 0) {
                    Tweet lastTweet = aTweets.getItem(aTweets.getCount() - 1);
                    
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

        pb = (ProgressBar) v.findViewById(R.id.pbLoadBar);
        
        // Return view
        return v;
    }
    
    // Store the listener (activity) that will have events fired once the fragment is attached
    @Override
    public void onAttach(Activity activity) {
      super.onAttach(activity);
        if (activity instanceof OnTweetClickedListener) {
          listener = (OnTweetClickedListener) activity;
        } else {
          throw new ClassCastException(activity.toString()
              + " must implement TweetsListFragment.OnTweetClickedListener");
        }
    }
    
    public void addAll(ArrayList<Tweet> tweets) {
        aTweets.addAll(tweets);
    }
    
    public void clearTweets() {
        aTweets.clear();
    }
    
    public void refreshTimeline() {
        populateTimeline(1, 0);
        lvTweets.setSelection(0);
    }
    
    public void onRefreshComplete() {
        lvTweets.onRefreshComplete();
    }
    
    public void showProgressBar() {
        if (pb != null) {
            pb.setVisibility(ProgressBar.VISIBLE);
        }
    }
    
    public void hideProgressBar() {
        if (pb != null) {
            pb.setVisibility(ProgressBar.INVISIBLE);
        }
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        Tweet tweet = tweets.get(position);
        listener.onTweetClicked(tweet);
    }
    
    public void customLoadMoreDataFromApi(int page, long maxId) {}
    public void fetchTimelineAsync(int page) {}
    public void populateTimeline(int page, long maxId) {}
    
    
}
