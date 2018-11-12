package com.hxnidc.qiu_ly.adapter;

import android.content.Context;

import com.hxnidc.qiu_ly.bean.BlogNewReaderBean;
import com.hxnidc.qiu_ly.holder.BlogRankHolder;
import com.hxnidc.qiu_ly.holder.BlogRowHolder;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

/**
 * Created by on 2017/7/12 17:38
 * Author：yrg
 * Describe:
 */


public class BlogReaderAdapter extends MultiItemTypeAdapter<BlogNewReaderBean> {
    public BlogReaderAdapter(Context context, List<BlogNewReaderBean> datas) {
        super(context, datas);

        addItemViewDelegate(new BlogRowHolder(context));
        addItemViewDelegate(new BlogRankHolder(context));

    }
}
