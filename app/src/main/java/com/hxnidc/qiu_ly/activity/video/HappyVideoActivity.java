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
import com.hxnidc.qiu_ly.adapter.HappyMultiAdapter;
import com.hxnidc.qiu_ly.bean.model.HappyVideoModel;
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

public class HappyVideoActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, LoadMoreWrapper.OnLoadMoreListener {

    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.happyRecyclerView)
    RecyclerView happyRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.loadingIndicatorView)
    AVLoadingIndicatorView loadingIndicatorView;
    @BindView(R.id.img_network)
    ImageView imgNetwork;
    private View footView;
    private ProgressBar progressBar;
    private TextView loading_text;
    private LoadMoreWrapper mLoadMoreWrapper;
    private HappyMultiAdapter happyMultiAdapter;
    private List<HappyVideoModel> happyVideoModelList = new ArrayList<>();
    private int page = 1;
    private String http = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_happy_video;
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
        happyRecyclerView.setLayoutManager(layoutManager);
        happyMultiAdapter = new HappyMultiAdapter(this, happyVideoModelList);
        footView = LayoutInflater.from(activity).inflate(R.layout.default_loading, happyRecyclerView, false);
        progressBar = (ProgressBar) footView.findViewById(R.id.ProgressBar);
        loading_text = (TextView) footView.findViewById(R.id.loading_text);
        mLoadMoreWrapper = new LoadMoreWrapper(happyMultiAdapter);
        mLoadMoreWrapper.setLoadMoreView(footView);
        mLoadMoreWrapper.setOnLoadMoreListener(this);
        happyRecyclerView.setAdapter(mLoadMoreWrapper);
        happyMultiAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                HappyVideoModel videoModel = happyMultiAdapter.getDatas().get(position);
                if (videoModel == null) return;
                Intent intent = new Intent(HappyVideoActivity.this, HappyPlayerActivity.class);
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
        if (!NetWorkUtils.isConnectedByState(this)) {
            ToastUtils.showToast(this, getString(R.string.network_fail_string));
            imgNetwork.setVisibility(View.VISIBLE);
            return;
        }
        onLoadMoreRequested();
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
            //String url = Math.random() > 0.5 ? Https.QIU_LY_HAPPY_LIST1_HTTP : Https.QIU_LY_HAPPY_LIST2_HTTP;
            new Thread(() -> {

                List<HappyVideoModel> happyVideoModels = VideoJsoupController.Instance().getHappyVideoList(Https.QIU_LY_HAPPY_LIST1_HTTP);
                if (happyVideoModels == null || happyVideoModels.size() <= 0) {
                    ToastUtils.showToast(this, getString(R.string.network_fail_string));
                    UIUtils.postDelayed(() -> {
                        swipeRefreshLayout.setRefreshing(false);
                        if (this.happyVideoModelList.size() <= 0) {
                            imgNetwork.setVisibility(View.VISIBLE);
                        }
                    }, 2000);
                    return;
                }
                this.happyVideoModelList.clear();
                this.happyVideoModelList.addAll(happyVideoModels);
                UIUtils.post(() -> {
                    page = 1;
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

    @Override
    public void onLoadMoreRequested() {
        try {
            if (NetWorkUtils.isDisconnectedByState(this)) {
                ToastUtils.showToast(this, "网络请求失败,请检查网络!");
                return;
            }
            if (page == 5) {
                progressBar.setVisibility(View.GONE);
                loading_text.setText("没有数据了");
                return;
            } else {
                progressBar.setVisibility(View.VISIBLE);
                loading_text.setText("正在加载...");
            }

            page++;
            if (page == 1) {
                http = Https.QIU_LY_HAPPY_LIST1_HTTP;
                Logger.e("====1");
            } else if (page == 2) {

                Logger.e("====2");
            } else if (page == 3) {
                http = Https.QIU_LY_HAPPY_LIST3_HTTP;
            } else if (page == 4) {
                http = Https.QIU_LY_HAPPY_LIST4_HTTP;
            }

            new Thread(() -> {

                List<HappyVideoModel> videoList = VideoJsoupController.Instance().getHappyVideoList("http://www.joy.cn/entertainment/all");
                if (videoList == null || videoList.size() <= 0) {
                    UIUtils.post(() -> {
                        progressBar.setVisibility(View.GONE);
                        loading_text.setText("加载数据失败!");
                        if (loadingIndicatorView != null)
                            loadingIndicatorView.hide();
                        if (this.happyVideoModelList.size() <= 0) {
                            imgNetwork.setVisibility(View.VISIBLE);
                        }
                    });
                    return;
                }
                this.happyVideoModelList.addAll(videoList);
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
}
