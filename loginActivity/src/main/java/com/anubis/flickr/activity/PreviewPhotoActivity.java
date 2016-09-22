package com.anubis.flickr.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.anubis.flickr.R;
import com.anubis.flickr.fragments.FlickrBaseFragment;
import com.anubis.flickr.util.ImageFilterProcessor;

public class PreviewPhotoActivity extends FragmentActivity {
    public static final String IMAGE_PNG = "image.png";
    public static final String PLEASE_WAIT = "Please wait";
    public static final String UPLOADING_IMAGE = "Uploading Image";
    public static final String PHOTOID_BEGINTAG = "<photoid>";
    public static final String PHOTOID_ENDTAG = "</photoid>";
    String filename;
    ProgressDialog dialog;
    private Bitmap photoBitmap;
    private Bitmap processedBitmap;
    private EditText etFilename;
    private ImageView ivPreview;
    private ImageFilterProcessor filterProcessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_photo);
        ivPreview = (ImageView) findViewById(R.id.ivPreview);
        etFilename = (EditText) findViewById(R.id.etPhotoName);
        photoBitmap = getIntent().getParcelableExtra(FlickrBaseFragment.PHOTO_BITMAP);
        filterProcessor = new ImageFilterProcessor(photoBitmap);
        redisplayPreview(ImageFilterProcessor.NONE);
    }

    private void redisplayPreview(int effectId) {
        processedBitmap = filterProcessor.applyFilter(effectId);
        ivPreview.setImageBitmap(processedBitmap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.preview_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.more || itemId == R.id.action_save)
            return true;

        int effectId = 0;

        if (itemId == R.id.filter_none) {
            effectId = ImageFilterProcessor.NONE;
        } else if (itemId == R.id.filter_blur) {
            effectId = ImageFilterProcessor.BLUR;
        } else if (itemId == R.id.filter_grayscale) {
            effectId = ImageFilterProcessor.GRAYSCALE;
        } else if (itemId == R.id.filter_crystallize) {
            effectId = ImageFilterProcessor.CRYSTALLIZE;
        } else if (itemId == R.id.filter_solarize) {
            effectId = ImageFilterProcessor.SOLARIZE;
        } else if (itemId == R.id.filter_glow) {
            effectId = ImageFilterProcessor.GLOW;
        } else {
            effectId = ImageFilterProcessor.NONE;
        }
        redisplayPreview(effectId);
        return true;
    }

    public void onSaveButton(MenuItem menuItem) {
        final ProgressDialog ringProgressDialog = new ProgressDialog(this, R.style.CustomProgessBarStyle);
        ringProgressDialog.setTitle(PLEASE_WAIT);
        ringProgressDialog.setMessage(UPLOADING_IMAGE);

        ringProgressDialog.setCancelable(true);
        ringProgressDialog.show();
        filename = etFilename.getText().toString();
        if (filename.length() == 0) {
            filename = IMAGE_PNG;
        }
        /*
        client.createPhotoPost(processedBitmap, filename, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int arg0, String id) {
                Intent data = new Intent();
                try {
                    data.putExtra("id", parseId(id));
                } catch (ArrayIndexOutOfBoundsException e) {
                    Log.e("ERROR", "createPhotoPost upload response error");
                    //todo
                }
                setResult(RESULT_OK, data);
                ringProgressDialog.dismiss();
                PreviewPhotoActivity.this.finish();
            }

            private String parseId(String id) {
                return id.substring((id.indexOf(PHOTOID_BEGINTAG)) + 9,
                        id.indexOf(PHOTOID_ENDTAG));
            }

            @Override
            public void onFailure(Throwable arg0, String arg1) {
                Log.e("ERROR", "onFailure in createPhotoPost" + arg0 + arg1);
                ringProgressDialog.dismiss();
            }
        });
    }
    */
    }
}
