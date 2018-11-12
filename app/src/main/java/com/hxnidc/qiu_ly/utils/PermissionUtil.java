package com.hxnidc.qiu_ly.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * PermissionUtil
 * Created by TnnoWu on 2017/01/18.
 */
public class PermissionUtil {

    public static final String KEY_PERMISSION_CONTACTS = "key_permission_contacts";
    public static final String KEY_PERMISSION_PHONE = "key_permission_phone";
    public static final String KEY_PERMISSION_CALENDAR = "key_permission_calendar";
    public static final String KEY_PERMISSION_CAMERA = "key_permission_camera";
    public static final String KEY_PERMISSION_SENSORS = "key_permission_sensors";
    public static final String KEY_PERMISSION_LOCATION = "key_permission_location";
    public static final String KEY_PERMISSION_EXTERNAL_STORAGE = "key_permission_external_storage";
    public static final String KEY_PERMISSION_MICROPHONE = "key_permission_microphone";
    public static final String KEY_PERMISSION_SMS = "key_permission_sms";

    public static boolean getBooleanSetting(Context context, String key, boolean defaultValue) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sp.getBoolean(key, defaultValue);
    }

    public static void setSetting(Context context, String key, boolean value) {

        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

}
