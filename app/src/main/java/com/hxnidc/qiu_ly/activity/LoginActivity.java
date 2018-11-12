package com.hxnidc.qiu_ly.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bm.library.Info;
import com.bm.library.PhotoView;
import com.hxnidc.qiu_ly.R;
import com.hxnidc.qiu_ly.bean.bmob.UserInfo;
import com.hxnidc.qiu_ly.config.Configurations;
import com.hxnidc.qiu_ly.utils.ConvertUtils;
import com.hxnidc.qiu_ly.utils.NetWorkUtils;
import com.hxnidc.qiu_ly.utils.ToastUtils;
import com.hxnidc.qiu_ly.utils.Utils;
import com.hxnidc.qiu_ly.view.FireworkView;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscriber;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_login_name)
    EditText etLoginName;
    @BindView(R.id.et_login_password)
    EditText etLoginPassword;
    @BindView(R.id.fire_login_work)
    FireworkView mFireworkView;
    @BindView(R.id.fire_login_username)
    FireworkView mFireUserName;
    @BindView(R.id.login_layout)
    RelativeLayout layout;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.tv_forget_password)
    TextView tvForgetPassword;
    @BindView(R.id.tv_login_register)
    TextView tvLoginRegister;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_login_photo)
    CircleImageView imgLoginPhoto;
    @BindView(R.id.img_login_eye)
    ImageView imgLoginEye;
    @BindView(R.id.img1)
    PhotoView img1;
    @BindView(R.id.img2)
    PhotoView img2;
    @BindView(R.id.LoadingIndicatorView)
    AVLoadingIndicatorView LoadingIndicatorView;
    private Boolean showPassword = true;
    Dialog bottomDialog;

    private String username = "";
    Info mRectF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setContentView(R.layout.activity_login);
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        try {

            imgLoginEye.setImageDrawable(getResources().getDrawable(R.drawable.icon_eye_close));
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            getSupportActionBar().setTitle("");
            toolbar.setNavigationOnClickListener(v -> finish());
            mFireworkView.bindEditText(etLoginName);
            mFireUserName.bindEditText(etLoginPassword);
            Utils.editTextShowPhone(etLoginName);

//            etLoginName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//                @Override
//                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                    return (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER);
//                }
//            });
//            etLoginPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//                @Override
//                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                    return (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER);
//                }
//            });

            if (Configurations.Instance().isFirstInto()) {
            }
        } catch (Exception ex) {
        }

    }

    @Override
    protected void initNewSave(Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {
        try {
            if (Configurations.Instance().isLogin() && BmobUser.getCurrentUser(UserInfo.class) != null) {
                if (Configurations.Instance().getBitmap() != null) {
                    showImages(Configurations.Instance().getBitmap());
                    img2.setImageBitmap(Configurations.Instance().getBitmap());
                }
            }

            if (!TextUtils.isEmpty(Configurations.Instance().getUsername())) {
                etLoginName.setText(Configurations.Instance().getUsername());
                Utils.editTextShowPhone(etLoginName);
            }

        } catch (Exception e) {
        }

    }

    @OnClick({R.id.img2, R.id.img_login_photo, R.id.btn_login, R.id.tv_login_register, R.id.tv_forget_password, R.id.img_login_eye})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_login_photo:
                passAvatar();
                break;
            case R.id.btn_login:
                passLogins();
                break;
            case R.id.tv_login_register:
                startOpenActivity(RegisterActivity.class);
                break;
            case R.id.tv_forget_password:

                startOpenActivity(ForgetActivity.class);

                break;
            case R.id.img_login_eye:
                passShowPassword();
                break;
            case R.id.img2:
                img2.enable();
                dimessImg();
                break;
        }
    }


    private void passShowPassword() {
        try {
            if (TextUtils.isEmpty(etLoginPassword.getText().toString().trim())) {
                return;
            }
            if (showPassword) {// 显示密码
                imgLoginEye.setImageDrawable(getResources().getDrawable(R.drawable.ic_remove_red_eye_black_24dp));
                etLoginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                etLoginPassword.setSelection(etLoginPassword.getText().toString().length());
                showPassword = !showPassword;
            } else {// 隐藏密码
                imgLoginEye.setImageDrawable(getResources().getDrawable(R.drawable.icon_eye_close));
                etLoginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                etLoginPassword.setSelection(etLoginPassword.getText().toString().length());
                showPassword = !showPassword;
            }
        } catch (Exception e) {
        }
    }

    /**
     * 获取头像
     */
    private void passAvatar() {

        try {
            //if (bottomDialog != null) bottomDialog.show();
            img1.disenable();
            if (Configurations.Instance().isLogin() && BmobUser.getCurrentUser(UserInfo.class) != null && Configurations.Instance().getBitmap() != null) {
                img2.setVisibility(View.VISIBLE);
                //获取img1的信息
                mRectF = img1.getInfo();
                //让img2从img1的位置变换到他本身的位置
                img2.animaFrom(mRectF);

            } else {

            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onBackPressed() {
        if (img2.getVisibility() == View.VISIBLE) {
            img2.animaTo(mRectF, new Runnable() {
                @Override
                public void run() {
                    img2.setVisibility(View.GONE);
                    //img1.setVisibility(View.VISIBLE);
                }
            });
        } else {
            super.onBackPressed();
        }
    }

    private void dimessImg() {

        // 让img2从自身位置变换到原来img1图片的位置大小
        img2.animaTo(mRectF, new Runnable() {
            @Override
            public void run() {
                img2.setVisibility(View.GONE);
                //img1.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void passLogins() {

        try {

            if (NetWorkUtils.isDisconnectedByState(this)) {
                ToastUtils.showToast(this, "亲, 木有网络 ( ⊙ o ⊙ )");
                return;
            }

            String phone = etLoginName.getText().toString().replaceAll(" ", "");
            if (TextUtils.isEmpty(phone)) {
                ToastUtils.showToast(this, "手机号码不能为空!");
                return;
            }
            String password = etLoginPassword.getText().toString().trim();
            if (TextUtils.isEmpty(password)) {
                ToastUtils.showToast(this, "密码不能为空!");
                return;
            }
            if (password.length() < 6) {
                ToastUtils.showToast(this, "密码长度不能小于6位");
                return;
            }
            if (ConvertUtils.isPhone(phone) == false) {
                new AlertDialog.Builder(this).setTitle("提示").setMessage("请输入正确的手机号码").setCancelable(true).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).show();
                return;
            }
            Logger.e("=====>" + phone + "=====" + password);
            if (LoadingIndicatorView != null) {
                LoadingIndicatorView.setVisibility(View.VISIBLE);
                LoadingIndicatorView.show();
            }
            UserInfo userinfo = new UserInfo();
            userinfo.setUsername(phone);
            userinfo.setPassword(password);
            userinfo.loginObservable(BmobUser.class).subscribe(new Subscriber<BmobUser>() {
                @Override
                public void onCompleted() {
                    Logger.e("----onCompleted----");
                }

                @Override
                public void onError(Throwable e) {
                    if (LoadingIndicatorView != null) {
                        LoadingIndicatorView.setVisibility(View.GONE);
                        LoadingIndicatorView.hide();
                    }
                    ToastUtils.showToast(LoginActivity.this, "登录失败!");
                    Logger.e(new BmobException(e).getMessage());
                }

                @Override
                public void onNext(BmobUser bmobUser) {
                    if (LoadingIndicatorView != null) {
                        LoadingIndicatorView.setVisibility(View.GONE);
                        LoadingIndicatorView.hide();
                    }
                    ToastUtils.showToast(LoginActivity.this, bmobUser.getUsername() + "登陆成功");
                    Configurations.Instance().setLogin(true);
                    EventBus.getDefault().post(userinfo);
                    //Configurations.Instance().setPassword(password);
                    Configurations.Instance().setUsername(userinfo.getUsername());
                    LoginActivity.this.finish();
                }
            });
        } catch (Exception e) {
            Logger.e(e.getMessage() + "");
        }

    }

    private void showImages(Bitmap bitmap) {
        imgLoginPhoto.setImageBitmap(bitmap);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventUserInfos(UserInfo user) {
        try {
            if (user == null) return;
            username = user.getUsername();
            if (etLoginName != null) {
                etLoginName.setText(user.getPhone());
                Utils.editTextShowPhone(etLoginName);
                etLoginPassword.setText("");
            }
            if (Configurations.Instance().isLogin()) {
                if (Configurations.Instance().getBitmap() != null) {
                    imgLoginPhoto.setImageBitmap(Configurations.Instance().getBitmap());
                }
            } else {
                imgLoginPhoto.setImageResource(R.mipmap.avatar_icon);
            }
        } catch (Exception e) {
        }
    }
    // TODO: 2017/8/8 获取图片设置
}


























