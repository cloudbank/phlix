package com.anubis.flickr.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anubis.flickr.FlickrClientApp;
import com.anubis.flickr.R;
import com.anubis.flickr.activity.ImageDisplayActivity;
import com.anubis.flickr.adapter.FriendsAdapter;
import com.anubis.flickr.adapter.PhotoArrayAdapter;
import com.anubis.flickr.models.Friends;
import com.anubis.flickr.models.Photo;
import com.anubis.flickr.models.Photos;
import com.anubis.flickr.models.Tag;
import com.anubis.flickr.models.User;
import com.anubis.flickr.models.Who;
import com.anubis.flickr.util.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import co.hkm.soltag.TagContainerLayout;
import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.anubis.flickr.R.id.username;

public class FriendsFragment extends FlickrBaseFragment {

    private Subscription subscription;
    private String mUsername, mUserId, mPreviousUser;

    private List<Photo> mPhotos;

    protected PhotoArrayAdapter getAdapter() {
        return mAdapter;
    }

    ProgressDialog ringProgressDialog;
    FriendsAdapter fAdapter;
    RecyclerView rvPhotos;
    List<Tag> mTags;
    protected SharedPreferences prefs;
    protected SharedPreferences.Editor editor;
    TagContainerLayout mTagView;
    Realm friendsRealm;
    public Friends mFriends;
    RealmChangeListener changeListener;
    RealmAsyncTask transaction;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mPhotos = new ArrayList<Photo>();
        mTags = new ArrayList<Tag>();
        fAdapter = new FriendsAdapter(getActivity(), mPhotos, false);
        ringProgressDialog = new ProgressDialog(getActivity(), R.style.CustomProgessBarStyle);
        this.prefs = Util.getUserPrefs();
        this.editor = this.prefs.edit();
        //slow right now--get it to work
        //@todo move shared prefs code to User
        //until hack the oauth to get full response w mUsername and id
        friendsRealm = Realm.getDefaultInstance();
        // get all the customers

        // ... build a list adapter and set it to the ListView/RecyclerView/etc
        mFriends = friendsRealm.where(Friends.class).equalTo("user.mUsername.content", username).findFirst();
        // set up a Realm change listener
        changeListener = new RealmChangeListener<Friends>() {
            @Override
            public void onChange(Friends f) {
                // This is called anytime the Realm database changes on any thread.
                // Please note, change listeners only work on Looper threads.
                // For non-looper threads, you manually have to use Realm.waitForChange() instead.
                updateDisplay(f);
            }
        };
        // Tell Realm to notify our listener when the customers results
        // have changed (items added, removed, updated, anything of the sort).
        mFriends.addChangeListener(changeListener);
        //syncadapter is too slow for friendsRealm query

        getLoginAndId();


        getTags();


        setRetainInstance(true);


    }

    private void getLoginAndId() {

        subscription = FlickrClientApp.getJacksonService().testLogin()
                .subscribeOn(Schedulers.io()) // optional if you do not wish to override the default behavior
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {

                        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(mUsername);
                        //Log.d("DEBUG","oncompleted");
                        getFriends(mUsername);
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
                    public void onNext(User user) {
                        Log.d("DEBUG", "mlogin: " + user);
                        //add photos to real
                        mUsername = user.getUser().getUsername().getContent();
                        mPreviousUser = Util.getCurrentUser();

                        editor.putString(FlickrClientApp.getAppContext().getResources().getString(R.string.previous_user), mPreviousUser);
                        editor.putString(FlickrClientApp.getAppContext().getString(R.string.current_user), mUsername);
                        editor.putString(FlickrClientApp.getAppContext().getString(R.string.user_id), user.getUser().getId());
                        editor.commit();
                    }
                });

    }


    void customLoadMoreDataFromApi(int page) {
        //@todo add the endless scroll

    }

    void getFriends(String username) {


        if (null == mFriends) {
            //first login for app or user

            //@todo if !isInit cancel sync
            // ContentResolver.cancelSync();
            //need to get mFriends getPhotos

            try {
                getPhotos();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            if (Util.isNewUser(mPreviousUser)) {
                //new user  w previous login  but current list
                //ContentResolver.cancelSync();
                //@todo don't need to get photos,

                //there is no change to friendsRealm
                updateDisplay(mFriends);

                ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(username);

            } else if (checkTime(mFriends.timestamp)) {  //24 hrs
                //stale but logged in mFriends @todo need to get mFriends getPhotos 24 hrs

                try {
                    getPhotos();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                // same user good w/in 24 hrs
                ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(username);
                // not stale isLoggedin don't need to get mFriends or update anything except toolbar
            }

        }


    }


    private void updateDisplay(Friends f) {
        mPhotos.addAll(f.getFriends());
        fAdapter.notifyDataSetChanged();
    }

    /*
        // Tell Realm to notify our listener when the friends results
        / have changed (items added, removed, updated, anything of the sort).
                mFriends.addChangeListener(new RealmChangeListener<Friends>()

        {
            @Override
            public void onChange (Friends results){
            // Query results are updated in real time
            mPhotos.addAll(mFriends.getFriends());
            fAdapter.notifyDataSetChanged();
        }
        }

        );
      */
    private boolean checkTime(Date timestamp) {
        Calendar cachedTime = Calendar.getInstance();
        cachedTime.setTime(timestamp);
        cachedTime.add(Calendar.HOUR, 23);
        return (Calendar.getInstance().getTime()).after(cachedTime.getTime());
    }


    private void getPhotos() throws Exception {


        ringProgressDialog.setTitle("Please wait");
        ringProgressDialog.setMessage("Retrieving friend photos");
        ringProgressDialog.setCancelable(true);
        ringProgressDialog.show();

        //use picasso cache if there try with setting and see if works as advertised
        //stackoverflow claim that it needs to be set in policy
        //SA
        //..run it in own process
        // remember to run in main thread in syncer
        //concat the tags
        String userId = prefs.getString(FlickrClientApp.getAppContext().getResources().getString(R.string.user_id), "");
        if (userId.length() == 0) {
            Log.e("ERROR", "user id is not set");
            throw new Exception("No user id in prefs");

        }
        subscription = FlickrClientApp.getJacksonService().getFriendsPhotos(userId)


                .subscribeOn(Schedulers.io()) // runs with thread pool
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Photos>() {
                    @Override
                    public void onCompleted() {

                        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(mUsername);
                        ringProgressDialog.dismiss();
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
                        Log.d("DEBUG", "mlogin: " + p);
                        //add photos to real
                        //@todo run in bg thread
                        updateFriendsList(p);


                        // mPhotos.addAll(p.getPhotos().getPhotoList());
                        // fAdapter.notifyDataSetChanged();
                    }
                });

    }

    private void updateFriendsList(final Photos p) {
        transaction = friendsRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                String username = Util.getUserPrefs().getString(FlickrClientApp.getAppContext().getResources().getString(R.string.current_user), "");
                Friends f;
                if (null == mFriends) {
                    f = friendsRealm.createObject(Friends.class, username);
                    f.addChangeListener(changeListener);
                } else {
                    f = mFriends;
                }
                //User_ u =
                //@todo  create User_ and persist here if needed
                f.user = username;  //primary key
                RealmList friendsList = f.getFriends(); //managed
                friendsList.addAll(p.getPhotos().getPhotoList());
                f.friends = friendsList;

                updateDisplay(f);

                f.timestamp = Calendar.getInstance().getTime();
                friendsRealm.copyToRealmOrUpdate(f);

                //friendsRealm.commitTransaction();
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Transaction was a success.
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
            }
        });


    }


    private void getTags() {
        String uid = prefs.getString("id", "");
        subscription = FlickrClientApp.getJacksonService().getTags(uid)
                .subscribeOn(Schedulers.io()) // optional if you do not wish to override the default behavior
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Who>() {
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
                        Log.e("ERROR", "error getting tags" + e);
                    }

                    @Override
                    public void onNext(Who w) {
                        Log.d("DEBUG", "tags for user: " + w);
                        //pass photos to fragment
                        mTags.addAll(w.getWho().getTags().getTag());
                        displayTags(mTags);
                    }
                });

    }

    public void displayTags(List<Tag> tags) {
        //tags.stream().map(it -> it.getContent()).collect(Collectors.toCollection())
        //when android catches up to 1.8
        for (Tag t : tags) {
            mTagView.addTag(t.getContent());
        }


    }

    protected void loadPhotos() {
        // clearAdapter();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!friendsRealm.isClosed()) {
            friendsRealm.close();
        }
        if (null != this.subscription) {
            this.subscription.unsubscribe();
        }
        if (null != this.ringProgressDialog) {
            this.ringProgressDialog = null;
        }


    }


    // Use onStart()/onStop() for Fragments as onDestroy() might not be called.
    @Override
    public void onStop() {
        super.onStop();
        if (!friendsRealm.isClosed()) {
            friendsRealm.close();
        }
        if (transaction != null && !transaction.isCancelled()) {
            transaction.cancel();
        }
        if (null != this.subscription) {
            this.subscription.unsubscribe();
        }
        if (null != this.ringProgressDialog) {
            this.ringProgressDialog = null;
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container,
                false);

        rvPhotos = (RecyclerView) view.findViewById(R.id.rvPhotos);

        rvPhotos.setAdapter(fAdapter);
        // StaggeredGridLayoutManager gridLayoutManager =
        //new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        // Attach the layout manager to the recycler view
        //rvPhotos.setLayoutManager(gridLayoutManager);
        rvPhotos.setLayoutManager(new GridLayoutManager(FlickrClientApp.getAppContext(), 3));
        //SpacesItemDecoration decoration = new SpacesItemDecoration(2);
        //rvPhotos.addItemDecoration(decoration);

        // vPhotos.setOnItemClickListener(mListener);
        // vPhotos.setOnScrollListener(mScrollListener);
        fAdapter.setOnItemClickListener(new FriendsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // String title = mTags.get(position).getTitle();
                Intent intent = new Intent(getActivity(),
                        ImageDisplayActivity.class);
                Photo result = mPhotos.get(position);
                intent.putExtra(RESULT, result);
                startActivity(intent);
                //Toast.makeText(getActivity(), title + " was clicked!", Toast.LENGTH_SHORT).show();
            }
        });
        mTagView = (TagContainerLayout) view.findViewById(R.id.my_tag_group);
        setHasOptionsMenu(true);
        return view;
    }


}
