package com.anubis.flickr.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.anubis.flickr.R;
import com.anubis.flickr.adapter.PhotoArrayAdapter;
import com.anubis.flickr.models.InterestingFlickrPhoto;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InterestingFragment extends FlickrBaseFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mType = InterestingFlickrPhoto.class;
        mAdapter = new PhotoArrayAdapter(getActivity(), mPhotoItems);
        loadPhotos(1, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interesting, container,
                false);
        vPhotos = (GridView) view.findViewById(R.id.gvPhotos);
        vPhotos.setAdapter(mAdapter);
        vPhotos.setOnItemClickListener(mListener);
        vPhotos.setOnScrollListener(mScrollListener);
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
        client.getInterestingnessList(new JsonHttpResponseHandler() {
            @Override
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
    }


}
