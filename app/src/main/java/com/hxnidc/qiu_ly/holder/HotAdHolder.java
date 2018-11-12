package com.hxnidc.qiu_ly.holder;

import android.content.Context;

import com.hxnidc.qiu_ly.R;
import com.hxnidc.qiu_ly.bean.model.FirstVideoModel;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by on 2017/9/6 16:29
 * Author：yrg
 * Describe:
 */

/**
 * 获取视频数据
 */
public class HotAdHolder implements ItemViewDelegate<FirstVideoModel> {

    private Context context;

    public HotAdHolder(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_ad_video_layout;
    }

    @Override
    public boolean isForViewType(FirstVideoModel item, int position) {
        return item.isAd();
    }

    @Override
    public void convert(ViewHolder holder, FirstVideoModel firstVideoModel, int position) {

//        if (firstVideoModel == null) return;
//        holder.setText(R.id.tv_first_title, firstVideoModel.getTitle());
//        holder.setText(R.id.tv_first_time, firstVideoModel.getTime());
//        ImageView imageView = (ImageView) holder.getView(R.id.img_first_video);
//        ImageUtils.loadImgUrl(context, firstVideoModel.getImgUrl(), imageView);

    }
}
