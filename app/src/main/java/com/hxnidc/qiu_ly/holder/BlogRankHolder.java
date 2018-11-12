package com.hxnidc.qiu_ly.holder;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hxnidc.qiu_ly.R;
import com.hxnidc.qiu_ly.bean.BlogNewReaderBean;
import com.orhanobut.logger.Logger;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by on 2017/7/12 17:57
 * Author：yrg
 * Describe:
 */


public class BlogRankHolder implements ItemViewDelegate<BlogNewReaderBean> {

    private Context context;

    public BlogRankHolder(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.blog_rank_rv_item_layout;
    }

    @Override
    public boolean isForViewType(BlogNewReaderBean item, int position) {
        return item.isFrov();
    }

    ShineButton shineButton;
    TextView tv_blog_comment_num;

    @Override
    public void convert(ViewHolder holder, BlogNewReaderBean blogNewReaderBean, int position) {
        holder.setText(R.id.te_rv_title, blogNewReaderBean.getTitle());
        holder.setText(R.id.tv_blog_time, blogNewReaderBean.getTime());
        shineButton = holder.getView(R.id.ShineButton);
        tv_blog_comment_num = holder.getView(R.id.tv_blog_comment_num);
        holder.setText(R.id.tv_blog_comment_num, (int) (Math.random() * 100 + Math.random() * 4) + "");
        if (blogNewReaderBean.getImgUrl() != null) {
            ImageView imageView = (ImageView) holder.getView(R.id.img_rv_Pic);
            Glide.with(context)
                    .load(blogNewReaderBean.getImgUrl())
                    .placeholder(R.drawable.icon_tag)
                    .error(R.drawable.icon_tag)
                    .dontAnimate()
                    .centerCrop()
                    .into(imageView);
        }


        shineButton.setOnCheckStateChangeListener((view, checked) -> {
            Logger.e(checked + "");
            if (checked) {
                tv_blog_comment_num.setText(Integer.parseInt(tv_blog_comment_num.getText().toString()) + 1 + "");
            } else {
                tv_blog_comment_num.setText(Integer.parseInt(tv_blog_comment_num.getText().toString()) - 1 + "");
            }
        });
    }
}
