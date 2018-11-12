package com.hxnidc.qiu_ly.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.hxnidc.qiu_ly.R;
import com.hxnidc.qiu_ly.config.Configurations;
import com.hxnidc.qiu_ly.utils.ToastUtils;
import com.hxnidc.qiu_ly.utils.UIUtils;
import com.hxnidc.qiu_ly.widget.CountDownView;

import butterknife.BindView;
//import cn.waps.AppConnect;


public class WelcomeActivity extends BaseActivity implements CountDownView.CountDownTimerListener, View.OnClickListener {

    @BindView(R.id.view_greet_skip)
    CountDownView viewGreetSkip;
    @BindView(R.id.img_greet_advert)
    ImageView imgGreetAdvert;

    private boolean isClickInto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setContentView(R.layout.activity_welcome);
//        AppConnect.getInstance("22f37fbefb3ac4e7d5d04a2ed0cc3e04", "default", this);
//        AppConnect.getInstance(this);
        //Bmob.initialize(this, "26392b353ada98140b51dcd3efa2010d");
        setSteepStatusBar(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initView() {
        //AppConnect.getInstance(Constants.WANPU_APP_ID,"default",this);
//        AppConnect.getInstance(this);
//        AppConnect.getInstance(this).initAdInfo();


        //Bmob 初始化


        viewGreetSkip.setCountDownTimerListener(this);
        viewGreetSkip.setOnClickListener(this);
        viewGreetSkip.start();
    }

    @Override
    protected void initNewSave(Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onStartCount() {

    }

    @Override
    public void onFinishCount() {
        try {
            if (isClickInto)
                return;
            skipActivityFunction();
        } catch (Exception ex) {
            ToastUtils.showCenterToast(activity, getString(R.string.load_page_frail_string));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_greet_skip:
                isClickInto = true;
                skipActivityFunction();
                break;
            case R.id.img_greet_advert:
                // todo skip Advertising  details
                break;
        }
    }

    /**
     * 点击返回键finish app
     */
    @Override
    public void onBackPressed() {
        exitApp();
    }

    /**
     * 页面跳转
     */
    private void skipActivityFunction() {
        UIUtils.postDelayed(() -> {
            if (Configurations.Instance().isFirstInto())
                startOpenActivity(MainActivity.class);
            else {
                Configurations.Instance().setFirstInto(true);
                startOpenActivity(GuidanceActivity.class);
            }
            this.finish();
        }, 1000);

    }

    @Override
    protected void onResume() {
        //App.me().refreshResources(this);
        super.onResume();
    }
}
