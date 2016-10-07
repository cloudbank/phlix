package com.anubis.flickr;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.Context;

import com.anubis.flickr.service.FlickrService;
import com.anubis.flickr.service.ServiceGenerator;
import com.facebook.stetho.Stetho;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.converter.jackson.JacksonConverterFactory;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;

public class FlickrClientApp extends Application {


    private static Context context;
    private static FlickrService service;
    private static FlickrClientApp instance;
    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "com.anubis.flickr.provider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "example.com";
    // The account name
    public static final String ACCOUNT = "dummyaccount";
    // Instance fields
    Account mAccount;

    public static FlickrClientApp getInstance() {
        return instance;
    }

    public static FlickrService getService() {
        return (FlickrService) service;
    }

    public static void setService(OkHttpOAuthConsumer consumer, String baseUrl) {
        service = ServiceGenerator.createRetrofitRxService(consumer, FlickrService.class,baseUrl, JacksonConverterFactory.create());
    }

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
        FlickrClientApp.context = this;
        Stetho.initializeWithDefaults(this);
        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);
        //TypefaceUtil.setDefaultFont(this, "SERIF", "fonts/Exo-Medium.otf");



    }


}
