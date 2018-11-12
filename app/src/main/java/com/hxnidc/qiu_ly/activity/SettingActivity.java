package com.hxnidc.qiu_ly.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.hxnidc.qiu_ly.R;
import com.hxnidc.qiu_ly.bean.bmob.UserInfo;
import com.hxnidc.qiu_ly.config.Configurations;
import com.hxnidc.qiu_ly.utils.ToastUtils;
import com.hxnidc.qiu_ly.utils.UIUtils;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;

/**
 * Created by on 2017/6/22 14:53
 * Author：yrg
 * Describe:
 */


public class SettingActivity extends BaseActivity {

    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.text_set_Cache)
    TextView text_set_Cache;
    @BindView(R.id.setting_toolbar)
    Toolbar settingToolbar;
    @BindView(R.id.subject_switchCompat)
    SwitchCompat subjectSwitchCompat;
    @BindView(R.id.weibo_switchCompat)
    SwitchCompat weiboSwitchCompat;
    @BindView(R.id.qq_switchCompat)
    SwitchCompat qqSwitchCompat;
    @BindView(R.id.push_switchCompat)
    SwitchCompat pushSwitchCompat;
    @BindView(R.id.not_show_switchCompat)
    SwitchCompat notShowSwitchCompat;
    @BindView(R.id.exit_login)
    Button exitLogin;
    @BindView(R.id.mr_login_layout)
    MaterialRippleLayout mrLoginLayout;
    @BindView(R.id.largeLabel)
    RelativeLayout largeLabel;
    @BindView(R.id.LoadingIndicatorView)
    AVLoadingIndicatorView LoadingIndicatorView;
    @BindView(R.id.ll_setting_clear)
    LinearLayout llSettingClear;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        setContentView(R.layout.activity_setting);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        try {
            setSupportActionBar(settingToolbar);
            settingToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            getSupportActionBar().setTitle("");
            settingToolbar.setNavigationOnClickListener(v -> finish());
            if (!Configurations.Instance().isLogin() || BmobUser.getCurrentUser(UserInfo.class) == null) {
                exitLogin.setBackgroundResource(R.color.gray);
            } else {
                exitLogin.setBackgroundResource(R.color.colorAccent);
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected void initNewSave(Bundle savedInstanceState) {

    }

    private void init() {

    }

    @Override
    protected void initData() {

        text_set_Cache.setText((int) (Math.random() * 50) + "M");
    }

    @OnClick({R.id.exit_login, R.id.ll_setting_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.exit_login:
                if (Configurations.Instance().isLogin() && BmobUser.getCurrentUser(UserInfo.class) != null) {
                    LoadingIndicatorView.setVisibility(View.VISIBLE);
                    LoadingIndicatorView.show();
                    Configurations.Instance().setLogin(false);
                    BmobUser.logOut();
                    EventBus.getDefault().post(new UserInfo());
                    UIUtils.postDelayed(() -> {
                        LoadingIndicatorView.setVisibility(View.GONE);
                        LoadingIndicatorView.hide();
                        exitLogin.setBackgroundResource(R.color.gray);
                        ToastUtils.showToast(SettingActivity.this, "您已退出登录!");
                    }, 1000);
                }
                break;
            case R.id.ll_setting_clear:
                text_set_Cache.setText("0");
                break;
        }
    }
}
