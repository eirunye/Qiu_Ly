package com.hxnidc.qiu_ly.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.hxnidc.qiu_ly.R;

import butterknife.BindView;

/**
 * Created by on 2017/6/22 14:52
 * Author：yrg
 * Describe:AboutActivity
 */

public class AboutActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        //setContentView(R.layout.activity_about);
        return R.layout.activity_about;
    }

    @Override
    protected void initView() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> this.finish());
        getSupportActionBar().setTitle("");

    }

    @Override
    protected void initNewSave(Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {


    }
}
