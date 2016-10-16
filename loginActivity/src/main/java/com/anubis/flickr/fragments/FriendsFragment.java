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
import com.anubis.flickr.models.Tag;
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
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
        mUsername = prefs.getString(FlickrClientApp.getAppContext().getResources().getString(R.string.current_user), "");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(mUsername);
        Log.d("onactivitycreated ff", mUsername);
        friendsRealm = Realm.getDefaultInstance();
        ringProgressDialog = new ProgressDialog(getActivity(), R.style.CustomProgessBarStyle);
        Log.d("REALM", "path: " + friendsRealm.getPath());
        ringProgressDialog.setTitle("Please wait");
        ringProgressDialog.setMessage("Retrieving friend photos");
        ringProgressDialog.setCancelable(true);
        ringProgressDialog.show();
        mFriends = friendsRealm.where(Friends.class).equalTo("user", mUsername).findFirst();

        while (mFriends == null   ) {

            mFriends = friendsRealm.where(Friends.class).equalTo("user", mUsername).findFirst();

        }

        Log.d("friends size ", String.valueOf(mFriends.getFriends().size()));
        ringProgressDialog.dismiss();

        changeListener = new RealmChangeListener<Friends>()

        {
            @Override
            public void onChange(Friends f) {
                // This is called anytime the Realm database changes on any thread.
                // Please note, change listeners only work on Looper threads.
                // For non-looper threads, you manually have to use Realm.waitForChange() instead.
                updateDisplay(f);
            }
        }

        ;

        mFriends.addChangeListener(changeListener);
        //may just be query without sync adapter change
        Log.d("onactivitycreated ff", mUsername);

        updateDisplay(mFriends);


    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mPhotos = new ArrayList<Photo>();
        mTags = new ArrayList<Tag>();
        fAdapter = new FriendsAdapter(getActivity(), mPhotos, false);

        this.prefs = Util.getUserPrefs();
        this.editor = this.prefs.edit();
        //slow right now--get it to work
        //@todo move shared prefs code to User
        //until hack the oauth to get full response w mUsername and id


        // getLoginAndId();


        getTags();


        setRetainInstance(true);


    }


    void customLoadMoreDataFromApi(int page) {
        //@todo add the endless scroll or take it out

    }

    void getFriends(String username) {

        if (null == mFriends) {
            //first login for app or user

            //@todo if !isInit cancel sync
            // ContentResolver.cancelSync();
            //need to get mFriends getPhotos

            try {
                //getPhotos();
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

                //((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(username);

            } else if (checkTime(mFriends.timestamp)) {  //24 hrs
                //stale but logged in mFriends @todo need to get mFriends getPhotos 24 hrs

                try {
                    // getPhotos();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                // same user good w/in 24 hrs
                //((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(username);
                // not stale isLoggedin don't need to get mFriends or update anything except toolbar
            }

        }


    }


    private void updateDisplay(Friends f) {
        mPhotos.addAll(f.getFriends());
        fAdapter.notifyDataSetChanged();
    }


    private boolean checkTime(Date timestamp) {
        Calendar cachedTime = Calendar.getInstance();
        cachedTime.setTime(timestamp);
        cachedTime.add(Calendar.HOUR, 23);
        return (Calendar.getInstance().getTime()).after(cachedTime.getTime());
    }


    private void getTags() {
        String uid = prefs.getString(FlickrClientApp.getAppContext().getResources().getString(R.string.user_id), "");
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
