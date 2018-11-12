package com.hxnidc.qiu_ly.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxnidc.qiu_ly.R;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by on 2017/9/9 15:52
 * Author：yrg
 * Describe:
 */


public class ReMostCommentFragment extends DialogFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final ReMostCommentFragment instance = new ReMostCommentFragment();


    public static ReMostCommentFragment getInstance() {
        return instance;
    }

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.loadingIndicatorView)
    AVLoadingIndicatorView loadingIndicatorView;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    Window window;
    Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar);
    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
////        window = getDialog().getWindow();
////        window.requestFeature(Window.FEATURE_NO_TITLE);
////        super.onActivityCreated(savedInstanceState);
////        window.setBackgroundDrawable(new ColorDrawable(0x00000000));
////        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_most_comment, container, false);

        unbinder = ButterKnife.bind(this, view);
        initView(view);
        initData();
        return view;
    }


    private void initView(View view) {

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.accent);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void initData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {

    }


    @OnClick(R.id.img_back)
    public void onViewClicked() {
        this.dismiss();
    }
}
