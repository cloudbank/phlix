package com.anubis.flickr.activity;

import android.content.SharedPreferences;
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
import com.anubis.flickr.models.Photos;
import com.anubis.flickr.models.User;

import java.util.ArrayList;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class PhotosActivity extends FragmentActivity {

    private MyPagerAdapter adapterViewPager;
    private ViewPager vpPager;

    protected SharedPreferences prefs;
    protected SharedPreferences.Editor editor;
    private Subscription subscription;
    private Photos mPhotos;

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
        this.prefs = this.getBaseContext().getSharedPreferences("user_prefs", 0);
        this.editor = this.prefs.edit();

        //getLogin();

        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager(), intializeItems());
        vpPager = (ViewPager) findViewById(R.id.vpPager);
        vpPager.setOffscreenPageLimit(2);
        vpPager.setAdapter(adapterViewPager);

    }

    private void getLogin() {
         subscription =  FlickrClientApp.getService().testLogin()
            .concatMap(new Func1<User, Observable<Photos>>() {
                @Override
                public Observable<Photos> call(User user) {
                    //username = user.getUser().getUsername().getContent();
                    return FlickrClientApp.getService().getFriendsPhotos(user.getUser().getId());

                }
            }).subscribeOn(Schedulers.io()) // optional if you do not wish to override the default behavior
                .observeOn(AndroidSchedulers.mainThread())
                     .subscribe(new Subscriber<Photos>() {
                    @Override
                    public void onCompleted() {
                        //Log.d("DEBUG","oncompleted");

                    }
                    @Override
                    public void onError(Throwable e) {
                        // cast to retrofit.HttpException to get the response code
                        if (e instanceof HttpException) {
                            HttpException response = (HttpException)e;
                            int code = response.code();
                            Log.e("ERROR",  String.valueOf(code));
                        }
                        Log.e("ERROR",  "error getting login/photos" + e);
                    }

                    @Override
                    public void onNext(Photos p ) {
                       // Log.d("DEBUG","mlogin: "+ u.getUser().getUsername().getContent());
                       //pass photos to fragment
                        mPhotos = p;
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

    @Override
    protected void onDestroy() {
        this.subscription.unsubscribe();
        super.onDestroy();

    }
}
