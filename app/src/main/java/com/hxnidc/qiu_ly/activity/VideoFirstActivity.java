package com.hxnidc.qiu_ly.activity;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hxnidc.qiu_ly.R;
import com.hxnidc.qiu_ly.adapter.RecyclerBaseAdapter;
import com.hxnidc.qiu_ly.bean.model.VideoModel;
import com.hxnidc.qiu_ly.holder.RecyclerItemViewHolder;
import com.hxnidc.qiu_ly.listener.SampleListener;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.ListVideoUtil;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class VideoFirstActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.video_recyclerView)
    RecyclerView videoRecyclerView;
    @BindView(R.id.video_full_container)
    FrameLayout videoFullContainer;
    LinearLayoutManager linearLayoutManager;
    int lastVisibleItem;
    int firstVisibleItem;
    ListVideoUtil listVideoUtil;
    RecyclerBaseAdapter recyclerBaseAdapter;
    List<VideoModel> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(new Explode());
        }
//        setContentView(R.layout.activity_video_first);
//        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_first;
    }

    @Override
    protected void initView() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> this.finish());
        getSupportActionBar().setTitle("");

        linearLayoutManager = new LinearLayoutManager(this);
        videoRecyclerView.setLayoutManager(linearLayoutManager);
        listVideoUtil = new ListVideoUtil(this);
        listVideoUtil.setFullViewContainer(videoFullContainer);
        listVideoUtil.setHideStatusBar(true);
        listVideoUtil.setHideActionBar(true);
        resolveData();
        recyclerBaseAdapter = new RecyclerBaseAdapter(this, dataList);
        recyclerBaseAdapter.setListVideoUtil(listVideoUtil);
    }

    @Override
    protected void initNewSave(Bundle savedInstanceState) {

        videoRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                // Debuger.printfLog("firstVisibleItem " + firstVisibleItem +" lastVisibleItem " + lastVisibleItem);
                //大于0说明有播放,//对应的播放列表TAG
                if (listVideoUtil.getPlayPosition() >= 0 && listVideoUtil.getPlayTAG().equals(RecyclerItemViewHolder.TAG)) {
                    //当前播放的位置
                    int position = listVideoUtil.getPlayPosition();
                    //不可视的是时候
                    if ((position < firstVisibleItem || position > lastVisibleItem)) {
                        //如果是小窗口就不需要处理
                        if (!listVideoUtil.isSmall() && !listVideoUtil.isFull()) {
                            //小窗口
                            int size = CommonUtil.dip2px(VideoFirstActivity.this, 150);
                            //actionbar为true才不会掉下面去
                            listVideoUtil.showSmallVideo(new Point(size, size), true, true);
                        }
                    } else {
                        if (listVideoUtil.isSmall()) {
                            listVideoUtil.smallVideoToNormal();
                        }
                    }
                }
            }
        });

        listVideoUtil.setVideoAllCallBack(new SampleListener() {
            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
                Debuger.printfLog("Duration " + listVideoUtil.getDuration() + " CurrentPosition " + listVideoUtil.getCurrentPositionWhenPlaying());
            }

            @Override
            public void onQuitSmallWidget(String url, Object... objects) {
                super.onQuitSmallWidget(url, objects);
                //大于0说明有播放,//对应的播放列表TAG
                if (listVideoUtil.getPlayPosition() >= 0 && listVideoUtil.getPlayTAG().equals(RecyclerItemViewHolder.TAG)) {
                    //当前播放的位置
                    int position = listVideoUtil.getPlayPosition();
                    //不可视的是时候
                    if ((position < firstVisibleItem || position > lastVisibleItem)) {
                        //释放掉视频
                        listVideoUtil.releaseVideoPlayer();
                        recyclerBaseAdapter.notifyDataSetChanged();
                    }
                }

            }
        });
        videoRecyclerView.setAdapter(recyclerBaseAdapter);

    }

    @Override
    protected void initData() {

    }

    private void resolveData() {
        for (int i = 0; i < 19; i++) {
            VideoModel videoModel = new VideoModel();
            dataList.add(videoModel);
        }
        if (recyclerBaseAdapter != null)
            recyclerBaseAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if (listVideoUtil.backFromFull()) {
            return;
        }
        super.onBackPressed();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        listVideoUtil.releaseVideoPlayer();
        GSYVideoPlayer.releaseAllVideos();
    }
}
