package com.hxnidc.qiu_ly.adapter;

import android.content.Context;

import com.hxnidc.qiu_ly.bean.HomeMultBean;
import com.hxnidc.qiu_ly.holder.HomaPageRowHolder;
import com.hxnidc.qiu_ly.holder.HomePageRankHolder;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

/**
 * Created by on 2017/7/6 13:25
 * Author：yrg
 * Describe:HomespageAdapter
 */


public class HomespageAdapter extends MultiItemTypeAdapter<HomeMultBean> {

    public HomespageAdapter(Context context, List<HomeMultBean> datas) {
        super(context, datas);
        addItemViewDelegate(new HomaPageRowHolder(context));
        addItemViewDelegate(new HomePageRankHolder(context));
    }
}
