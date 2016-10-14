package com.anubis.flickr.models;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sabine on 10/6/16.
 */

public class Friends extends RealmObject {

    public Date timestamp;

    public Friends() {}

    @PrimaryKey
    public String user;

    public  RealmList<Photo> friends;

    public RealmList<Photo> getFriends() {
        return friends;
    }

    public  void setFriends(RealmList<Photo> mFriends) {
        friends = mFriends;
    }


}
