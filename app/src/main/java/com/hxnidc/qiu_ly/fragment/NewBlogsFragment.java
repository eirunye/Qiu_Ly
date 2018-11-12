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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hxnidc.qiu_ly.R;
import com.hxnidc.qiu_ly.activity.NewBlogsActivity;
import com.hxnidc.qiu_ly.adapter.BlogReaderAdapter;
import com.hxnidc.qiu_ly.bean.BlogNewReaderBean;
import com.hxnidc.qiu_ly.utils.JsoupUtil;
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
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by on 2017/7/3 16:54
 * Author：yrg
 * Describe:
 */


public class NewBlogsFragment extends Fragment implements LoadMoreWrapper.OnLoadMoreListener, android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.blogRecyclerView)
    RecyclerView blogRecyclerView;
    @BindView(R.id.SwipeRefreshLayout)
    SwipeRefreshLayout SwipeRefreshLayout;
    @BindView(R.id.loadingIndicatorView)
    AVLoadingIndicatorView loadingIndicatorView;
    @BindView(R.id.img_network)
    ImageView imgNetwork;
    Unbinder unbinder;

    private View footView;
    int page = 2;
    private BlogReaderAdapter blogReaderAdapter;
    private List<BlogNewReaderBean> blogNewReaderBeanList = new ArrayList<>();
    private LoadMoreWrapper mLoadMoreWrapper;
    private LinearLayout ll_footview;
    private ProgressBar progressBar;
    private TextView loading_text;

    public static NewBlogsFragment newInstance() {
        Bundle args = new Bundle();
        NewBlogsFragment fragment = new NewBlogsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newblogs, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        initData();
        return view;
    }

    private void init() {
        imgNetwork.setOnClickListener(v -> {
            initData();
        });
        SwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.accent);
        SwipeRefreshLayout.setOnRefreshListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        blogRecyclerView.setLayoutManager(linearLayoutManager);
        blogReaderAdapter = new BlogReaderAdapter(getActivity(), blogNewReaderBeanList);
        mLoadMoreWrapper = new LoadMoreWrapper(blogReaderAdapter);
        footView = LayoutInflater.from(getActivity()).inflate(R.layout.default_loading, blogRecyclerView, false);
        progressBar = (ProgressBar) footView.findViewById(R.id.ProgressBar);
        loading_text = (TextView) footView.findViewById(R.id.loading_text);
        //ll_footview = (LinearLayout) footView.findViewById(R.id.ll_footview);
        mLoadMoreWrapper.setLoadMoreView(footView);
        mLoadMoreWrapper.setOnLoadMoreListener(this);
        blogRecyclerView.setAdapter(mLoadMoreWrapper);
        blogReaderAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (blogReaderAdapter.getDatas() == null) return;
                Intent intent = new Intent(getActivity(), NewBlogsActivity.class);
                intent.putExtra("blogs", blogReaderAdapter.getDatas().get(position).getHref());
                intent.putExtra("imgUrl", blogReaderAdapter.getDatas().get(position).getImgUrl());
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

        if (loadingIndicatorView != null)
            loadingIndicatorView.show();

        getBlogsListData();

        viewShowOrHide();
    }

    private void viewShowOrHide() {
        imgNetwork.setVisibility(View.GONE);
    }

    private void getBlogsListData() {
        try {
            new Thread(() -> {
                List<BlogNewReaderBean> blogNewReaderBeanList = JsoupUtil.getBlogData("http://www.yxj18.com/xinwen/shehui/index_2.html");
                if (blogNewReaderBeanList == null || blogNewReaderBeanList.size() <= 0) {
                    UIUtils.postDelayed(() -> {
                        loading_text.setText("加载数据失败");
                        progressBar.setVisibility(View.GONE);
                        //UIUtils.postDelayed(() -> SwipeRefreshLayout.setRefreshing(false), 3000);
                        if (loadingIndicatorView != null)
                            loadingIndicatorView.hide();
                        return;
                    }, 4000);
                }
                this.blogNewReaderBeanList.addAll(blogNewReaderBeanList);
                UIUtils.postDelayed(() -> {
                    mLoadMoreWrapper.notifyDataSetChanged();
                    SwipeRefreshLayout.setRefreshing(false);
                    loading_text.setText("正在加载...");
                    progressBar.setVisibility(View.VISIBLE);
                    if (loadingIndicatorView != null)
                        loadingIndicatorView.hide();
                }, 5000);
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
     * 上拉加载
     */
    @Override
    public void onLoadMoreRequested() {

        if (NetWorkUtils.isDisconnectedByState(getActivity())) {
            progressBar.setVisibility(View.GONE);
            loading_text.setText("网络连接失败!");
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
        new Thread(() -> {
            List<BlogNewReaderBean> blogNewReaderBeanList = new ArrayList<BlogNewReaderBean>(JsoupUtil.getBlogData("http://www.yxj18.com/xinwen/shehui/index_" + page + ".html"));
            if (blogNewReaderBeanList == null || blogNewReaderBeanList.size() <= 0) {
                UIUtils.post(() -> {
                    progressBar.setVisibility(View.GONE);
                    loading_text.setText("加载数据失败!");
                    if (loadingIndicatorView != null)
                        loadingIndicatorView.hide();

                });
                return;
            }

            this.blogNewReaderBeanList.addAll(blogNewReaderBeanList);
            UIUtils.post(() -> {
                progressBar.setVisibility(View.VISIBLE);
                loading_text.setText("正在加载...");
                if (loadingIndicatorView != null)
                    loadingIndicatorView.hide();
                mLoadMoreWrapper.notifyDataSetChanged();
            });
        }).start();
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        try {
            UIUtils.post(() -> SwipeRefreshLayout.setRefreshing(true));
            if (NetWorkUtils.isDisconnectedByState(getActivity())) {
                SwipeRefreshLayout.setRefreshing(false);
                ToastUtils.showToast(getActivity(), "网络连接失败!");
                return;
            }
            new Thread(() -> {
                List<BlogNewReaderBean> blogNewReaderBeanList = new ArrayList<BlogNewReaderBean>(JsoupUtil.getBlogData("http://www.yxj18.com/xinwen/shehui/index_2.html"));
                if (blogNewReaderBeanList == null || blogNewReaderBeanList.size() <= 0) {
                    UIUtils.postDelayed(() -> {
                        SwipeRefreshLayout.setRefreshing(false);
                        progressBar.setVisibility(View.GONE);
                        loading_text.setText("数据加载失败!");
                    }, 2000);
                    return;
                }
                this.blogNewReaderBeanList.clear();
                this.blogNewReaderBeanList.addAll(blogNewReaderBeanList);
                UIUtils.post(() -> {
                    page = 2;
                    viewShowOrHide();
                    progressBar.setVisibility(View.VISIBLE);
                    loading_text.setText("正在加载...");
                    SwipeRefreshLayout.setRefreshing(false);
                    mLoadMoreWrapper.notifyDataSetChanged();
                });
            }).start();
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }

    }
}
