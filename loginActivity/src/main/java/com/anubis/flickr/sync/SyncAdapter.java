package com.anubis.flickr.sync;

/**
 * Created by sabine on 10/6/16.
 */

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.anubis.flickr.FlickrClientApp;
import com.anubis.flickr.R;
import com.anubis.flickr.activity.LoginActivity;
import com.anubis.flickr.models.Photo;
import com.anubis.flickr.models.Photos;
import com.anubis.flickr.models.Tag;
import com.anubis.flickr.models.User;
import com.anubis.flickr.models.UserInfo;
import com.anubis.flickr.models.UserModel;
import com.anubis.flickr.models.Who;
import com.anubis.flickr.util.Util;

import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

import static com.anubis.flickr.FlickrClientApp.getJacksonService;

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    // Global variables
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;
    public static final int SYNC_INTERVAL = 60 * 180;  //@todo change to 23 hrs
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;
    private static final int WEATHER_NOTIFICATION_ID = 3004;
    Realm realm;
    Subscription loginSubscription, friendsSubscription, interestingSubscription;

    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    /*
     * Specify the code you want to run in the sync adapter. The entire
     * sync adapter runs in a background thread, so you don't have to set
     * up your own background processing.
     */
    @Override
    public void onPerformSync(
            Account account,
            Bundle extras,
            String authority,
            ContentProviderClient provider,
            SyncResult syncResult) {
    /*
     * Put the data transfer code here.
     */
        /*
        Handler handler = new Handler(Looper.getMainLooper());

        handler.post(new Runnable() {

            @Override
            public void run() {
                //Your UI code here
                Toast.makeText(FlickrClientApp.getAppContext(), "Hiney, hiney, hiney", Toast.LENGTH_SHORT).show();
            }
        });
        */
        //getFriendsPhotos();//add tags
        Log.d("SYNC", "starting onPerformSync");
        getLoginAndFriends();
        //getInterestingPhotos();
        //getRecent and Hotags
        //get Commons --> this should not need update
        notifyWeather();
        Log.d("SYNC", "onPeformSync");

    }
/*

    */

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();

    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = FlickrClientApp.AUTHORITY;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     *
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                FlickrClientApp.AUTHORITY, bundle);
        Log.d("SYNC", "sync request");
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                FlickrClientApp.ACCOUNT, FlickrClientApp.ACCOUNT_TYPE);

        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {

        /*
         * Add the account and account type, empty password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
            //Log.d("SYNC", "about to call onACCOUNT");
            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        SyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, FlickrClientApp.AUTHORITY, true);

        /*
         * Finally, let's do a sync to get things started--
         * NOT NEEDED @todo
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

    private void notifyWeather() {
        /*
        //Context context = getContext();
        //checking the last update and notify if it' the first of the day
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String displayNotificationsKey = context.getString(R.string.pref_enable_notifications_key);
        boolean displayNotifications = prefs.getBoolean(displayNotificationsKey,
                Boolean.parseBoolean(context.getString(R.string.pref_enable_notifications_default)));

        if ( displayNotifications ) {

            String lastNotificationKey = context.getString(R.string.pref_last_notification);
            long lastSync = prefs.getLong(lastNotificationKey, 0);

            if (System.currentTimeMillis() - lastSync >= DAY_IN_MILLIS) {
                // Last sync was more than 1 day ago, let's send a notification with the weather.
                String locationQuery = Utility.getPreferredLocation(context);

                Uri weatherUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(locationQuery, System.currentTimeMillis());

                // we'll query our contentProvider, as always
                Cursor cursor = context.getContentResolver().query(weatherUri, NOTIFY_WEATHER_PROJECTION, null, null, null);

                if (cursor.moveToFirst()) {
                    int weatherId = cursor.getInt(INDEX_WEATHER_ID);
                    double high = cursor.getDouble(INDEX_MAX_TEMP);
                    double low = cursor.getDouble(INDEX_MIN_TEMP);
                    String desc = cursor.getString(INDEX_SHORT_DESC);

                    int iconId = Utility.getIconResourceForWeatherCondition(weatherId);
                    Resources resources = context.getResources();
                    Bitmap largeIcon = BitmapFactory.decodeResource(resources,
                            Utility.getArtResourceForWeatherCondition(weatherId));
                    String title = context.getString(R.string.app_name);

                    // Define the text of the forecast.
                    String contentText = String.format(context.getString(R.string.format_notification),
                            desc,
                            Utility.formatTemperature(context, high),
                            Utility.formatTemperature(context, low));
*/
        // NotificationCompatBuilder is a very convenient way to build backward-compatible
        // notifications.  Just throw in some data.
        Context context = FlickrClientApp.getAppContext();
        int iconId = R.drawable.ic_launcher;
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setColor(context.getResources().getColor(R.color.SkyBlue))
                        .setSmallIcon(iconId)
                        .setContentTitle("photos")
                        .setContentText("photo data has been updated");

        // Make something interesting happen when the user clicks on the notification.
        // In this case, opening the app is sufficient.
        Intent resultIntent = new Intent(context, LoginActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // WEATHER_NOTIFICATION_ID allows you to update the notification later on.
        mNotificationManager.notify(WEATHER_NOTIFICATION_ID, mBuilder.build());

        //refreshing last sync
        // SharedPreferences.Editor editor = prefs.edit();
        // editor.putLong(lastNotificationKey, System.currentTimeMillis());
        //editor.commit();
    }


    private void getLoginAndFriends() {
        loginSubscription = FlickrClientApp.getJacksonService().testLogin()
                .concatMap(new Func1<User, Observable<UserInfo>>() {
                    @Override
                    public Observable<UserInfo> call(User user) {
                        String username = user.getUser().getUsername().getContent();
                        String prevUser = Util.getCurrentUser();
                        SharedPreferences.Editor editor = Util.getUserPrefs().edit();

                        editor.putString(FlickrClientApp.getAppContext().getResources().getString(R.string.previous_user), prevUser);
                        editor.putString(FlickrClientApp.getAppContext().getString(R.string.current_user), username);
                        editor.putString(FlickrClientApp.getAppContext().getString(R.string.user_id), user.getUser().getUserId());
                        editor.commit();
                        Observable<Who> tagsObservable = FlickrClientApp.getJacksonService().getTags(user.getUser().getUserId());
                        return FlickrClientApp.getJacksonService().getFriendsPhotos(user.getUser().getUserId()).zipWith(tagsObservable, new Func2<Photos, Who, UserInfo>() {

                            @Override
                            public UserInfo call(Photos p, Who w) {
                                return new UserInfo(w, p);
                            }

                        });
                    }
                }).subscribeOn(Schedulers.immediate()) // no thread pool; bg
                .observeOn(Schedulers.immediate())
                .subscribe(new Subscriber<UserInfo>() {
                    @Override
                    public void onCompleted() {
                        Handler handler = new Handler(Looper.getMainLooper());

                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                //Your UI code here
                                Toast.makeText(FlickrClientApp.getAppContext(), "Got our friends", Toast.LENGTH_SHORT).show();
                            }
                        });

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
                        Log.e("ERROR", "error getting login" + e);
                        //signout
                    }

                    @Override
                    public void onNext(UserInfo userInfo) {

                        //add photos to real

                        //if (username.length() == 0) {
                        //throw new Exception("username is not set")
                        //stop the sync adapter and remove account
                        //try to sign out gracefully

                        Photos photos = userInfo.getFriends();
                        Who w = userInfo.getWho();
                        Log.d("SYNC&&&", "who id" + w.getWho().getId());
                        //f.user.username.content =
                        List<Tag> tags = w.getWho().getTags().getTag();
                        // }
                        realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        String id = w.getWho().getId();

                        UserModel u = null;
                        u = realm.where(UserModel.class).equalTo("id", id).findFirst();
                        Log.d("SYNC&&&^^", "user in realm" + u);
                        if (null == u) {
                            u = realm.createObject(UserModel.class, id);
                        }
                        //f.user = username;  cannot reset primary key even if same
                        RealmList flist = u.getFriendsList();  //managed
                        for (Photo p : photos.getPhotos().getPhotoList()) {
                            flist.add(p);
                        }
                        RealmList tagsList = u.getTagsList();
                        for (Tag t : tags) {
                            tagsList.add(t);
                        }
                        Log.d("SYNC&&&", "flist.size" + flist.size());
                        Log.d("SYNC&&&**", "u" + u);
                        //f.user.username.content =
                        u.name = Util.getCurrentUser();
                        u.timestamp = Calendar.getInstance().getTime();
                        realm.copyToRealmOrUpdate(u);  //deep copy
                        realm.commitTransaction();
                        realm.close();
                        Log.d("DEBUG", "end get userinfo: " + photos);
                    }

                });


    }


    public void getInterestingPhotos() {
        //@todo offline mode
        //@TODO need iterableFLATMAP TO GET ALL PAGES
        interestingSubscription = getJacksonService().getInterestingPhotos("1")

                .subscribeOn(AndroidSchedulers.mainThread()) // optional if you do not wish to override the default behavior
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Photos>() {
                    @Override
                    public void onCompleted() {


                        Log.d("DEBUG", "oncompleted interesting");

                    }

                    @Override
                    public void onError(Throwable e) {
                        // cast to retrofit.HttpException to get the response code
                        if (e instanceof HttpException) {
                            HttpException response = (HttpException) e;
                            int code = response.code();
                            Log.e("ERROR", String.valueOf(code));
                        }
                        Log.e("ERROR", "error getting interesting photos" + e);
                    }

                    @Override
                    public void onNext(Photos p) {
                        Log.d("DEBUG", "onNext interesting: " + p);
                        //pass photos to fragment
                    }
                });

    }
}






