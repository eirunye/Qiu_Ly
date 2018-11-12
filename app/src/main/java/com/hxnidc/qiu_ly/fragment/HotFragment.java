package com.hxnidc.qiu_ly.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.flyco.tablayout.widget.MsgView;
import com.hxnidc.qiu_ly.R;
import com.hxnidc.qiu_ly.adapter.HotAdapter;

import butterknife.BindView;

/**
 * Created by on 2017/7/3 11:19
 * Author：yrg
 * Describe: HotFragment
 */


public class HotFragment extends BaseFragment implements ViewPager.OnPageChangeListener, OnTabSelectListener {

    @BindView(R.id.hot_segmentTabLayout)
    SegmentTabLayout hotSegmentTabLayout;
    @BindView(R.id.hot_viewPage)
    ViewPager hotViewPage;
    private HotAdapter mHotAdapter;

    public static HotFragment newInstance() {
        Bundle args = new Bundle();
        HotFragment fragment = new HotFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_hot;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

        String[] mTitles = {"热点", "推荐", "阅读"};
        hotSegmentTabLayout.setTabData(mTitles);
        hotViewPage.setOnPageChangeListener(this);
        hotSegmentTabLayout.setOnTabSelectListener(this);
        //mRadioGroup.setOnCheckedChangeListener(this);
        mHotAdapter = new HotAdapter(getActivity().getSupportFragmentManager(), mTitles);
        hotViewPage.setAdapter(mHotAdapter);
        hotViewPage.setCurrentItem(0);
        //mRadioGroup.check(mRadioGroup.getChildAt(0).getId());
        hotViewPage.setOffscreenPageLimit(3);

    }

    @Override
    protected void initData() {
        hotSegmentTabLayout.showDot(1);
        MsgView msgView = hotSegmentTabLayout.getMsgView(2);
        if (msgView != null)
            msgView.setBackgroundResource(R.color.colorPrimary);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        hotSegmentTabLayout.setCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabSelect(int position) {
        hotViewPage.setCurrentItem(position);

    }

    @Override
    public void onTabReselect(int position) {

    }
}
