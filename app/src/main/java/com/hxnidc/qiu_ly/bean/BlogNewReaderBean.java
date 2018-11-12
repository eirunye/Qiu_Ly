package com.hxnidc.qiu_ly.bean;

/**
 * Created by on 2017/7/12 16:45
 * Author：yrg
 * Describe:
 */


public class BlogNewReaderBean {

    private String title;
    private String time;
    private String href;
    private String imgUrl;
    private String comment;
    private boolean isFrov;

    public boolean isFrov() {
        return isFrov;
    }

    public void setFrov(boolean frov) {
        isFrov = frov;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
