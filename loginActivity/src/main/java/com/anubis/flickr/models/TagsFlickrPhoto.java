package com.anubis.flickr.models;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Table(name = "tags")
public class TagsFlickrPhoto extends FlickrPhoto {

    @Column(name = "tags")
    private List<Comment> tags = new ArrayList<Comment>();

    public TagsFlickrPhoto() {
        super();
    }

    public TagsFlickrPhoto(JSONObject jsonObject) {
        super(jsonObject);
    }

}
