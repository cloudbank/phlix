package com.anubis.flickr.models;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by sabine on 10/6/16.
 */

public class Friends extends RealmObject {
    private Date timestamp;

    private RealmList<Photo> friends;
}
