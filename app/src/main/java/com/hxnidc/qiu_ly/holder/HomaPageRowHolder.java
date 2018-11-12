package com.hxnidc.qiu_ly.holder;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.example.zhouwei.library.CustomPopWindow;
import com.hxnidc.qiu_ly.R;
import com.hxnidc.qiu_ly.bean.HomeMultBean;
import com.hxnidc.qiu_ly.utils.ImageUtils;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by on 2017/7/13 17:56
 * Author：yrg
 * Describe:
 */


public class HomaPageRowHolder implements ItemViewDelegate<HomeMultBean> {

    private Context context;


    public HomaPageRowHolder(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_rv_ad_layout;
    }

    @Override
    public boolean isForViewType(HomeMultBean item, int position) {
        return item.isAd();
    }

    @Override
    public void convert(ViewHolder holder, HomeMultBean homeMultBean, int position) {
        if (homeMultBean == null) return;
        holder.setText(R.id.tv_item_ad_title, homeMultBean.getTitle());
        //holder.setText(R.id.)
        if (TextUtils.isEmpty(homeMultBean.getImgUrl())) return;
        ImageView imageView = holder.getView(R.id.img_ad_picture);
        ImageUtils.loadImgAdUrl(context, homeMultBean.getImgUrl(), imageView);
        ImageView imgShowWindow = holder.getView(R.id.img_show_window);
        imgShowWindow.setOnClickListener(v->{
            showPopuWindow(imgShowWindow);
        });
    }

    private void showPopuWindow(ImageView imgShowWindow) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.pop_layout1,null);
        CustomPopWindow popWindow = new CustomPopWindow.PopupWindowBuilder(context)
                .setView(contentView)
                .setFocusable(true)
                .setOutsideTouchable(true)
                .setAnimationStyle(R.style.CustomPopWindowStyle) // 添加自定义显示和消失动画
                .create()
                .showAsDropDown(imgShowWindow,0,10);
    }
}
