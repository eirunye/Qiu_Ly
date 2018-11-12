package com.hxnidc.qiu_ly.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hxnidc.qiu_ly.R;
import com.hxnidc.qiu_ly.bean.bmob.UserBean;
import com.hxnidc.qiu_ly.utils.ToastUtils;
import com.hxnidc.qiu_ly.view.FireworkView;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.OnClick;

public class SetPassWordActivity extends BaseActivity {

    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_password_sure)
    EditText etPasswordSure;
    @BindView(R.id.fire_setPass_username)
    FireworkView fireSetPassUsername;
    @BindView(R.id.fire_setPass_work)
    FireworkView fireSetPassWork;
    @BindView(R.id.fire_setPass_sure)
    FireworkView fireSetPassSure;
    @BindView(R.id.btn_setPassword)
    Button btnSetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setContentView(R.layout.activity_set_pass_word);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_pass_word;
    }

    @Override
    protected void initView() {

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> this.finish());
        toolbar.setTitle("");
        fireSetPassWork.bindEditText(etPassword);
        fireSetPassSure.bindEditText(etPasswordSure);
        fireSetPassUsername.bindEditText(etUsername);
        etUsername.setText(UserBean.Instance().getUsername());

        etPassword.setOnEditorActionListener((textView, i, keyEvent) -> {
            return (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER);

        });
        etPasswordSure.setOnEditorActionListener((textView, i, keyEvent) -> {
            return (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER);
        });
        etUsername.setOnEditorActionListener((textView, i, keyEvent) -> {
            return (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER);
        });
    }

    @Override
    protected void initNewSave(Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.btn_setPassword)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_setPassword:
                passSubmit();
                break;
        }
    }

    private void passSubmit() {
        try {
            String username = etUsername.getText().toString().trim();
            if (TextUtils.isEmpty(username)) {
                ToastUtils.showToast(this, "用户名不能为空!");
                return;
            }
            String password = etPassword.getText().toString().trim();
            if (TextUtils.isEmpty(password)) {
                ToastUtils.showToast(this, "密码不能为空!");
                return;
            }
            String passwordSure = etPasswordSure.getText().toString().trim();
            if (TextUtils.isEmpty(passwordSure)) {
                ToastUtils.showToast(this, "请填写确认密码!");
                return;
            }
            if (password.length() < 6) {
                ToastUtils.showToast(this, "密码长度不能小于6");
                return;
            }
            if (!password.equals(passwordSure)) {
                ToastUtils.showToast(this, "请确认密码!");
                return;
            }

//            UserBean.Instance().setPassword(password);
//            UserBean.Instance().signUp(new SaveListener<UserBean>() {
//                @Override
//                public void done(UserBean userBean, BmobException e) {
//
//                    if (userBean == null) {
//                        ToastUtils.showToast(SetPassWordActivity.this, "设置密码失败!");
//                        return;
//                    } else {
//                        ToastUtils.showToast(SetPassWordActivity.this, "设置密码成功，可以使用手机号和密码登录!");
//                        SetPassWordActivity.this.finish();
//                    }
//                }
//            });
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }

    }
}
