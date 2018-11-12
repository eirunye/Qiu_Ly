package com.hxnidc.qiu_ly.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.hxnidc.qiu_ly.R;
import com.hxnidc.qiu_ly.bean.RecomBean;
import com.hxnidc.qiu_ly.utils.ImageUtils;
import com.hxnidc.qiu_ly.utils.JsoupUtil;
import com.hxnidc.qiu_ly.utils.NetWorkUtils;
import com.hxnidc.qiu_ly.utils.ToastUtils;
import com.hxnidc.qiu_ly.utils.UIUtils;
import com.hxnidc.qiu_ly.widget.CommentDialog;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;
import com.zzhoujay.richtext.RichText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
//import cn.waps.AppConnect;
//import cn.waps.AppListener;

public class RecommendDetailsActivity extends BaseActivity implements CommentDialog.OnSendListener, View.OnClickListener {

    @BindView(R.id.detail_img)
    ImageView detailImg;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    @BindView(R.id.textview)
    TextView textview;
    @BindView(R.id.AdLinearLayout)
    LinearLayout AdLinearLayout;
    @BindView(R.id.tv_detail_show_recommend)
    TextView tvDetailShowRecommend;
    @BindView(R.id.recommend_RecyclerView)
    RecyclerView recommendRecyclerView;
    @BindView(R.id.tv_comment)
    TextView tvComment;
    @BindView(R.id.tv_more_comment)
    TextView tvMoreComment;
    @BindView(R.id.img_share)
    ImageView imgShare;
    @BindView(R.id.detail_content)
    CoordinatorLayout detailContent;
    @BindView(R.id.fab_share)
    FloatingActionButton fabShare;
    @BindView(R.id.fab_setting)
    FloatingActionButton fabSetting;
    @BindView(R.id.fab_comment)
    FloatingActionButton fabComment;
    @BindView(R.id.menu_fab)
    FloatingActionMenu menuFab;
    private RichText richText;
    private List<RecomBean> recomBeanList = new ArrayList<>();
    private String recommendHref;
    private CommonAdapter<RecomBean> commonAdapter;
    private LoadMoreWrapper mLoadMoreWrapper;
    private View adFootView;
    private CommentDialog commentDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recommend_details;
    }

    @Override
    protected void initView() {

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> this.finish());
        getSupportActionBar().setTitle("");
        toolbar.setOnMenuItemClickListener((item) -> {
            switch (item.getItemId()) {
                case R.id.action_share:
                    ToastUtils.showToast(RecommendDetailsActivity.this, "分享");
                    break;
            }
            return true;
        });
        fabShare.setOnClickListener(this);
        fabSetting.setOnClickListener(this);
        fabComment.setOnClickListener(this);
        menuFab.setClosedOnTouchOutside(true);
        recommendRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        commonAdapter = new CommonAdapter<RecomBean>(this, R.layout.item_rv_detail_recom_layout, recomBeanList) {
            @Override
            protected void convert(ViewHolder holder, RecomBean recomBean, int position) {
                if (recomBean == null) return;
                holder.setText(R.id.tv_title, recomBean.getTitle());
                //holder.setText(R.id.tv_tagNum, recomBean.getTag());
                ImageView imageView = (ImageView) holder.getView(R.id.img_Pic);
                ImageUtils.loadImgUrl(RecommendDetailsActivity.this, recomBean.getImgUrl(), imageView);
            }
        };
        recommendRecyclerView.setNestedScrollingEnabled(false);
        mLoadMoreWrapper = new LoadMoreWrapper(commonAdapter);
//        AppConnect.getInstance(this).setBannerAdNoDataListener(new AppListener() {
//            @Override
//            public void onBannerNoData() {
//                Log.e("debug", "Banner广告无数据");
//            }
//        });
        adFootView = LayoutInflater.from(this).inflate(R.layout.ad_rv_more_layout, recommendRecyclerView, false);
        LinearLayout adlayout = (LinearLayout) adFootView.findViewById(R.id.AdLinearLayout);
//        AppConnect.getInstance(this).showBannerAd(this, adlayout);
        mLoadMoreWrapper.setLoadMoreView(adFootView);
        recommendRecyclerView.setAdapter(mLoadMoreWrapper);
        commonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(RecommendDetailsActivity.this, RecommendDetailsActivity.class);
                intent.putExtra("recommendHref", commonAdapter.getDatas().get(position).getHref());
                intent.putExtra("recommendImgUrl", commonAdapter.getDatas().get(position).getImgUrl());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    protected void initNewSave(Bundle savedInstanceState) {


    }

    @Override
    protected void initData() {

        if (!NetWorkUtils.isConnectedByState(this)) {
            ToastUtils.showToast(this, "网络连接失败!");
            return;
        }
        getRecommendData();
        Logger.e("RECOM:" + recommendHref);
        getBottomRecList();
        commentDialog = new CommentDialog(this);
        commentDialog.setOnSendListener(this);
    }


    private void getRecommendData() {

        try {
            //AppConnect.getInstance(this).showBannerAd(this, AdLinearLayout);
            Intent intent = getIntent();
            recommendHref = intent.getStringExtra("recommendHref");
            Logger.e("recommendHref:" + recommendHref);
            String recommendImgUrl = intent.getStringExtra("recommendImgUrl");
            if (intent == null || TextUtils.isEmpty(recommendHref)) return;
            assert textview != null;
            new Thread(() -> {
                try {
                    String element = JsoupUtil.getNewRecommendDetails(activity, recommendHref);
                    if (TextUtils.isEmpty(element)) {
                        UIUtils.post(() -> {
                            if (avi != null) avi.hide();
                        });
                        return;
                    }
                    UIUtils.post(() -> {
                        if (avi != null)
                            avi.hide();
                        richText = RichText.from(element).into(textview);
                        //html_text.setHtml(element, new HtmlResImageGetter(html_text));
                        if (!TextUtils.isEmpty(recommendImgUrl))
                            ImageUtils.loadImgAdUrl(activity, recommendImgUrl, detailImg);
                        tvDetailShowRecommend.setVisibility(View.VISIBLE);

                    });

                } catch (Exception e) {
                    Logger.e("getRecommendData:" + e.getMessage());
                }
            }).start();
        } catch (Exception e) {
        }
    }

    private void getBottomRecList() {
        if (TextUtils.isEmpty(recommendHref)) {
            Logger.e("RECOM==:" + recommendHref + "");
            return;
        }
        new Thread(() -> {
            List<RecomBean> recomList = JsoupUtil.getRecommendListDetailData("http://www.cnys.com/yscs/3.html");
            if (recomList == null || recomList.size() <= 0) {
                UIUtils.postDelayed(() -> {
                    if (avi != null) {
                        avi.hide();
                    }
                }, 1000);
                return;
            }
            this.recomBeanList.addAll(recomList);
            UIUtils.post(() -> {
                mLoadMoreWrapper.notifyDataSetChanged();
                if (avi != null) {
                    avi.hide();
                }
            });
        }).start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (richText != null) {
            richText.clear();
            richText = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.blogs_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.tv_comment, R.id.tv_more_comment, R.id.img_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_comment:
                if (commentDialog != null)
                    commentDialog.show();
                break;
            case R.id.tv_more_comment:

                break;
            case R.id.img_share:
                break;
        }
    }

    @Override
    public void sendComment(String content) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_comment:
                if (commentDialog != null)
                    commentDialog.show();
                break;
            case R.id.fab_share:
                break;
            case R.id.fab_setting:
                break;
        }
        menuFab.close(true);
    }
}
