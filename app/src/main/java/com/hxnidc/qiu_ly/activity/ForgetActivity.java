package com.hxnidc.qiu_ly.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.balysv.materialripple.MaterialRippleLayout;
import com.hxnidc.qiu_ly.R;
import com.hxnidc.qiu_ly.utils.ToastUtils;
import com.hxnidc.qiu_ly.view.FireworkView;

import butterknife.BindView;
import butterknife.OnClick;

public class ForgetActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edit_forget)
    EditText editForget;
    @BindView(R.id.btn_fund_password)
    Button btnFundPassword;
    @BindView(R.id.mr_login_layout)
    MaterialRippleLayout mrLoginLayout;
    @BindView(R.id.find_work)
    FireworkView fireWork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setContentView(R.layout.activity_forget);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget;
    }

    @Override
    protected void initView() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(v -> finish());
        fireWork.bindEditText(editForget);
    }

    @Override
    protected void initNewSave(Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.btn_fund_password)
    public void onViewClicked(View view) {

        ToastUtils.showToast(this, "正在找回密码!");

    }
}
