package com.hxnidc.qiu_ly.adapter;

import android.content.Context;

import com.hxnidc.qiu_ly.bean.model.HappyVideoModel;
import com.hxnidc.qiu_ly.holder.HappyAdHolder;
import com.hxnidc.qiu_ly.holder.HappyVideoListHolder;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

/**
 * Created by on 2017/9/7 15:57
 * Author：yrg
 * Describe:
 */


public class HappyMultiAdapter extends MultiItemTypeAdapter<HappyVideoModel> {

    public HappyMultiAdapter(Context context, List<HappyVideoModel> datas) {
        super(context, datas);
        addItemViewDelegate(new HappyVideoListHolder(context));
        addItemViewDelegate(new HappyAdHolder(context));
    }
}
