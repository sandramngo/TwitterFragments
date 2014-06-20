package com.codepath.apps.basictwitter;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ComposeActivity extends Activity {
    private TwitterClient client;
    private EditText etTweet;
    private User thisUser;
    private TextView tvUserName;
    private TextView tvUserScreenName;
    private ImageView ivProfileImage;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        client = TwitterApplication.getRestClient();
        
        etTweet = (EditText) findViewById(R.id.etComposeTweet);
        tvUserName = (TextView) findViewById(R.id.tvUserNameCompose);
        tvUserScreenName = (TextView) findViewById(R.id.tvUserScreenNameCompose);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImageCompose);
        
        thisUser = (User) getIntent().getExtras().getSerializable("user");
        tvUserName.setText(thisUser.getName());
        tvUserScreenName.setText("@" + thisUser.getScreenName());
        ivProfileImage.setImageResource(android.R.color.transparent);
        ImageLoader loader = ImageLoader.getInstance();
        loader.displayImage(thisUser.getProfileImageUrl(), ivProfileImage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_compose, menu);
        return true;
    }
    
    public void onTweet(MenuItem mi) {
        String tweetText = etTweet.getText().toString();
        client.postTweet(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject json) {
                setResult(RESULT_OK);
                finish();
            }
        }, tweetText);
    }
}
