package com.anubis.flickr.fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;

import com.anubis.flickr.R;
import com.anubis.flickr.activity.LoginActivity;
import com.anubis.flickr.activity.PreviewPhotoActivity;
import com.anubis.flickr.adapter.PhotoArrayAdapter;
import com.anubis.flickr.listener.EndlessScrollListener;
import com.anubis.flickr.network.OAuthBaseClient;

import java.io.File;

import eu.janmuller.android.simplecropimage.CropImage;

public abstract class FlickrBaseFragment extends Fragment {

    public static final String RESULT = "result";
    public static final String TYPE = "type";
    public static final String PHOTO_BITMAP = "photo_bitmap";
    protected static final String PAGE = "page";
    protected static final String TITLE = "title";
    private static final int TAKE_PHOTO_CODE = 1;
    private static final int CROP_PHOTO_CODE = 3;
    private static final int POST_PHOTO_CODE = 4;
    private final static int CAMERA_RQ = 6969;

    public final String APP_TAG = "FlickrApp";
    public String photoFileName = "photo.jpg";
    File mediaStorageDir;
    //List<Photo> mTags;
    PhotoArrayAdapter mAdapter;
    AbsListView vPhotos;
    OnPhotoPostedListener mCallback;


    // Container Activity must implement this interface
    public interface OnPhotoPostedListener {
        public void onPhotoPosted();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnPhotoPostedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnPhotoPostedListener");
        }
    }


    /*AdapterView.OnItemClickListener mListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> adapter, View parent,
                                int position, long arg3) {
            Intent intent = new Intent(getActivity(),
                    ImageDisplayActivity.class);
            Photo result = mTags.get(position);
            intent.putExtra(RESULT, result);
            startActivity(intent);
        }
    };*/
    EndlessScrollListener mScrollListener = new EndlessScrollListener() {
        @Override
        public void onLoadMore(int page, int totalItemsCount) {

            //@todo add pulltorefresh
            // there is some bug in scroll after I added the pull down
            // refresh library
            // if (totalItemsCount > 1) {
            customLoadMoreDataFromApi(page);
            // }
        }
    };
    private String title;
    private int page;
    private Bitmap photoBitmap;

    // newInstance constructor for creating fragment with arguments
    public static FlickrBaseFragment newInstance(int page, String title, FlickrBaseFragment f) {
        Bundle args = new Bundle();
        args.putInt(PAGE, page);
        args.putString(TITLE, title);
        f.setArguments(args);
        return f;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        page = getArguments().getInt(PAGE, 0);
        title = getArguments().getString(TITLE);
       // mTags = new ArrayList<Photo>();
       // mAdapter = new PhotoArrayAdapter(getActivity(), mTags);


        // Get safe storage directory for photos
        mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                APP_TAG);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(APP_TAG, "Directory exists: " + mediaStorageDir.isDirectory());
            Log.d(APP_TAG, "Directory exists: " + mediaStorageDir.getPath());
            Log.d(APP_TAG,
                    "Directory exists: "
                            + Environment.getExternalStorageState());
            Log.d(APP_TAG, "failed to create directory");
        }

    }

   /* void clearAdapter() {
        mAdapter.clear();
        //mTags.clear();
        mAdapter.notifyDataSetChanged();
    }*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.photos, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_take_photo) {
           // new MaterialCamera(this).stillShot()
             //       .start(CAMERA_RQ);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(photoFileName));
            try {
                startActivityForResult(intent, TAKE_PHOTO_CODE);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                Log.e("ERROR", "cannot take picture", e);
            }



        } else if (itemId == R.id.action_logout) {
            signOut();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == TAKE_PHOTO_CODE) {
                Uri takenPhotoUri = getPhotoFileUri(photoFileName);
                cropPhoto(takenPhotoUri);
            } else if (requestCode == CROP_PHOTO_CODE) {
                String path = data.getStringExtra(CropImage.IMAGE_PATH);
                // cropped bitmap
                if (path == null) {
                    return;
                }
                photoBitmap = BitmapFactory.decodeFile(path);
                startPreviewPhotoActivity();
            } else if (requestCode == POST_PHOTO_CODE) {
                //PhotosActivity activity = ((PhotosActivity) getActivity());
                // @todo if api changes update this
               // Photo photo = new Photo();
               // photo.setId(data.getStringExtra("id"));
                mCallback.onPhotoPosted();


            }
        }
    }

    private void startPreviewPhotoActivity() {
        Intent i = new Intent(getActivity(), PreviewPhotoActivity.class);
        i.putExtra(PHOTO_BITMAP, photoBitmap);
        startActivityForResult(i, POST_PHOTO_CODE);
    }

    private void cropPhoto(Uri photoUri) {

        Intent intent = new Intent(getActivity(), CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, new File(mediaStorageDir.getPath() + File.separator + photoFileName).toString());
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, 1);
        intent.putExtra(CropImage.ASPECT_Y, 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        startActivityForResult(intent, CROP_PHOTO_CODE);
    }

    public Uri getPhotoFileUri(String fileName) {
        return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator
                + fileName));
    }

    public void signOut() {

        OAuthBaseClient.getInstance(getActivity().getApplicationContext(), null).clearTokens();
      // @todo usereditor.clear()
       // Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.bye), Toast.LENGTH_LONG).show();
        Intent bye = new Intent(getActivity(), LoginActivity.class);

        startActivity(bye);
    }

    abstract void customLoadMoreDataFromApi(int page);
}
