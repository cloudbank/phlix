package com.anubis.flickr.network;

import android.content.ContentResolver;
import android.content.SharedPreferences;

import com.anubis.flickr.FlickrClientApp;
import com.anubis.flickr.R;
import com.anubis.flickr.models.Photos;
import com.anubis.flickr.models.User;
import com.anubis.flickr.sync.SyncAdapter;
import com.anubis.flickr.util.Util;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by sabine on 10/11/16.
 */

public class NetworkUtil {

    private NetworkUtil() {

    }

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


    public Observable<Photos> getFriendsList() {


        //use picasso cache if there try with setting and see if works as advertised
        //stackoverflow claim that it needs to be set in policy
        //SA
        //..run it in own process
        // remember to run in main thread in syncer
        //concat the tags\
        //@todo refactor to get the username w response from oauth kit

        return FlickrClientApp.getJacksonService().testLogin()
                .concatMap(new Func1<User, Observable<Photos>>() {
                    @Override
                    public Observable<Photos> call(User user) {
                        String username = user.getUser().getUsername().getContent();
                        SharedPreferences.Editor editor = Util.getUserPrefs().edit();
                        if (Util.isNewUser(username)) {
                            //clear the caches, prefs, lists, realm, stop the sync!, reset the toolbar
                            //@todo
                            if (!Util.isInit()) {
                                ContentResolver.cancelSync(SyncAdapter.getSyncAccount(FlickrClientApp.getAppContext()), FlickrClientApp.AUTHORITY);
                            }

                            editor.putString(FlickrClientApp.getAppContext().getResources().getString(R.string.current_user), username);

                        }

                        editor.commit();
                        //save User to realm
                        return FlickrClientApp.getJacksonService().getFriendsPhotos(user.getUser().getId());

                    }
                });

    }

}


