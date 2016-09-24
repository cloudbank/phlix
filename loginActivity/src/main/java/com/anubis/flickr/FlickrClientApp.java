package com.anubis.flickr;

import android.content.Context;

import com.anubis.flickr.service.FlickrService;
import com.anubis.flickr.service.ServiceGenerator;
import com.facebook.stetho.Stetho;

import retrofit2.converter.jackson.JacksonConverterFactory;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;

public class FlickrClientApp extends com.activeandroid.app.Application {


    private static Context context;
    private static FlickrService service;
    private static FlickrClientApp instance;

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
        FlickrClientApp.context = this;
        Stetho.initializeWithDefaults(this);


        // Create global configuration and initialize ImageLoader with this
        // configuration
       /* DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory()
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheOnDisc()
                .showStubImage(android.R.drawable.btn_star)
                .resetViewBeforeLoading()
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext()).defaultDisplayImageOptions(defaultOptions)
                // .threadPriority(Thread.NORM_PRIORITY-2)
                .build();

        ImageLoader.getInstance().init(config); */


    }


}
