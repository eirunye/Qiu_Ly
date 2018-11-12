package com.hxnidc.qiu_ly.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.SearchView;

import com.hxnidc.qiu_ly.R;

import butterknife.BindView;

public class SearchActivity extends BaseActivity {

    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setContentView(R.layout.activity_search);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> this.finish());
        getSupportActionBar().setTitle("");
        searchView.requestFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    @Override
    protected void initNewSave(Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {

    }
}
