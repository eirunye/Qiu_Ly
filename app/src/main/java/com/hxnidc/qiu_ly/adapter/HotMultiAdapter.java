package com.hxnidc.qiu_ly.adapter;

import android.content.Context;

import com.hxnidc.qiu_ly.bean.model.FirstVideoModel;
import com.hxnidc.qiu_ly.holder.HotAdHolder;
import com.hxnidc.qiu_ly.holder.HotVideoListHolder;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

/**
 * Created by on 2017/9/6 16:04
 * Author：yrg
 * Describe:HotMultiAdapter 视频多布局
 */


public class HotMultiAdapter extends MultiItemTypeAdapter<FirstVideoModel> {

    public HotMultiAdapter(Context context, List<FirstVideoModel> datas) {
        super(context, datas);
        addItemViewDelegate(new HotVideoListHolder(context));
        addItemViewDelegate(new HotAdHolder(context));
    }
}
