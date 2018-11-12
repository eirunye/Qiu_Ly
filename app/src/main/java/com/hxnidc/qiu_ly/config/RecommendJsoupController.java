package com.hxnidc.qiu_ly.config;

import android.content.Context;

import com.hxnidc.qiu_ly.bean.RecomBean;
import com.orhanobut.logger.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by on 2017/9/4 17:57
 * Author：yrg
 * Describe:
 */


public class RecommendJsoupController {

    private static RecommendJsoupController Ins;

    public static synchronized RecommendJsoupController Instance() {
        if (Ins == null) {
            synchronized (RecommendJsoupController.class) {
                if (Ins == null) {
                    Ins = new RecommendJsoupController();
                }
            }
        }
        return Ins;
    }

    public List<RecomBean> getRecommendListData(String url) {

        List<RecomBean> recomList = null;
        try {
            recomList = new ArrayList<>();
            Document document = Jsoup.parse(new URL(url), 5000);

            if (document == null) return null;
            Elements elements = document.getElementsByClass("imNews bgWhite chinaNews").first().getElementsByClass("listPad").first().getElementsByClass("listBoxT14").first().getElementsByTag("li");
            Logger.e("getRecommendListData:" + elements.size());

            for (int i = 0; i < elements.size(); i++) {

                if (i % 2 == 0) {
                    continue;
                }
                RecomBean recomBean = new RecomBean();
                String href = elements.get(i).getElementsByTag("a").first().attr("href");
                String title = elements.get(i).getElementsByTag("a").first().attr("title");
                String tagNum = elements.get(i).getElementsByTag("a").last().text();
                recomBean.setHref(href);
                recomBean.setTitle(title);
                recomBean.setTag(tagNum);
                recomList.add(recomBean);
                //Logger.e("href:" + href + "\n" + "title:" + title + "tagNum:" + tagNum);
            }
            Collections.shuffle(recomList);
            return recomList;

        } catch (Exception e) {
            return null;
        }
    }

    public String getRecommendElement(String url, Context context) {
        Document document = null;
        try {
            document = Jsoup.parse(new URL(url), 5000);
            if (document == null) return null;
            Element element = document.getElementsByClass("text").first();
            element.getElementsByTag("script").remove();
            //Logger.e(element.toString());
            return element.toString();

        } catch (Exception e) {
            return null;

        }
    }

    public List<RecomBean> getVideoRecommendList(String url, String httpHead) {
        Document document = null;
        try {
            List<RecomBean> recomBeanList = new ArrayList<>();
            document = Jsoup.parse(new URL(url), 5000);
            if (document == null) return null;

            Elements elements = document.getElementById("play-list1").getElementsByClass("cont").first().getElementsByClass("box");

            Logger.e("size:" + elements.size());

            for (int i = 0; i < elements.size(); i++) {
                if (i % 3 == 0) {
                    RecomBean recomBean = new RecomBean();
                    String href = elements.get(i).getElementsByTag("a").attr("href");
                    String imgUrl = "";
                    if (href.contains("ksp")) {
                        imgUrl = httpHead + elements.get(i).getElementsByTag("img").attr("src").substring(3);
                    } else {
                        imgUrl = httpHead + href.split("/")[4] + elements.get(i).getElementsByTag("img").attr("src").substring(1);
                    }
                    String title = elements.get(i).getElementsByTag("h4").text();
                    Logger.e("href:" + href + "\n" + "title:" + title + "\n" + "img:" + imgUrl);
                    recomBean.setHref(href);
                    recomBean.setTitle(title);
                    recomBean.setImgUrl(imgUrl);
                    recomBeanList.add(recomBean);
                }
            }
            return recomBeanList;

        } catch (Exception e) {
        }

        return null;
    }

    public List<RecomBean> getArtSportRecommendList(String url, String httpHead) {
        Document document = null;
        List<RecomBean> videoModelList = null;
        try {
            document = Jsoup.parse(new URL(url), 5000);
            if (document == null) return null;
            videoModelList = new ArrayList<>();
            Elements elements = document.getElementsByClass("content_right2").first().getElementsByTag("ul").first().getElementsByTag("li");
            Logger.e("size:" + elements.size());
            for (int i = 4; i < 13; i++) {
                if (i % 3 == 0) {
                    continue;
                }
                RecomBean videoModel = new RecomBean();
                String href = "http://www.joy.cn/sport/" + elements.get(i).getElementsByTag("a").first().attr("href");
                String imgUrl = elements.get(i).getElementsByTag("img").attr("src");
                String title = elements.get(i).getElementsByTag("img").attr("alt");
                videoModel.setImgUrl(imgUrl);
                videoModel.setHref(href);
                videoModel.setTitle(title);

                Logger.e("imgUrl:" + imgUrl + "\n" + "title:" + title + "\n" + "href:" + href);
                videoModelList.add(videoModel);
            }
            Collections.shuffle(videoModelList);
            return videoModelList;
        } catch (Exception e) {

        }
        return null;

    }

    public List<RecomBean> getHappyRecommendList(String url, String httpHead) {
        Document document = null;
        try {
            List<RecomBean> recomBeanList = new ArrayList<>();
            document = Jsoup.parse(new URL(url), 5000);
            if (document == null) return null;

            Elements elements = document.getElementById("play-list1").getElementsByClass("cont").first().getElementsByClass("box");

            Logger.e("size:" + elements.size());

            for (int i = 0; i < elements.size(); i++) {
                if (i % 3 == 0) {
                    RecomBean recomBean = new RecomBean();

//                    Logger.e("href:" + href + "\n" + "title:" + title + "\n" + "img:" + imgUrl);
//                    recomBean.setHref(href);
//                    recomBean.setTitle(title);
//                    recomBean.setImgUrl(imgUrl);
//                    recomBeanList.add(recomBean);
                }
            }
            return recomBeanList;

        } catch (Exception e) {
        }

        return null;
    }


}






























