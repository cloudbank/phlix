package com.anubis.flickr.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FlickrPhoto extends Model implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    // add index
    @Column(name = "uid", unique = true)
    protected String uid;
    @Column(name = "author")
    protected String author;
    @Column(name = "title")
    protected String title;
    @Column(name = "url")
    private String url;
    @Column(name = "dateTaken")
    private String dateTaken;
    @Column(name = "comments")
    private List<Comment> comments = new ArrayList<Comment>();
    public FlickrPhoto() {
        super();
    }

    public FlickrPhoto(JSONObject object) {
        super();

        try {
            this.uid = object.getString("id");
            this.title = object.getString("title");
            setUrl(object);
            this.author = object.getString("ownername");
            this.dateTaken = object.getString("datetaken");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public FlickrPhoto(JSONObject object, boolean camera) {
        super();
        try {
            this.uid = object.getString("id");
            this.title = object.getJSONObject("title").getString("_content");
            this.author = object.getJSONObject("owner").getString("username");
            setUrl(object);
            this.dateTaken = object.getJSONObject("dates").getString("taken");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static FlickrPhoto byPhotoUid(String uid,
                                         Class<? extends FlickrPhoto> klass) {
        return new Select().from(klass).where("uid = ?", uid).executeSingle();
    }

    public static List<? extends FlickrPhoto> recentItems(
            Class<? extends FlickrPhoto> klass) {
        return new Select().from(klass).orderBy("id DESC").limit("300")
                .execute();
    }

    public static ArrayList<FlickrPhoto> fromJsonArray(JSONArray response) {
        ArrayList<FlickrPhoto> photos = new ArrayList<FlickrPhoto>();
        for (int i = 0; i < response.length(); i++) {
            JSONObject jo = null;
            try {
                jo = response.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
            FlickrPhoto t = new FlickrPhoto(jo);

            if (null != t) {
                photos.add(t);
            }
        }

        return photos;
    }

    public String getUid() {
        return uid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(JSONObject object) {
        try {

            this.url = "http://farm" + object.getInt("farm")
                    + ".staticflickr.com/" + object.getInt("server") + "/"
                    + uid + "_" + object.getString("secret") + ".jpg";
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getTitle() {
        return title;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getDateTaken() {
        return dateTaken;
    }

    public String getAuthor() {
        return author;
    }

    public static class Comment implements Serializable {
        String author;
        String content;

        public Comment(String author, String content) {
            this.author = author;
            this.content = content;
        }

        public String getAuthor() {
            return author;
        }

        public String getContent() {
            return content;
        }

    }

}
