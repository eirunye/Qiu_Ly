package com.hxnidc.qiu_ly.activity.video;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxnidc.qiu_ly.R;
import com.hxnidc.qiu_ly.activity.BaseActivity;
import com.hxnidc.qiu_ly.bean.RecomBean;
import com.hxnidc.qiu_ly.config.RecommendJsoupController;
import com.hxnidc.qiu_ly.config.VideoJsoupController;
import com.hxnidc.qiu_ly.constant.Https;
import com.hxnidc.qiu_ly.listener.SampleListener;
import com.hxnidc.qiu_ly.utils.ImageUtils;
import com.hxnidc.qiu_ly.utils.NetWorkUtils;
import com.hxnidc.qiu_ly.utils.ToastUtils;
import com.hxnidc.qiu_ly.utils.UIUtils;
import com.hxnidc.qiu_ly.videos.LandLayoutVideo;
import com.hxnidc.qiu_ly.widget.CommentDialog;
import com.orhanobut.logger.Logger;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.wang.avi.AVLoadingIndicatorView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
//import cn.waps.AppConnect;

import static com.hxnidc.qiu_ly.activity.PlayerActivity.TRANSITION;


/**
 * Created by on 2017/8/18 15:17
 * Author：yrg
 * Describe:HappyPlayerActivity 视频
 */
public class ArtsOrSportsPlayerActivity extends BaseActivity {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_more_comment)
    TextView tvMoreComment;
    @BindView(R.id.videoRecyclerView)
    RecyclerView videoRecyclerView;
    @BindView(R.id.post_detail_nested_scroll)
    NestedScrollView postDetailNestedScroll;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    @BindView(R.id.detail_player)
    LandLayoutVideo detailPlayer;
    @BindView(R.id.tv_author)
    TextView tvAuthor;
    @BindView(R.id.btn_action_video)
    Button btnActionVideo;
    private ImageView imageView;
    private boolean isPlay;
    private boolean isPause;
    private OrientationUtils orientationUtils;
    private String title = "";
    private List<RecomBean> recomBeanList = new ArrayList<>();
    private String recommendHref;
    private CommonAdapter<RecomBean> commonAdapter;
    private LoadMoreWrapper mLoadMoreWrapper;
    private View adFootView;
    private CommentDialog commentDialog;
    private String hrefUrl;
    private String imgUrl;
    private String pm4Url;
    private boolean isTransition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isTransition = getIntent().getBooleanExtra(TRANSITION, false);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_arts_or_sports_player;
    }

    @Override
    protected void initView() {

        imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        resolveNormalVideoUI();
        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(this, detailPlayer);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);

        detailPlayer.setIsTouchWiget(true);
        //detailPlayer.setIsTouchWigetFull(false);
        //关闭自动旋转
        detailPlayer.setRotateViewAuto(false);
        detailPlayer.setLockLand(false);
        detailPlayer.setShowFullAnimation(false);
        detailPlayer.setNeedLockFull(true);
        detailPlayer.setSeekRatio(1);
        //detailPlayer.setOpenPreView(false);
        detailPlayer.getBackButton().setVisibility(View.VISIBLE);
        detailPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        detailPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //直接横屏
                orientationUtils.resolveByClick();

                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                detailPlayer.startWindowFullscreen(ArtsOrSportsPlayerActivity.this, true, true);
            }
        });

        detailPlayer.setStandardVideoAllCallBack(new SampleListener() {
            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
                //开始播放了才能旋转和全屏
                orientationUtils.setEnable(true);
                isPlay = true;
            }

            @Override
            public void onAutoComplete(String url, Object... objects) {
                super.onAutoComplete(url, objects);
            }

            @Override
            public void onClickStartError(String url, Object... objects) {
                super.onClickStartError(url, objects);
            }

            @Override
            public void onQuitFullscreen(String url, Object... objects) {
                super.onQuitFullscreen(url, objects);
                if (orientationUtils != null) {
                    orientationUtils.backToProtVideo();
                }
            }
        });

        detailPlayer.setLockClickListener(new LockClickListener() {
            @Override
            public void onClick(View view, boolean lock) {
                if (orientationUtils != null) {
                    //配合下方的onConfigurationChanged
                    orientationUtils.setEnable(!lock);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        //先返回正常状态
        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            detailPlayer.getFullscreenButton().performClick();
            return;
        }
        //释放所有
        detailPlayer.setStandardVideoAllCallBack(null);
        GSYVideoPlayer.releaseAllVideos();
        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.onBackPressed();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                }
            }, 500);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            if (newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_USER) {
                if (!detailPlayer.isIfCurrentIsFullscreen()) {
                    detailPlayer.startWindowFullscreen(ArtsOrSportsPlayerActivity.this, true, true);
                }
            } else {
                //新版本isIfCurrentIsFullscreen的标志位内部提前设置了，所以不会和手动点击冲突
                if (detailPlayer.isIfCurrentIsFullscreen()) {
                    StandardGSYVideoPlayer.backFromWindowFull(this);
                }
                if (orientationUtils != null) {
                    orientationUtils.setEnable(true);
                }
            }
        }
    }

    private void resolveNormalVideoUI() {
        //增加title
        detailPlayer.getTitleTextView().setVisibility(View.GONE);
        //detailPlayer.getTitleTextView().setText("文体视频");
        detailPlayer.getBackButton().setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPause = false;
        detailPlayer.onVideoPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isPause = true;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoPlayer.releaseAllVideos();
        //GSYPreViewManager.instance().releaseMediaPlayer();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }

    @Override
    protected void initNewSave(Bundle savedInstanceState) {
        videoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commonAdapter = new CommonAdapter<RecomBean>(this, R.layout.item_rv_video_recommend_layout, recomBeanList) {
            @Override
            protected void convert(ViewHolder holder, RecomBean recomBean, int position) {
                if (recomBean == null) return;
                holder.setText(R.id.tv_title, recomBean.getTitle());
                //holder.setText(R.id.tv_tagNum, recomBean.getTag());
                ImageView imageView = (ImageView) holder.getView(R.id.img_Pic);
                ImageUtils.loadImgUrl(ArtsOrSportsPlayerActivity.this, recomBean.getImgUrl(), imageView);
            }
        };
        mLoadMoreWrapper = new LoadMoreWrapper(commonAdapter);
        adFootView = LayoutInflater.from(this).inflate(R.layout.ad_rv_more_layout, videoRecyclerView, false);
        mLoadMoreWrapper.setLoadMoreView(adFootView);
        videoRecyclerView.setAdapter(mLoadMoreWrapper);
        commonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

                Intent intent = new Intent(ArtsOrSportsPlayerActivity.this, ArtsOrSportsPlayerActivity.class);
                intent.putExtra("hrefUrl", commonAdapter.getDatas().get(position).getHref());
                intent.putExtra("imgUrl", commonAdapter.getDatas().get(position).getImgUrl());
                intent.putExtra("title", commonAdapter.getDatas().get(position).getTitle());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            hrefUrl = intent.getStringExtra("hrefUrl");
            imgUrl = intent.getStringExtra("imgUrl");
            //Logger.e("href===>" + hrefUrl + "\n" + imgUrl);
            title = intent.getStringExtra("title");
        }
        if (!TextUtils.isEmpty(imgUrl)) {
            ImageUtils.loadImgAdUrl(ArtsOrSportsPlayerActivity.this, imgUrl, imageView);
            detailPlayer.setThumbImageView(imageView);
        }
        //显示广告
//        AppConnect.getInstance(this).initPopAd(this);
//        AppConnect.getInstance(this).showPopAd(this);
        getTvPlayerData();
        getBottomRecList();
    }

    /**
     *
     */
    private void getTvPlayerData() {

        try {
            if (!NetWorkUtils.isConnectedByState(this)) {
                ToastUtils.showToast(this, "网络连接失败!");
                return;
            }
            new Thread(() -> {
                pm4Url = VideoJsoupController.Instance().getVideHappyPlayer(hrefUrl);
                Logger.e("pm4Url:" + pm4Url);
                if (!TextUtils.isEmpty(pm4Url)) {
                    UIUtils.post(() -> {
                        detailPlayer.setUp(pm4Url, true, title);
                        tvMoreComment.setVisibility(View.VISIBLE);
                    });
                } else {
                    ToastUtils.showToast(ArtsOrSportsPlayerActivity.this, "网络请求失败");
                }
            }).start();

        } catch (Exception e) {

        }

    }

    private void getBottomRecList() {

        if (!NetWorkUtils.isConnectedByState(this)) {
            ToastUtils.showToast(this, "网络连接失败!");
            return;
        }
        new Thread(() -> {
            List<RecomBean> recomList = RecommendJsoupController.Instance().getArtSportRecommendList(Https.QIU_LY_ARTS_RECOMM_HTTP, "");
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

}
