package com.anubis.flickr.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;

import com.anubis.flickr.FlickrClientApp;

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

    public static  boolean isLoggedIn() {
        return FlickrClientApp.getAppContext().getSharedPreferences("Flickr_User_Prefs", 0).contains("username");
    }

    public static  boolean isNewUser(String username) {
        return FlickrClientApp.getAppContext().getSharedPreferences("Flickr_User_Prefs", 0).getString("username","").equals(username);
    }

    public static String getUserId() {
        SharedPreferences prefs = FlickrClientApp.getAppContext().getSharedPreferences("Flickr_User_Prefs", 0);
        return prefs.getString("id", "");
    }

    public static SharedPreferences getUserPrefs() {
        return  FlickrClientApp.getAppContext().getSharedPreferences("Flickr_User_Prefs", 0);
    }
}
