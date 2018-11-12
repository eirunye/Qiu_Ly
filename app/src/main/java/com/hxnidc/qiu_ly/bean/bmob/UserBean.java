package com.hxnidc.qiu_ly.bean.bmob;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by on 2017/7/26 16:23
 * Author：yrg
 * Describe:
 */


public class UserBean extends BmobUser {

    // 父类中已经存在的属性
    // private String id;
    // private String username;
    // private String password;
    // private String email;
    // private String regTime;


    private static UserBean Ins;

    public static synchronized UserBean Instance() {
        if (Ins == null) {
            synchronized (UserBean.class) {
                if (Ins == null) {
                    Ins = new UserBean();
                }
            }
        }
        return Ins;
    }


    private String area;
    private String state;
    private String sex;
    private String phone;
    private String anthCode;
    private BmobFile image;
    private String password;

    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    public BmobFile getImage() {
        return image;
    }

    public void setImage(BmobFile image) {
        this.image = image;
    }

    public String getAnthCode() {
        return anthCode;
    }

    public void setAnthCode(String anthCode) {
        this.anthCode = anthCode;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


}
