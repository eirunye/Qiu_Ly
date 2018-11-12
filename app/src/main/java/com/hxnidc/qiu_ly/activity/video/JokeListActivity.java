package com.hxnidc.qiu_ly.activity.video;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hxnidc.qiu_ly.R;
import com.hxnidc.qiu_ly.activity.BaseActivity;
import com.hxnidc.qiu_ly.adapter.HotMultiAdapter;
import com.hxnidc.qiu_ly.bean.model.FirstVideoModel;
import com.hxnidc.qiu_ly.config.VideoJsoupController;
import com.hxnidc.qiu_ly.constant.Https;
import com.hxnidc.qiu_ly.utils.NetWorkUtils;
import com.hxnidc.qiu_ly.utils.ToastUtils;
import com.hxnidc.qiu_ly.utils.UIUtils;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class JokeListActivity extends BaseActivity implements LoadMoreWrapper.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.hotListRecyclerView)
    RecyclerView hotListRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.loadingIndicatorView)
    AVLoadingIndicatorView loadingIndicatorView;
    @BindView(R.id.img_network)
    ImageView imgNetwork;
    private ProgressBar progressBar;
    private TextView loading_text;
    private List<FirstVideoModel> listVideoModelList = new ArrayList<>();
    private int page = 1;
    private HotMultiAdapter hotMultiAdapter;
    private View footView;
    private LoadMoreWrapper mLoadMoreWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_joke_list;
    }

    @Override
    protected void initView() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> this.finish());
        getSupportActionBar().setTitle("");
        imgNetwork.setOnClickListener(v -> {
            if (loadingIndicatorView != null)
                loadingIndicatorView.show();
            onLoadMoreRequested();
        });
    }

    @Override
    protected void initNewSave(Bundle savedInstanceState) {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.accent);
        swipeRefreshLayout.setOnRefreshListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        hotListRecyclerView.setLayoutManager(layoutManager);
        hotMultiAdapter = new HotMultiAdapter(this, listVideoModelList);
        footView = LayoutInflater.from(activity).inflate(R.layout.default_loading, hotListRecyclerView, false);
        progressBar = (ProgressBar) footView.findViewById(R.id.ProgressBar);
        loading_text = (TextView) footView.findViewById(R.id.loading_text);
        mLoadMoreWrapper = new LoadMoreWrapper(hotMultiAdapter);
        mLoadMoreWrapper.setLoadMoreView(footView);
        mLoadMoreWrapper.setOnLoadMoreListener(this);
        hotListRecyclerView.setAdapter(mLoadMoreWrapper);
        hotMultiAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                FirstVideoModel videoModel = hotMultiAdapter.getDatas().get(position);
                if (videoModel == null) return;
                Intent intent = new Intent(JokeListActivity.this, JokeTvActivity.class);
                intent.putExtra("hrefUrl", videoModel.getHref());
                intent.putExtra("imgUrl", videoModel.getImgUrl());
                intent.putExtra("title", videoModel.getTitle());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (intent != null)
            page = Integer.parseInt(intent.getStringExtra("page"));

        if (!NetWorkUtils.isConnectedByState(this)) {
            ToastUtils.showToast(this, getString(R.string.network_fail_string));
            imgNetwork.setVisibility(View.VISIBLE);
            // return;
        }
        Logger.e("page:" + page);
        onLoadMoreRequested();

    }

    @Override
    public void onLoadMoreRequested() {

        try {
            if (NetWorkUtils.isDisconnectedByState(this)) {
                ToastUtils.showToast(this, "网络请求失败,请检查网络!");
                return;
            }
            if (page == 30) {
                progressBar.setVisibility(View.GONE);
                loading_text.setText("没有数据了");
                return;
            } else {
                progressBar.setVisibility(View.VISIBLE);
                loading_text.setText("正在加载...");
            }
            page++;
            new Thread(() -> {

                List<FirstVideoModel> firstModelList = VideoJsoupController.Instance().firstVideoData(Https.QIU_LY_HOT_FIRST_HTTP + page + Https.QIU_LY_HTML_STRING, "http://v.dzwww.com/ksp");
                if (firstModelList == null || firstModelList.size() <= 0) {
                    UIUtils.post(() -> {
                        progressBar.setVisibility(View.GONE);
                        loading_text.setText("加载数据失败!");
                        if (loadingIndicatorView != null)
                            loadingIndicatorView.hide();
                    });
                    return;
                }
                //Collections.shuffle(firstModelList);
                this.listVideoModelList.addAll(firstModelList);
                UIUtils.post(() -> {
                    progressBar.setVisibility(View.VISIBLE);
                    loading_text.setText("正在加载...");
                    if (loadingIndicatorView != null)
                        loadingIndicatorView.hide();
                    mLoadMoreWrapper.notifyDataSetChanged();
                });
            }).start();

        } catch (Exception e) {
        }

    }

    @Override
    public void onRefresh() {
        try {
            UIUtils.post(() -> swipeRefreshLayout.setRefreshing(true));
            if (NetWorkUtils.isDisconnectedByState(this)) {
                swipeRefreshLayout.setRefreshing(false);
                ToastUtils.showToast(this, "网络请求失败,请检查网络!");
                return;
            }
            String url = Math.random() > 0.5 ? "http://v.dzwww.com/ksp/" : "http://v.dzwww.com/ksp/default_1.htm";
            String httpHead = "http://v.dzwww.com/ksp";
            new Thread(() -> {

                List<FirstVideoModel> FirstModelList = VideoJsoupController.Instance().firstVideoData(url, httpHead);
                if (FirstModelList == null || FirstModelList.size() <= 0) {
                    ToastUtils.showToast(this, getString(R.string.network_fail_string));
                    UIUtils.postDelayed(() -> swipeRefreshLayout.setRefreshing(false), 2000);
                    return;
                }
                this.listVideoModelList.clear();
                this.listVideoModelList.addAll(FirstModelList);
                UIUtils.post(() -> {
                    page = 3;
                    viewShowOrHide();
                    swipeRefreshLayout.setRefreshing(false);
                    mLoadMoreWrapper.notifyDataSetChanged();
                });
            }).start();
        } catch (Exception e) {
            Logger.e(e.getMessage() + "");
        }
    }

    private void viewShowOrHide() {
        imgNetwork.setVisibility(View.GONE);
    }
}
