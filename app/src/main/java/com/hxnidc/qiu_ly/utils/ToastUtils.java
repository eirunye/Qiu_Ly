package com.hxnidc.qiu_ly.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by on 2017/6/14 10:47
 * Author：yrg
 * Describe:ToastUtils
 */


public class ToastUtils {

    private static Toast toast;

    public static void showToast(Context context,
                                 String content) {
        if (toast == null) {
            toast = Toast.makeText(context,
                    content,
                    Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

    public static void showCenterToast(Context context, String content) {

        if (toast == null) {
            toast = Toast.makeText(context,
                    content,
                    Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}
