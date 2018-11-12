package com.hxnidc.qiu_ly.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.huxq17.swipecardsview.SwipeCardsView;
import com.hxnidc.qiu_ly.R;
import com.hxnidc.qiu_ly.activity.video.ArtsOrSportsActivity;
import com.hxnidc.qiu_ly.activity.video.HappyPlayerActivity;
import com.hxnidc.qiu_ly.activity.video.HappyVideoActivity;
import com.hxnidc.qiu_ly.activity.video.HotListVideoActivity;
import com.hxnidc.qiu_ly.activity.video.JokeListActivity;
import com.hxnidc.qiu_ly.adapter.VideoAttentionAdapter;
import com.hxnidc.qiu_ly.bean.model.ContentBean;
import com.hxnidc.qiu_ly.config.VideoJsoupController;
import com.hxnidc.qiu_ly.constant.Https;
import com.hxnidc.qiu_ly.utils.HorizonGetDataUtils;
import com.hxnidc.qiu_ly.utils.NetWorkUtils;
import com.hxnidc.qiu_ly.utils.ToastUtils;
import com.hxnidc.qiu_ly.utils.UIUtils;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by on 2017/7/24 15:48
 * Author：yrg
 * Describe:
 */


public class VideoAttentionFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    @BindView(R.id.horizontal_recyclerView)
    RecyclerView horizontalRecyclerView;
    @BindView(R.id.loadingIndicatorView)
    AVLoadingIndicatorView loadingIndicatorView;
    @BindView(R.id.img_network)
    ImageView imgNetwork;
    @BindView(R.id.swipCardsView)
    SwipeCardsView swipeCardsView;
    //    @BindView(R.id.swipeRefreshLayout)
//    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.fab_Previous)
    FloatingActionButton fabPrevious;
    @BindView(R.id.fab_left)
    FloatingActionButton fabLeft;
    @BindView(R.id.fab_right)
    FloatingActionButton fabRight;
    @BindView(R.id.fab_loading)
    FloatingActionButton fabLoading;
    @BindView(R.id.menu_fab)
    FloatingActionMenu menuFab;

    private int curIndex;
    private int page = 1;
    private List<ContentBean> mList = new ArrayList<>();
    private VideoAttentionAdapter adapter;
    private CommonAdapter<String> horizontalAdapter;
    private List<String> horizontalFirstList = new ArrayList<>();

    public static VideoAttentionFragment newInstance() {
        Bundle args = new Bundle();
        VideoAttentionFragment fragment = new VideoAttentionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video_attention_layout;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        fabPrevious.setOnClickListener(this);
        fabLeft.setOnClickListener(this);
        fabRight.setOnClickListener(this);
        fabLoading.setOnClickListener(this);
        menuFab.setClosedOnTouchOutside(true);
//        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.accent);
//        swipeRefreshLayout.setOnRefreshListener(this);
        imgNetwork.setOnClickListener(v -> {
            initData();
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        horizontalRecyclerView.setLayoutManager(linearLayoutManager);

        horizontalAdapter = new CommonAdapter<String>(getActivity(), R.layout.rv_item_horizontal_layout, horizontalFirstList) {
            @Override
            protected void convert(ViewHolder holder, String data, int position) {
                if (TextUtils.isEmpty(data)) return;
                holder.setText(R.id.tv_rv_item, data);
            }
        };
        horizontalRecyclerView.setAdapter(horizontalAdapter);

        horizontalAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (horizontalAdapter.getDatas() == null) return;
                //openFragmentToActivity(getActivity(), VideoFirstActivity.class);
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
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        getHorizontalData();

        //whether retain last card,defalut false
        swipeCardsView.retainLastCard(true);
        //Pass false if you want to disable swipe feature,or do nothing.
        swipeCardsView.enableSwipe(true);

        swipeCardsView.setCardsSlideListener(new SwipeCardsView.CardsSlideListener() {
            @Override
            public void onShow(int index) {
                curIndex = index;
                Logger.e("test showing index = " + index);

            }

            @Override
            public void onCardVanish(int index, SwipeCardsView.SlideType type) {
                String orientation = "";
                switch (type) {
                    case LEFT:
                        orientation = "向左飞出";
                        break;
                    case RIGHT:
                        orientation = "向右飞出";
                        break;
                }
//                toast("test position = "+index+";卡片"+orientation);
            }

            @Override
            public void onItemClick(View cardImageView, int index) {
               // ToastUtils.showToast(getActivity(), "点击了 position=" + index);
                Intent intent = new Intent(getActivity(), HappyPlayerActivity.class);
                intent.putExtra("hrefUrl", mList.get(index).getUrl());
                intent.putExtra("imgUrl", mList.get(index).getImgUrl());
                intent.putExtra("title", mList.get(index).getTitle());
                startActivity(intent);

            }
        });

    }

    @Override
    protected void initData() {

        if (!NetWorkUtils.isConnectedByState(getActivity())) {
            ToastUtils.showToast(getActivity(), getString(R.string.network_fail_string));
            imgNetwork.setVisibility(View.VISIBLE);
            return;
        }
        if (loadingIndicatorView != null) {
            loadingIndicatorView.show();
        }

        getListData();
        viewShowOrHide();
    }


    private void viewShowOrHide() {
        imgNetwork.setVisibility(View.GONE);
    }

    private void getListData() {
        //String url = "http://www.hxnews.com/news/sp/rdsp/201708/10/1279590.shtml";

        new Thread(() -> {
            List<ContentBean> contentBeanList = VideoJsoupController.Instance().getAttentionVideoList("http://www.joy.cn/entertainment/all");
            if (contentBeanList == null || contentBeanList.size() <= 0) {
                UIUtils.post(() -> {
                    if (loadingIndicatorView != null)
                        loadingIndicatorView.hide();
                });
                return;
            }
            this.mList.addAll(contentBeanList);
            UIUtils.post(() -> {
                if (loadingIndicatorView != null)
                    loadingIndicatorView.hide();
                showCard();
            });
        }).start();

    }

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

    /**
     * 卡片向左边飞出
     */
    public void doLeftOut() {
        swipeCardsView.slideCardOut(SwipeCardsView.SlideType.LEFT);
    }

    /**
     * 卡片向右边飞出
     */
    public void doRightOut() {
        swipeCardsView.slideCardOut(SwipeCardsView.SlideType.RIGHT);
    }

    /**
     * 从头开始，重新浏览
     */
    public void swipeToPre() {
        //必须先改变adapter中的数据，然后才能由数据变化带动页面刷新
        if (mList != null) {
            adapter.setData(mList);
            if (curIndex > 0) {
                swipeCardsView.notifyDatasetChanged(curIndex - 1);
            } else {
                ToastUtils.showToast(getActivity(), "已经是第一张卡片了");
            }
        }
    }

    /**
     * 从头开始，重新浏览
     */
    public void doRetry() {
        //必须先改变adapter中的数据，然后才能由数据变化带动页面刷新
        if (mList != null) {
            adapter.setData(mList);
            swipeCardsView.notifyDatasetChanged(0);
        }
    }

    /**
     * 显示cardsview
     */
    private void showCard() {
        if (adapter == null) {
            adapter = new VideoAttentionAdapter(mList, getActivity());
            swipeCardsView.setAdapter(adapter);
        } else {
            //if you want to change the UI of SwipeCardsView,you must modify the data first
            adapter.setData(mList);
            swipeCardsView.notifyDatasetChanged(curIndex);
        }
    }


    public void onRefresh() {

        try {
            UIUtils.post(() -> {
                if (loadingIndicatorView != null)
                    loadingIndicatorView.show();
            });
            if (NetWorkUtils.isDisconnectedByState(getActivity())) {
                // swipeRefreshLayout.setRefreshing(false);
                if (loadingIndicatorView != null)
                    loadingIndicatorView.hide();
                ToastUtils.showToast(getActivity(), "网络请求失败,请检查网络!");
                return;
            }
            //String url = Math.random() > 0.5 ? Https.QIU_LY_HAPPY_LIST1_HTTP : Https.QIU_LY_HAPPY_LIST2_HTTP;
            new Thread(() -> {

                List<ContentBean> contentBeanList = VideoJsoupController.Instance().getAttentionVideoList(Https.QIU_LY_HAPPY_LIST1_HTTP);
                if (contentBeanList == null || contentBeanList.size() <= 0) {
                    ToastUtils.showToast(getActivity(), getString(R.string.network_fail_string));
                    UIUtils.postDelayed(() -> {
                        // swipeRefreshLayout.setRefreshing(false);
                        if (loadingIndicatorView != null)
                            loadingIndicatorView.hide();
                        if (this.mList.size() <= 0) {
                            imgNetwork.setVisibility(View.VISIBLE);
                        }
                    }, 2000);
                    return;
                }
                this.mList.clear();
                this.mList.addAll(contentBeanList);
                UIUtils.post(() -> {
                    page = 1;
                    viewShowOrHide();
                    ///swipeRefreshLayout.setRefreshing(false);
                    if (loadingIndicatorView != null)
                        loadingIndicatorView.hide();
                    swipeCardsView.notifyDatasetChanged(0);
                });
            }).start();
        } catch (Exception e) {
            Logger.e(e.getMessage() + "");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_Previous:
                swipeToPre();
                break;
            case R.id.fab_left:
                doLeftOut();
                break;
            case R.id.fab_right:
                doRightOut();
                break;
            case R.id.fab_loading:
                onRefresh();
                break;
        }
        menuFab.setAnimated(true);
    }
}
