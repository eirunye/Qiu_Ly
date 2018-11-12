package com.hxnidc.qiu_ly.bean;

/**
 * Created by on 2017/7/6 15:32
 * Author：yrg
 * Describe:
 */


public class AdNextBean {
    private String imgUrl;
    private String htmlUrl;

    public AdNextBean(String imgUrl, String htmlUrl) {
        this.imgUrl = imgUrl;
        this.htmlUrl = htmlUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }
}
