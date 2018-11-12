package com.hxnidc.qiu_ly.bean;

import java.util.List;

/**
 * Created by on 2017/7/6 17:27
 * Author：yrg
 * Describe:
 */


public class NewTwoBean {

    private List<NBean> imgList;

    public static class NBean {
        private String imgUrl;
        private String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }
    }


    public NewTwoBean() {
    }

    public NewTwoBean(List<NBean> imgList) {
        this.imgList = imgList;

    }

    public List<NBean> getImgList() {
        return imgList;
    }

    public void setImgList(List<NBean> imgList) {
        this.imgList = imgList;
    }


}
