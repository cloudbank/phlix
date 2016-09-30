package com.anubis.flickr.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;

import com.anubis.flickr.FlickrClientApp;
import com.anubis.flickr.R;
import com.anubis.flickr.fragments.FlickrBaseFragment;
import com.anubis.flickr.models.Comment;
import com.anubis.flickr.models.Comments;
import com.anubis.flickr.models.ImageDisplay;
import com.anubis.flickr.models.Photo;
import com.anubis.flickr.models.PhotoInfo;
import com.anubis.flickr.models.Tag;
import com.anubis.flickr.util.DateUtility;
import com.anubis.flickr.util.ImageRoundedTransformation;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import co.hkm.soltag.TagContainerLayout;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

import static com.anubis.flickr.R.id.comments;
import static com.anubis.flickr.R.id.username;

public class ImageDisplayActivity extends AppCompatActivity {

    WebView wvComments;
    List mTagsList = new ArrayList();
    TagContainerLayout mTags;
    EditText etComments;
    String mUid = "";
    String mContent;
    StringBuilder mBuilder;
    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);
        Photo photo = (Photo) getIntent().getSerializableExtra(
                FlickrBaseFragment.RESULT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
// ...
// Display icon in the toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.flickr_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        // getSupportActionBar().setElevation(3);
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setSubtitle(R.string.image_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImageView imageView = (ImageView) findViewById(R.id.ivResult);
        Picasso.with(getBaseContext()).load(photo.getUrl()).transform(new ImageRoundedTransformation(5,5)).resize(300,300).centerCrop().into(imageView);
        //ImageLoader imageLoader = ImageLoader.getInstance();
        //imageLoader.displayImage(photo.getUrl(), image);
        TextView tvUsername = (TextView) findViewById(username);
        tvUsername.setText("By: "+photo.getOwnername());
        TextView tvTimestamp = (TextView) findViewById(R.id.timestamp);
        tvTimestamp.setText(DateUtility.relativeTime(photo.getDatetaken(), this));
        TextView tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText(photo.getTitle());
        etComments = (EditText) findViewById(R.id.etComments);
        etComments.setScroller(new Scroller(this));
        etComments.setMaxLines(1);
        etComments.setVerticalScrollBarEnabled(true);
        etComments.setMovementMethod(new ScrollingMovementMethod());
        wvComments = (WebView) findViewById(comments);
        wvComments.setBackgroundColor(getResources().getColor(R.color.AliceBlue));
        wvComments.setVerticalScrollBarEnabled(true);
        wvComments.setHorizontalScrollBarEnabled(true);
        mUid = photo.getId();
        mTags =    (TagContainerLayout) findViewById(R.id.tag_group);

        //@todo
        getComments(mUid);
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
        Observable<PhotoInfo> photoInfo = FlickrClientApp.getService().getPhotoInfo(uid);
        subscription = FlickrClientApp.getService().getComments(uid).zipWith(photoInfo, new Func2<Comments, PhotoInfo, ImageDisplay>() {
            @Override
            public ImageDisplay call(Comments c, PhotoInfo p) {
                return new ImageDisplay(p, c);
            }


        }).subscribeOn(Schedulers.io()) // optional if you do not wish to override the default behavior
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ImageDisplay>() {
                    @Override
                    public void onCompleted() {
                        if (mTagsList.size() == 0) {
                            mTags.setVisibility(View.INVISIBLE);
                        } else {
                            mTags.setVisibility(View.VISIBLE);
                        }

                        //ringProgressDialog.dismiss();
                        //Log.d("DEBUG","oncompleted");

                    }

                    @Override
                    public void onError(Throwable e) {
                        // cast to retrofit.HttpException to get the response code
                        if (e instanceof HttpException) {
                            HttpException response = (HttpException) e;
                            int code = response.code();
                            Log.e("ERROR", String.valueOf(code));
                        }
                        Log.e("ERROR", "error getting interesting photos" + e);
                    }

                    @Override
                    public void onNext(ImageDisplay imageDisplay) {
                        Log.d("DEBUG", "mlogin: " + comments);
                        //pass comments to webview
                        displayComments(wvComments, imageDisplay.getComments().getComments().getCommentList(), false);
                        mTagsList =imageDisplay.getPhoto().getPhoto().getTags().getTag();
                        displayPhotoInfo(mTagsList);
                    }
                });

    }

    public void displayPhotoInfo(List<Tag> tags)  {
        //tags.stream().map(it -> it.getContent()).collect(Collectors.toCollection())
        //when android catches up to 1.8
        for( Tag t : tags) {
            mTags.addTag(t.getContent());
        }


    }



    public void addComment(View v) {
        String commentString = etComments.getText().toString();
        try {
            commentString = URLEncoder.encode(commentString, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (commentString.length() > 0) {
        }

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
        mBuilder.append("<html><head>  <style> body {color: #4169E1;  font-size: 12px;}</style></head><br>");
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


            mBuilder.append("<b>" + c.getAuthorname() + "</b>: " + htmlString + "<br><br>");
        }
        mBuilder.append("</body></html>");
        commentsView.loadUrl("about:blank");
        commentsView.loadData(mBuilder.toString(), "text/html", "utf-8");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();

    }
}
