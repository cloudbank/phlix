package com.anubis.flickr.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.anubis.flickr.FlickrClientApp;
import com.anubis.flickr.R;
import com.anubis.flickr.fragments.FriendsFragment;
import com.anubis.flickr.fragments.InterestingFragment;
import com.anubis.flickr.fragments.SearchFragment;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;

public class PhotosActivity extends FragmentActivity {

    String mLogin;
    private MyPagerAdapter adapterViewPager;
    private ViewPager vpPager;
    private String username;

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
        getLogin();

        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager(), intializeItems());
        vpPager = (ViewPager) findViewById(R.id.vpPager);
        vpPager.setOffscreenPageLimit(2);
        vpPager.setAdapter(adapterViewPager);
    }

    private void getLogin() {
        FlickrClientApp.getRestClient().testLogin(new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(Throwable arg0, String arg1) {
                Log.e("ERROR", "onFailure getLogin  " + arg0.getMessage() + ": " + arg1);
            }

            @Override
            public void onSuccess(String response) {
                mLogin = response.substring((response.indexOf("<username>")) + 10,
                        response.indexOf("</username>"));
                       /*.setText("Logged in as : "
                        + response.substring((response.indexOf("<username>")) + 10,
                                response.indexOf("</username>")));*/
                ActionBar ab = getActionBar();
                ab.setSubtitle(mLogin);
            }
        });
    }

    public ArrayList<Fragment> intializeItems() {
        ArrayList<Fragment> a = new ArrayList<Fragment>();
        a.add(FriendsFragment.newInstance(0, getResources().getString(R.string.friends_and_you), new FriendsFragment()));
        a.add(InterestingFragment.newInstance(1, getResources().getString(R.string.interesting_today), new InterestingFragment()));
        a.add(SearchFragment.newInstance(2, getResources().getString(R.string.search_by_tag_text), new SearchFragment()));
        return a;
    }
    //keep small number of pages in memory mostly
    public static class MyPagerAdapter extends FragmentPagerAdapter {
        public static int NUM_ITEMS = 3;
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
            return getItem(position).getArguments().getString("title");
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


}
