package com.hxnidc.qiu_ly.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hxnidc.qiu_ly.fragment.HotspotDetailsFragment;
import com.hxnidc.qiu_ly.fragment.NewBlogsFragment;
import com.hxnidc.qiu_ly.fragment.RecommendFragment;

/**
 * Created by on 2017/7/3 16:51
 * Author：yrg
 * Describe:
 */


public class HotAdapter extends FragmentPagerAdapter {
    private String[] mTitles;

    public HotAdapter(FragmentManager fm, String[] mTitles) {
        super(fm);
        this.mTitles = mTitles;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = HotspotDetailsFragment.newInstance();
                break;
            case 1:
                fragment = RecommendFragment.newInstance();
                break;
            case 2:
                fragment = NewBlogsFragment.newInstance();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
