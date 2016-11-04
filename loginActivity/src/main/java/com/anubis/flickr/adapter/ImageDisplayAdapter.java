package com.anubis.flickr.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anubis.flickr.R;
import com.anubis.flickr.models.Comment;
import com.anubis.flickr.util.Util;

import java.util.List;

/**
 * Created by sabine on 9/26/16.
 */

public class ImageDisplayAdapter extends RecyclerView.Adapter<ImageDisplayAdapter.ViewHolder> {


    // Define listener member variable
    private OnItemClickListener listener;
    protected SharedPreferences prefs;
    protected SharedPreferences.Editor editor;


    public OnItemClickListener getListener(){
        return this.listener;

    }
    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        TextView content;
        TextView author;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(final View itemView, final OnItemClickListener listener) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (listener != null && position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });
             author = (TextView)itemView.findViewById(R.id.author);
             content= (TextView) itemView.findViewById(R.id.content);

        }
    }

    // Store a member variable for the contacts
    private List<Comment> mComments;
    // Store the context for easy access
    private Context mContext;
    private boolean mStaggered;

    // Pass in the contact array into the constructor
    public ImageDisplayAdapter(Context context, List<Comment> comments, boolean staggered) {
        mStaggered = staggered;
        mComments = comments;
        mContext = context;
        this.prefs = Util.getUserPrefs();
        this.editor = this.prefs.edit();

    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ImageDisplayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View photosView = inflater.inflate(R.layout.comment_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(photosView, getListener());
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ImageDisplayAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Comment comment = mComments.get(position);

        TextView author = viewHolder.author;
        TextView content = viewHolder.content;
        // Set item views based on your views and data model
        author.setText(comment.getAuthorname());
        content.setText(Html.fromHtml(comment.getContent()));
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mComments.size();
    }



}
