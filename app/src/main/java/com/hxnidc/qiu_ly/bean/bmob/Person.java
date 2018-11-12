package com.hxnidc.qiu_ly.bean.bmob;

import cn.bmob.v3.BmobObject;

/**
 * Created by on 2017/7/28 10:49
 * Author：yrg
 * Describe:
 */


public class Person extends BmobObject {

    private String username;

    public String getPasswords() {
        return passwords;
    }

    public void setPasswords(String passwords) {
        this.passwords = passwords;
    }

    private String passwords;
    private String phone;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
