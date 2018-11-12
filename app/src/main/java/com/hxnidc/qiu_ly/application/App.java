package com.hxnidc.qiu_ly.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.DisplayMetrics;

import com.hxnidc.qiu_ly.config.Configurations;
import com.hxnidc.qiu_ly.utils.ConfigUtil;
import com.hxnidc.qiu_ly.utils.UIUtils;
import com.hxnidc.qiu_ly.utils.Utils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import cn.bmob.v3.Bmob;
import okhttp3.OkHttpClient;


public class App extends Application {
    public static List<?> images = new ArrayList<>();
    public static List<String> titles = new ArrayList<>();
    public static int H, W;
    public static App app;
    private boolean isNight;
    private static String THEME_KEY = "theme_mode";

    public static App me() {
        if (app == null) {
            app = new App();
        }
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, "26392b353ada98140b51dcd3efa2010d");
        app = this;
        initThemeMode();
        getScreen(this);
        UIUtils.init(this);
        Utils.init(this);
        //HxUtils.init(this);
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
//        CookieJarImpl cookieJar1 = new CookieJarImpl(new MemoryCookieStore());
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .addInterceptor(new LoggerInterceptor("TAG"))
                //.cookieJar(cookieJar1)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .build();
        OkHttpUtils.initClient(okHttpClient);
        Logger.addLogAdapter(isDebuggable() ? new AndroidLogAdapter() : new DiskLogAdapter());
        Configurations.Instance().init();
//        Colorful.defaults()
//                .primaryColor(Colorful.ThemeColor.RED)
//                .accentColor(Colorful.ThemeColor.BLUE)
//                .translucent(false)
//                .dark(false);

        //Colorful.init(this);
    }

    private void initThemeMode() {
        isNight = ConfigUtil.getBoolean(THEME_KEY, false);
        if (isNight) {
            //夜间模式
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            //白天模式
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public void setTheme(AppCompatActivity activity, boolean mode) {
        if (isNight == mode) {
            return;
        }
        if (!mode) {
            //白天模式
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            activity.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            //白天模式
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            activity.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        isNight = mode;
        ConfigUtil.putBoolean(THEME_KEY, isNight);
        activity.recreate();
    }

    /**
     * 刷新UI_MODE模式
     */
    public void refreshResources(Activity activity) {
        isNight = ConfigUtil.getBoolean(THEME_KEY, false);
        if (isNight) {
            updateConfig(activity, Configuration.UI_MODE_NIGHT_YES);
        } else {
            updateConfig(activity, Configuration.UI_MODE_NIGHT_NO);
        }
    }

    /**
     * google官方bug，暂时解决方案
     * 手机切屏后重新设置UI_MODE模式（因为在dayNight主题下，切换横屏后UI_MODE会出错，会导致资源获取出错，需要重新设置回来）
     */
    private void updateConfig(Activity activity, int uiNightMode) {
        Configuration newConfig = new Configuration(activity.getResources().getConfiguration());
        newConfig.uiMode &= ~Configuration.UI_MODE_NIGHT_MASK;
        newConfig.uiMode |= uiNightMode;
        activity.getResources().updateConfiguration(newConfig, null);
    }

    public void getScreen(Context aty) {
        DisplayMetrics dm = aty.getResources().getDisplayMetrics();
        H = dm.heightPixels;
        W = dm.widthPixels;
    }

    private boolean isDebuggable() {
        boolean debuggable = false;
        PackageManager pm = this.getPackageManager();
        try {
            ApplicationInfo appinfo = pm.getApplicationInfo(this.getPackageName(), 0);
            debuggable = (0 != (appinfo.flags & ApplicationInfo.FLAG_DEBUGGABLE));
        } catch (PackageManager.NameNotFoundException e) {
        /* debuggable variable will remain false */
        }

        return debuggable;
    }
}
