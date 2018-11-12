package com.hxnidc.qiu_ly.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.hxnidc.qiu_ly.R;

import butterknife.BindView;

public class CommentActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_comment;
    }

    @Override
    protected void initView() {

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(v -> this.finish());

    }

    @Override
    protected void initNewSave(Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {

    }
}
