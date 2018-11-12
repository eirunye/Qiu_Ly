package com.hxnidc.qiu_ly.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.hxnidc.qiu_ly.R;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
//import cn.waps.AppConnect;

public class AdActivity extends AppCompatActivity {

    @BindView(R.id.AdLinearLayout)
    LinearLayout AdLinearLayout;
    @BindView(R.id.miniAdLinearLayout)
    LinearLayout miniAdLinearLayout;
    @BindView(R.id.button)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
        ButterKnife.bind(this);
//        AppConnect.getInstance("22f37fbefb3ac4e7d5d04a2ed0cc3e04", "default", this);
        init();
//        AppConnect.getInstance(this).initPopAd(this);
        LinearLayout adlayout = (LinearLayout) findViewById(R.id.AdLinearLayout);
//        AppConnect.getInstance(this).showBannerAd(this, adlayout);
    }

    private void init() {

        //AppConnect.getInstance(this);
        //AppConnect.getInstance(this).initAdInfo();
//        AppConnect.getInstance(this).setBannerAdNoDataListener(new AppListener() {
//            @Override
//            public void onBannerNoData() {
//                Logger.e("Banner广告无数据");
//            }
//        });
//
//
//        //设置关闭积分墙癿监听接口，必须在showOffers接口之前调用
//        AppConnect.getInstance(this).setOffersCloseListener(new AppListener() {
//            @Override
//            public void onOffersClose() {
//                // TODO 关闭积分墙时癿操作代码
//                super.onOffersClose();
//            }
//        });

    }

    @Override
    protected void onDestroy() {
//        AppConnect.getInstance(this).close();
        super.onDestroy();
    }

    @OnClick(R.id.button)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button:
                //AppConnect.getInstance(this).showPopAd(this);
                //AppConnect.getInstance(this).showAppOffers(this);
                //AppConnect.getInstance(AdActivity.this).showAppOffers(this);
                Logger.e("================dianji============");
                //AppConnect.getInstance(this).showOffers(this);

                //设置迷你广告背景颜色
//                AppConnect.getInstance(this).setAdBackColor(Color.argb(50, 120, 240, 120));
//设置迷你广告广告诧颜色
//                AppConnect.getInstance(this).setAdForeColor(Color.YELLOW);
//若未设置以上两个颜色，则默认为黑底白字
                LinearLayout miniLayout = (LinearLayout) findViewById(R.id.miniAdLinearLayout);
//                AppConnect.getInstance(this).showMiniAd(this, miniLayout, 10); //默认 10 秒切换一次广告
                break;
        }
    }
}
