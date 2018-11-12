package com.hxnidc.qiu_ly.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxnidc.qiu_ly.R;
import com.hxnidc.qiu_ly.bean.bmob.UserInfo;
import com.hxnidc.qiu_ly.config.Configurations;
import com.hxnidc.qiu_ly.utils.ConvertUtils;
import com.hxnidc.qiu_ly.utils.ToastUtils;
import com.hxnidc.qiu_ly.view.FireworkView;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends BaseActivity {


    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.register_toolbar)
    Toolbar registerToolbar;
    @BindView(R.id.et_register_name)
    EditText etRegisterName;
    @BindView(R.id.et_register_pass)
    EditText etRegisterPass;
    @BindView(R.id.tv_password)
    TextView tvPassword;
    @BindView(R.id.et_register_sure_pass)
    EditText etRegisterSurePass;
    @BindView(R.id.img_register_eye)
    ImageView imgRegisterEye;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.fire_register_work)
    FireworkView fireRegisterWork;
    @BindView(R.id.fire_register_username)
    FireworkView fireRegisterUsername;
    @BindView(R.id.fire_register_code)
    FireworkView fireRegisterCode;
    @BindView(R.id.LoadingIndicatorView)
    AVLoadingIndicatorView LoadingIndicatorView;
    private Boolean showPassword = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setContentView(R.layout.activity_register);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        try {
            imgRegisterEye.setImageResource(R.drawable.icon_eye_close);
            setSupportActionBar(registerToolbar);
            registerToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            getSupportActionBar().setTitle("");
            registerToolbar.setNavigationOnClickListener(v -> finish());
            fireRegisterWork.bindEditText(etRegisterPass);
            fireRegisterUsername.bindEditText(etRegisterName);
            fireRegisterCode.bindEditText(etRegisterSurePass);
            etRegisterName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    return (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER);
                }
            });
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
    }

    @Override
    protected void initNewSave(Bundle savedInstanceState) {
        if (Configurations.Instance().isLogin()) {
            ToastUtils.showToast(RegisterActivity.this, "您目前处于登录状态！");
        }
    }

    @Override
    protected void initData() {
        passAuthCode();
    }

    @OnClick({R.id.btn_register, R.id.img_register_eye})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                passRegister();
                break;
            case R.id.img_register_eye:
                passShowPassword();
                break;
        }
    }

    private void passShowPassword() {

        if (TextUtils.isEmpty(etRegisterPass.getText().toString().trim())) {
            return;
        }
        if (showPassword) {// 显示密码
            imgRegisterEye.setImageResource(R.drawable.ic_remove_red_eye_black_24dp);
            etRegisterPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            etRegisterPass.setSelection(etRegisterPass.getText().toString().length());
            showPassword = !showPassword;
        } else {// 隐藏密码
            imgRegisterEye.setImageResource(R.drawable.icon_eye_close);
            etRegisterPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            etRegisterPass.setSelection(etRegisterPass.getText().toString().length());
            showPassword = !showPassword;
        }
    }

    /**
     * 判断手机号码
     */
    private void passAuthCode() {

        etRegisterName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s == null || s.length() == 0)
                    return;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < s.length(); i++) {
                    if (i != 3 && i != 8 && s.charAt(i) == ' ') {
                        continue;
                    } else {
                        sb.append(s.charAt(i));
                        if ((sb.length() == 4 || sb.length() == 9)
                                && sb.charAt(sb.length() - 1) != ' ') {
                            sb.insert(sb.length() - 1, ' ');
                        }
                    }
                }
                if (!sb.toString().equals(s.toString())) {
                    int index = start + 1;
                    if (sb.charAt(start) == ' ') {
                        if (before == 0) {
                            index++;
                        } else {
                            index--;
                        }
                    } else {
                        if (before == 1) {
                            index--;
                        }
                    }
                    etRegisterName.setText(sb.toString());
                    etRegisterName.setSelection(index);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

//        if (countSeconds == 60) {
//
//            Log.e("tag", "mobile==" + mobile);
//
//        } else {
//            Toast.makeText(RegisterActivity.this, "不能重复发送验证码", Toast.LENGTH_SHORT).show();
//        }

    }

    /**
     * 发送手机获取验证
     *
     * @param mobile
     */
//    private void requestVerifyCode(String mobile) {
//
//        CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(tvRegisterAuthCode, 60000, 1000); //倒计时1分钟
//        mCountDownTimerUtils.start();
//
//        // TODO: 2017/7/27 提交手机号到后台获取验证码
//
//    }

    /**
     * 注册
     */
    private void passRegister() {

        try {
            String userphone = etRegisterName.getText().toString().replaceAll(" ", "");
            if (TextUtils.isEmpty(userphone)) {
                ToastUtils.showToast(this, "手机号码不能为空!");
                return;
            }
            String usercode = etRegisterPass.getText().toString().trim();
            String surePass = etRegisterSurePass.getText().toString().trim();
            if (TextUtils.isEmpty(usercode)) {
                ToastUtils.showToast(this, "密码不能为空!");
                return;
            }
            if (TextUtils.isEmpty(surePass)) {
                ToastUtils.showToast(this, "请输入确认密码!");
                return;
            }
            if (usercode.length() < 6) {
                ToastUtils.showToast(this, "密码不能小于6位!");
                return;
            }

            if (!usercode.equals(surePass)) {
                ToastUtils.showToast(this, "请确认密码!");
                return;
            }

            if (ConvertUtils.isPhone(userphone) == false) {
                new AlertDialog.Builder(this).setTitle("提示").setMessage("请输入正确的手机号码").setCancelable(true).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).show();
                return;
            }

            Logger.e("=====>进入" + userphone + "===" + usercode);
            if (LoadingIndicatorView != null) {
                LoadingIndicatorView.setVisibility(View.VISIBLE);
                LoadingIndicatorView.show();
            }
            UserInfo user = new UserInfo();
            user.setUsername(userphone);
            user.setPassword(usercode);
            addSubscription(user.signUp(new SaveListener<UserInfo>() {
                @Override
                public void done(UserInfo userInfo, BmobException e) {
                    if (LoadingIndicatorView != null) {
                        LoadingIndicatorView.hide();
                        LoadingIndicatorView.setVisibility(View.GONE);
                    }

                    if (e == null) {
                        ToastUtils.showToast(RegisterActivity.this, "注册成功!");

                        EventBus.getDefault().post(userInfo);
                        RegisterActivity.this.finish();
                    } else {
                        ToastUtils.showToast(RegisterActivity.this, "注册失败!");
                    }
                    Logger.e("======ok");
                }
            }));

        } catch (Exception e) {

        }


    }
}
