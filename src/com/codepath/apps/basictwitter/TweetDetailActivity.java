package com.codepath.apps.basictwitter;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetDetailActivity extends Activity {
    private TextView tvUserName;
    private TextView tvUserScreenName;
    private ImageView ivProfileImage;
    private TextView tvBody;
    private Tweet tweet;
    private EditText etReplyToTweet;
    private Button btnReplyToTweet;
    private TwitterClient client;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);
        client = TwitterApplication.getRestClient();
        
        tvUserName = (TextView) findViewById(R.id.tvUserNameDetail);
        tvUserScreenName = (TextView) findViewById(R.id.tvUserScreenNameDetail);
        tvBody = (TextView) findViewById(R.id.tvBodyDetail);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImageDetail);
        ivProfileImage.setImageResource(android.R.color.transparent);
        etReplyToTweet = (EditText) findViewById(R.id.etReplyToTweet);
        btnReplyToTweet = (Button) findViewById(R.id.btnReplyToTweet);
        
        this.tweet = (Tweet) getIntent().getSerializableExtra("tweet");
        tvUserName.setText(tweet.getUser().getName());
        tvUserScreenName.setText("@" + tweet.getUser().getScreenName());
        tvBody.setText(tweet.getBody());
        ImageLoader loader = ImageLoader.getInstance();
        loader.displayImage(tweet.getUser().getProfileImageUrl(), ivProfileImage);
        etReplyToTweet.setHint("Reply to " + tweet.getUser().getName());
        etReplyToTweet.setOnFocusChangeListener(new OnFocusChangeListener() {
            
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String replyText = etReplyToTweet.getText().toString();
                String replyToUser = "@" + tweet.getUser().getScreenName();
                if (hasFocus && !replyText.startsWith(replyToUser)) {
                    etReplyToTweet.setText(replyToUser);

                    int textLength = etReplyToTweet.getText().length();
                    etReplyToTweet.setSelection(textLength, textLength);
                }
            }
        });
        
//        Typeface gothamNarrow = Typeface.createFromAsset(getAssets(), "fonts/GothamNarrow-Medium.otf");
//        tvUserName.setTypeface(gothamNarrow);
//        tvUserScreenName.setTypeface(gothamNarrow);
//        tvBody.setTypeface(gothamNarrow);
//        btnReplyToTweet.setTypeface(gothamNarrow);
//        etReplyToTweet.setTypeface(gothamNarrow);
    }
    
    public void onReplyToTweet(View view) {
        String replyText = etReplyToTweet.getText().toString();
        String statusId = tweet.getUid() + "";
        client.replyTweet(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject json) {
                setResult(RESULT_OK);
                finish();
            }
        }, replyText, statusId);
    }
}
