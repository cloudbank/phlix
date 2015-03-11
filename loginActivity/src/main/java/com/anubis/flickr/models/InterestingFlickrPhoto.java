package com.anubis.flickr.models;

import com.activeandroid.annotation.Table;

import org.json.JSONObject;

@Table(name = "interesting")
public class InterestingFlickrPhoto extends FlickrPhoto {

    public InterestingFlickrPhoto() {
        super();
    }

    public InterestingFlickrPhoto(JSONObject jsonObject) {
        super(jsonObject);
    }

}
