package com.hxnidc.qiu_ly.bean.bmob;

import java.io.File;

import cn.bmob.v3.BmobUser;

/**
 * Created by on 2017/7/28 11:10
 * Author：yrg
 * Describe:
 */


public class UserPerson extends BmobUser {

    private String phone;
    private String age;
    private File imgFile;


    public File getImgFile() {
        return imgFile;
    }

    public void setImgFile(File imgFile) {
        this.imgFile = imgFile;
    }

    private String sex;
    private String birthday;
    private String address;
    private String signature;

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }


    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhone() {

        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
