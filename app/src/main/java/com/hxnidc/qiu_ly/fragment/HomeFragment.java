package com.hxnidc.qiu_ly.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hxnidc.qiu_ly.R;
import com.hxnidc.qiu_ly.activity.HomeDetailsActivity;
import com.hxnidc.qiu_ly.activity.LoginActivity;
import com.hxnidc.qiu_ly.activity.SearchActivity;
import com.hxnidc.qiu_ly.activity.video.HotListVideoActivity;
import com.hxnidc.qiu_ly.adapter.HomespageAdapter;
import com.hxnidc.qiu_ly.bean.BannerBean;
import com.hxnidc.qiu_ly.bean.HomeMultBean;
import com.hxnidc.qiu_ly.bean.HomeNewModel;
import com.hxnidc.qiu_ly.bean.NewBean;
import com.hxnidc.qiu_ly.constant.Https;
import com.hxnidc.qiu_ly.listener.OnStateNavigationListener;
import com.hxnidc.qiu_ly.utils.NetWorkUtils;
import com.hxnidc.qiu_ly.utils.ToastUtils;
import com.hxnidc.qiu_ly.utils.UIUtils;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;
import com.zaaach.toprightmenu.TopRightMenu;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import com.zaaach.toprightmenu.MenuItem;
import com.zaaach.toprightmenu.TopRightMenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;
//import cn.waps.AppConnect;
import okhttp3.Call;


/**
 * Created by on 2017/7/3 10:31
 * Author：yrg
 * Describe:
 */


public class HomeFragment extends BaseFragment implements LoadMoreWrapper.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, BGABanner.Adapter<ImageView, String>, BGABanner.Delegate<ImageView, String> {

    @BindView(R.id.img_home_state)
    ImageView imgHomeState;
    @BindView(R.id.ll_home_search)
    LinearLayout llHomeSearch;
    @BindView(R.id.img_home_show)
    ImageView imgHomeShow;
    @BindView(R.id.img_network)
    ImageView imgNetwork;
    @BindView(R.id.frame_home_bar)
    FrameLayout frameHomeBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.loadingIndicatorView)
    AVLoadingIndicatorView loadingIndicatorView;
    private ProgressBar progressBar;
    private TextView loading_text;
    private HomespageAdapter homespageAdapter;
    private List<HomeMultBean> homeMultBeanList = new ArrayList<>();
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private EmptyWrapper mEmptyWrapper;
    private LoadMoreWrapper mLoadMoreWrapper;
    private int height = 400;// 滑动开始变色的高,真实项目中此高度是由广告轮播或其他首页view高度决定
    private int overallXScroll = 0;
    private int mDistance = 0;
    private int maxDistance = 255;//当距离在[0,255]变化时，透明度在[0,255之间变化]
    private OnStateNavigationListener onStateNavigationListener;

    private List<BannerBean.ImagesBean> bannerImagerList = new ArrayList<>();
    private View bannerView;
    private View adView;
    private View footView;
    private int page = 1;
    private BGABanner bGABanner;
    private LinearLayout miniAdLinearLayout;
    private TopRightMenu mTopRightMenu;
    private boolean isRefresh;
    //http://news.hainan.net/photo/guoneiguoji/list_1.shtml

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onStateNavigationListener = (OnStateNavigationListener) activity;
        } catch (Exception ex) {
            Logger.e(ex.getMessage());
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        try {
//            onStateNavigationListener = (OnStateNavigationListener) context;
//        } catch (Exception ex) {
//            Logger.e(ex.getMessage());
//        }
//    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.accent);
        swipeRefreshLayout.setProgressViewOffset(false, 100, 150);
        swipeRefreshLayout.setOnRefreshListener(this);
        imgNetwork.setOnClickListener(v -> {
            initData();
            onLoadMoreRequested();
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(linearLayoutManager);
        homespageAdapter = new HomespageAdapter(activity, homeMultBeanList);
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(homespageAdapter);
        bannerView = LayoutInflater.from(activity).inflate(R.layout.home_banner_layout, recyclerView, false);
        bGABanner = (BGABanner) bannerView.findViewById(R.id.banner_guide_content);
        bGABanner.setAdapter(this);
        bGABanner.setDelegate(this);
        adView = LayoutInflater.from(activity).inflate(R.layout.home_vertical_ad_layout, recyclerView, false);
        miniAdLinearLayout = (LinearLayout) adView.findViewById(R.id.miniAdLinearLayout);

        footView = LayoutInflater.from(activity).inflate(R.layout.default_loading, recyclerView, false);
        progressBar = (ProgressBar) footView.findViewById(R.id.ProgressBar);

        loading_text = (TextView) footView.findViewById(R.id.loading_text);
        mHeaderAndFooterWrapper.addHeaderView(bannerView);
        mHeaderAndFooterWrapper.addHeaderView(adView);
        mLoadMoreWrapper = new LoadMoreWrapper(mHeaderAndFooterWrapper);
        mLoadMoreWrapper.setLoadMoreView(footView);
        mLoadMoreWrapper.setOnLoadMoreListener(this);
        recyclerView.setAdapter(mLoadMoreWrapper);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                overallXScroll = overallXScroll + dy;// 累加y值 解决滑动一半y值为0
                if (overallXScroll <= 0) {   //设置标题的背景颜色
                    frameHomeBar.setBackgroundColor(Color.argb((int) 0, 41, 193, 246));
                } else if (overallXScroll > 0 && overallXScroll <= height) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
                    float scale = (float) overallXScroll / height;
                    float alpha = (255 * scale);
                    frameHomeBar.setBackgroundColor(getResources().getColor(R.color.colorAccent));//   Color.argb((int) alpha, 255, 64, 129));
                } else {
                    frameHomeBar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }
            }
        });

        homespageAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (homespageAdapter.getDatas() == null) return;
                Intent intent = new Intent();
                intent.putExtra("homeHtml", homespageAdapter.getDatas().get(position - 2).getHref());
                intent.putExtra("homeImgUrl", homespageAdapter.getDatas().get(position - 2).getImgUrl());
                intent.setClass(activity, HomeDetailsActivity.class);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        //showPopupWindow();
    }


    @Override
    protected void initData() {

//        new Thread(() -> {
//            List<HomeMultBean> homeMultBeanList = new ArrayList<>(JsoupUtil.getHomeNewData("http://news.hainan.net/photo/shehuiwanxiang/list_1.shtml"));
//            if (homeMultBeanList == null || homeMultBeanList.size() <= 0) return;
//            this.homeMultBeanList.addAll(homeMultBeanList);
//            UIUtils.post(() -> {
//                mLoadMoreWrapper.notifyDataSetChanged();
//            });
//        }).start();

        //showNewBaseData();
        if (!NetWorkUtils.isConnectedByState(getActivity())) {
            ToastUtils.showToast(getActivity(), "网络请求失败,请检查网络!");
            imgNetwork.setVisibility(View.VISIBLE);
            isRefresh = true;
            return;
        }

        if (loadingIndicatorView != null)
            loadingIndicatorView.show();


//        getMiniData();

        getBannerData();

        viewShowOrHide();

    }

    private void viewShowOrHide() {
        imgNetwork.setVisibility(View.GONE);
    }

    private void showNewBaseData() {
        try {

//            if (NetWorkUtils.isDisconnectedByState(getActivity())) {
//
//                return;
//            }

            //String url = Math.random() > 0.5 ? Https.QIU_LY_GUONEI_NEW_HTTP : Https.QIU_LY_KEJI_NEW_HTTP;

            OkHttpUtils.get().url(Https.QIU_LY_WX_NEW_HTTP).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    ToastUtils.showToast(getActivity(), "网络请求失败,请检查网络!");
                }

                @Override
                public void onResponse(String response, int id) {
if(null==respone||"".equals(respone)){return;}
                    Gson gson = new Gson();
                    HomeNewModel homeNewModel = gson.fromJson(response, HomeNewModel.class);
                    if (homeNewModel == null) return;
                    List<HomeNewModel.NewslistBean> newslist = homeNewModel.getNewslist();
                    if (newslist == null || newslist.size() <= 0) {
                        return;
                    }
                    List<HomeMultBean> homeMultList = new ArrayList<>();
                    for (int i = 0; i < newslist.size(); i++) {
                        HomeMultBean homeMultBean = new HomeMultBean();
                        if (Math.random() > 0.8 && i % 2 == 0)
                            homeMultBean.setAd(true);
                        else homeMultBean.setAd(false);
                        homeMultBean.setHref(newslist.get(i).getUrl());
                        homeMultBean.setImgUrl(newslist.get(i).getPicUrl() + ".jpg");
                        homeMultBean.setTitle(newslist.get(i).getTitle());
                        homeMultBean.setIntro(newslist.get(i).getDescription());
                        homeMultBean.setTime(newslist.get(i).getCtime());
                        homeMultList.add(homeMultBean);
                    }
                    homeMultBeanList.addAll(homeMultList);
                    UIUtils.post(() -> {
                        mLoadMoreWrapper.notifyDataSetChanged();
                    });
                }
            });

        } catch (Exception e) {
        }
    }

    private void getMiniData() {
        try {
            //设置迷你广告背景颜色
//            AppConnect.getInstance(getActivity()).setAdBackColor(Color.argb(225, 200, 198, 196));
//            //设置迷你广告广告诧颜色
//            AppConnect.getInstance(getActivity()).setAdForeColor(Color.YELLOW);
//            //若未设置以上两个颜色，则默认为黑底白字
//            AppConnect.getInstance(getActivity()).showMiniAd(getActivity(), miniAdLinearLayout, 10);
        } catch (Exception e) {

        }
    }


    private List<String> imgUrl = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();

    private void getBannerData() {
        try {
            OkHttpUtils.get().url(Https.BANNER_HTTP).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    ToastUtils.showToast(activity, "网络请求失败!");
                    isRefresh = true;
                }

                @Override
                public void onResponse(String response, int id) {
                    Gson gson = new Gson();
                    BannerBean bannerBean = gson.fromJson(response, BannerBean.class);
                    imgUrl.clear();
                    titleList.clear();
                    for (BannerBean.ImagesBean imagesBean : bannerBean.getImages()) {
                        imgUrl.add(Https.BASEBANNER_HTTP + imagesBean.getUrl());
                        titleList.add(imagesBean.getCopyright());
                    }
                    if (imgUrl.size() <= 0)
                        return;
                    bGABanner.setData(imgUrl, titleList);
                    isRefresh = false;
                }
            });
        } catch (Exception ex) {
            ToastUtils.showToast(activity, "网络请求失败");
        }
    }


    private void getNewData() {
        try {
            OkHttpUtils.get().url(Https.NEW_HTTP).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    ToastUtils.showToast(getActivity(), "网络请求失败!");
                }

                @Override
                public void onResponse(String response, int id) {
                    Gson gson = new Gson();
                    NewBean newBean = gson.fromJson(response, NewBean.class);
                    //newBean.getRecent().size();

                    //ToastUtils.showToast(activity, "成功");
                }
            });
        } catch (Exception ex) {
            ToastUtils.showToast(getActivity(), "网络请求失败!");
        }
    }


    @OnClick({R.id.img_home_state, R.id.img_home_show, R.id.ll_home_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_home_state:
                onStateNavigationListener.stateNavigation(0);
                break;
            case R.id.img_home_show:
                showPopupWindow();
                break;
            case R.id.ll_home_search:
                openFragmentToActivity(activity, SearchActivity.class);
                break;
        }
    }

    /**
     * showPopupWindow
     */
    private void showPopupWindow() {
        mTopRightMenu = new TopRightMenu(getActivity());
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem(R.mipmap.recommd_icon, getString(R.string.recommd_string)));
        menuItems.add(new MenuItem(R.mipmap.login_icon, getString(R.string.login_string)));
        //menuItems.add(new MenuItem(R.mipmap.sweep_icon, getString(R.string.sweep_string)));
        mTopRightMenu
                .setHeight((int) UIUtils.dp2px(120))     //默认高度480
                .setWidth((int) UIUtils.px2dp(320))      //默认宽度wrap_content
                .showIcon(true)     //显示菜单图标，默认为true
                .dimBackground(true)           //背景变暗，默认为true
                .needAnimationStyle(true)   //显示动画，默认为true
                .setAnimationStyle(R.style.TRM_ANIM_STYLE)  //默认为R.style.TRM_ANIM_STYLE
                .addMenuList(menuItems)
                //.addMenuItem(new MenuItem(R.mipmap.atten_icon, getString(R.string.attention_string)))
                .setOnMenuItemClickListener(new TopRightMenu.OnMenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(int position) {
                        if (position == 1)
                            openFragmentToActivity(getActivity(), LoginActivity.class);
//                        else if (position == 3)
//                            openFragmentToActivity(getActivity(), AttentionActivity.class);
//                        else if (position == 2) {
//                            Toast.makeText(getActivity(), "扫一扫", Toast.LENGTH_SHORT).show();
//                        }
                        else if (position == 0) {
                            // Toast.makeText(getActivity(), "推荐", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), HotListVideoActivity.class);
                            intent.putExtra("page", 2 + "");
                            startActivity(intent);
                        }
                    }
                })
                .showAsDropDown(imgHomeShow, -225, 0);
//                        .showAsDropDown(moreBtn);
    }

    /**
     * 上拉加载
     */
    @Override
    public void onLoadMoreRequested() {
        try {
            if (page == 10) {
                progressBar.setVisibility(View.GONE);
                loading_text.setText("没有数据了");
                return;
            } else {
                progressBar.setVisibility(View.VISIBLE);
                loading_text.setText("正在加载...");
            }
            page++;
            double num = Math.random();
            String url = null;
//            if (num > 0.2) {
//                url = Https.QIU_LY_GUONEI_NEW_HTTP;
//            } else if (num > 0.4) {
//                url = Https.QIU_LY_KEJI_NEW_HTTP;
//            } else if (num > 0.6) {
//                url = Https.QIU_LY_STARTUP_NEW_HTTP;
//            } else if (num > 0.8) {
//                url = Https.QIU_LY_YULE_NEW_HTTP;
//            } else {
//            }
            progressBar.setVisibility(View.GONE);
            url = Https.QIU_LY_WX_NEW_HTTP;

            //String url = Math.random() > 0.5 ? Https.QIU_LY_GUONEI_NEW_HTTP : Https.QIU_LY_KEJI_NEW_HTTP;

            OkHttpUtils.get().url(url).addParams("page", page + "").build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    ToastUtils.showToast(getActivity(), "网络请求失败,请检查网络!");
                    progressBar.setVisibility(View.GONE);
                    loading_text.setText("加载数据失败!");
                    if (loadingIndicatorView != null)
                        loadingIndicatorView.hide();
                }

                @Override
                public void onResponse(String response, int id) {
                    Gson gson = new Gson();
                    HomeNewModel homeNewModel = gson.fromJson(response, HomeNewModel.class);
                    if (homeNewModel == null) return;
                    List<HomeNewModel.NewslistBean> newslist = homeNewModel.getNewslist();
                    if (newslist == null || newslist.size() <= 0) {
                        return;
                    }
                    List<HomeMultBean> homeMultList = new ArrayList<>();
                    for (int i = 0; i < newslist.size(); i++) {
                        HomeMultBean homeMultBean = new HomeMultBean();
                        if (Math.random() > 0.8 && i % 2 == 0)
                            homeMultBean.setAd(true);
                        else homeMultBean.setAd(false);
                        homeMultBean.setHref(newslist.get(i).getUrl());
                        homeMultBean.setImgUrl(newslist.get(i).getPicUrl());
                        homeMultBean.setTitle(newslist.get(i).getTitle());
                        homeMultBean.setIntro(newslist.get(i).getDescription());
                        homeMultBean.setTime(newslist.get(i).getCtime());
                        homeMultList.add(homeMultBean);
                    }
                    Collections.shuffle(homeMultList);
                    homeMultBeanList.addAll(homeMultList);
                    UIUtils.post(() -> {
//                        progressBar.setVisibility(View.VISIBLE);
                        loading_text.setText("正在加载...");
                        mLoadMoreWrapper.notifyDataSetChanged();
                        if (loadingIndicatorView != null)
                            loadingIndicatorView.hide();
                    });
                }
            });

//            new Thread(() -> {
//                List<HomeMultBean> homeMultBeanList = new ArrayList<HomeMultBean>(JsoupUtil.getHomeNewData("http://news.hainan.net/photo/shehuiwanxiang/list_" + page + ".shtml"));
//                if (homeMultBeanList == null || homeMultBeanList.size() <= 0) {
//                    UIUtils.post(() -> {
//                        progressBar.setVisibility(View.GONE);
//                        loading_text.setText("加载数据失败!");
//                    });
//                    return;
//                }
//                //Collections.shuffle(homeMultBeanList);
//                this.homeMultBeanList.addAll(homeMultBeanList);
//                UIUtils.post(() -> {
//                    progressBar.setVisibility(View.VISIBLE);
//                    loading_text.setText("正在加载...");
//                    mLoadMoreWrapper.notifyDataSetChanged();
//                });
//            }).start();

        } catch (Exception e) {
        }

    }

    @Override
    public void onRefresh() {
        try {

            UIUtils.post(() -> swipeRefreshLayout.setRefreshing(true));
            if (NetWorkUtils.isDisconnectedByState(getActivity())) {
                swipeRefreshLayout.setRefreshing(false);
                ToastUtils.showToast(getActivity(), "网络请求失败,请检查网络!");
                return;
            }

            if (isRefresh) {
                getMiniData();
                getBannerData();
            }

            OkHttpUtils.get().url(Https.QIU_LY_WX_NEW_HTTP).addParams("page", page + "").build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    ToastUtils.showToast(getActivity(), "网络请求失败,请检查网络!");
                    UIUtils.postDelayed(() -> swipeRefreshLayout.setRefreshing(false), 2000);
                }

                @Override
                public void onResponse(String response, int id) {
                    Gson gson = new Gson();
                    HomeNewModel homeNewModel = gson.fromJson(response, HomeNewModel.class);
                    if (homeNewModel == null) return;
                    List<HomeNewModel.NewslistBean> newslist = homeNewModel.getNewslist();
                    List<HomeMultBean> homeMultList = new ArrayList<>();
                    for (int i = 0; i < newslist.size(); i++) {
                        HomeMultBean homeMultBean = new HomeMultBean();
                        if (Math.random() > 0.8 && i % 2 == 0)
                            homeMultBean.setAd(true);
                        else homeMultBean.setAd(false);
                        homeMultBean.setHref(newslist.get(i).getUrl());
                        homeMultBean.setImgUrl(newslist.get(i).getPicUrl() + ".jpg");
                        homeMultBean.setTitle(newslist.get(i).getTitle());
                        homeMultBean.setIntro(newslist.get(i).getDescription());
                        homeMultBean.setTime(newslist.get(i).getCtime());
                        homeMultList.add(homeMultBean);
                    }
                    Collections.shuffle(homeMultList);
                    homeMultBeanList.clear();
                    homeMultBeanList.addAll(homeMultList);
                    UIUtils.post(() -> {
                        page = 1;
                        viewShowOrHide();
                        swipeRefreshLayout.setRefreshing(false);
//                        progressBar.setVisibility(View.VISIBLE);
                        loading_text.setText("加载数据中...");
                        mLoadMoreWrapper.notifyDataSetChanged();
                    });

                }
            });
//            new Thread(() -> {
//                List<HomeMultBean> homeMultBeanList = new ArrayList<HomeMultBean>(JsoupUtil.getHomeNewData("http://news.hainan.net/photo/shehuiwanxiang/list_1.shtml"));
//                if (homeMultBeanList == null || homeMultBeanList.size() <= 0) {
//                    UIUtils.postDelayed(() -> swipeRefreshLayout.setRefreshing(false), 2000);
//                    return;
//                }
//                this.homeMultBeanList.clear();
//                this.homeMultBeanList.addAll(homeMultBeanList);
//                //Collections.shuffle(this.homeMultBeanList);
//                UIUtils.post(() -> {
//                    page = 1;
//                    swipeRefreshLayout.setRefreshing(false);
//                    progressBar.setVisibility(View.VISIBLE);
//                    loading_text.setText("加载数据中...");
//                    mLoadMoreWrapper.notifyDataSetChanged();
//                });
//            }).start();
        } catch (Exception e) {

        }
    }

    @Override
    public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
        Glide.with(getActivity())
                .load(model)
                .placeholder(R.drawable.icon_tag)
                .error(R.drawable.icon_tag)
                .dontAnimate()
                .centerCrop()
                .into(itemView);
    }

    @Override
    public void onBannerItemClick(BGABanner banner, ImageView itemView, String model, int position) {
        //  Toast.makeText(banner.getContext(), "点击了第" + (position + 1) + "页", Toast.LENGTH_SHORT).show();
//        AppConnect.getInstance(getActivity()).showOffers(getActivity());
    }
}
