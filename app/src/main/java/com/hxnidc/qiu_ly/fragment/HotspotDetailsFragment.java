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
import android.widget.TextView;

import com.hxnidc.qiu_ly.R;
import com.hxnidc.qiu_ly.activity.HotSpotDetailsActivity;
import com.hxnidc.qiu_ly.activity.JokeActivity;
import com.hxnidc.qiu_ly.bean.Gif;
import com.hxnidc.qiu_ly.bean.HotPageBean;
import com.hxnidc.qiu_ly.bean.HotspotBannerModel;
import com.hxnidc.qiu_ly.utils.ImageUtils;
import com.hxnidc.qiu_ly.utils.JsoupUtil;
import com.hxnidc.qiu_ly.utils.NetWorkUtils;
import com.hxnidc.qiu_ly.utils.ToastUtils;
import com.hxnidc.qiu_ly.utils.UIUtils;
import com.orhanobut.logger.Logger;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.wang.avi.AVLoadingIndicatorView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

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


public class HotspotDetailsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    ArrayList<Gif> list = new ArrayList<>();
    List<HotPageBean> hotPageBeanList = new ArrayList<>();
    @BindView(R.id.HRecyclerView)
    RecyclerView mHRecyclerView;
    @BindView(R.id.hotspot_swipeRefreshLayout)
    SwipeRefreshLayout hotspotSwipeRefreshLayout;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    @BindView(R.id.img_network)
    ImageView imgNetwork;

    private RecyclerView hotspotRecyclerView;
    private HeaderAndFooterWrapper headerAndFooterWrapper;
    private View headerView;
    Unbinder unbinder;
    private int page = 2;
    private CommonAdapter<HotspotBannerModel> bannerModelCommonAdapter;
    private List<HotspotBannerModel> hotspotBannerModelList = new ArrayList<>();

    private CommonAdapter<HotPageBean> adapter;

    public static HotspotDetailsFragment newInstance() {
        Bundle args = new Bundle();
        HotspotDetailsFragment fragment = new HotspotDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hotspot, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        initData();
        setBannerData();
        return view;
    }

    private TextView tv_rv_num;
    private ShineButton shineButton;

    private void init() {

        hotspotSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.accent);
        hotspotSwipeRefreshLayout.setOnRefreshListener(this);
        mHRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CommonAdapter<HotPageBean>(getActivity(), R.layout.hotspot_rv_item_layout, hotPageBeanList) {
            @Override
            protected void convert(ViewHolder holder, HotPageBean hotPageBean, int position) {
                if (hotPageBean == null) return;
                holder.setText(R.id.te_rv_title, hotPageBean.getTitle());
                holder.setText(R.id.tv_hot_time, hotPageBean.getTime());
                //holder.setText(R.id.tv_rv_checknum, hotPageBean.getChecknum());
                //holder.setText(R.id.tv_hot_source, "备注:" );
                tv_rv_num = (TextView) holder.getView(R.id.tv_rv_num);
                shineButton = (ShineButton) holder.getView(R.id.po_image0);
                holder.setText(R.id.tv_rv_num, hotPageBean.getIntro() + "");
                ImageView imageView = (ImageView) holder.getView(R.id.img_rv_Pic);
                ImageUtils.loadImgUrl(getActivity(), hotPageBean.getImgurl(), imageView);
                shineButton.setOnCheckStateChangeListener((view, checked) -> {
                    Logger.e(checked + "");
                    if (checked) {
                        tv_rv_num.setText(Integer.parseInt(tv_rv_num.getText().toString()) + 1 + "");
                    } else {
                        tv_rv_num.setText(Integer.parseInt(tv_rv_num.getText().toString()) - 1 + "");
                    }
                });

            }
        };
        imgNetwork.setOnClickListener(v -> {
            initData();
        });
        headerAndFooterWrapper = new HeaderAndFooterWrapper(adapter);
        headerView = LayoutInflater.from(getActivity()).inflate(R.layout.banner_hotspot_layout, mHRecyclerView, false);
        headerAndFooterWrapper.addHeaderView(headerView);
        hotspotRecyclerView = (RecyclerView) headerView.findViewById(R.id.hotspotRecyclerView);
        hotspotRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        bannerModelCommonAdapter = new CommonAdapter<HotspotBannerModel>(getActivity(), R.layout.layout_item_rv_hospot_banner, hotspotBannerModelList) {
            @Override
            protected void convert(ViewHolder holder, HotspotBannerModel hotspotBannerModel, int position) {

                if (hotspotBannerModel == null) return;
                holder.setText(R.id.tv_hotspot_title, hotspotBannerModel.getHotspotTitle());
                holder.setImageResource(R.id.img_hotspot_icon, hotspotBannerModel.getHotspotIcon());
            }
        };
        bannerModelCommonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (position == 0)
                    startActivity(new Intent(getActivity(), JokeActivity.class));

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        hotspotRecyclerView.setAdapter(bannerModelCommonAdapter);
        mHRecyclerView.setAdapter(headerAndFooterWrapper);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (adapter.getDatas() == null) return;

                Intent intent = new Intent(getActivity(), HotSpotDetailsActivity.class);
                intent.putExtra("spotImageUrl", adapter.getDatas().get(position - 1).getImgurl());
                intent.putExtra("spotHref", adapter.getDatas().get(position - 1).getHref());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }

    private void setBannerData() {

        try {
            List<HotspotBannerModel> hotspotList = new ArrayList<>(JsoupUtil.getHotSpotBannerData());
            hotspotBannerModelList.addAll(hotspotList);
            bannerModelCommonAdapter.notifyDataSetChanged();
        } catch (Exception e) {
        }

    }

    private void initData() {
        if (!NetWorkUtils.isConnectedByState(getActivity())) {
            ToastUtils.showToast(getActivity(), "网络连接失败!");
            imgNetwork.setVisibility(View.VISIBLE);
            if (avi != null)
                avi.hide();
            return;
        }

        if (avi != null)
            avi.show();
        getDetailListData();
        viewShowOrHide();
    }

    private void viewShowOrHide() {
        imgNetwork.setVisibility(View.GONE);
    }

    private void getDetailListData() {
        try {
            new Thread(() -> {
                //list = JsoupUtil.getGif("http://www.zbjuran.com/dongtai/", 0);
                //List<HotPageBean> hotPageBeanList = JsoupUtil.getNewData("http://photo.yangtse.com/citypic/index_2.html");
//                List<HotPageBean> hotPageBeanList = new ArrayList<HotPageBean>(JsoupUtil.getNewData("http://photo.yangtse.com/citypic/index.html"));
//                if (hotPageBeanList == null || hotPageBeanList.size() <= 0) return;
//                //hotspotSwipeRefreshLayout.setRefreshing(false);
//                this.hotPageBeanList.addAll(hotPageBeanList);
//                UIUtils.post(() -> adapter.notifyDataSetChanged());
                List<HotPageBean> hotPageBeanList = JsoupUtil.getListData("http://www.guancha.cn/industry-science");
                if (hotPageBeanList == null || hotPageBeanList.size() <= 0) {
                    UIUtils.post(() -> avi.hide());
                    return;
                }
                this.hotPageBeanList.addAll(hotPageBeanList);
                UIUtils.post(() -> {
                    avi.hide();
                    headerAndFooterWrapper.notifyDataSetChanged();
                });
            }).start();
        } catch (Exception ex) {

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        try {
            UIUtils.post(() -> hotspotSwipeRefreshLayout.setRefreshing(true));
            new Thread(() -> {
                List<HotPageBean> hotPageBeanList = JsoupUtil.getListData("http://www.guancha.cn/industry-science");
                if (hotPageBeanList == null || hotPageBeanList.size() <= 0) {
                    UIUtils.postDelayed(() -> {
                        hotspotSwipeRefreshLayout.setRefreshing(false);
                        if (avi != null)
                            avi.hide();
                    }, 2000);
                    return;
                }
                this.hotPageBeanList.clear();
                this.hotPageBeanList.addAll(hotPageBeanList);
                UIUtils.post(() -> {
                    page = 2;
                    viewShowOrHide();
                    if (avi != null)
                        avi.hide();
                    hotspotSwipeRefreshLayout.setRefreshing(false);
                    headerAndFooterWrapper.notifyDataSetChanged();
                });
            }).start();
        } catch (Exception e) {
            Logger.e(e.getMessage() + "");
        }
    }
}
