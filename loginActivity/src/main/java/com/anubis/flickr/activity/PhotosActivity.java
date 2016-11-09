package com.anubis.flickr.activity;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.HandlerThread;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.anubis.flickr.FlickrClientApp;
import com.anubis.flickr.R;
import com.anubis.flickr.fragments.FlickrBaseFragment;
import com.anubis.flickr.fragments.FriendsFragment;
import com.anubis.flickr.fragments.InterestingFragment;
import com.anubis.flickr.fragments.SearchFragment;
import com.anubis.flickr.fragments.TagsFragment;
import com.anubis.flickr.models.Photos;
import com.anubis.flickr.sync.SyncAdapter;
import com.anubis.flickr.util.Util;

import java.util.ArrayList;

import rx.Subscription;

public class PhotosActivity extends AppCompatActivity implements FlickrBaseFragment.OnPhotoPostedListener {

    private MyPagerAdapter adapterViewPager;
    private ViewPager vpPager;

    protected SharedPreferences prefs;
    protected SharedPreferences.Editor editor;
    private Subscription subscription;
    private Photos mPhotos;
    HandlerThread handlerThread;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    public void onPhotoPosted() {
        //==there are 50 max in friends call
        // ==you cannot get the url from the post
        // ==you have to build the url from a network call to e.g. to getphotoInfo anyway
        // and put it in the photo cache manually and then refresh the viewpager
        //just call friends and get it all in one shot w updates to friend photos as bonus
        //
        //FriendsFragment f = (FriendsFragment)adapterViewPager.getItem(0);
        // @todo
        //getFriendsList()
        Log.d("POST", "callback");
        vpPager.setCurrentItem(0);
    }


    public ViewPager getVpPager() {
        return vpPager;
    }

    public MyPagerAdapter getAdapterViewPager() {
        return adapterViewPager;
    }

    public void activateProgressBar(boolean activate) {
        setProgressBarIndeterminateVisibility(activate);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        //oauthkit shared prefs
        SharedPreferences authPrefs = getApplicationContext().getSharedPreferences(getString(R.string.OAuthKit_Prefs), 0);

        if (!Util.getCurrentUser().equals(authPrefs.getString(getString(R.string.username), ""))) {
            //@todo stop the sync adapter and restart
            //find out how to properly stop before restart
            ContentResolver.cancelSync(new Account(authPrefs.getString(getString(R.string.username),""), getApplication().getString(R.string.account_type)), getApplication().getString(R.string.authority));
            // could also cancelSync(null);
            updateUserInfo(authPrefs);
        }
        //@todo check that sync adapter is running as planned for repeat login
        //this only runs the sync if no account account exists; else it should be running
        SyncAdapter.initializeSyncAdapter(this);


        setContentView(R.layout.activity_photos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_rocket);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        //getSupportActionBar().setElevation(3);
        //getSupportActionBar().setTitle(R.string.app_name);
        toolbar.setTitleTextColor(getResources().getColor(R.color.Seashell));
        getSupportActionBar().setSubtitle(Util.getCurrentUser());
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.Azure));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.friends_and_you));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.interesting_today));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tags));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.commons_search));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager(), intializeItems());
        vpPager = (ViewPager) findViewById(R.id.vpPager);
        vpPager.setOffscreenPageLimit(3);
        vpPager.setAdapter(adapterViewPager);
        vpPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(onTabSelectedListener(vpPager));


    }

    private void updateUserInfo(SharedPreferences authPrefs) {

        this.prefs = Util.getUserPrefs();
        this.editor = this.prefs.edit();

        editor.putString(getApplicationContext().getString(R.string.current_user), authPrefs.getString(getApplicationContext().getString(R.string.username), ""));
        editor.putString(getApplicationContext().getString(R.string.user_id), authPrefs.getString(getApplicationContext().getString(R.string.user_nsid), ""));

        editor.commit();
        // in a bg thread


    }



    private TabLayout.OnTabSelectedListener onTabSelectedListener(final ViewPager pager) {
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
    }


    public ArrayList<Fragment> intializeItems() {
        ArrayList<Fragment> a = new ArrayList<Fragment>();
        a.add(FriendsFragment.newInstance(0, getResources().getString(R.string.friends_and_you), new FriendsFragment()));
        a.add(InterestingFragment.newInstance(1, getResources().getString(R.string.interesting_today), new InterestingFragment()));
        a.add(SearchFragment.newInstance(2, getResources().getString(R.string.tags), new TagsFragment()));
        a.add(SearchFragment.newInstance(3, getResources().getString(R.string.commons_search), new SearchFragment()));
        return a;
    }


    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();


    }

    //keep small number of pages in memory mostly
    public static class MyPagerAdapter extends FragmentPagerAdapter {
        public static int NUM_ITEMS = 4;
        public FragmentManager mFragmentManager;
        private ArrayList<Fragment> mPagerItems;

        public MyPagerAdapter(FragmentManager fragmentManager, ArrayList<Fragment> pagerItems) {
            super(fragmentManager);
            this.mFragmentManager = fragmentManager;
            mPagerItems = pagerItems;
        }

        public void setPagerItems(ArrayList<Fragment> pagerItems) {
            if (mPagerItems != null)
                for (int i = 0; i < mPagerItems.size(); i++) {
                    mFragmentManager.beginTransaction().remove(mPagerItems.get(i))
                            .commit();
                }
            mPagerItems = pagerItems;
        }

        // Returns the fragment to display for that page

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return mPagerItems.get(0);
                case 1:
                    return mPagerItems.get(1);
                case 2:
                    return mPagerItems.get(2);
                case 3:
                    return mPagerItems.get(3);
                default:
                    return null;
            }
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return getItem(position).getArguments().getString(FlickrClientApp.getAppContext().getString(R.string.title));
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return mPagerItems.size();
        }

        public Fragment getPagerItem(int i) {
            return mPagerItems.get(i);
        }

        public void setPagerItem(FriendsFragment f) {
            mPagerItems.remove(0);
            mPagerItems.add(0, f);

        }

    }

    @Override
    protected void onDestroy() {
        //this.subscription.unsubscribe();
        super.onDestroy();

    }
}
