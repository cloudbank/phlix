package com.anubis.flickr.util;

import android.util.Log;

import com.anubis.flickr.FlickrClientApp;
import com.anubis.flickr.models.Photos;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sabine on 10/11/16.
 */

public class NetworkUtil {

    private NetworkUtil() {

    }
    Subscription friendsSubscription;

    private static NetworkUtil instance;

    public static NetworkUtil getInstance() {
        if (null == instance) {
            instance = new NetworkUtil();
        }
        return instance;
    }

    public Observable<Photos> getPhotosSubscription(String id) {


        //use picasso cache if there try with setting and see if works as advertised
        //stackoverflow claim that it needs to be set in policy
        //SA
        //..run it in own process
        // remember to run in main thread in syncer
        //concat the tags

        return FlickrClientApp.getJacksonService().getFriendsPhotos(id);


    }


    public void getFriendsList() {


        //use picasso cache if there try with setting and see if works as advertised
        //stackoverflow claim that it needs to be set in policy
        //SA
        //..run it in own process
        // remember to run in main thread in syncer
        //concat the tags\
        //@todo refactor to get the username w response from oauth kit



            friendsSubscription = FlickrClientApp.getJacksonService().getFriendsPhotos("1")
                    .subscribeOn(Schedulers.io()) // optional if you do not wish to override the default behavior
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Photos>() {
                        @Override
                        public void onCompleted() {
                            //Log.d("DEBUG","oncompleted");

                        }

                        @Override
                        public void onError(Throwable e) {
                            // cast to retrofit.HttpException to get the response code
                            if (e instanceof HttpException) {
                                HttpException response = (HttpException) e;
                                int code = response.code();
                                Log.e("ERROR", String.valueOf(code));
                            }
                            Log.e("ERROR", "error getting friends/photos" + e);
                        }

                        @Override
                        public void onNext(Photos p) {
                            // Log.d("DEBUG","mlogin: "+ u.getUser().getUsername().getContent());
                            //pass photos to fragment
                           // mPhotos = p;
                        }
                    });

        }


    }




