package com.codepath.apps.basictwitter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.format.DateUtils;

public class TweetUtils {
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);
     
        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL).toString();
            int firstSpace = relativeDate.indexOf(" ");  
            int firstInt = 0;
            char[] crs = relativeDate.toCharArray();
            for (int i = 0; i < crs.length; i++) {
                if (Character.isDigit(crs[i])) {
                   firstInt = i;
                   break;
                }
            }
            firstSpace = relativeDate.indexOf(" ", firstInt);
            relativeDate = relativeDate.substring(0, firstSpace) + relativeDate.charAt(firstSpace + 1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
     
        return relativeDate;
    }
}
