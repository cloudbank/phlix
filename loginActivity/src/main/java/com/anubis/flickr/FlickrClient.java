package com.anubis.flickr;

import android.content.Context;
import android.graphics.Bitmap;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FlickrApi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * client is oauthasynchttpclient with default HMACSha1 that overrides
 * sendRequest
 */
public class FlickrClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = FlickrApi.class;
    public static final String REST_URL = "https://api.flickr.com/services/rest";
    public static final String REST_UP_URL = "https://up.flickr.com/services/upload";
    public static final String REST_CONSUMER_KEY = "3b9d2687f93eb4b4835a112b41d28db0";
    public static final String REST_CONSUMER_SECRET = "fe875d894a6e85c9";
    public static final String REST_CALLBACK_URL = "oauth://cprest";
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    public FlickrClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY,
                REST_CONSUMER_SECRET, REST_CALLBACK_URL);

    }

    public void getInterestingnessList(AsyncHttpResponseHandler handler, int page) {
        String apiUrl = getApiUrl("?api_key=" + REST_CONSUMER_KEY
                + "&format=json&nojsoncallback=1&per_page=50&page=" + page
                + "&extras=date_taken,owner_name&method=flickr.interestingness.getList");
        //Log.d("DEBUG", "Sending API call to " + apiUrl);
        client.get(apiUrl, null, handler);
    }

    public void getFriendsList(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("?api_key="
                + REST_CONSUMER_KEY
                + "&format=json&nojsoncallback=1&user_id=30840477%40N06&just_friends=1&extras=date_taken,owner_name&count=50&include_self=1&method=flickr.photos.getContactsPublicPhotos");
        //Log.d("DEBUG", "Sending API call to " + apiUrl);
        client.get(apiUrl, null, handler);
    }

    public void getComments(AsyncHttpResponseHandler handler, String uid) {
        String apiUrl = getApiUrl("?api_key=" + REST_CONSUMER_KEY
                + "&format=json&nojsoncallback=1&photo_id="
                + uid + "&method=flickr.photos.comments.getList");
        //Log.d("DEBUG", "Sending API call to " + apiUrl);
        client.get(apiUrl, null, handler);
    }

    public void search(JsonHttpResponseHandler handler, String searchString, int page) {
        // comma delimted tags check for spaces
        String apiUrl = getApiUrl("?api_key="
                        + REST_CONSUMER_KEY
                        + "&format=json&nojsoncallback=1&page="
                        + page
                        + "&per_page=50&extras=date_taken,owner_name,tags,description&method=flickr.photos.search"
                        + searchString
        );
        //Log.d("DEBUG", "Sending API call to " + apiUrl);
        client.get(apiUrl, null, handler);
    }

    /* signed request for photo upload */
    public void createPhotoPost(Bitmap bitmap, String filename, AsyncHttpResponseHandler handler) {
        String apiUrl = REST_UP_URL;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        final byte[] bytes = stream.toByteArray();
        RequestParams params = new RequestParams();
        params.put("photo", new ByteArrayInputStream(bytes), filename);
        //Log.d("DEBUG", "Sending API call to " + apiUrl + params.toString());
        client.post(apiUrl, params, handler);
    }

    public void addComment(JsonHttpResponseHandler handler,
                           String commentString, String uid) {
        String api_sig = "", apiUrl = "";
        StringBuilder url = new StringBuilder();
        try {
            commentString = URLEncoder.encode(commentString, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        url.append("?comment_text=")
                .append(commentString)
                .append("&format=json&method=flickr.photos.comments.addComment&nojsoncallback=1&photo_id=")
                .append(uid);
        apiUrl = getApiUrl(url.toString());
        //Log.d("DEBUG", "Sending API call to " + apiUrl);
        client.post(apiUrl, null, handler);
    }

    public void testLogin(AsyncHttpResponseHandler handler) {
        String api_sig = "", apiUrl = "";
        apiUrl = getApiUrl("?method=flickr.test.login");
        //Log.d("DEBUG", "Sending API call to " + apiUrl);
        client.get(apiUrl, null, handler);
    }

}
