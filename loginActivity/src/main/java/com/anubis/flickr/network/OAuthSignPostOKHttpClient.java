package com.anubis.flickr.network;


import android.content.SharedPreferences;
import android.net.Uri;

import com.anubis.flickr.util.AsyncSimpleTask;

import java.util.Properties;

import oauth.signpost.http.HttpParameters;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthProvider;


/**
 * Created by sabine on 9/18/16.
 */

public class OAuthSignPostOKHttpClient {    //
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


    private OkHttpOAuthConsumer consumer;
    private OkHttpOAuthProvider provider;
    private String callbackUrl;
    private OAuthSignPostOKHttpClient.OAuthTokenHandler handler;
    private String accessToken;
    private SharedPreferences prefs;


    public OAuthSignPostOKHttpClient(Properties prop, OAuthSignPostOKHttpClient.OAuthTokenHandler handler, SharedPreferences prefs) {
        this.handler = handler;
        this.prefs = prefs;
        callbackUrl = prop.getProperty("callbackUrl");
        if (callbackUrl == null) {
            callbackUrl = "oob";
        }

        this.consumer = new OkHttpOAuthConsumer(prop.getProperty("consumerKey"), prop.getProperty("consumerSecret"));
        this.provider = new OkHttpOAuthProvider(prop.getProperty("requestTokenEndpoint"), prop.getProperty("accessTokenEndpoint"), prop.getProperty("authorizationEndpoint"));
    }

    public OkHttpOAuthConsumer getConsumer() {
        return this.consumer;
    }

    public void setConsumer(OkHttpOAuthConsumer consumer) {
        this.consumer = consumer;
    }

    public OkHttpOAuthProvider getProvider() {
        return this.provider;
    }

    String getCallback() {
        return this.callbackUrl;
    }

    public void fetchRequestToken() {
        //replace w retrofit
        new AsyncSimpleTask(new AsyncSimpleTask.AsyncSimpleTaskHandler() {
            Exception e = null;


            public void doInBackground() {
                try {
                    provider.retrieveRequestToken(getConsumer(), getCallback(), (new String[]{}));
                    // @todo version2

                } catch (Exception var2) {
                    this.e = var2;
                }

            }

            public void onPostExecute() {
                if (this.e != null) {
                    OAuthSignPostOKHttpClient.this.handler.onFailure(this.e);
                } else {

                    OAuthSignPostOKHttpClient.this.handler.onReceivedRequestToken(getConsumer(), getProvider().getAuthorizationWebsiteUrl());
                }

            }
        });
    }


    public void fetchAccessToken(final Uri uri) {
        new AsyncSimpleTask(new AsyncSimpleTask.AsyncSimpleTaskHandler() {
            Exception e = null;

            public void doInBackground() {
                Uri authorizedUri = uri;
                String oauth_verifier = null;
                if (authorizedUri.getQuery().contains("code")) {
                    oauth_verifier = authorizedUri.getQueryParameter("code");
                } else if (authorizedUri.getQuery().contains("oauth_verifier")) {
                    oauth_verifier = authorizedUri.getQueryParameter("oauth_verifier");
                }

                try {
                    if (oauth_verifier == null) {
                        throw new Exception("No verifier code was returned with uri \'" + uri + "\' " + "and access token cannot be retrieved");
                    }
                    provider.retrieveAccessToken(getConsumer(), oauth_verifier);
                    setUserIdAndName(provider.getResponseParameters());


                } catch (Exception var4) {
                    this.e = var4;
                }

            }

            public void onPostExecute() {
                if (this.e != null) {
                    OAuthSignPostOKHttpClient.this.handler.onFailure(this.e);
                } else {

                    OAuthSignPostOKHttpClient.this.handler.onReceivedAccessToken(getConsumer());
                }

            }
        });
    }

    public String getAccessToken() {
        return this.accessToken;
    }


    public void setAccessToken(com.anubis.flickr.models.Token token) {
        if (accessToken == null) {
            this.accessToken = null;
        } else {
            this.accessToken = accessToken;
        }

    }

    public void setUserIdAndName(HttpParameters params) {
        SharedPreferences.Editor editor = this.prefs.edit();
        editor.putString("username", params.get("username").first());
        editor.putString("userId",params.get("user_nsid").first() );
        editor.commit();


    }


    public interface OAuthTokenHandler {
        void onReceivedRequestToken(OkHttpOAuthConsumer consumer, String authorizeUrl);

        void onReceivedAccessToken(OkHttpOAuthConsumer consumer);

        void onFailure(Exception var1);
    }
}


