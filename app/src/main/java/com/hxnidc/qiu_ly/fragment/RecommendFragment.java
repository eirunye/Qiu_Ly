package com.hxnidc.qiu_ly.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hxnidc.qiu_ly.R;
import com.hxnidc.qiu_ly.activity.RecommendDetailsActivity;
import com.hxnidc.qiu_ly.bean.RecomBean;
import com.hxnidc.qiu_ly.utils.ImageUtils;
import com.hxnidc.qiu_ly.utils.JsoupUtil;
import com.hxnidc.qiu_ly.utils.NetWorkUtils;
import com.hxnidc.qiu_ly.utils.ToastUtils;
import com.hxnidc.qiu_ly.utils.UIUtils;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by on 2017/7/3 16:54
 * Author：yrg
 * Describe:
 */


public class RecommendFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, LoadMoreWrapper.OnLoadMoreListener {

    @BindView(R.id.RecomRecyclerView)
    RecyclerView RecomRecyclerView;
    @BindView(R.id.recomSwipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.img_network)
    ImageView imgNetwork;
    @BindView(R.id.loadingIndicatorView)
    AVLoadingIndicatorView loadingIndicatorView;
    private List<RecomBean> recomBeanList = new ArrayList<>();
    private CommonAdapter<RecomBean> adapter;
    private LoadMoreWrapper mLoadMoreWrapper;
    Unbinder unbinder;
    private int page = 2;
    private View footView;
    private ProgressBar progressBar;
    private TextView loading_text;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;

    public static RecommendFragment newInstance() {
        Bundle args = new Bundle();
        RecommendFragment fragment = new RecommendFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        initData();
        return view;
    }


    private void init() {
        imgNetwork.setOnClickListener(v -> {
            initData();
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.accent);
        swipeRefreshLayout.setOnRefreshListener(this);
        RecomRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CommonAdapter<RecomBean>(getActivity(), R.layout.recomm_rv_items_layout, recomBeanList) {
            @Override
            protected void convert(ViewHolder holder, RecomBean recomBean, int position) {
                if (recomBean == null) return;
                holder.setText(R.id.te_rv_title, recomBean.getTitle());
                holder.setText(R.id.tv_recommend_time, recomBean.getTime());
                holder.setText(R.id.tv_recommend_tag, recomBean.getTag().replaceAll("\\d+", "").replaceAll("-", ""));
                ImageView imageView = (ImageView) holder.getView(R.id.img_rv_Pic);
                ImageUtils.loadImgUrl(getActivity(), recomBean.getImgUrl(), imageView);
            }
        };


//        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(adapter);
//        TextView t1 = new TextView(getActivity());
//        t1.setText("Header 1");
//        mHeaderAndFooterWrapper.addFootView(t1);


        mLoadMoreWrapper = new LoadMoreWrapper(adapter);
        footView = LayoutInflater.from(getActivity()).inflate(R.layout.default_loading, RecomRecyclerView, false);
        progressBar = (ProgressBar) footView.findViewById(R.id.ProgressBar);
        loading_text = (TextView) footView.findViewById(R.id.loading_text);
        mLoadMoreWrapper.setLoadMoreView(footView);
        mLoadMoreWrapper.setOnLoadMoreListener(this);
        RecomRecyclerView.setAdapter(mLoadMoreWrapper);

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (adapter.getDatas() == null) return;
                Intent intent = new Intent(getActivity(), RecommendDetailsActivity.class);
                intent.putExtra("recommendHref", adapter.getDatas().get(position).getHref());
                intent.putExtra("recommendImgUrl", adapter.getDatas().get(position).getImgUrl());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }

    private void initData() {
        if (!NetWorkUtils.isConnectedByState(getActivity())) {
            ToastUtils.showToast(getActivity(), "网络连接失败!");
            imgNetwork.setVisibility(View.VISIBLE);
            return;
        }
        if (loadingIndicatorView != null) {
            loadingIndicatorView.show();
        }
        getRecommendListData();
        viewShowOrHide();
    }

    private void viewShowOrHide() {
        imgNetwork.setVisibility(View.GONE);
    }

    private void getRecommendListData() {
        try {
            //String url = "http://www.cnys.com/sjys/" + page + ".html";
            //String urls = "http://www.cnys.com/yscs/2.html";
            String url = Math.random() > 0.5 ? "http://www.cnys.com/sjys/2.html" : "http://www.cnys.com/yscs/2.html";

            new Thread(() -> {
                List<RecomBean> recomBeanList = JsoupUtil.getRecomData(url, 1);
                if (recomBeanList == null || recomBeanList.size() <= 0) {
                    UIUtils.postDelayed(() -> {
                        if (loadingIndicatorView != null) {
                            loadingIndicatorView.hide();
                        }

                    }, 5000);
                    return;
                }
                this.recomBeanList.addAll(recomBeanList);
                UIUtils.post(() -> {
                    mLoadMoreWrapper.notifyDataSetChanged();
                    if (loadingIndicatorView != null) {
                        loadingIndicatorView.hide();
                    }
                });
            }).start();

        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        try {
            UIUtils.post(() -> swipeRefreshLayout.setRefreshing(true));
            if (NetWorkUtils.isDisconnectedByState(getActivity())) {
                swipeRefreshLayout.setRefreshing(false);
                ToastUtils.showToast(getActivity(), getString(R.string.network_fail_string));
                return;
            }
            String url = Math.random() > 0.5 ? "http://www.cnys.com/sjys/2.html" : "http://www.cnys.com/yscs/2.html";
            new Thread(() -> {
                List<RecomBean> recomBeanList = JsoupUtil.getRecomData(url, 1);
                if (recomBeanList == null || recomBeanList.size() <= 0) {
                    ToastUtils.showToast(getActivity(), getString(R.string.network_fail_string));
                    UIUtils.postDelayed(() -> swipeRefreshLayout.setRefreshing(false), 2000);
                    return;
                }
                this.recomBeanList.clear();
                this.recomBeanList.addAll(recomBeanList);
                UIUtils.post(() -> {
                    page = 2;
                    viewShowOrHide();
                    swipeRefreshLayout.setRefreshing(false);
                    mLoadMoreWrapper.notifyDataSetChanged();
                });
            }).start();
        } catch (Exception e) {
            Logger.e(e.getMessage() + "");
        }
    }

    /**
     * 上拉加载
     */
    @Override
    public void onLoadMoreRequested() {
        try {
            if (NetWorkUtils.isDisconnectedByState(getActivity())) {
                progressBar.setVisibility(View.GONE);
                loading_text.setText(getString(R.string.network_fail_string));
                return;
            }
            if (page == 6) {
                progressBar.setVisibility(View.GONE);
                loading_text.setText("没有数据了");
                return;
            } else {
                progressBar.setVisibility(View.VISIBLE);
                loading_text.setText("正在加载...");
            }
            page++;
            String url = Math.random() > 0.5 ? "http://www.cnys.com/sjys/" + page + ".html" : "http://www.cnys.com/yscs/" + page + ".html";
            new Thread(() -> {
                List<RecomBean> recomBeanList = new ArrayList<RecomBean>(JsoupUtil.getRecomData(url, 1));
                if (recomBeanList == null || recomBeanList.size() <= 0) {
                    UIUtils.post(() -> {
                        progressBar.setVisibility(View.GONE);
                        loading_text.setText("加载数据失败!");
                        if (loadingIndicatorView != null)
                            loadingIndicatorView.hide();
                    });
                    return;
                }
                this.recomBeanList.addAll(recomBeanList);
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
