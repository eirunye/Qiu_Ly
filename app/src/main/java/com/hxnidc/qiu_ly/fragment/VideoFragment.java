package com.hxnidc.qiu_ly.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;

import com.flyco.tablayout.SegmentTabLayout;
import com.hxnidc.qiu_ly.R;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by on 2017/7/3 11:05
 * Author：yrg
 * Describe:
 */


public class VideoFragment extends BaseFragment {

    @BindView(R.id.video_segmentTabLayout)
    SegmentTabLayout videoSegmentTabLayout;
    @BindView(R.id.video_frameLayout)
    FrameLayout videoFrameLayout;
    ArrayList<Fragment> fragmentList = new ArrayList<>();
    VideoFirstFragment videoFirstFragment;
    VideoAttentionFragment videoAttentionFragment;

    private String[] mTitles = {"视频", "推荐"};

    public static VideoFragment newInstance() {
        Bundle args = new Bundle();
        VideoFragment fragment = new VideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {


    }

    @Override
    protected void initData() {
        try {
            videoFirstFragment = VideoFirstFragment.newInstance();
            videoAttentionFragment = VideoAttentionFragment.newInstance();
            fragmentList.add(0, videoFirstFragment);
            fragmentList.add(1, videoAttentionFragment);
            videoSegmentTabLayout.setTabData(mTitles, getActivity(), R.id.video_frameLayout, fragmentList);
        } catch (Exception e) {
            Logger.e(e.getMessage() + "");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (fragmentList != null) {
            fragmentList.clear();
        }
        if (videoFirstFragment != null) {
            videoFirstFragment = null;
        }
        if (videoAttentionFragment != null) {
            videoAttentionFragment = null;
        }
    }
}
