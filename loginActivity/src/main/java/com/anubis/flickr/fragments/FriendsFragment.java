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
import io.realm.RealmList;
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
    Realm realm;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //syncadapter is too slow for realm query
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
                        Log.e("ERROR", "error getting login/photos" + e);
                    }

                    @Override
                    public void onNext(User user) {
                        Log.d("DEBUG", "mlogin: " + user);
                        //add photos to real
                        mUsername = user.getUser().getUsername().getContent();
                        mPreviousUser = Util.getCurrentUser();

                        editor.putString(getContext().getResources().getString(R.string.previous_user), mPreviousUser);
                        editor.putString(getContext().getResources().getString(R.string.current_user), mUsername);
                        editor.putString(getContext().getResources().getString(R.string.user_id), user.getUser().getId());
                        editor.commit();
                    }
                });

    }


    void customLoadMoreDataFromApi(int page) {
        //@todo add the endless scroll

    }

    void getFriends(String username) {
        ringProgressDialog.setTitle("Please wait");
        ringProgressDialog.setMessage("Retrieving friend photos");
        ringProgressDialog.setCancelable(true);
        ringProgressDialog.show();

        Friends f;
        realm = Realm.getDefaultInstance();
        //----->  entry in realm
        f = realm.where(Friends.class).equalTo("user.mUsername.content", username).findFirst();
        realm.close();
        if (null == f) {
            //first login for app or user

            //@todo if !isInit cancel sync
            // ContentResolver.cancelSync();
            //need to get f getPhotos
            getPhotos();
        } else {
//----->  diff entry in prefs,
            if (Util.isNewUser(mPreviousUser)) {
                //new user  w previous login  but current list
                //ContentResolver.cancelSync();
                //@todo don't need to get photos,
                updateDisplay(f);

                ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(username);
                //----->  stale entry in realm,
            } else if (checkTime(f.timestamp)) {  //24 hrs
                //stale but logged in f @todo need to get f getPhotos 24 hrs

                getPhotos();
                //
            } else {

                ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(username);
                // not stale isLoggedin don't need to get f or update anything except toolbar
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
                f.addChangeListener(new RealmChangeListener<Friends>()

        {
            @Override
            public void onChange (Friends results){
            // Query results are updated in real time
            mPhotos.addAll(f.getFriends());
            fAdapter.notifyDataSetChanged();
        }
        }

        );
      */
    private boolean checkTime(Date timestamp) {

        Calendar cal = Calendar.getInstance()
        cal.setTime(cal.getTime());
        cal.add(Calendar.DATE, 1);
        //yourDate = cal.getTime();

    }


    private void getPhotos() {


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
        String userId ="";
        subscription = FlickrClientApp.getJacksonService().getFriendsPhotos(userId)


                .subscribeOn(Schedulers.io()) // optional if you do not wish to override the default behavior
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
                        Log.e("ERROR", "error getting login/photos" + e);
                    }

                    @Override
                    public void onNext(Photos p) {
                        Log.d("DEBUG", "mlogin: " + p);
                        //add photos to real
                        realm = Realm.getDefaultInstance();
                        realm.beginTransaction();

                        String username = Util.getUserPrefs().getString(FlickrClientApp.getAppContext().getResources().getString(R.string.current_user), "");
                        Friends f = realm.where(Friends.class).equalTo("user.mUsername.content", username).findFirst();
                        if (null == f) {
                            f = realm.createObject(Friends.class, username);
                        }
                        //User_ u =
                        //@todo  create User_ and persist here if needed
                        f.user = username;
                        RealmList friendsList = f.getFriends();
                        friendsList.addAll(p.getPhotos().getPhotoList());
                        f.friends = friendsList;

                        f.timestamp = Calendar.getInstance().getTime();
                        realm.copyToRealmOrUpdate(f);

                        realm.commitTransaction();
                        realm.close();

                        mPhotos.addAll(p.getPhotos().getPhotoList());
                        fAdapter.notifyDataSetChanged();
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
        realm.close();
        if (null != this.subscription) {
            this.subscription.unsubscribe();
        }
        this.ringProgressDialog = null;


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
