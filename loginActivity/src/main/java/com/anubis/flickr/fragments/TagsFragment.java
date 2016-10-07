package com.anubis.flickr.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anubis.flickr.FlickrClientApp;
import com.anubis.flickr.R;
import com.anubis.flickr.activity.ImageDisplayActivity;
import com.anubis.flickr.adapter.PhotoArrayAdapter;
import com.anubis.flickr.adapter.TagsAdapter;
import com.anubis.flickr.models.Hottags;
import com.anubis.flickr.models.Photo;
import com.anubis.flickr.models.Photos;
import com.anubis.flickr.models.Tag;

import java.util.ArrayList;
import java.util.List;

import co.hkm.soltag.TagContainerLayout;
import co.hkm.soltag.TagView;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TagsFragment extends FlickrBaseFragment {

    List mTags;
    TagContainerLayout mTagsView;
    private Subscription subscription, subscription2;
    private String username;
    private List<Photo> mPhotos;
    protected PhotoArrayAdapter getAdapter() {
        return mAdapter;
    }
    ProgressDialog ringProgressDialog;
    TagsAdapter tAdapter;
    RecyclerView rvPhotos;
    protected SharedPreferences prefs;
    protected SharedPreferences.Editor editor;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPhotos = new ArrayList<Photo>();
        tAdapter = new TagsAdapter(getActivity(), mPhotos, false);
        //ringProgressDialog= new ProgressDialog(getContext(), R.style.CustomProgessBarStyle);
        this.prefs = this.getContext().getSharedPreferences("Flickr_User_Prefs", 0);
        this.editor = this.prefs.edit();
        mTags = new ArrayList<Tag>();

        getTags();
        getPhotos();
        setRetainInstance(true);


    }



    void customLoadMoreDataFromApi(int page) {
        //@todo add the endless scroll

    }

    private void getTags() {

        subscription =  FlickrClientApp.getService().getHotTags()
             .subscribeOn(Schedulers.io()) // optional if you do not wish to override the default behavior
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Hottags>() {
                    @Override
                    public void onCompleted() {


                        //Log.d("DEBUG","oncompleted");

                    }
                    @Override
                    public void onError(Throwable e) {
                        // cast to retrofit.HttpException to get the response code
                        if (e instanceof HttpException) {
                            HttpException response = (HttpException)e;
                            int code = response.code();
                            Log.e("ERROR",  String.valueOf(code));
                        }
                        Log.e("ERROR",  "error getting tags" + e);
                    }

                    @Override
                    public void onNext(Hottags h ) {
                        Log.d("DEBUG","hottags: "+ h);
                        //pass photos to fragment
                        mTags.addAll(h.getHottags().getTag());
                        displayPhotoInfo(mTags);
                    }
                });

    }


    private void getPhotos() {

        subscription2 =  FlickrClientApp.getService().getRecentPhotos()
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
                            HttpException response = (HttpException)e;
                            int code = response.code();
                            Log.e("ERROR",  String.valueOf(code));
                        }
                        Log.e("ERROR",  "error getting tags/photos" + e);
                    }

                    @Override
                    public void onNext(Photos p ) {
                        Log.d("DEBUG","tags photos: "+ p);
                        //pass photos to fragment
                        mPhotos.addAll(p.getPhotos().getPhotoList());
                        tAdapter.notifyDataSetChanged();
                    }
                });

    }

    public void displayPhotoInfo(List<Tag> tags) {
        //tags.stream().map(it -> it.getContent()).collect(Collectors.toCollection())
        //when android catches up to 1.8
        for (Tag t : tags) {
            mTagsView.addTag(t.getContent());
        }


    }

    protected void loadPhotos() {
       // clearAdapter();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.subscription.unsubscribe();
        this.subscription2.unsubscribe();


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tags, container,
                false);
        mTagsView = (TagContainerLayout) view.findViewById(R.id.tag_group);
        mTagsView.setOnTagClickListener(new TagView.OnTagClickListener() {

            @Override
            public void onTagClick(int position, String text) {
                Toast.makeText(getContext(),"Tag "+text,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTagLongClick(final int position, String text) {
                // ...
            }
        });
        rvPhotos = (RecyclerView) view.findViewById(R.id.rvPhotos);
        rvPhotos.setAdapter(tAdapter);
       // StaggeredGridLayoutManager gridLayoutManager =
        //new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        // Attach the layout manager to the recycler view
        //rvPhotos.setLayoutManager(gridLayoutManager);
        rvPhotos.setLayoutManager(new GridLayoutManager(getContext(),3));
        //SpacesItemDecoration decoration = new SpacesItemDecoration(2);
        //rvPhotos.addItemDecoration(decoration);

        //rvPhotos.setOnItemClickListener(mListener);
        //rvPhotos.setOnScrollListener(mScrollListener);
        tAdapter.setOnItemClickListener(new TagsAdapter.OnItemClickListener() {
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
        setHasOptionsMenu(true);
        return view;
    }



}
