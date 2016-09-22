package com.anubis.flickr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anubis.flickr.R;
import com.anubis.flickr.models.FlickrPhoto;
import com.anubis.flickr.models.FriendsFlickrPhoto;
import com.anubis.flickr.util.DateUtility;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PhotoArrayAdapter extends ArrayAdapter<FlickrPhoto> {
    private int mLayout = 0;

    //@todo is there a better pattern
    public PhotoArrayAdapter(Context context, List<FlickrPhoto> photoList,
                             boolean listview) {
        super(context, R.layout.photo_item_friends, photoList);
        this.mLayout = R.layout.photo_item_friends;
    }

    public PhotoArrayAdapter(Context context, List<FlickrPhoto> photoList) {
        super(context, R.layout.photo_item, photoList);
        this.mLayout = R.layout.photo_item;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FlickrPhoto photo = this.getItem(position);
        //ImageLoader imageLoader = ImageLoader.getInstance();
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflator = LayoutInflater.from(getContext());
            convertView = (RelativeLayout) inflator.inflate(mLayout, parent,
                    false);
            viewHolder.ivImage = (ImageView) convertView.findViewById(R.id.ivPhoto);

            if (mLayout == R.layout.photo_item_friends) {
                viewHolder.name = (TextView) convertView.findViewById(R.id.friends_username);
                viewHolder.timestamp = (TextView) convertView.findViewById(R.id.friends_timestamp);
                viewHolder.title = (TextView) convertView.findViewById(R.id.friends_title);
            }
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (mLayout == R.layout.photo_item_friends) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) viewHolder.ivImage
                    .getLayoutParams();
            lp.height = 600; // photo.getPhotoHeight() * 2;
            lp.width = 600; //
            viewHolder.ivImage.setLayoutParams(lp);

            viewHolder.name.setText(((FriendsFlickrPhoto) photo).getName());
            viewHolder.title.setText(photo.getTitle());
            viewHolder.timestamp.setText(DateUtility.relativeTime(photo.getDateTaken(),
                    this.getContext()));

        } else {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) viewHolder.ivImage
                    .getLayoutParams();
            lp.height = 200; // photo.getPhotoHeight() * 2;
            lp.width = 200; // photo.getPhoto//set the title, name, comments
            viewHolder.ivImage.setLayoutParams(lp);
        }
        //imageLoader.displayImage(photo.getUrl(), viewHolder.ivImage);
        Picasso.with(this.getContext()).load(photo.getUrl()).fit().centerCrop()
                .placeholder(android.R.drawable.btn_star)
                .error(android.R.drawable.btn_star)
                .into(viewHolder.ivImage);
        return convertView;
    }

    private static class ViewHolder {
        TextView name;
        TextView title;
        ImageView ivImage;
        TextView timestamp;
    }

}
