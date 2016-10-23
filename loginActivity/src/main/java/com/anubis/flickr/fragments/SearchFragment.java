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
import com.anubis.flickr.adapter.SearchAdapter;
import com.anubis.flickr.adapter.SpacesItemDecoration;
import com.anubis.flickr.models.Common;
import com.anubis.flickr.models.Photo;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;


public class SearchFragment extends FlickrBaseFragment {


    RecyclerView rvPhotos;
    SearchAdapter searchAdapter;
    List<Photo> sPhotos = new ArrayList<Photo>();
    Realm commonsRealm, r;
    RealmChangeListener changeListener;
    ProgressDialog ringProgressDialog;

    @Override
    public void onDestroy() {
        super.onDestroy();
        commonsRealm.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("TABS","search onresume");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //this gets called along w lifecycle when vp recycles fragment ie  commons tab
        //get from realm in random order
        Log.d("TABS","search activcreated");
        changeListener = new RealmChangeListener<Common>()


        {
            @Override
            public void onChange(Common c) {
                // This is called anytime the Realm database changes on any thread.
                // Please note, change listeners only work on Looper threads.
                // For non-looper threads, you manually have to use Realm.waitForChange() instead.
                updateDisplay(c);
            }
        };


        commonsRealm = Realm.getDefaultInstance();

        ringProgressDialog.setTitle("Please wait");
        ringProgressDialog.setMessage("Retrieving tags/recent photos");
        ringProgressDialog.setCancelable(true);
        ringProgressDialog.show();
        Common c = commonsRealm.where(Common.class).findFirst();
        Log.d("DEBUG", "commonsFragment" + c);
        if (null == c) {
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


            Log.d("COMMON PRESENT", "list: " + c);
            updateDisplay(c);
            c.addChangeListener(changeListener);
            if (null != r) {
                r.removeAllChangeListeners();
                r.close();
            }

            //updateDisplay(interesting);

        }
        ringProgressDialog.dismiss();


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchAdapter = new SearchAdapter(FlickrClientApp.getAppContext(), sPhotos, true);
        //loadPhotos(1, true);
        Log.d("TABS","search oncreate");
        ringProgressDialog = new ProgressDialog(getActivity(), R.style.MyDialogTheme);
        setRetainInstance(true);
    }

    private void updateDisplay() {
        Common c = commonsRealm.where(Common.class).findFirst();
        sPhotos.clear();
        sPhotos.addAll(c.getCommonPhotos());
        searchAdapter.notifyDataSetChanged();
    }

    private void updateDisplay(Common c) {

        sPhotos.clear();
        sPhotos.addAll(c.getCommonPhotos());
        searchAdapter.notifyDataSetChanged();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        rvPhotos = (RecyclerView) view.findViewById(R.id.rvSearch);

        rvPhotos.setAdapter(searchAdapter);
        //rvPhotos.setOnItemClickListener(mListener);
        //rvPhotos.setOnScrollListener(mScrollListener);
        //rvPhotos.setLayoutManager(gridLayoutManager);
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rvPhotos.setLayoutManager(gridLayoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration(15);
        rvPhotos.addItemDecoration(decoration);
       /* rvPhotos.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                customLoadMoreDataFromApi(page);
            }
        });*/
        //rvPhotos.setOnItemClickListener(mListener);
        //rvPhotos.setOnScrollListener(mScrollListener);
        searchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // String title = mTags.get(position).getTitle();
                Intent intent = new Intent(getActivity(),
                        ImageDisplayActivity.class);
                Photo photo = sPhotos.get(position);
                intent.putExtra(RESULT, photo.getId());
                startActivity(intent);
                //Toast.makeText(getActivity(), title + " was clicked!", Toast.LENGTH_SHORT).show();
            }
        });


        setHasOptionsMenu(true);
        return view;

    }


    void customLoadMoreDataFromApi(int page) {
        /*loadPhotos(page, false);*/
    }

    void clearAdapter() {
        sPhotos.clear();
        searchAdapter.notifyDataSetChanged();
    }


}
