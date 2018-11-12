package com.hxnidc.qiu_ly.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.zhouwei.library.CustomPopWindow;
import com.hxnidc.qiu_ly.R;
import com.hxnidc.qiu_ly.bean.BlogNewReaderBean;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by on 2017/7/12 17:55
 * Author：yrg
 * Describe:
 */


public class BlogRowHolder implements ItemViewDelegate<BlogNewReaderBean> {

    private Context context;

    public BlogRowHolder(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.blog_row_rv_item_layout;
    }

    @Override
    public boolean isForViewType(BlogNewReaderBean item, int position) {
        return !item.isFrov();
    }

    @Override
    public void convert(ViewHolder holder, BlogNewReaderBean blogNewReaderBean, int position) {

        holder.setText(R.id.tv_item_ad_title, blogNewReaderBean.getTitle());
        ImageView imageView = (ImageView) holder.getView(R.id.img_ad_msg);
        ImageView img_show_tip = (ImageView) holder.getView(R.id.img_show_tip);
        if (blogNewReaderBean.getImgUrl() != null) {
            Glide.with(context)
                    .load(blogNewReaderBean.getImgUrl())
                    .placeholder(R.drawable.icon_tag)
                    .error(R.drawable.icon_tag)
                    .dontAnimate()
                    //.centerCrop()
                    .into(imageView);
        }
        img_show_tip.setOnClickListener(v -> {
            showPopuWindow(img_show_tip);
        });
    }


    private void showPopuWindow(ImageView imgShowWindow) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.pop_layout1, null);
        CustomPopWindow popWindow = new CustomPopWindow.PopupWindowBuilder(context)
                .setView(contentView)
                .setFocusable(true)
                .setOutsideTouchable(true)
                .setAnimationStyle(R.style.CustomPopWindowStyle) // 添加自定义显示和消失动画
                .create()
                .showAsDropDown(imgShowWindow, 0, 10);
    }
}
