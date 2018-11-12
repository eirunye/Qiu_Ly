package com.hxnidc.qiu_ly.holder;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hxnidc.qiu_ly.R;
import com.hxnidc.qiu_ly.bean.HomeMultBean;
import com.orhanobut.logger.Logger;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by on 2017/7/13 17:59
 * Author：yrg
 * Describe:
 */


public class HomePageRankHolder implements ItemViewDelegate<HomeMultBean> {
    private Context context;

    public HomePageRankHolder(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_rv_normal_layout;
    }

    @Override
    public boolean isForViewType(HomeMultBean item, int position) {
        return !item.isAd();
    }

    private TextView tv_rv_num;
    private ShineButton shineButton;

    @Override
    public void convert(ViewHolder holder, HomeMultBean homeMultBean, int position) {
        if (homeMultBean == null) return;
        holder.setText(R.id.tv_rv_title, homeMultBean.getTitle());
        holder.setText(R.id.tv_rv_description, homeMultBean.getIntro());
        holder.setText(R.id.tv_rv_time, homeMultBean.getTime());
        tv_rv_num = (TextView) holder.getView(R.id.tv_rv_num);
        holder.setText(R.id.tv_rv_num, (int) (Math.random() * 100 + Math.random() * 7) + "");
        shineButton = (ShineButton) holder.getView(R.id.btn_rv_favorite);
        Logger.e("==>" + homeMultBean.getImgUrl());
        ImageView imageView = (ImageView) holder.getView(R.id.img_rv_Picture);
        Glide.with(context)
                .load(homeMultBean.getImgUrl())
                //.placeholder(R.drawable.holder)
                .error(R.drawable.icon_tag)
                .dontAnimate()
                //.centerCrop()
                .into(imageView);
        shineButton.setOnCheckStateChangeListener((view, checked) -> {
            Logger.e(checked + "");
            if (checked) {
                tv_rv_num.setText(Integer.parseInt(tv_rv_num.getText().toString()) + 1 + "");
            } else {
                tv_rv_num.setText(Integer.parseInt(tv_rv_num.getText().toString()) - 1 + "");
            }
        });
        //ImageUtils.loadImgUrl(context, homeMultBean.getImgUrl(), imageView);
    }
}
