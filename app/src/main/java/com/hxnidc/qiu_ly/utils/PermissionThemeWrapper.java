package com.hxnidc.qiu_ly.utils;

import android.content.Context;
import android.view.ContextThemeWrapper;

import com.hxnidc.qiu_ly.R;


/**
 * PermissionThemeWrapper
 * Created by TnnoWu on 2017/01/18.
 */
public class PermissionThemeWrapper extends ContextThemeWrapper {

    public PermissionThemeWrapper() {
        super();
    }

    public PermissionThemeWrapper(Context base) {
        this(base, layoutInt());
    }

    public PermissionThemeWrapper(Context base, int themeRes) {
        super(base, themeRes);
    }

    private static int layoutInt() {
        return R.style.DefaultPopTheme;
    }

}
