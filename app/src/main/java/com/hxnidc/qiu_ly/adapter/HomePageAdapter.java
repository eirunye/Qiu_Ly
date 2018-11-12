package com.hxnidc.qiu_ly.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hxnidc.qiu_ly.R;
import com.hxnidc.qiu_ly.bean.AdMarqBean;
import com.hxnidc.qiu_ly.bean.AdNextBean;
import com.hxnidc.qiu_ly.bean.BannerBean;
import com.hxnidc.qiu_ly.bean.NewBean;
import com.hxnidc.qiu_ly.bean.NewTwoBean;
import com.hxnidc.qiu_ly.constant.Https;
import com.hxnidc.qiu_ly.utils.ToastUtils;
import com.sunfusheng.marqueeview.MarqueeView;
import com.zhy.adapter.recyclerview.CommonAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by on 2017/7/5 10:41
 * Author：yrg
 * Describe:
 */


public class HomePageAdapter extends RecyclerView.Adapter implements BGABanner.Adapter<ImageView, String>, BGABanner.Delegate<ImageView, String> {

    private List<BannerBean.ImagesBean> bannerImageList;
    private List<AdMarqBean> adMarqBeanList;
    private List<NewBean.RecentBean> recentBeanList;
    private List<AdNextBean> adNextBeanList;
    private NewTwoBean newTwoBean;
    private CommonAdapter<NewBean.RecentBean> newAdpter;
    private List<Object> objectList;
    private Context mContext;
    private LayoutInflater inflater;
    private List<String> imgUrl = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private static final int TYPE_TOP = 0;//头部布局
    private static final int TYPE_CENTER = 1;//
    private static final int TYPE_CATEGORY = 2;//中间的四个快速入口
    private static final int TYPE_HEADER = 3;//每个分类的head
    private static final int REFRESHPOSITION = 4;//下部head的位置
    private int CENTERPOSITION;//中间head的位置
    private static final int TYPE_REFRESH = 5;//最下面的布局
    private int currentType = TYPE_TOP;

    public HomePageAdapter(Context mContext) {
        this.mContext = mContext;
        this.inflater = LayoutInflater.from(mContext);
        bannerImageList = new ArrayList<>();
        adMarqBeanList = new ArrayList<>();
        recentBeanList = new ArrayList<>();
        objectList = new ArrayList<>();
        adNextBeanList = new ArrayList<>();
        //nBeanList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_TOP) {
            View viewBanner = inflater.inflate(R.layout.home_banner_layout, parent, false);
//            StaggeredGridLayoutManager.LayoutParams params =
//                    (StaggeredGridLayoutManager.LayoutParams) viewBanner.getLayoutParams();
//            params.setFullSpan(true);//最为重要的一个方法，占满全屏,以下同理
//            viewBanner.setLayoutParams(params);
            return new HomeBannerHolder(viewBanner);
        } else if (viewType == TYPE_CENTER) {
            return new HomeAdHolder(inflater.inflate(R.layout.home_vertical_ad_layout, parent, false));
        } else if (viewType == TYPE_CATEGORY) {
            return new HomeNewHolder(inflater.inflate(R.layout.home_rv_new_layout, null));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof HomeBannerHolder && bannerImageList.size() > 0) {
            insetsBannerData((HomeBannerHolder) holder, bannerImageList);
        } else if (holder instanceof HomeAdHolder && adMarqBeanList.size() > 0) {
            insetsAdData((HomeAdHolder) holder, adMarqBeanList);
        } else if (holder instanceof HomeNewHolder && objectList.size() > 0) {
            HomeNewHolder newHolder = (HomeNewHolder) holder;
            insetsNewData(newHolder, position);
        }
    }

    private void insetsNewData(HomeNewHolder holder, int position) {
//        holder.newRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//        NewWithAdAdapter newWithAdAdapter = new NewWithAdAdapter(mContext);
//        newWithAdAdapter.addAll(objectList);
//        holder.newRecyclerView.setAdapter(newWithAdAdapter);
        //holder.newRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
//        newAdpter = new CommonAdapter<NewBean.RecentBean>(mContext, R.layout.home_rv_item_layout, recentBeanList) {
//            @Override
//            protected void convert(ViewHolder holder, NewBean.RecentBean recentBean, int position) {
//                //ImageUtils.load(mContext, recentBean.getThumbnail(), holder.getView(R.id.img_rv_Picture));
//                ImageView img = (ImageView) holder.getView(R.id.img_rv_Picture);
//                Glide.with(mContext)
//                        .load(recentBean.getThumbnail())
//                        .placeholder(R.drawable.holder)
//                        .error(R.drawable.holder)
//                        .dontAnimate()
//                        .centerCrop()
//                        .into(img);
//                holder.setText(R.id.te_rv_title, recentBean.getTitle());
//                holder.setText(R.id.te_rv_title, recentBean.getTitle());
//                holder.setText(R.id.tv_rv_num, new Random().nextInt(10) * 10 + "评");
//            }
//        };


    }

    private void insetsAdData(HomeAdHolder holder, List<AdMarqBean> adMarqBeanList) {
        List<String> stringList = new ArrayList<>();
        for (AdMarqBean adMarqBean : adMarqBeanList) {
            stringList.add(adMarqBean.getName() + adMarqBean.getUrl());
        }
        if (stringList.size() <= 0)
            return;
        holder.marqueeView.startWithList(stringList);
        holder.marqueeView.setOnItemClickListener((position, view) ->
                ToastUtils.showToast(mContext, position + "")
        );
    }

    private void insetsBannerData(HomeBannerHolder holder, List<BannerBean.ImagesBean> bannerImageList) {
        holder.bannerGuideContent.setAdapter(this);
        holder.bannerGuideContent.setDelegate(this);
        for (BannerBean.ImagesBean imagesBean : bannerImageList) {
            imgUrl.add(Https.BASEBANNER_HTTP + imagesBean.getUrl());
            titleList.add(imagesBean.getCopyright());
        }
        if (imgUrl.size() <= 0)
            return;
        holder.bannerGuideContent.setData(imgUrl, titleList);

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case TYPE_TOP:
                currentType = TYPE_TOP;
                break;
            case TYPE_CENTER:
                currentType = TYPE_CENTER;
                break;
            case TYPE_CATEGORY:
                currentType = TYPE_CATEGORY;
                break;
            case TYPE_HEADER:
                currentType = TYPE_HEADER;
                break;
            case REFRESHPOSITION:
                currentType = REFRESHPOSITION;
                break;
            case TYPE_REFRESH:
                currentType = TYPE_REFRESH;
                break;
        }
        return currentType;
    }

    public void getBannerData(List<BannerBean.ImagesBean> bannerImageList) {
        this.bannerImageList.clear();
        this.bannerImageList.addAll(bannerImageList);
        this.notifyDataSetChanged();
    }

    public void getAdMerqData(List<AdMarqBean> adMarqBeanList) {
        this.adMarqBeanList.addAll(adMarqBeanList);
    }

    public void setAdNextBeanListData(List<AdNextBean> adNextBeanListData) {
        this.adNextBeanList.clear();
        this.adNextBeanList.addAll(adNextBeanListData);

    }

    public void getNewData(List<NewBean.RecentBean> recentBeanList) {
        //recentBeanList.clear();
        this.recentBeanList.addAll(recentBeanList);
        int index = 0;
        for (int i = 0; i < recentBeanList.size(); i++) {
            objectList.add(recentBeanList.get(i));
            if (Math.random() < 0.2) {
                objectList.add(adNextBeanList.get(index % adNextBeanList.size()));
                index++;
            }
            if (i == 10) {
                if (newTwoBean != null) {
                    objectList.add(newTwoBean);
                }
            }
        }
        this.notifyDataSetChanged();
    }

    @Override
    public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
        Glide.with(mContext)
                .load(model)
                .placeholder(R.drawable.icon_tag)
                .error(R.drawable.icon_tag)
                .dontAnimate()
                .centerCrop()
                .into(itemView);
    }

    @Override
    public void onBannerItemClick(BGABanner banner, ImageView itemView, String model, int position) {

        ToastUtils.showToast(mContext, position + 1 + "");
        //Toast.makeText(mContext, "ppppp" + position, Toast.LENGTH_LONG).show();

    }

    public void setNewTwoData(NewTwoBean nBeanList) {

        this.newTwoBean = nBeanList;
    }


    public class HomeBannerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.banner_guide_content)
        BGABanner bannerGuideContent;

        public HomeBannerHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class HomeAdHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.marqueeView)
        MarqueeView marqueeView;

        public HomeAdHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class HomeNewHolder extends RecyclerView.ViewHolder {

        public HomeNewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
