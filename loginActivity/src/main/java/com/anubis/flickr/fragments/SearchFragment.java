package com.anubis.flickr.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.anubis.flickr.R;
import com.anubis.flickr.models.TagsFlickrPhoto;
import com.anubis.flickr.util.FlickrUtility;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchFragment extends FlickrBaseFragment {

    EditText etQuery;
    String searchText = "";
    RadioButton rbTagOr, rbTagAnd, rbText;
    TextView info;
    RadioGroup group;
    private String searchString = "";
    private Button btnQuery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = TagsFlickrPhoto.class;
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        group = (RadioGroup) view.findViewById(R.id.radioGroup1);
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
        vPhotos = (GridView) view.findViewById(R.id.gvPhotos);
        vPhotos.setAdapter(mAdapter);
        vPhotos.setOnItemClickListener(mListener);
        vPhotos.setOnScrollListener(mScrollListener);

        etQuery = (EditText) view.findViewById(R.id.etQuery);
        btnQuery = (Button) view.findViewById(R.id.btnQuery);
        btnQuery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                searchText = etQuery.getText().toString();
                boolean empty = (searchText.trim().length() == 0);
                if (!empty && (!searchText.equals(searchString))) {
                    searchString = searchUrl(searchText);
                    loadPhotos(1, true);
                } else if (empty) {
                    Toast.makeText(getActivity(), "Enter search term",
                            Toast.LENGTH_SHORT).show();
                }
                FlickrUtility.hideKeyboard(getActivity());

            }

            private String searchUrl(String searchText) {
                if (rbTagOr.isChecked() || rbTagAnd.isChecked()) {
                    searchText = (searchText.trim()).replaceAll("[\\s]+", ",");
                    // searchText = Uri.encode(searchText);
                    if (rbTagOr.isChecked()) {
                        searchText = "&tag_mode=any&tags=" + searchText;
                    } else {
                        searchText = "&tag_mode=all&tags=" + searchText;
                    }
                } else if (rbText.isChecked()) {
                    searchText = Uri.encode(searchText);
                    searchText = "&text=" + searchText;
                }
                return searchText;
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
        client.search(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(JSONObject json) {
                try {
                    JSONArray photos = json.getJSONObject("photos")
                            .getJSONArray("photo");
                    if (photos.length() == 0) {
                        Toast t = Toast.makeText(getActivity(), "No new photos for that search right now", Toast.LENGTH_SHORT);
                        t.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                        t.show();

                    } else {
                        for (int x = 0; x < photos.length(); x++) {
                            String uid = photos.getJSONObject(x).getString("id");
                            TagsFlickrPhoto p = (TagsFlickrPhoto) TagsFlickrPhoto
                                    .byPhotoUid(uid, TagsFlickrPhoto.class);
                            if (p == null) {
                                p = new TagsFlickrPhoto(photos.getJSONObject(x));
                            }

                            p.save();
                            mAdapter.add(p);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Error getting JSON", e.toString());
                }

            }

            @Override
            public void onFailure(Throwable arg0, JSONObject arg1) {
                Log.e("ERROR", ": onFailure: SearchFragment " + arg0 + " " + arg1);
            }

        }, searchString, page);
    }
}
