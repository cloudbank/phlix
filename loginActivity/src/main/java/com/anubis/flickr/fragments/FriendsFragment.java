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
import com.anubis.flickr.models.Photo;
import com.anubis.flickr.models.Photos;
import com.anubis.flickr.models.Tag;
import com.anubis.flickr.models.User;
import com.anubis.flickr.models.Who;

import java.util.ArrayList;
import java.util.List;

import co.hkm.soltag.TagContainerLayout;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class FriendsFragment extends FlickrBaseFragment {

    private Subscription subscription;
    private String username;
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
        ringProgressDialog= new ProgressDialog(getActivity(), R.style.CustomProgessBarStyle);
        this.prefs = FlickrClientApp.getAppContext().getSharedPreferences("Flickr_User_Prefs", 0);
        this.editor = this.prefs.edit();

        getPhotos();
        getTags();


        setRetainInstance(true);


    }



    void customLoadMoreDataFromApi(int page) {
        //@todo add the endless scroll

    }
//flickr.tags.getListUser
    private void getPhotos() {
        ringProgressDialog.setTitle("Please wait");
        ringProgressDialog.setMessage("Retrieving interesting photos");
        ringProgressDialog.setCancelable(true);
        ringProgressDialog.show();
        subscription =  FlickrClientApp.getJacksonService().testLogin()
                .concatMap(new Func1<User, Observable<Photos>>() {
                    @Override
                    public Observable<Photos> call(User user) {
                        username = user.getUser().getUsername().getContent();
                        editor.putString("username", username);
                        editor.putString("id",user.getUser().getId());
                        editor.commit();
                        return FlickrClientApp.getJacksonService().getFriendsPhotos(user.getUser().getId());

                    }
                }).subscribeOn(Schedulers.io()) // optional if you do not wish to override the default behavior
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Photos>() {
                    @Override
                    public void onCompleted() {

                        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle(username);

                        ringProgressDialog.dismiss();
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
                        Log.e("ERROR",  "error getting login/photos" + e);
                    }

                    @Override
                    public void onNext(Photos p ) {
                        Log.d("DEBUG","mlogin: "+ p);
                        //pass photos to fragment
                        mPhotos.addAll(p.getPhotos().getPhotoList());
                        fAdapter.notifyDataSetChanged();
                    }
                });

    }

    private void getTags() {
        String uid = prefs.getString("id","");
        subscription =  FlickrClientApp.getJacksonService().getTags(uid)
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
                            HttpException response = (HttpException)e;
                            int code = response.code();
                            Log.e("ERROR",  String.valueOf(code));
                        }
                        Log.e("ERROR",  "error getting tags" + e);
                    }

                    @Override
                    public void onNext(Who w ) {
                        Log.d("DEBUG","tags for user: "+ w);
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
        this.subscription.unsubscribe();
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
        rvPhotos.setLayoutManager(new GridLayoutManager(FlickrClientApp.getAppContext(),3));
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
        mTagView = (TagContainerLayout)view.findViewById(R.id.my_tag_group);
        setHasOptionsMenu(true);
        return view;
    }



}
