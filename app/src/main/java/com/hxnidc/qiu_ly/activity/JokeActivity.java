package com.hxnidc.qiu_ly.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hxnidc.qiu_ly.R;
import com.hxnidc.qiu_ly.bean.Jokebean;
import com.hxnidc.qiu_ly.utils.JsoupUtil;
import com.hxnidc.qiu_ly.utils.NetWorkUtils;
import com.hxnidc.qiu_ly.utils.ToastUtils;
import com.hxnidc.qiu_ly.utils.UIUtils;
import com.orhanobut.logger.Logger;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.wang.avi.AVLoadingIndicatorView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;
import com.zzhoujay.richtext.RichText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class JokeActivity extends BaseActivity implements LoadMoreWrapper.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.joke_recyclerview)
    RecyclerView jokeRecyclerview;
    @BindView(R.id.joke_swipeRefreshLayout)
    SwipeRefreshLayout jokeSwipeRefreshLayout;
    @BindView(R.id.loadingIndicatorView)
    AVLoadingIndicatorView loadingIndicatorView;
    private List<Jokebean> jokebeanList = new ArrayList<>();
    private CommonAdapter<Jokebean> adapter;
    private RichText richText;
    private LoadMoreWrapper mLoadMoreWrapper;
    private View footView;
    private ProgressBar progressBar;
    private TextView loading_text;
    int maxDescripLine = 5;
    private int page = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setContentView(R.layout.activity_joke);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_joke;
    }

    @Override
    protected void initView() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(v -> this.finish());
        loadingIndicatorView.show();
    }

    @Override
    protected void initNewSave(Bundle savedInstanceState) {
        jokeSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.accent);
        jokeSwipeRefreshLayout.setOnRefreshListener(this);
        jokeRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommonAdapter<Jokebean>(JokeActivity.this, R.layout.item_rv_joke_layout, jokebeanList) {
            @Override
            protected void convert(ViewHolder holder, Jokebean jokebean, int position) {

                if (jokebean == null) return;
                TextView textView = holder.getView(R.id.description_view);
                TextView tvJokenum = holder.getView(R.id.tv_joke_num);
                ImageView imgExpand = holder.getView(R.id.expand_view);
                ShineButton shineButton = (ShineButton) holder.getView(R.id.po_btn_like);
                assert textView != null;
                if (!TextUtils.isEmpty(jokebean.getContent())) {
                    richText = RichText.from(jokebean.getContent()).into(textView);
                }
                tvJokenum.setText(jokebean.getLinkNum());
                shineButton.setOnCheckStateChangeListener((view, checked) -> {
                    Logger.e(checked + "");
                    if (checked) {
                        tvJokenum.setText(Integer.parseInt(jokebean.getLinkNum()) + 1 + "");
                    } else {
                        tvJokenum.setText(Integer.parseInt(tvJokenum.getText().toString()) - 1 + "");
                    }
                });
                textView.setHeight(textView.getLineHeight() * maxDescripLine);
                if (textView.getLineCount() > maxDescripLine) {
                    imgExpand.setVisibility(View.VISIBLE);
                } else {
                    imgExpand.setVisibility(View.GONE);
                }

                //imgExpand.setVisibility(textView.getLineCount() > maxDescripLine ? View.VISIBLE : View.GONE);

//                textView.post(() -> {
//                    imgExpand.setVisibility(textView.getLineCount() > maxDescripLine ? View.VISIBLE : View.GONE);
//                });
                imgExpand.setOnClickListener(new View.OnClickListener() {
                    boolean isExpand;//是否已展开的状态

                    @Override
                    public void onClick(View view) {
                        isExpand = !isExpand;
                        textView.clearAnimation();//清楚动画效果
                        final int deltaValue;//默认高度，即前边由maxLine确定的高度
                        final int startValue = textView.getHeight();//起始高度
                        int durationMillis = 350;//动画持续时间
                        if (isExpand) {
                            /**
                             * 折叠动画
                             * 从实际高度缩回起始高度
                             */
                            deltaValue = textView.getLineHeight() * textView.getLineCount() - startValue;
                            RotateAnimation animation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                            animation.setDuration(durationMillis);
                            animation.setFillAfter(true);
                            imgExpand.startAnimation(animation);
                        } else {
                            /**
                             * 展开动画
                             * 从起始高度增长至实际高度
                             */
                            deltaValue = textView.getLineHeight() * maxDescripLine - startValue;
                            RotateAnimation animation = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                            animation.setDuration(durationMillis);
                            animation.setFillAfter(true);
                            imgExpand.startAnimation(animation);
                        }
                        Animation animation = new Animation() {
                            protected void applyTransformation(float interpolatedTime, Transformation t) { //根据ImageView旋转动画的百分比来显示textview高度，达到动画效果
                                textView.setHeight((int) (startValue + deltaValue * interpolatedTime));
                            }
                        };
                        animation.setDuration(durationMillis);
                        textView.startAnimation(animation);
                    }
                });
            }
        };
        mLoadMoreWrapper = new LoadMoreWrapper(adapter);
        footView = LayoutInflater.from(this).inflate(R.layout.default_loading, jokeRecyclerview, false);
        progressBar = (ProgressBar) footView.findViewById(R.id.ProgressBar);
        loading_text = (TextView) footView.findViewById(R.id.loading_text);
        mLoadMoreWrapper.setLoadMoreView(footView);
        mLoadMoreWrapper.setOnLoadMoreListener(this);
        jokeRecyclerview.setAdapter(mLoadMoreWrapper);
    }

    @Override
    protected void initData() {
        getJokeData();
    }

    private void getJokeData() {
        try {
            new Thread(() -> {
                List<Jokebean> jokebeanLists = JsoupUtil.getJokeData("http://www.biedoul.com/wenzi/lengxiaohua/40/");
                if (jokebeanLists == null || jokebeanLists.size() <= 0) {
                    UIUtils.post(() -> {
                        loadingIndicatorView.hide();
                    });
                    return;
                }
                this.jokebeanList.addAll(jokebeanLists);
                UIUtils.post(() -> {
                    if (jokebeanList != null)
                        mLoadMoreWrapper.notifyDataSetChanged();
                    loadingIndicatorView.hide();
                });
            }).start();
        } catch (Exception e) {
            Logger.e(e.getMessage() + "");
        }
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
    public void onLoadMoreRequested() {
        try {
            if (NetWorkUtils.isDisconnectedByState(this)) {
                progressBar.setVisibility(View.GONE);
                loading_text.setText("网络连接失败!");
                return;
            }
            if (page == 35) {
                progressBar.setVisibility(View.GONE);
                loading_text.setText("没有数据了");
                return;
            } else {
                progressBar.setVisibility(View.VISIBLE);
                loading_text.setText("正在加载...");
            }
            page++;
            String url = Math.random() > 0.5 ? "http://www.biedoul.com/wenzi/lengxiaohua/" + page + "/" : "http://www.biedoul.com/wenzi/nhduanzi/" + page + "/";
            new Thread(() -> {
                List<Jokebean> jokebeanLists = new ArrayList<>(JsoupUtil.getJokeData(url));
                if (jokebeanLists == null || jokebeanLists.size() <= 0) {
                    UIUtils.post(() -> {
                        progressBar.setVisibility(View.GONE);
                        loading_text.setText("加载数据失败!");
                    });
                    return;
                }
                this.jokebeanList.addAll(jokebeanLists);
                UIUtils.post(() -> {
                    progressBar.setVisibility(View.VISIBLE);
                    loading_text.setText("正在加载...");
                    mLoadMoreWrapper.notifyDataSetChanged();
                });
            }).start();
        } catch (Exception e) {

        }


    }

    @Override
    public void onRefresh() {
        try {
            UIUtils.post(() -> jokeSwipeRefreshLayout.setRefreshing(true));
            if (NetWorkUtils.isDisconnectedByState(this)) {
                jokeSwipeRefreshLayout.setRefreshing(false);
                ToastUtils.showToast(this, "网络连接失败!");
                return;
            }
            String url = Math.random() > 0.5 ? "http://www.biedoul.com/wenzi/lengxiaohua/2/" : "http://www.biedoul.com/wenzi/nhduanzi/3/";
            new Thread(() -> {
                List<Jokebean> jokebeanLists = new ArrayList<>(JsoupUtil.getJokeData(url));
                if (jokebeanLists == null || jokebeanLists.size() <= 0) {
                    new Handler().postDelayed(() -> jokeSwipeRefreshLayout.setRefreshing(false), 2000);
                    //ToastUtils.showToast(getContext(), "没有最新了!");
                    return;
                }
                this.jokebeanList.clear();
                this.jokebeanList.addAll(jokebeanLists);
                UIUtils.post(() -> {
                    page = 20;
                    jokeSwipeRefreshLayout.setRefreshing(false);
                    mLoadMoreWrapper.notifyDataSetChanged();
                });
            }).start();
        } catch (Exception e) {
            Logger.e(e.getMessage() + "");
        }
    }
}



























