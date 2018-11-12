package com.hxnidc.qiu_ly.adapter;

import android.content.Context;

import com.hxnidc.qiu_ly.bean.model.VideoModel;
import com.hxnidc.qiu_ly.holder.ArtsOrSportVideoListHolder;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

/**
 * Created by on 2017/9/8 18:33
 * Author：yrg
 * Describe:
 */


public class MultiVideoModelAdapter extends MultiItemTypeAdapter<VideoModel> {

    public MultiVideoModelAdapter(Context context, List<VideoModel> datas) {
        super(context, datas);
        addItemViewDelegate(new ArtsOrSportVideoListHolder(context));
    }
}
