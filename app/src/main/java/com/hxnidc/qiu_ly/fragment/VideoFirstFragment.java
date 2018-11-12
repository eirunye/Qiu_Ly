package com.hxnidc.qiu_ly.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hxnidc.qiu_ly.R;
import com.hxnidc.qiu_ly.activity.video.ArtsOrSportsActivity;
import com.hxnidc.qiu_ly.activity.video.HappyVideoActivity;
import com.hxnidc.qiu_ly.activity.video.HotListVideoActivity;
import com.hxnidc.qiu_ly.activity.video.JokeListActivity;
import com.hxnidc.qiu_ly.activity.video.JokeTvActivity;
import com.hxnidc.qiu_ly.bean.model.FirstVideoModel;
import com.hxnidc.qiu_ly.config.VideoJsoupController;
import com.hxnidc.qiu_ly.constant.Https;
import com.hxnidc.qiu_ly.utils.HorizonGetDataUtils;
import com.hxnidc.qiu_ly.utils.ImageUtils;
import com.hxnidc.qiu_ly.utils.NetWorkUtils;
import com.hxnidc.qiu_ly.utils.ToastUtils;
import com.hxnidc.qiu_ly.utils.UIUtils;
import com.orhanobut.logger.Logger;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.wang.avi.AVLoadingIndicatorView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by on 2017/7/24 15:46
 * Author：yrg
 * Describe:
 */


public class VideoFirstFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, LoadMoreWrapper.OnLoadMoreListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.content_recyclerView)
    RecyclerView contentRecyclerView;
    @BindView(R.id.first_swipeRefreshLayout)
    SwipeRefreshLayout firstSwipeRefreshLayout;
    @BindView(R.id.loadingIndicatorView)
    AVLoadingIndicatorView loadingIndicatorView;
    @BindView(R.id.img_network)
    ImageView imgNetwork;
    private View footView;
    private ProgressBar progressBar;
    private TextView loading_text;
    private LoadMoreWrapper mLoadMoreWrapper;

    private CommonAdapter<String> horizontalAdapter;
    private List<String> horizontalFirstList = new ArrayList<>();
    private List<FirstVideoModel> firstVideoModelList = new ArrayList<>();

    private CommonAdapter<FirstVideoModel> contentAdapter;
    private int page = 3;
    private ShineButton shineButton;
    TextView tv_first_time;

    public static VideoFirstFragment newInstance() {
        Bundle args = new Bundle();
        VideoFirstFragment fragment = new VideoFirstFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video_first_layout;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        imgNetwork.setOnClickListener(v -> {
            initData();
        });
        initSetAdapter();

    }

    private void initSetAdapter() {
        try {
            firstSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.accent);
            firstSwipeRefreshLayout.setOnRefreshListener(this);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
            LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
            recyclerView.setLayoutManager(linearLayoutManager);
            contentRecyclerView.setLayoutManager(layoutManager);

            horizontalAdapter = new CommonAdapter<String>(getActivity(), R.layout.rv_item_horizontal_layout, horizontalFirstList) {
                @Override
                protected void convert(ViewHolder holder, String data, int position) {
                    if (TextUtils.isEmpty(data)) return;
                    holder.setText(R.id.tv_rv_item, data);
                }
            };
            recyclerView.setAdapter(horizontalAdapter);

            horizontalAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    if (horizontalAdapter.getDatas() == null) return;
                    if (position == 0) {
                        Intent intent = new Intent(getActivity(), HotListVideoActivity.class);
                        intent.putExtra("page", page + "");
                        startActivity(intent);
                    }
                    //openFragmentToActivity(getActivity(), PlayerActivity.class);
                    else if (position == 1) {
                        Intent intent = new Intent(getActivity(), JokeListActivity.class);
                        intent.putExtra("page", page + "");
                        startActivity(intent);
                    } else if (position == 2) {
                        openFragmentToActivity(getActivity(), HappyVideoActivity.class);
                    } else if (position == 3) {
                        openFragmentToActivity(getActivity(), ArtsOrSportsActivity.class);
                    }
                    //openFragmentToActivity(getActivity(), VideoFirstActivity.class);
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });

            contentAdapter = new CommonAdapter<FirstVideoModel>(getActivity(), R.layout.item_rv_firstvideo_layout, firstVideoModelList) {
                @Override
                protected void convert(ViewHolder holder, FirstVideoModel firstVideoModel, int position) {
                    if (firstVideoModel == null) return;
                    holder.setText(R.id.tv_first_title, firstVideoModel.getTitle());
                    holder.setText(R.id.tv_first_time, (int) (Math.random() * 100 + Math.random() * 4) + "");
                    tv_first_time = (TextView) holder.getView(R.id.tv_first_time);
                    shineButton = (ShineButton) holder.getView(R.id.ShineButton);
                    shineButton.setOnCheckStateChangeListener((view, checked) -> {
                        Logger.e(checked + "");
                        if (checked) {
                            tv_first_time.setText(Integer.parseInt(tv_first_time.getText().toString()) + 1 + "");
                        } else {
                            tv_first_time.setText(Integer.parseInt(tv_first_time.getText().toString()) - 1 + "");
                        }
                    });
                    ImageView imageView = (ImageView) holder.getView(R.id.img_first_video);
                    ImageUtils.loadImgUrl(getActivity(), firstVideoModel.getImgUrl(), imageView);
                }
            };
            mLoadMoreWrapper = new LoadMoreWrapper(contentAdapter);
            footView = LayoutInflater.from(getActivity()).inflate(R.layout.default_loading, contentRecyclerView, false);
            progressBar = (ProgressBar) footView.findViewById(R.id.ProgressBar);
            loading_text = (TextView) footView.findViewById(R.id.loading_text);
            if (page <= 30) {
                loading_text.setOnClickListener(v -> onLoadMoreRequested());
            }
            mLoadMoreWrapper.setLoadMoreView(footView);
            mLoadMoreWrapper.setOnLoadMoreListener(this);
            contentRecyclerView.setAdapter(mLoadMoreWrapper);
            contentAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    Intent intent = new Intent(getActivity(), JokeTvActivity.class);
                    intent.putExtra("hrefUrl", contentAdapter.getDatas().get(position).getHref());
                    intent.putExtra("imgUrl", contentAdapter.getDatas().get(position).getImgUrl());
                    intent.putExtra("title", contentAdapter.getDatas().get(position).getTitle());
                    startActivity(intent);
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
        } catch (Exception e) {

        }
        getHorizontalData();
    }

    @Override
    protected void initData() {

        if (!NetWorkUtils.isConnectedByState(getActivity())) {
            ToastUtils.showToast(getActivity(), getString(R.string.network_fail_string));
            imgNetwork.setVisibility(View.VISIBLE);
            return;
        }

        if (loadingIndicatorView != null)
            loadingIndicatorView.show();

        getFirstVideoData();

        viewShowOrHide();
    }

    private void viewShowOrHide() {
        imgNetwork.setVisibility(View.GONE);
    }

    /**
     * 获取视频资源
     */
    private void getFirstVideoData() {

        try {

            new Thread(() -> {

                List<FirstVideoModel> videoModelList = VideoJsoupController.Instance().firstVideoData("http://dv.dzwww.com/gn/", "http://dv.dzwww.com/gn");

                if (videoModelList == null || videoModelList.size() <= 0) {
                    UIUtils.post(() -> {
                        if (loadingIndicatorView != null)
                            loadingIndicatorView.hide();
                        footView.setVisibility(View.VISIBLE);
                    });
                    return;
                }
                firstVideoModelList.addAll(videoModelList);
                UIUtils.post(() -> {
                    if (loadingIndicatorView != null)
                        loadingIndicatorView.hide();
                    footView.setVisibility(View.VISIBLE);
                    mLoadMoreWrapper.notifyDataSetChanged();
                });
            }).start();

        } catch (Exception e) {
        }

    }

    /**
     *
     *
     *
     */
    private void getHorizontalData() {
        try {
            List<String> horizontalList = new ArrayList<>(HorizonGetDataUtils.HorizonGetDatas());
            if (horizontalList == null || horizontalList.size() <= 0) return;
            this.horizontalFirstList.clear();
            this.horizontalFirstList.addAll(horizontalList);
            horizontalAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        try {
            UIUtils.post(() -> firstSwipeRefreshLayout.setRefreshing(true));
            if (NetWorkUtils.isDisconnectedByState(getActivity())) {
                firstSwipeRefreshLayout.setRefreshing(false);
                ToastUtils.showToast(getActivity(), "网络请求失败,请检查网络!");
                return;
            }

            String url = Math.random() > 0.5 ? "http://dv.dzwww.com/gn/default_1.htm" : "http://v.dzwww.com/ksp/default_3.htm";
            String httpHead = url.contains("gn") ? "http://dv.dzwww.com/gn" : "http://v.dzwww.com/ksp";
            new Thread(() -> {
                List<FirstVideoModel> FirstModelList = VideoJsoupController.Instance().firstVideoData(url, httpHead);
                if (FirstModelList == null || FirstModelList.size() <= 0) {
                    ToastUtils.showToast(getActivity(), getString(R.string.network_fail_string));
                    UIUtils.postDelayed(() -> firstSwipeRefreshLayout.setRefreshing(false), 2000);
                    return;
                }
                this.firstVideoModelList.clear();
                this.firstVideoModelList.addAll(FirstModelList);
                UIUtils.post(() -> {
                    page = 3;
                    viewShowOrHide();
                    firstSwipeRefreshLayout.setRefreshing(false);
                    mLoadMoreWrapper.notifyDataSetChanged();
                });
            }).start();
        } catch (Exception e) {
            Logger.e(e.getMessage() + "");
        }

    }

    @Override
    public void onLoadMoreRequested() {

        try {
            if (page == 4) {
                progressBar.setVisibility(View.VISIBLE);
                loading_text.setText("正在加载...");
                UIUtils.postDelayed(() -> {
                    Intent intent = new Intent(getActivity(), HotListVideoActivity.class);
                    intent.putExtra("page", page + "");
                    startActivity(intent);
                    onRefresh();
                }, 2000);
                //ToastUtils.showToast(getActivity(), "更多进入热门");
                return;
            } else {
                progressBar.setVisibility(View.VISIBLE);
                loading_text.setText("正在加载...");
            }
            page++;
            double num = Math.random();
            String url = null;

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
                //Collections.shuffle(homeMultBeanList);
                this.firstVideoModelList.addAll(firstModelList);
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
