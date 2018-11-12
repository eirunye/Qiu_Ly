package com.hxnidc.qiu_ly.config;

import com.hxnidc.qiu_ly.bean.model.ContentBean;
import com.hxnidc.qiu_ly.bean.model.FirstVideoModel;
import com.hxnidc.qiu_ly.bean.model.HappyVideoModel;
import com.hxnidc.qiu_ly.bean.model.VideoModel;
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
 * Created by on 2017/8/18 15:17
 * Author：yrg
 * Describe:
 */


public class VideoJsoupController {
    private static VideoJsoupController Ins;

    public static synchronized VideoJsoupController Instance() {
        if (Ins == null) {
            synchronized (VideoJsoupController.class) {
                if (Ins == null) {
                    Ins = new VideoJsoupController();
                }
            }
        }
        return Ins;
    }

    public List<FirstVideoModel> firstVideoData(String url, String httpHead) {
        List<FirstVideoModel> videoModelArrayList = null;
        try {
            videoModelArrayList = new ArrayList<>();
            Document doc = Jsoup.parse(new URL(url), 5000);
            if (doc == null) return null;
            //Logger.e(doc.toString());
            Element element = doc.getElementById("pic-list");
            Element ul = element.getElementsByTag("ul").first();
            Elements elements = ul.getElementsByTag("li");
            //Logger.e("====>" + elements.size());
            for (int i = 0; i < elements.size(); i++) {
                if (Math.random() > 0.6) {
                    continue;
                }
                FirstVideoModel firstVideoModel = new FirstVideoModel();
                String href = httpHead + elements.get(i).getElementsByTag("a").first().attr("href").substring(1) + "";
                String imgUrl = httpHead + elements.get(i).getElementsByTag("a").first().select("img").attr("src").substring(1) + "";
                String title = elements.get(i).getElementsByTag("h3").first().getElementsByTag("a").text() + "";
                String time = elements.get(i).getElementsByTag("p").first().getElementsByTag("span").text() + "";
                firstVideoModel.setHref(href);
                firstVideoModel.setImgUrl(imgUrl);
                firstVideoModel.setTime(time);
                firstVideoModel.setTitle(title);
                firstVideoModel.setAd(false);

//                Document document = Jsoup.parse(href);
//                if (document != null && i == 1) {
//                    Logger.e("document:" + document.toString());
//                    Logger.e("href:" + href);
//                    Element wrapper = document.getElementById("wrapper");
//                    Element video1 = wrapper.getElementById("video1");
//                    Logger.e(video1.toString());
//                }


//                Element video1Element = document.getElementById("video1");
//                String txt = video1Element.getElementById("videocontent").getElementsByTag("script").toString() + "";
//                String videourl = video1Element.getElementById("al").getElementsByTag("object").first().select("embed").attr("flashvars") + "";
//+ "txt:" + txt + "\n" + "videourl:" + videourl
                videoModelArrayList.add(firstVideoModel);
//                Logger.e("imgUrl:" + imgUrl + "\n" + "title:" + title + "\n" + "href:" + href + "\n" + "time:" + time + "\n");
            }

            return videoModelArrayList;

        } catch (Exception e) {
            Logger.e("==============" + e.getMessage());
            return null;
        }
    }


    public String getListVideoData(String url) {

        try {

            Document document = Jsoup.parse(new URL(url), 5000);

            if (document == null) return null;
            Element element = document.getElementById("wrapper").getElementById("video1").getElementById("show1").getElementsByTag("script").first();//.getElementsByClass("video1").first();
            String[] split = element.toString().split(";");

            String pm4Url = split[1].split("pv")[1].split(",")[0].substring(2, split[1].split("pv")[1].split(",")[0].length() - 1);

            return pm4Url;
        } catch (Exception e) {
        }

        return null;

    }

    //<iframe id="iframe1" name="iframe1" width="100%" height="370" frameborder="0" scrolling="no" src="http://vod.xinhuanet.com/v/vod.html?vid=447399"></iframe>
    //http://download.3g.joy.cn/video/226/60226292/1406187813309_hd.mp4
    //http://download.3g.joy.cn/video/242/60242171/1503621201076_hd.mp4
    //http://www.ku6.com/2017/detail.html?vid=EYP4wZKwXvUiiUY2c_VtKQ
    public List<HappyVideoModel> getHappyVideoList(String url) {
        Document document = null;
        try {
            List<HappyVideoModel> happyVideoModelList = new ArrayList<>();
            document = Jsoup.parse(new URL(url), 5000);
            if (document == null) return null;
            Elements elements = document.getElementsByClass("content_right2").first().getElementsByTag("ul").first().getElementsByTag("li");
            //Logger.e("kkkkkkkk" + elements.toString());
            Logger.e("size:" + elements.size());
            for (int i = 0; i < elements.size(); i++) {
                if (i % 3 == 0) {
                    continue;
                }
                HappyVideoModel happyVideoModel = new HappyVideoModel();
                String href = "http://www.joy.cn/entertainment/" + elements.get(i).getElementsByTag("a").first().attr("href");
                String imgUrl = elements.get(i).getElementsByTag("img").attr("src");
                String title = elements.get(i).getElementsByTag("img").attr("alt");
                happyVideoModel.setImgUrl(imgUrl);
                happyVideoModel.setHref(href);
                happyVideoModel.setTitle(title);
                happyVideoModel.setAd(false);
                //Logger.e("imgUrl:" + imgUrl + "\n" + "title:" + title + "\n" + "href:" + href);
                happyVideoModelList.add(happyVideoModel);
            }
            Collections.shuffle(happyVideoModelList);
            return happyVideoModelList;

        } catch (Exception e) {

        }

        return null;
    }

    public String getVideHappyPlayer(String url) {
        Document document = null;
        try {
            document = Jsoup.parse(new URL(url), 5000);
            if (document == null) return null;
            String src = document.getElementsByTag("video").first().getElementsByTag("source").attr("src");
            return src;
        } catch (Exception e) {
        }
        return null;
    }

    public List<VideoModel> getVideoModelList(String url) {
        Document document = null;
        List<VideoModel> videoModelList = null;
        try {
            document = Jsoup.parse(new URL(url), 5000);
            if (document == null) return null;
            videoModelList = new ArrayList<>();
            Elements elements = document.getElementsByClass("content_right2").first().getElementsByTag("ul").first().getElementsByTag("li");
            Logger.e("size:" + elements.size());
            for (int i = 0; i < elements.size(); i++) {
                if (i % 3 == 0) {
                    continue;
                }
                VideoModel videoModel = new VideoModel();
                String href = "http://www.joy.cn/sport/" + elements.get(i).getElementsByTag("a").first().attr("href");
                String imgUrl = elements.get(i).getElementsByTag("img").attr("src");
                String title = elements.get(i).getElementsByTag("img").attr("alt");
                videoModel.setImgUrl(imgUrl);
                videoModel.setHref(href);
                videoModel.setTitle(title);
                videoModel.setAd(false);
                Logger.e("imgUrl:" + imgUrl + "\n" + "title:" + title + "\n" + "href:" + href);
                videoModelList.add(videoModel);
            }
            Collections.shuffle(videoModelList);
            return videoModelList;
        } catch (Exception e) {

        }
        return null;
    }


    public String getArtRecommendVideoData(String url) {

        try {

            Document document = Jsoup.parse(new URL(url), 5000);

            if (document == null) return null;

            String pm4Url = document.getElementById("text-content").getElementsByTag("script").first().toString();
            Logger.e("====>" + pm4Url);

            return null;
        } catch (Exception e) {
        }

        return null;

    }


    public List<ContentBean> getAttentionVideoList(String url) {
        Document document = null;
        try {
            List<ContentBean> contentBeanList = new ArrayList<>();
            document = Jsoup.parse(new URL(url), 5000);
            if (document == null) return null;
            Elements elements = document.getElementsByClass("content_right2").first().getElementsByTag("ul").first().getElementsByTag("li");
            //Logger.e("kkkkkkkk" + elements.toString());
            Logger.e("size:" + elements.size());
            for (int i = 0; i < elements.size(); i++) {
                ContentBean contentBean = new ContentBean();
                String href = "http://www.joy.cn/entertainment/" + elements.get(i).getElementsByTag("a").first().attr("href");
                String imgUrl = elements.get(i).getElementsByTag("img").attr("src");
                String title = elements.get(i).getElementsByTag("img").attr("alt");
                contentBean.setImgUrl(imgUrl);
                contentBean.setUrl(href);
                contentBean.setTitle(title);
                contentBean.setAd(false);
                //Logger.e("imgUrl:" + imgUrl + "\n" + "title:" + title + "\n" + "href:" + href);
                contentBeanList.add(contentBean);
            }
            Collections.shuffle(contentBeanList);
            return contentBeanList;

        } catch (Exception e) {

        }

        return null;
    }
}
