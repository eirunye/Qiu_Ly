package com.hxnidc.qiu_ly.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.hxnidc.qiu_ly.R;

import butterknife.BindView;
import cn.bingoogolapple.bgabanner.BGABanner;

public class GuidanceActivity extends BaseActivity {

    @BindView(R.id.banner_guide_background)
    BGABanner bannerGuideBackground;
    @BindView(R.id.banner_guide_foreground)
    BGABanner bannerGuideForeground;
    @BindView(R.id.tv_guide_skip)
    TextView tvGuideSkip;
    @BindView(R.id.btn_guide_enter)
    Button btnGuideEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setSteepStatusBar(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_guidance;
    }

    @Override
    protected void initView() {

        bannerGuideForeground.setEnterSkipViewIdAndDelegate(R.id.btn_guide_enter, R.id.tv_guide_skip, () -> {
            startOpenActivity(MainActivity.class);
            finish();
        });
    }

    @Override
    protected void initNewSave(Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {
        bannerGuideBackground.setData(R.drawable.uoko_guide_background_1, R.drawable.uoko_guide_background_2, R.drawable.uoko_guide_background_3);
        bannerGuideForeground.setData(R.drawable.uoko_guide_foreground_1, R.drawable.uoko_guide_foreground_2, R.drawable.uoko_guide_foreground_3);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bannerGuideBackground.setBackgroundResource(android.R.color.white);
    }
}
