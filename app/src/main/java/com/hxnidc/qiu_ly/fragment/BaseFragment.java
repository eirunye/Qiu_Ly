package com.hxnidc.qiu_ly.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxnidc.qiu_ly.activity.BaseActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by on 2017/7/1 22:41
 * Author：yrg
 * Describe:
 */


public abstract class BaseFragment extends Fragment {

    public BaseActivity activity;
    private Unbinder unbinder;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (BaseActivity) activity;
    }

    //获取布局文件ID
    protected abstract int getLayoutId();

    //获取宿主Activity
    protected BaseActivity getHoldingActivity() {
        return activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, view);
        initView(view, savedInstanceState);
        initData();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    protected abstract void initView(View view, Bundle savedInstanceState);

    /**
     * 初始化数据
     */
    abstract protected void initData();

    public void openFragmentToActivity(Context context, Class<?> clz) {
        Intent intent = new Intent(context, clz);
        startActivity(intent);
    }

}
