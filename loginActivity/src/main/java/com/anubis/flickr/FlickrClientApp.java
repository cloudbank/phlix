package com.anubis.flickr;

import android.content.Context;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class FlickrClientApp extends com.activeandroid.app.Application {
    private static Context context;

    public static FlickrClient getRestClient() {
        return (FlickrClient) FlickrClient.getInstance(FlickrClient.class,
                FlickrClientApp.context);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FlickrClientApp.context = this;

        // Create global configuration and initialize ImageLoader with this
        // configuration
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
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

        ImageLoader.getInstance().init(config);


    }
}
