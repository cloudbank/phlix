package com.anubis.flickr.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.anubis.flickr.network.OAuthBaseClient;

/**
 * Created by sabine on 9/20/16.
 */


public abstract class OAuthLoginActivity  extends FragmentActivity
        implements OAuthBaseClient.OAuthAccessHandler {

    private OAuthBaseClient client;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //prevent leaking activity
         if (null != this.client.getAccessHandler() ) {
             OAuthBaseClient.OAuthAccessHandler handler = this.client.getAccessHandler();
             handler = null;
         }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.client  =  OAuthBaseClient.getInstance(this.getApplicationContext(), this);
    }





    // Use this to properly assign the new intent with callback code
    // for activities with a "singleTask" launch mode
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    // Extract the uri data and call authorize to retrieve access token
    // This is why after the browser redirects to the app, authentication is completed
    @SuppressWarnings("unchecked")
    @Override
    protected void onResume() {
        super.onResume();
        //Class<T> clientClass = getClientClass();
        // Extracts the authenticated url data after the user
        // authorizes the OAuth app in the browser
        if (this.client.getPrefs().contains("request_token")) {
            Uri uri = getIntent().getData();
            try {
                //client = (T) com.anubis.flickr.network.OAuthBaseClient.getInstance(client.getClass(), this);
                client.authorize(uri); // fetch access token (if needed)
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




}

/*
 *1) set props in app.properties
 * 2) Subclass OAuthLoginActivity
 * 3) Invoke .login
 * 4) Optionally override
 *   a) onLoginSuccess
 *   b) onLoginFailure(Exception e)
 * 5) In other activities that need the client
 *   a) c = TwitterClient.getSharedClient()
*    b) c.getTimeline(...)
 * 6) Modify AndroidManifest.xml to add an IntentFilter w/ the callback URL
 * defined in the OAuthBaseClient.
 */
