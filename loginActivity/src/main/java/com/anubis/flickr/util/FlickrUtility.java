package com.anubis.flickr.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by sabine on 3/10/15.
 */
public class FlickrUtility {

    public static void hideKeyboard(Context ctx) {
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(((Activity)ctx).getCurrentFocus().getWindowToken(), 0);
    }





}
