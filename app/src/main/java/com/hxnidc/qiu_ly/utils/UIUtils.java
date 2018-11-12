package com.hxnidc.qiu_ly.utils;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.orhanobut.logger.Logger;

import java.io.File;


/**
 * Created by Hider on 16/2/7.
 */

/**
 * @项目名 lanxingman_app
 * @包名 lxm.xingtu.com.lanxingman_app.util
 * @类名 MD5Utils
 * @时间 16/2/7
 * @作者 XC
 * @描述 TODO:用来提供全局上下文，全局handler，资源文件
 */
public class UIUtils {

    /**
     * 全局的上下文
     */
    private static Context mBaseContext;
    private static Handler mHandler;

    /**
     * 初始化工具
     *
     * @param application
     */
    public static void init(Application application) {
        mBaseContext = application;

        //在主线程中new
        mHandler = new Handler();
    }

    public static Context getContext() {
        return mBaseContext;
    }

    public static void post(Runnable task) {
        try {
            mHandler.post(task);
        } catch (Exception ex) {
            Logger.e("Exception Occurs " + ex.getMessage());
        }
    }

    public static void postDelayed(Runnable task, long delayed) {
        mHandler.postDelayed(task, delayed);
    }

    public static void removeCallbacks(Runnable task) {
        mHandler.removeCallbacks(task);
    }

    public static Resources getResources() {
        return mBaseContext.getResources();
    }

    public static String[] getStringArray(int resId) {
        return getResources().getStringArray(resId);
    }

    public static String getPackageName() {
        return getContext().getPackageName();
    }

    public static File getCacheDir() {
        return getContext().getCacheDir();
    }

    /**
     * dp --> px
     *
     * @param dp
     * @return
     */
    public static float dp2px(float dp) {
        float dimension = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics());
        return dimension;
    }

    public static float px2dp(float px) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,
                px,
                getResources().getDisplayMetrics());
    }

    public static String getString(int id, Object... formatArgs) {
        return getResources().getString(id, formatArgs);
    }

    public static Drawable getDrawable(int resId) {

        return getResources().getDrawable(resId);
    }

    public static int getVisibe(int value) {
        try {
            if (value == 0) return View.INVISIBLE;
            else return View.VISIBLE;
        } catch (Exception ex) {
            return View.VISIBLE;
        }

    }

    public static void toggleKeyboard(Context context) {
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        im.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

    }

    public static void hideKeyBorad(Context context, View view) {
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKeyBoard(Context context, View view) {
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        view.requestFocus();
        im.showSoftInput(view, im.SHOW_FORCED);
    }

    public static String getVersionName() {
        String versionName = null;
        try {
            versionName = getContext().getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static int getVersionCode() {
        int versionCode = 0;
        try {
            versionCode = getContext().getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取渠道
     *
     * @return
     */
    public static String getChannel() {
        String msg = "";
        try {
            ApplicationInfo appInfo = UIUtils.getContext().getPackageManager().getApplicationInfo(UIUtils.getPackageName(), PackageManager.GET_META_DATA);
            msg = appInfo.metaData.getString("UMENG_CHANNEL");
        } catch (Exception e) {
            e.printStackTrace();
            msg = "360";
        }
        return msg;
    }

    public static String getNimAppKey() {
        String appKey = "";
        try {
            ApplicationInfo appInfo = UIUtils.getContext().getPackageManager().getApplicationInfo(UIUtils.getPackageName(), PackageManager.GET_META_DATA);
            appKey = appInfo.metaData.getString("com.netease.nim.appKey");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appKey;
    }

    /**
     * 检查登录
     * @return false为未登录, true为登录
     */
//    public static boolean checkLogin(){
//        if(PreferenceUtils.getString(UIUtils.getContext(), "gid")==null||PreferenceUtils.getString(UIUtils.getContext(),"gid").equals("6")){
//            Intent intent = new Intent(UIUtils.getContext(),LoginActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.putExtra("isEnter", true);
//            UIUtils.getContext().startActivity(intent);
//            return false;
//        }
//        return true;
//    }
}
