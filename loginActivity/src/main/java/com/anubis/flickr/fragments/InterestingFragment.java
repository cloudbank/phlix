package com.anubis.flickr.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.anubis.flickr.models.Photo;
import com.anubis.flickr.models.Photos;

import java.util.ArrayList;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class InterestingFragment extends FlickrBaseFragment {
    InterestingAdapter rAdapter;
    RecyclerView rvPhotos;
    ProgressDialog ringProgressDialog;
    private Subscription subscription;
    //@todo move up
    List<Photo> mInteresting = new ArrayList<Photo>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        ringProgressDialog = new ProgressDialog(getActivity(), R.style.MyDialogTheme);

        rAdapter = new InterestingAdapter(FlickrClientApp.getAppContext(), mInteresting, true);
        loadPhotos(1, true);
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
                Photo result = mInteresting.get(position);
                intent.putExtra(RESULT, result);
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
        loadPhotos(page, false);
    }

    public void loadPhotos(int page, boolean clear) {
        //@todo offline mode
        if (clear) {
            clearAdapter();
        }

        subscription = FlickrClientApp.getJacksonService().getInterestingPhotos(String.valueOf(page))

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
                        Log.e("ERROR", "error getting interesting photos" + e);
                    }

                    @Override
                    public void onNext(Photos p) {
                        Log.d("DEBUG", "interesting: " + p);
                        //pass photos to fragment
                        mInteresting.addAll(p.getPhotos().getPhotoList());
                        rAdapter.notifyDataSetChanged();
                    }
                });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if ( null != ringProgressDialog ) {
            ringProgressDialog = null;
        }
       subscription.unsubscribe();
    }
}



