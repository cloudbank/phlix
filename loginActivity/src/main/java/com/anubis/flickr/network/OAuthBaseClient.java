package com.anubis.flickr.network;

/**
 * Created by sabine on 9/18/16.
 */

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.anubis.flickr.models.Token;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;

public class OAuthBaseClient {
    protected String baseUrl;
    protected Context context;
    protected OAuthSignPostOKHttpClient client;
    protected SharedPreferences prefs;
    protected SharedPreferences.Editor editor;
    protected OAuthBaseClient.OAuthAccessHandler accessHandler;
    protected String callbackUrl;
    protected int requestIntentFlags = -1;
    protected static HashMap<Class<? extends OAuthBaseClient>, OAuthBaseClient> instances = new HashMap();
    private static OAuthBaseClient instance;

    public SharedPreferences getPrefs() {
        return this.prefs;
    }


    public OAuthBaseClient.OAuthAccessHandler getAccessHandler() {
        return this.accessHandler;
    }

    public static OAuthBaseClient getInstance(Context context, OAuthBaseClient.OAuthAccessHandler handler) {
        if (instance == null) {
            instance = new OAuthBaseClient(context, handler);
        }
        return instance;
    }

    //@singleton
    private OAuthBaseClient(Context context, OAuthBaseClient.OAuthAccessHandler handler) {
        this.context = context;
        this.accessHandler = handler;
        final Properties prop = new Properties();
        try {
            prop.load(this.context.getAssets().open("app.properties"));
        } catch (IOException ex) {
            Log.e("ERROR", "App must be configured with app.properties");
            ex.printStackTrace();

        }

        this.baseUrl = prop.getProperty("baseUrl");
        this.callbackUrl = prop.getProperty("callbackUrl");
        this.client = new OAuthSignPostOKHttpClient(prop, new OAuthSignPostOKHttpClient.OAuthTokenHandler() {

            //@todo implement interface and ovverride here
            public void onReceivedRequestToken(OkHttpOAuthConsumer consumer, String authorizeUrl) {
                if (consumer != null)

                {
                    OAuthBaseClient.this.editor.putString("request_token", consumer.getToken());
                    OAuthBaseClient.this.editor.putString("request_token_secret", consumer.getTokenSecret());
                    OAuthBaseClient.this.editor.commit();
                }
                //open the authorize intent view with intent data set in filter
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(authorizeUrl + consumer.getToken() + "&perms=delete"));
                if (OAuthBaseClient.this.requestIntentFlags != -1) {
                    intent.setFlags(OAuthBaseClient.this.requestIntentFlags);

                }
                //OAuthBaseClient.this.client.setConsumer(consumer);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                OAuthBaseClient.this.context.startActivity(intent);
            }

            public void onReceivedAccessToken(OkHttpOAuthConsumer consumer) {

                OAuthBaseClient.this.client.setAccessToken(new Token(consumer.getToken(), consumer.getTokenSecret()));
                OAuthBaseClient.this.editor.putString("oauth_token", consumer.getToken());
                OAuthBaseClient.this.editor.putString("oauth_token_secret", consumer.getTokenSecret());
                OAuthBaseClient.this.editor.commit();


                OAuthBaseClient.this.accessHandler.onLoginSuccess(consumer, prop.getProperty("baseUrl"));
            }

            public void onFailure(Exception e) {
                OAuthBaseClient.this.accessHandler.onLoginFailure(e);
            }
        });
        this.context = context;
        this.prefs = this.context.getSharedPreferences("OAuth_" + this.getClass().getSimpleName() + "_Prefs", 0);
        this.editor = this.prefs.edit();
        if (this.checkAccessToken() != null) {
            this.client.setAccessToken(this.checkAccessToken());
        }

    }

    public void connect() {
        this.client.fetchRequestToken();
    }

    public void authorize(Uri uri) {
        // this.accessHandler = handler;
        if (this.checkAccessToken() == null && uri != null) {
            String uriServiceCallback = uri.getScheme() + "://" + uri.getHost();
            if (uriServiceCallback.equals(this.callbackUrl)) {
                this.client.fetchAccessToken(uri);
            }
        } else if (this.checkAccessToken() != null) {
            OAuthBaseClient.this.accessHandler.onLoginSuccess(this.getClient().getConsumer(), this.baseUrl);
        }

    }


    public Token checkAccessToken() {
        return this.prefs.contains("oauth_token") && this.prefs.contains("oauth_token_secret") ? new Token(this.prefs.getString("oauth_token", ""), this.prefs.getString("oauth_token_secret", "")) : null;
    }

    protected OAuthSignPostOKHttpClient getClient() {
        return this.client;
    }

    protected Token getRequestToken() {
        return (new Token(this.prefs.getString("request_token", ""), this.prefs.getString("request_token_secret", "")));
    }


    protected void setBaseUrl(String url) {
        this.baseUrl = url;
    }

    protected String getApiUrl(String path) {
        return this.baseUrl + "/" + path;
    }

    public void clearTokens() {
        this.client.setAccessToken((Token) null);
        this.editor.remove("oauth_token");
        this.editor.remove("oauth_token_secret");
        this.editor.remove("request_token");
        this.editor.remove("request_token_secret");

        this.editor.commit();
    }

    public boolean isAuthenticated() {
        return this.client.getAccessToken() != null;
    }

    public void setRequestIntentFlags(int flags) {
        this.requestIntentFlags = flags;
    }

    public interface OAuthAccessHandler {
        void onLoginSuccess(OkHttpOAuthConsumer consumer, String baseUrl);

        void onLoginFailure(Exception var1);
    }


}

