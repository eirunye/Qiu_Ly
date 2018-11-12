package com.hxnidc.qiu_ly.config;

import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hxnidc.qiu_ly.bean.bmob.UserBean;
import com.hxnidc.qiu_ly.utils.ImageUtils;
import com.hxnidc.qiu_ly.utils.SPUtils;

/**
 * Created by on 2017/7/1 14:38
 * Author：yrg
 * Describe:Configurations
 */


public class Configurations {
    private static Configurations Ins;

    public static synchronized Configurations Instance() {
        if (Ins == null) {
            synchronized (Configurations.class) {
                if (Ins == null) {
                    Ins = new Configurations();
                }
            }
        }
        return Ins;
    }

    private SPUtils spUtils;

    private Gson gson = new Gson();

    public void init() {
        spUtils = new SPUtils("Qiu_Ly");
        this.isFirstInto = spUtils.getBoolean("isFirstInto");

    }

    private boolean isFirstInto;

    public boolean isFirstInto() {
        return isFirstInto;
    }

    public void setFirstInto(boolean firstInto) {
        this.isFirstInto = firstInto;
        spUtils.putBoolean("isFirstInto", firstInto);
    }


    public void setGsonObject(UserBean userBean) {

        try {
            if (userBean == null) return;
            String strJson = gson.toJson(userBean);
            spUtils.putString("user", strJson);
        } catch (Exception e) {

        }
    }

    public void setRegUser(String key) {

    }

    private boolean isLogin = false;

    public boolean isLogin() {
        boolean loginTag = spUtils.getBoolean("isLogin");
        isLogin = loginTag;
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
        //String userPhone = UserPerson.getCurrentUser(UserPerson.class).getPhone();
        spUtils.putBoolean("isLogin", isLogin);
    }

    private String password;
    private String username;

    public String getPassword() {
        this.password = spUtils.getString("password");
        return this.password;
    }

    public void setPassword(String password) {
        spUtils.putString("password", password);
        this.password = password;
    }

    public String getUsername() {
        this.username = spUtils.getString("username");
        return this.username;
    }

    public void setUsername(String username) {
        spUtils.putString("username", username);
        this.username = username;
    }

    private Bitmap bitmap;

    public Bitmap getBitmap() {
        Bitmap bitmap1 = ImageUtils.convertStringToIcon(spUtils.getString("imgFile"));
        return bitmap1;
    }


    public void setBitmap(Bitmap bitmap) {
        spUtils.putString("imgFile", ImageUtils.convertIconToString(bitmap));
        this.bitmap = bitmap;
    }

    private UserBean userBean;

    public UserBean getUserBean() {
        String strJson = spUtils.getString("user");
        this.userBean = gson.fromJson(strJson, new TypeToken<UserBean>() {
        }.getType());
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }
}
