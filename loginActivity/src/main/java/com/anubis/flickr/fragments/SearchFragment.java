package com.anubis.flickr.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.anubis.flickr.FlickrClientApp;
import com.anubis.flickr.R;
import com.anubis.flickr.activity.ImageDisplayActivity;
import com.anubis.flickr.adapter.SearchAdapter;
import com.anubis.flickr.adapter.SpacesItemDecoration;
import com.anubis.flickr.listener.EndlessRecyclerViewScrollListener;
import com.anubis.flickr.models.Photo;
import com.anubis.flickr.models.Photos;
import com.anubis.flickr.models.TagsFlickrPhoto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchFragment extends FlickrBaseFragment {

    EditText etQuery;
    String searchText = "";
    RadioButton rbTagOr, rbTagAnd, rbText;
    TextView info;
    RadioGroup group;
    private String searchString = "";
    private Button btnQuery;
    RecyclerView rvPhotos;
    SearchAdapter searchAdapter;
    List<Photo> sPhotos = new ArrayList<Photo>();
    private Subscription subscription;
    Map data = new HashMap<String, String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = TagsFlickrPhoto.class;
        searchAdapter = new SearchAdapter(getContext(), sPhotos, true);
        loadPhotos(1, true);

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
       /* group = (RadioGroup) view.findViewById(R.id.radioGroup1);
        rbTagOr = (RadioButton) view.findViewById(R.id.radio0);
        rbTagAnd = (RadioButton) view.findViewById(R.id.radio1);
        rbText = (RadioButton) view.findViewById(R.id.radio2);
        info = (TextView) view.findViewById(R.id.textView1);

        group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String terms = "";
                if (rbTagOr.isChecked()) {
                    terms = getActivity().getResources().getString(R.string.or_search);
                } else if (rbTagAnd.isChecked()) {
                    terms = getActivity().getResources().getString(R.string.and_search);
                } else if (rbText.isChecked()) {
                    terms = getActivity().getResources().getString(R.string.text_search);
                }
                info.setText("Search by " + terms);
            }
        });
        */
        rvPhotos = (RecyclerView) view.findViewById(R.id.rvSearch);

        rvPhotos.setAdapter(searchAdapter);
        //rvPhotos.setOnItemClickListener(mListener);
        //rvPhotos.setOnScrollListener(mScrollListener);
        //rvPhotos.setLayoutManager(gridLayoutManager);
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        rvPhotos.setLayoutManager(gridLayoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration(15);
        rvPhotos.addItemDecoration(decoration);
        rvPhotos.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                customLoadMoreDataFromApi(page);
            }
        });
        //rvPhotos.setOnItemClickListener(mListener);
        //rvPhotos.setOnScrollListener(mScrollListener);
        searchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // String title = mTags.get(position).getTitle();
                Intent intent = new Intent(getActivity(),
                        ImageDisplayActivity.class);
                Photo result = sPhotos.get(position);
                intent.putExtra(RESULT, result);
                intent.putExtra(TYPE, mType);
                startActivity(intent);
                //Toast.makeText(getActivity(), title + " was clicked!", Toast.LENGTH_SHORT).show();
            }
        });
        //etQuery = (EditText) view.findViewById(R.id.etQuery);
        //btnQuery = (Button) view.findViewById(R.id.btnQuery);
        /*btnQuery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                searchText = etQuery.getText().toString();
                boolean empty = (searchText.trim().length() == 0);
                if (!empty && (!searchText.equals(searchString))) {
                    searchUrl(searchText);
                    loadPhotos(1,true);
                } else if (empty) {
                    Toast.makeText(getActivity(), "Enter search term",
                            Toast.LENGTH_SHORT).show();
                }
                FlickrUtility.hideKeyboard(getActivity());
*/
        setHasOptionsMenu(true);
        return view;

    }

    private void searchUrl(String searchText) {
        if (rbTagOr.isChecked() || rbTagAnd.isChecked()) {
            searchText = (searchText.trim()).replaceAll("[\\s]+", ",");
            // searchText = Uri.encode(searchText);
            if (rbTagOr.isChecked()) {
                data.put("tag_mode", "any");
                data.put("tags", searchText);
            } else {
                data.put("tag_mode", "all");
                data.put("tags", searchText);
            }
        } else if (rbText.isChecked()) {
            data.put("text", Uri.encode(searchText));
        }

        //return searchText;
    }


    void customLoadMoreDataFromApi(int page) {
        loadPhotos(page, false);
    }

    void clearAdapter() {
        sPhotos.clear();
        searchAdapter.notifyDataSetChanged();
    }

    private void loadPhotos(int page, boolean clear) {
        if (clear) {
            clearAdapter();
        }
        data.put("page", String.valueOf(page));
        data.put("is_commons", "true");
        data.put("text","Search The Commons");
        subscription = FlickrClientApp.getService().commons(data)
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
                        Log.e("ERROR", "error getting tags/photos" + e);
                    }

                    @Override
                    public void onNext(Photos p) {
                        Log.d("DEBUG", "tags photos: " + p);
                        //pass photos to fragment
                        sPhotos.addAll(p.getPhotos().getPhotoList());
                        searchAdapter.notifyDataSetChanged();
                    }
                });

    }


}
