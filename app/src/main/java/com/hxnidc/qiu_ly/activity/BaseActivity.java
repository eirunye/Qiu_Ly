package com.hxnidc.qiu_ly.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.hxnidc.qiu_ly.R;
import com.hxnidc.qiu_ly.utils.PermissionThemeWrapper;
import com.hxnidc.qiu_ly.utils.PermissionUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
//import cn.waps.AppConnect;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by on 2017/6/22 14:52
 * Author：yrg
 * Describe:
 */


public abstract class BaseActivity extends AppCompatActivity {

    // public static NetEvent event;
    public BaseActivity activity;
    private int netMobile;
    protected AlertDialog mRequestSettingsDialog = null;
    protected AlertDialog mPermissionFinishDialog = null;

    /**
     * 记录处于前台的Activity
     */
    private static BaseActivity mForegroundActivity = null;
    /**
     * 记录所有活动的Activity
     */
    private static final List<BaseActivity> mActivities = new LinkedList<BaseActivity>();
    private CompositeSubscription mCompositeSubscription;

    /**
     * 解决Subscription内存泄露问题
     *
     * @param s
     */
    protected void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(s);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        activity = this;
        if (isSetStatusBar) {
            steepStatusBar();
        }
        setContentView(getLayoutId());

        ButterKnife.bind(this);
        initView();
        initNewSave(savedInstanceState);
        initData();
        initEvent();
    }

    protected abstract int getLayoutId();


    public void inspectNet() {
        //NetWorkUtils.getNetWorkState(HXBaseActivity.this);
    }


    /**
     * 是否沉浸状态栏
     **/
    private boolean isSetStatusBar = false;

    /**
     * [是否设置沉浸状态栏]
     *
     * @param isSetStatusBar
     */
    public void setSteepStatusBar(boolean isSetStatusBar) {
        this.isSetStatusBar = isSetStatusBar;
    }

    /**
     * [沉浸状态栏]
     */
    private void steepStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }


    /**
     * 判断有无网络 。
     *
     * @return true 有网, false 没有网络.
     */
    public boolean isNetConnect() {
        if (netMobile == 1) {
            return true;
        } else if (netMobile == 0) {
            return true;
        } else if (netMobile == -1) {
            return false;
        }
        return false;
    }

    /**
     * 获取id
     *
     * @return
     */
    //abstract protected int getLayoutId();

    /**
     * 初始化view
     */
    abstract protected void initView();


    protected abstract void initNewSave(Bundle savedInstanceState);

    /**
     * 初始化数据
     */
    abstract protected void initData();

    /**
     * 初始化事件
     */
    protected void initEvent() {

    }

    @Override
    protected void onResume() {
        mForegroundActivity = this;
        super.onResume();
        //showPermission();
    }

    public void showPermission() {
        boolean mStoragePermissionCheck = false;
        boolean mCameraPermissionCheck = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                mCameraPermissionCheck = false;
                PermissionUtil.setSetting(this, PermissionUtil.KEY_PERMISSION_CAMERA, false);
            } else {
                mCameraPermissionCheck = true;
                PermissionUtil.setSetting(this, PermissionUtil.KEY_PERMISSION_CAMERA, true);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                mStoragePermissionCheck = false;
                PermissionUtil.setSetting(this, PermissionUtil.KEY_PERMISSION_EXTERNAL_STORAGE, false);
            } else {
                mStoragePermissionCheck = true;
                PermissionUtil.setSetting(this, PermissionUtil.KEY_PERMISSION_EXTERNAL_STORAGE, true);
            }
        }
        if (mCameraPermissionCheck && mStoragePermissionCheck) {
            dismissPermissionFinishDialog();
        }
    }

    /**
     * dismissPermissionFinishDialog
     */
    protected void dismissPermissionFinishDialog() {
        if (mPermissionFinishDialog != null) {
            if (mPermissionFinishDialog.isShowing()) {
                mPermissionFinishDialog.dismiss();
            }
        }
        if (mRequestSettingsDialog != null) {
            if (mRequestSettingsDialog.isShowing()) {
                mRequestSettingsDialog.dismiss();
            }
        }
    }

    protected void showRequestSettingsDialog() {

        String message = getResources().getString(R.string.message_start);

        if (mRequestSettingsDialog != null) {
            if (mRequestSettingsDialog.isShowing()) {
                return;
            }
        }
        // 拍摄照片和录制视频
        if (!PermissionUtil.getBooleanSetting(this, PermissionUtil.KEY_PERMISSION_CAMERA, false)) {
            message = message + getResources().getString(R.string.message_camera);
        }
        if (!PermissionUtil.getBooleanSetting(this, PermissionUtil.KEY_PERMISSION_EXTERNAL_STORAGE, false)) {
            message = message + getResources().getString(R.string.message_external_storage);
        }
        message = message + getResources().getString(R.string.message_end);
        mRequestSettingsDialog = new AlertDialog.Builder(new PermissionThemeWrapper(this))
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.message_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", getPackageName(), null));
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.message_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();
        mRequestSettingsDialog.show();
    }

    @Override
    protected void onPause() {
        mForegroundActivity = null;
        super.onPause();
//        Logger.e("Atctivy is Pause");
    }


    protected void initActionBar() {

    }

    public void startOpenActivity(Class<?> clz) {
        Intent intent = new Intent(this, clz);
        startActivity(intent);
    }
    /**
     * 关闭所有Activity
     */
    public static void finishAll() {
        List<BaseActivity> copy;
        synchronized (mActivities) {
            copy = new ArrayList<BaseActivity>(mActivities);
        }
        for (BaseActivity activity : copy) {
            activity.finish();
        }
    }

    /**
     * 关闭所有Activity，除了参数传递的Activity
     */
    public static void finishAll(BaseActivity except) {
        List<BaseActivity> copy;
        synchronized (mActivities) {
            copy = new ArrayList<BaseActivity>(mActivities);
        }
        for (BaseActivity activity : copy) {
            if (activity != except)
                activity.finish();
        }
    }

    /**
     * 隐藏软件盘
     */
    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 得到InputMethodManager的实例
        if (imm.isActive()) {
            // 如果开启
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                    InputMethodManager.HIDE_NOT_ALWAYS);
            // 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
        }
    }

    /**
     * 是否有启动的Activity
     */
    public static boolean hasActivity() {
        return mActivities.size() > 0;
    }

    /**
     * 获取当前处于前台的activity
     */
    public static BaseActivity getForegroundActivity() {
        return mForegroundActivity;
    }

    /**
     * 获取当前处于栈顶的activity，无论其是否处于前台
     */
    public static BaseActivity getCurrentActivity() {
        List<BaseActivity> copy;
        synchronized (mActivities) {
            copy = new ArrayList<BaseActivity>(mActivities);
        }
        if (copy.size() > 0) {
            return copy.get(copy.size() - 1);
        }
        return null;
    }

    protected void initFindViewById() {

    }


    /**
     * 退出应用
     */

    private long exitTime = 0;

    public void exitApp() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(this, R.string.click_again_finish_app, Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            ///退出应用
//            AppConnect.getInstance(this).close();
            finishAll();
            System.exit(0);
            Process.killProcess(Process.myPid());
        }
    }

    /**
     * 点击空白区域隐藏软键盘
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.mCompositeSubscription != null) {
            this.mCompositeSubscription.unsubscribe();
        }
    }
}
