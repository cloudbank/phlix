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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.anubis.flickr.FlickrClientApp;
import com.anubis.flickr.R;
import com.anubis.flickr.activity.ImageDisplayActivity;
import com.anubis.flickr.adapter.FriendsAdapter;
import com.anubis.flickr.adapter.PhotoArrayAdapter;
import com.anubis.flickr.models.Photo;
import com.anubis.flickr.models.Tag;
import com.anubis.flickr.models.UserModel;
import com.anubis.flickr.util.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import co.hkm.soltag.TagContainerLayout;
import io.realm.Realm;
import io.realm.RealmChangeListener;

public class FriendsFragment extends FlickrBaseFragment {

    private String  mUserId, mPreviousUser;

    private List<Photo> mPhotos, cPhotos;

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
    Realm userRealm, r;
    public UserModel mUser;
    RealmChangeListener changeListener;
    RadioGroup rg;
    RadioButton rb1, rb5;
    View view;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //this gets called along w lifecycle when vp recycles fragment ie  commons tab


        changeListener = new RealmChangeListener<UserModel>()

        {
            @Override
            public void onChange(UserModel u) {
                // This is called anytime the Realm database changes on any thread.
                // Please note, change listeners only work on Looper threads.
                // For non-looper threads, you manually have to use Realm.waitForChange() instead.

                //have to redraw the view
                view.invalidate();
                rb1.setChecked(false);
                rb5.setChecked(true);
                updateDisplay(u);
            }
        };


        userRealm = Realm.getDefaultInstance();


        ringProgressDialog.setTitle("Please wait");
        ringProgressDialog.setMessage("Retrieving friend photos");
        ringProgressDialog.setCancelable(true);
        ringProgressDialog.show();
        mUserId = Util.getUserId();
        Log.d("USER ID", "id: " + mUserId);


        mUser = userRealm.where(UserModel.class).equalTo("userId", mUserId).findFirst();
        //init is running slow
        //@todo add separate realms for rest
        r = Realm.getDefaultInstance();
        if (null == mUser) {

            RealmChangeListener realmListener = new RealmChangeListener<Realm>() {
                @Override
                public void onChange(Realm r) {
                    updateDisplay();
                }
            };
            r.addChangeListener(realmListener);

        } else {
            Log.d("USER PRESENT", "user: " + mUser);
            mUser.addChangeListener(changeListener);
            updateDisplay(mUser);
            if (null != r) {
                r.removeAllChangeListeners();
                r.close();
            }


        }
        ringProgressDialog.dismiss();
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio1) {
                    //some code

                    makeSingle(cPhotos);
                    fAdapter.notifyDataSetChanged();
                    Toast.makeText(FlickrClientApp.getAppContext(), "Howza1", Toast.LENGTH_SHORT).show();

                } else if (checkedId == R.id.radio5) {
                    //some code
                    Toast.makeText(FlickrClientApp.getAppContext(), "Howza5", Toast.LENGTH_SHORT).show();
                   updateDisplay();

                }

            }
        });

    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mPhotos = new ArrayList<Photo>();
        cPhotos = new ArrayList<Photo>();
        mTags = new ArrayList<Tag>();
        fAdapter = new FriendsAdapter(getActivity(), mPhotos, false);
        ringProgressDialog = new ProgressDialog(getActivity(), R.style.CustomProgessBarStyle);
        this.prefs = Util.getUserPrefs();
        this.editor = this.prefs.edit();
        //slow right now--get it to work
        //@todo move shared prefs code to User
        //until hack the oauth to get full response w mUsername and userId


        // getLoginAndId();


        //getTags();


        setRetainInstance(true);


    }


    void customLoadMoreDataFromApi(int page) {
        //@todo add the endless scroll or take it out

    }

    void getFriends(String username) {

        if (null == mUser) {
            //first login for app or user

            //@todo if !isInit cancel sync
            // ContentResolver.cancelSync();
            //need to get mUser getPhotos

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

                //there is no change to userRealm
                updateDisplay(mUser);

                //((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(username);

            } else if (checkTime(mUser.timestamp)) {  //24 hrs
                //stale but logged in mUser @todo need to get mUser getPhotos 24 hrs

                try {
                    // getPhotos();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                // same user good w/in 24 hrs
                //((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(username);
                // not stale isLoggedin don't need to get mUser or update anything except toolbar
            }

        }


    }


    private void updateDisplay(UserModel u) {
        mPhotos.clear();
        cPhotos.clear();
        displayTags(u.getTagsList());

        cPhotos.addAll(u.getFriendsList());
        mPhotos.addAll(u.getFriendsList());
        fAdapter.notifyDataSetChanged();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(Util.getCurrentUser());


    }

    private void updateDisplay() {
        cPhotos.clear();
        mPhotos.clear();
        UserModel u = userRealm.where(UserModel.class).equalTo("userId", Util.getUserId()).findFirst();
        displayTags(u.getTagsList());

        cPhotos.addAll(u.getFriendsList());

        mPhotos.addAll(u.getFriendsList());
        fAdapter.notifyDataSetChanged();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(Util.getCurrentUser());


    }


    private boolean checkTime(Date timestamp) {
        Calendar cachedTime = Calendar.getInstance();
        cachedTime.setTime(timestamp);
        cachedTime.add(Calendar.HOUR, 23);
        return (Calendar.getInstance().getTime()).after(cachedTime.getTime());
    }


    public void displayTags(List<Tag> tags) {
        //tags.stream().map(it -> it.getContent()).collect(Collectors.toCollection())
        //when android catches up to 1.8
        mTagView.removeAllTags();
        for (Tag t : tags) {

            mTagView.addTag(t.getContent());
        }


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!userRealm.isClosed()) {
            userRealm.close();
        }

        if (null != this.ringProgressDialog) {
            this.ringProgressDialog = null;
        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friends, container,
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
                Photo photo = mPhotos.get(position);
                intent.putExtra(RESULT, photo.getId());
                startActivity(intent);
                //Toast.makeText(getActivity(), title + " was clicked!", Toast.LENGTH_SHORT).show();
            }
        });
        mTagView = (TagContainerLayout) view.findViewById(R.id.my_tag_group);

        rg = (RadioGroup) view.findViewById(R.id.radioGroup1);
        rb1 = (RadioButton) view.findViewById(R.id.radio1);
        rb5 = (RadioButton) view.findViewById(R.id.radio5);

        setHasOptionsMenu(true);
        return view;
    }


    private void makeSingle(List<Photo> p) {
        mPhotos.clear();
        String current = "";

        for (int i = 0; i < p.size(); i++) {
            if (!current.equals(p.get(i).getOwnername())) {
                mPhotos.add(p.get(i));
                current = p.get(i).getOwnername();

            }
        }


    }


}
