package com.anubis.flickr.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anubis.flickr.FlickrClientApp;
import com.anubis.flickr.R;
import com.anubis.flickr.adapter.RecyclerAdapter;
import com.anubis.flickr.models.InterestingFlickrPhoto;
import com.anubis.flickr.models.Photos;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class InterestingFragment extends FlickrBaseFragment {
    RecyclerAdapter rAdapter;
    RecyclerView rvPhotos;
    ProgressDialog ringProgressDialog;
    private Subscription subscription;
    //@todo move up
    private Photos mPhotos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        ringProgressDialog = new ProgressDialog(getContext(), R.style.CustomProgessBarStyle);

        mType = InterestingFlickrPhoto.class;
        rAdapter = new RecyclerAdapter(getContext(), mPhotoItems);
        loadPhotos(1, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interesting, container,
                false);
        rvPhotos = (RecyclerView) view.findViewById(R.id.rvPhotos);

        rvPhotos.setAdapter(rAdapter);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        // Attach the layout manager to the recycler view
        rvPhotos.setLayoutManager(gridLayoutManager);
        // vPhotos.setOnItemClickListener(mListener);
        // vPhotos.setOnScrollListener(mScrollListener);
        rAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String title = mPhotoItems.get(position).getTitle();
                Toast.makeText(getActivity(), title + " was clicked!", Toast.LENGTH_SHORT).show();
            }
        });
        setHasOptionsMenu(true);
        return view;
    }

    void customLoadMoreDataFromApi(int page) {
        loadPhotos(page, false);
    }

    public void loadPhotos(int page, boolean clear) {
        if (clear) {
            clearAdapter();
        }
        ringProgressDialog.setTitle("Please wait");
        ringProgressDialog.setMessage("Retrieving photos");
        ringProgressDialog.setCancelable(true);
        ringProgressDialog.show();
        subscription = FlickrClientApp.getService().getInterestingPhotos()

                .subscribeOn(Schedulers.io()) // optional if you do not wish to override the default behavior
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Photos>() {
                    @Override
                    public void onCompleted() {


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
                        Log.e("ERROR", "error getting interesting photos" + e);
                    }

                    @Override
                    public void onNext(Photos p) {
                        Log.d("DEBUG", "mlogin: " + p);
                        //pass photos to fragment
                        mPhotos = p;
                        mPhotoItems.addAll(mPhotos.getPhotos().getPhotoList());
                        rAdapter.notifyDataSetChanged();
                    }
                });

    }
        /*
        client.getInterestingnessList(new JsonHttpResponseHandler() {
            @Override
            // @TODO: 9/17/16   get the db work off the main thread
            //@todo use jackson
            public void onSuccess(JSONObject json) {
                // Add new photos to SQLite
                try {
                    JSONArray photos = json.getJSONObject("photos")
                            .getJSONArray("photo");
                    for (int x = 0; x < photos.length(); x++) {
                        String uid = photos.getJSONObject(x).getString("id");
                        InterestingFlickrPhoto p = (InterestingFlickrPhoto) InterestingFlickrPhoto
                                .byPhotoUid(uid, InterestingFlickrPhoto.class);
                        if (p == null) {
                            p = new InterestingFlickrPhoto(photos
                                    .getJSONObject(x));
                        }
                        p.save();
                        mAdapter.add(p);
                    }
                    mAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Error getting JSON", e.toString());
                }
            }

            @Override
            public void onFailure(Throwable arg0, JSONObject arg1) {
                Log.e("ERROR", ": onFailure: InterestingFragment " + arg0 + " " + arg1);
            }

        }, page);
        */
}



