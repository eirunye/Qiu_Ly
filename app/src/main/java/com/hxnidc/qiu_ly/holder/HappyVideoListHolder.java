package com.hxnidc.qiu_ly.holder;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxnidc.qiu_ly.R;
import com.hxnidc.qiu_ly.bean.model.HappyVideoModel;
import com.hxnidc.qiu_ly.utils.ImageUtils;
import com.orhanobut.logger.Logger;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by on 2017/9/8 17:37
 * Author：yrg
 * Describe:
 */


public class HappyVideoListHolder implements ItemViewDelegate<HappyVideoModel> {
    private Context context;
    private ShineButton shineButton;
    private TextView tv_first_time;

    public HappyVideoListHolder(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_rv_firstvideo_layout;
    }

    @Override
    public boolean isForViewType(HappyVideoModel item, int position) {
        return !item.isAd();
    }

    @Override
    public void convert(ViewHolder holder, HappyVideoModel happyVideoModel, int position) {
        if (happyVideoModel == null) return;
        holder.setText(R.id.tv_first_title, happyVideoModel.getTitle());
        holder.setText(R.id.tv_first_time, (int) (Math.random() * 100 + Math.random() * 5) + "");
        tv_first_time = holder.getView(R.id.tv_first_time);
        shineButton = holder.getView(R.id.ShineButton);
        shineButton.setOnCheckStateChangeListener((view, checked) -> {
            Logger.e(checked + "");
            if (checked) {
                tv_first_time.setText(Integer.parseInt(tv_first_time.getText().toString()) + 1 + "");
            } else {
                tv_first_time.setText(Integer.parseInt(tv_first_time.getText().toString()) - 1 + "");
            }
        });
        ImageView imageView = (ImageView) holder.getView(R.id.img_first_video);
        ImageUtils.loadImgUrl(context, happyVideoModel.getImgUrl(), imageView);
    }
}
