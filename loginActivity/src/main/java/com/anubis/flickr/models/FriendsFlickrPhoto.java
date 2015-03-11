package com.anubis.flickr.models;

import com.activeandroid.annotation.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Table(name = "friends")
public class FriendsFlickrPhoto extends FlickrPhoto {

    public FriendsFlickrPhoto() {
        super();
    }

    public FriendsFlickrPhoto(JSONObject jsonObject)
            throws JSONException {
        super(jsonObject);
    }

    public FriendsFlickrPhoto(JSONObject jsonObject, boolean camera)
            throws JSONException {
        super(jsonObject, camera);
    }

    // from camera
    public FriendsFlickrPhoto(String id) {
        this.uid = id;
        this.title = "image.png";

    }

    public static List<Comment> getCommentsFromArray(JSONArray response) {
        ArrayList<Comment> result = new ArrayList<Comment>();
        for (int i = 0; i < response.length(); i++) {
            JSONObject row;
            try {
                row = response.getJSONObject(i);
                Comment c = new Comment(row.getString("authorname"),
                        row.getString("_content"));
                result.add(c);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public String getName() {
        return author;
    }

}
