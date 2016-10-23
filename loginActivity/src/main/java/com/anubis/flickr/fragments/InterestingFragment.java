package com.anubis.flickr.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anubis.flickr.FlickrClientApp;
import com.anubis.flickr.R;
import com.anubis.flickr.activity.ImageDisplayActivity;
import com.anubis.flickr.adapter.InterestingAdapter;
import com.anubis.flickr.listener.EndlessRecyclerViewScrollListener;
import com.anubis.flickr.models.Interesting;
import com.anubis.flickr.models.Photo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;

public class InterestingFragment extends FlickrBaseFragment {
    InterestingAdapter rAdapter;
    RecyclerView rvPhotos;
    ProgressDialog ringProgressDialog;
   // private Subscription subscription;
    //@todo move up
    List<Photo> mInteresting = new ArrayList<Photo>();
    RealmChangeListener changeListener;
    Realm interestingRealm, r;


    @Override
    public void onResume() {
        super.onResume();
        Log.d("TABS","interesting onresume");
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //this gets called along w lifecycle when vp recycles fragment ie  commons tab

        Log.d("TABS","interesting activcreated");
        changeListener = new RealmChangeListener<Interesting>()

        {
            @Override
            public void onChange(Interesting i) {
                // This is called anytime the Realm database changes on any thread.
                // Please note, change listeners only work on Looper threads.
                // For non-looper threads, you manually have to use Realm.waitForChange() instead.
                updateDisplay(i);
            }
        };


        interestingRealm = Realm.getDefaultInstance();

        ringProgressDialog.setTitle("Please wait");
        ringProgressDialog.setMessage("Retrieving friend photos");
        ringProgressDialog.setCancelable(true);
        ringProgressDialog.show();
        Date maxDate = interestingRealm.where(Interesting.class).maximumDate("timestamp");
        Interesting interesting = interestingRealm.where(Interesting.class).equalTo("timestamp", maxDate).findFirst();
        if (null == interesting) {
            r = Realm.getDefaultInstance();
            RealmChangeListener realmListener = new RealmChangeListener<Realm>() {
                @Override
                public void onChange(Realm r) {
                    updateDisplay();
                }
            };
            r.addChangeListener(realmListener);

        } else {
            //init is running slow
            //@todo add separate realms for rest


            Log.d("INTERESTING PRESENT", "list: " + interesting);
            updateDisplay(interesting);
            interesting.addChangeListener(changeListener);
            if ( null != r) {
                r.removeAllChangeListeners();
                r.close();
            }


            //updateDisplay(interesting);

        }
        ringProgressDialog.dismiss();

    }


    private void updateDisplay() {
        Date maxDate = interestingRealm.where(Interesting.class).maximumDate("timestamp");
        Interesting i = interestingRealm.where(Interesting.class).equalTo("timestamp", maxDate).findFirst();
        mInteresting.clear();
        mInteresting.addAll(i.getInterestingPhotos());
        rAdapter.notifyDataSetChanged();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Log.d("TABS","interesting oncreate");
        ringProgressDialog = new ProgressDialog(getActivity(), R.style.MyDialogTheme);

        rAdapter = new InterestingAdapter(FlickrClientApp.getAppContext(), mInteresting, true);
        //loadPhotos(1, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interesting, container,
                false);
        rvPhotos = (RecyclerView) view.findViewById(R.id.rvPhotos);

        rvPhotos.setAdapter(rAdapter);
        // make a heterogenous listview here

        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        // Attach the layout manager to the recycler view
        rvPhotos.setLayoutManager(gridLayoutManager);
        //SpacesItemDecoration decoration = new SpacesItemDecoration(2);
        //rvPhotos.addItemDecoration(decoration);
        rvPhotos.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                customLoadMoreDataFromApi(page);
            }
        });
        // vPhotos.setOnItemClickListener(mListener);
        // vPhotos.setOnScrollListener(mScrollListener);
        rAdapter.setOnItemClickListener(new InterestingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(),
                        ImageDisplayActivity.class);
                Photo photo = mInteresting.get(position);

                intent.putExtra(RESULT, photo.getId());
                startActivity(intent);
                //Toast.makeText(getActivity(), title + " was clicked!", Toast.LENGTH_SHORT).show();
            }
        });
        setHasOptionsMenu(true);
        return view;
    }

    void clearAdapter() {
        mInteresting.clear();
        rAdapter.notifyDataSetChanged();
    }

    void customLoadMoreDataFromApi(int page) {
        /*loadPhotos(page, false); */
    }



    public void updateDisplay(Interesting i) {
        mInteresting.clear();
        mInteresting.addAll(i.getInterestingPhotos());
        rAdapter.notifyDataSetChanged();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != ringProgressDialog) {
            ringProgressDialog = null;
        }
        interestingRealm.close();
    }
}



