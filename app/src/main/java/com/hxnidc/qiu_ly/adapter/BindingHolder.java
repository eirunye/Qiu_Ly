package com.hxnidc.qiu_ly.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by user on 2017-05-23.
 */

public class BindingHolder extends RecyclerView.ViewHolder {

    private ViewDataBinding mViewDataBinding;
    public View container;
    //public AnimateCheckBox animateCheckBox;
    public Button button;
    public LinearLayout linearLayout;
    public TextView textView;
    public ImageView imageView;

    public BindingHolder(View rowView) {
        super(rowView);
        container = rowView;
        mViewDataBinding = DataBindingUtil.bind(rowView);
    }

    public ViewDataBinding getBinding() {
        return mViewDataBinding;
    }
}