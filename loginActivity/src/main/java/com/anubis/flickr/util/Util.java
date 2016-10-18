package com.anubis.flickr.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;

import com.anubis.flickr.FlickrClientApp;
import com.anubis.flickr.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by sabine on 9/21/16.
 */

public class Util {

    public static String getProperty(String key, Context context) throws IOException {
        Properties properties = new Properties();
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open("config.properties");
        properties.load(inputStream);
        return properties.getProperty(key);

    }

    public static  boolean isInit() {
        return !(FlickrClientApp.getAppContext().getSharedPreferences("Flickr_User_Prefs", 0).contains(FlickrClientApp.getAppContext().getResources().getString(R.string.current_user)));
    }

    public static  boolean isNewUser(String username) {
        return !FlickrClientApp.getAppContext().getSharedPreferences("Flickr_User_Prefs", 0).getString(FlickrClientApp.getAppContext().getResources().getString(R.string.current_user),"").equals(username);
    }

    public static String getCurrentUser() {
        SharedPreferences prefs = Util.getUserPrefs();
        return prefs.getString(FlickrClientApp.getAppContext().getResources().getString(R.string.current_user), "");
    }

    public static String getUserId() {
        SharedPreferences prefs = Util.getUserPrefs();
        return prefs.getString(FlickrClientApp.getAppContext().getResources().getString(R.string.user_id), "");
    }

    public static SharedPreferences getUserPrefs() {
        return  FlickrClientApp.getAppContext().getSharedPreferences("Flickr_User_Prefs", 0);
    }
}
