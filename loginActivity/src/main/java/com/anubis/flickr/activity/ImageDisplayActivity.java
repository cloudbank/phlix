package com.anubis.flickr.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;

import com.anubis.flickr.R;
import com.anubis.flickr.fragments.FlickrBaseFragment;
import com.anubis.flickr.models.FlickrPhoto;
import com.anubis.flickr.models.FlickrPhoto.Comment;
import com.anubis.flickr.models.Photo;
import com.anubis.flickr.util.DateUtility;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageDisplayActivity extends Activity {

    WebView wvComments;
    EditText etComments;
    String mUid = "";
    Class<FlickrPhoto> type;
    String mContent;
    StringBuilder mBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);
        Photo photo = (Photo) getIntent().getSerializableExtra(
                FlickrBaseFragment.RESULT);
        ActionBar ab = getActionBar();
        ab.setSubtitle(R.string.image_detail);
        ab.setDisplayHomeAsUpEnabled(true);
        type = (Class<FlickrPhoto>) getIntent().getSerializableExtra(FlickrBaseFragment.TYPE);
        ImageView imageView = (ImageView) findViewById(R.id.ivResult);
        Picasso.with(getBaseContext()).load(photo.getUrl()).into(imageView);
        //ImageLoader imageLoader = ImageLoader.getInstance();
        //imageLoader.displayImage(photo.getUrl(), image);
        TextView tvUsername = (TextView) findViewById(R.id.username);
        tvUsername.setText(photo.getOwnername());
        TextView tvTimestamp = (TextView) findViewById(R.id.timestamp);
        tvTimestamp.setText(DateUtility.relativeTime(photo.getDatetaken(), this));
        TextView tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText(photo.getTitle());
        etComments = (EditText) findViewById(R.id.etComments);
        etComments.setScroller(new Scroller(this));
        etComments.setMaxLines(1);
        etComments.setVerticalScrollBarEnabled(true);
        etComments.setMovementMethod(new ScrollingMovementMethod());
        wvComments = (WebView) findViewById(R.id.comments);
        wvComments.setBackgroundColor(Color.parseColor("#FFFFFF"));
        wvComments.setVerticalScrollBarEnabled(true);
        wvComments.setHorizontalScrollBarEnabled(true);
        mUid = photo.getId();
        //@todo
        //getComments(mUid);
        // get focus off edittext, hide kb
        // WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getComments(final String uid) {
        /*
        client.getComments(new JsonHttpResponseHandler() {

            @Override
            public void onFailure(Throwable arg0, JSONObject arg1) {
                Log.e("ERROR", "onFailure: getComments " + arg0 + " " + arg1);
                //@todo get the last comments from db
            }

            @Override
            public void onSuccess(JSONObject json) {
                try {
                    JSONObject c = json.getJSONObject("comments");
                    if (c.has("comment")) {
                        JSONArray cArray = c.getJSONArray("comment");
                        List<com.anubis.flickr.models.FlickrPhoto.Comment> comments = FriendsFlickrPhoto
                                .getCommentsFromArray(cArray);
                        FlickrPhoto p = FlickrPhoto.byPhotoUid(
                                uid, type);
                        //@todo
                        if (!(p.getComments().equals(comments))) {
                            p.setComments(comments);
                            p.save();
                        }
                        displayComments(wvComments, comments, false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("ERROR", "Error getting JSON");
                }
            }
        }, uid);
        */
    }

    public void addComment(View v) {
        String commentString = etComments.getText().toString();
        if (commentString.length() > 0) { }

            /*
            client.addComment(new JsonHttpResponseHandler() {

                @Override
                public void onFailure(Throwable arg0, JSONObject arg1) {
                    Log.e("ERROR", "onFailure addComment" + arg0 + arg1);
                }

                @Override
                public void onSuccess(JSONObject json) {
                    FlickrPhoto p = FlickrPhoto.byPhotoUid(mUid,
                            type);
                    String author = null, comment = null;

                    try {
                        comment = json.getJSONObject("comment").getString(
                                "_content");
                        author = json.getJSONObject("comment").getString(
                                "authorname");
                        List<Comment> comments_list = p.getComments();
                        comments_list.add(new Comment(author, comment));
                        p.setComments(comments_list);
                        p.save();
                        etComments.setText("");
                        displayComments(wvComments, comments_list, true);

                    } catch (JSONException e) {
                        Log.e("ERROR", "Error getting JSON");
                        e.printStackTrace();
                    }

                }

            }, commentString, mUid);
            FlickrUtility.hideKeyboard(this);
            // refresh the view
            //wvComments.reload();
        } else {
            Toast.makeText(this, R.string.enter_comment, Toast.LENGTH_SHORT).show();
        }
        */

    }

    private void displayComments(WebView commentsView, List<Comment> comments, boolean added) {
        mBuilder = new StringBuilder();
        mBuilder.append("<html><head>  <style> body {color: #2B2351;  font-size: 12px;}</style></head><br>");
        for (Comment c : comments) {
            mContent = c.getContent();
            String htmlString = mContent;

            if (mContent.contains("[http") && !mContent.contains("src")) {
                //make the link work but leave images
                htmlString = mContent.replaceAll("\\[(\\s*http\\S+\\s*)\\]", "<a href=\"" + "$1" + "\">$1</a><br>");
                //@todo look for other corner cases
                // } else if (mContent.contains("http") && !added) {
                //     htmlString = mContent.replaceAll("http\\S+", "<a href=\"" + "$0" + "\">$0</a>");
                // }
            }


            mBuilder.append("<b>" + c.getAuthor() + "</b>: " + htmlString + "<br><br>");
        }
        mBuilder.append("</body></html>");
        commentsView.loadUrl("about:blank");
        commentsView.loadData(mBuilder.toString(), "text/html", "utf-8");

    }


}
