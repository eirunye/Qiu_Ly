package com.hxnidc.qiu_ly.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.hxnidc.qiu_ly.R;
import com.hxnidc.qiu_ly.bean.BlogNewReaderBean;
import com.hxnidc.qiu_ly.bean.Gif;
import com.hxnidc.qiu_ly.bean.HomeMultBean;
import com.hxnidc.qiu_ly.bean.HotPageBean;
import com.hxnidc.qiu_ly.bean.HotspotBannerModel;
import com.hxnidc.qiu_ly.bean.Jokebean;
import com.hxnidc.qiu_ly.bean.RecomBean;
import com.orhanobut.logger.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by jiangzehui on 16/11/9.
 */
public class JsoupUtil {


    public static ArrayList<String> list_dongtai = new ArrayList<>();
    public static ArrayList<String> list_xiegif = new ArrayList<>();
    public static ArrayList<String> list_gaoxiao = new ArrayList<>();


    public static int getListSize(int type) {
        switch (type) {
            case 0:
                return list_dongtai.size();

            case 1:
                return list_xiegif.size();
            case 2:
                return list_gaoxiao.size();

            default:
                return 0;

        }
    }

    public static String getListItem(int position, int type) {
        switch (type) {
            case 0:
                return list_dongtai.get(position);

            case 1:
                return list_xiegif.get(position);
            case 2:
                return list_gaoxiao.get(position);
            default:
                return "";

        }
    }

    /**
     * 抓取网页动态图
     *
     * @param urls
     * @param type
     * @return
     */
    public static ArrayList<Gif> getGif(String urls, int type) {


        ArrayList<Gif> list = new ArrayList<>();
        Document doc = null;
        try {
            doc = Jsoup.parse(new URL(urls), 5000);
            Elements es_item = doc.getElementsByClass("item");
            for (int i = 0; i < es_item.size(); i++) {
                Element et = es_item.get(i).getElementsByTag("h3").first();
                if (et != null) {
                    String title = et.getElementsByTag("b").text();
                    String url = es_item.get(i).select("img").first().attr("src");
                    Log.i("jsoup", title + "\t\t" + url + "\n");
                    list.add(new Gif(title, url));

                }

            }
            Elements es_page = doc.getElementsByClass("page").first().getElementsByTag("select").first().getElementsByTag("option");
            for (int i = 0; i < es_page.size(); i++) {
                Element et = es_page.get(i);
                if (et != null) {
                    switch (type) {
                        case 0:
                            list_dongtai.add(et.attr("value"));
                            break;
                        case 1:
                            list_xiegif.add(et.attr("value"));
                            break;
                        case 2:
                            list_gaoxiao.add(et.attr("value"));
                            break;
                    }


                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List<HomeMultBean> getHomeListData(String url) {

        Document document = null;
        List<HomeMultBean> homeMultBeanList = new ArrayList<>();
        try {
            document = Jsoup.parse(new URL(url), 5000);
            if (document == null) return homeMultBeanList;
            Elements elements = document.getElementsByClass("pic-list cf").first().getElementsByTag("li");
            Logger.e(elements.size() + "");
            for (int i = 0; i < elements.size(); i++) {
                HomeMultBean homeMultBean = new HomeMultBean();
                String imgUrl = elements.get(i).getElementsByTag("a").first().select("img").attr("src") + "";
                String title = elements.get(i).getElementsByTag("a").first().select("img").attr("alt") + "";
                String href = elements.get(i).getElementsByTag("a").first().select("img").attr("href") + "";
                Logger.e(imgUrl + "\n" + title + "\n" + href);
                if (Math.random() > 0.8 && i % 2 == 0)
                    homeMultBean.setAd(true);
                else homeMultBean.setAd(false);
                homeMultBean.setHref(href);
                homeMultBean.setImgUrl(imgUrl);
                homeMultBean.setTitle(title);
                homeMultBeanList.add(homeMultBean);
            }
            Collections.shuffle(homeMultBeanList);
            return homeMultBeanList;

        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
        return homeMultBeanList;
    }


    public static List<HomeMultBean> getHomeNewData(String url) {

        Document document = null;
        List<HomeMultBean> homeMultBeanList = new ArrayList<>();
        try {
            document = Jsoup.parse(new URL(url), 5000);
            Elements elements = document.getElementsByClass("masonry").first().getElementsByTag("li");
            Logger.e("size:" + elements.size());
            for (int i = 0; i < elements.size(); i++) {
                HomeMultBean homeMultBean = new HomeMultBean();
                String href = elements.get(i).getElementsByClass("subject").first().select("a").attr("href") + "";
                String title = elements.get(i).getElementsByClass("subject").first().select("a").attr("title") + "";
                String imgUrl = elements.get(i).getElementsByClass("pic").first().getElementsByTag("a").first().select("img").attr("src") + "";
                String time = elements.get(i).getElementsByClass("info").first().getElementsByTag("span").first().text() + "";
                String intro = elements.get(i).getElementsByClass("summary").first().getElementsByTag("span").first().text() + "";
                if (Math.random() > 0.8 && i % 2 == 0)
                    homeMultBean.setAd(true);
                else homeMultBean.setAd(false);
                homeMultBean.setHref(href);
                homeMultBean.setImgUrl(imgUrl);
                homeMultBean.setTitle(title);
                homeMultBeanList.add(homeMultBean);
                //Logger.e(href + "\n" + title + "\n" + time + "\n" + imgUrl + "\n" + intro);
            }
            Collections.shuffle(homeMultBeanList);
            return homeMultBeanList;

        } catch (Exception e) {
            //Logger.e(e.getMessage() + "");
            return homeMultBeanList;
        }
    }

    public static List<HomeMultBean> showHomeNewData(String url) {
        Document document = null;
        List<HomeMultBean> homeMultBeanList = new ArrayList<>();
        try {
            document = Jsoup.parse(new URL(url), 5000);
            if (document == null) return null;
            Elements elements = document.getElementsByClass("dy-list").first().getElementsByClass("news-item");
            Logger.e("showHomeNewData=====size=>" + elements.size());
            for (int i = 0; i < elements.size(); i++) {
                HomeMultBean homeMultBean = new HomeMultBean();
                Element element = elements.get(i).getElementsByClass("news-item-c").first();
                Element element1 = element.getElementsByClass("img-area").first().getElementsByTag("a").first();
                String href = element1.attr("href") + "";
                String imgUrl = element1.select("img").attr("src") + "";
                String title = element1.select("img").attr("alt") + "";
                String contexts = element.getElementsByClass("txt-area").first().getElementsByClass("news-abst").first().text() + "";
                String time = elements.get(i).getElementsByClass("news-item-c").first().getElementsByClass("txt-area").first().getElementsByClass("news-time").last().text() + "";
                if (Math.random() > 0.8 && i % 2 == 0)
                    homeMultBean.setAd(true);
                else homeMultBean.setAd(false);
                homeMultBean.setHref(href);
                homeMultBean.setImgUrl(imgUrl);
                homeMultBean.setTitle(title);
                homeMultBeanList.add(homeMultBean);
                Logger.e(href + "\n" + title + "\n" + time + "\n" + imgUrl + "\n");
            }
            Collections.shuffle(homeMultBeanList);
            return homeMultBeanList;

        } catch (Exception e) {

        }
        return null;
    }


    public static List<HotPageBean> getNewData(String url) {
//<img src="http://pic.yangtse.com/d/file/photo/citypic/2017/07/08/09e93ffb9ac44d302c96a40977968343.jpg" width="95">
        List<HotPageBean> hotPageBeanList = new ArrayList<>();
        Document doc = null;
        try {
            doc = Jsoup.parse(new URL(url), 5000);
            //if (doc == null) return hotPageBeanList;
            Element ele = doc.getElementsByClass("nodelist").first();
            Elements elements = ele.getElementsByTag("li");
            //Logger.e("Size:" + elements.size());
            if (elements.size() <= 0 || elements == null) return hotPageBeanList;
            for (int i = 0; i < elements.size(); i++) {
                String title = elements.get(i).getElementsByTag("h2").first().text();
                String intro = elements.get(i).getElementsByClass("des").first().text();
                String href = elements.get(i).getElementsByTag("a").last().attr("href");
                String imgUrl = elements.get(i).getElementsByTag("a").last().select("img").attr("src");
                String source = elements.get(i).getElementsByClass("info1").first().text();
                String time = elements.get(i).getElementsByClass("info2").first().text();
                //Logger.e(title + "\n" + intro + "\n" + href + "\n" + source + "\n" + time + "\n" + imgUrl);
                HotPageBean hotPageBean = new HotPageBean();
                hotPageBean.setTitle(title);
                hotPageBean.setImgurl("http://pic.yangtse.com" + imgUrl);
                hotPageBean.setIntro(intro);
                hotPageBean.setHref(href);
                hotPageBean.setTime(time);
                hotPageBean.setSource(source);
                hotPageBeanList.add(hotPageBean);
            }
            return hotPageBeanList;
        } catch (Exception ex) {

            return hotPageBeanList;
        }
    }


    public static List<RecomBean> getRecomData(String url, int page) {
        Document doc = null;
        List<RecomBean> recomBeanList = new ArrayList<>();
        try {
            doc = Jsoup.parse(new URL(url), 5000);
            if (doc == null) return recomBeanList;
            Element element = doc.getElementsByClass("newslist3").first();
            Elements elements = element.getElementsByTag("li");
            //Logger.e(elements.size() + "");
            for (int i = 0; i < elements.size(); i++) {
                RecomBean recomBean = new RecomBean();
                String href = elements.get(i).getElementsByTag("a").first().attr("href") + "";
                //String title = elements.get(i).select("a").first().text();
                String time = elements.get(i).getElementsByTag("span").first().getElementsByTag("b").text() + "";
                String tag = elements.get(i).getElementsByTag("span").first().text();
                String imgUrl = elements.get(i).getElementsByTag("i").first().select("img").attr("src") + "";
                String title = elements.get(i).getElementsByTag("i").first().select("img").attr("alt") + "";
                recomBean.setTitle(title);
                recomBean.setTime(time);
                recomBean.setImgUrl(imgUrl);
                recomBean.setTag(tag);
                recomBean.setHref("http:" + href);
                recomBeanList.add(recomBean);
                //Collections.shuffle(recomBeanList);
                //Logger.e(href + "\n" + title + "\n" + time + "\n" + tag + "\n" + imgUrl);
            }
            return recomBeanList;
        } catch (Exception e) {

        }
        return recomBeanList;
    }


    public static List<HotPageBean> getListData(String url) {

        Document document = null;
        List<HotPageBean> hotPageBeanList = new ArrayList<>();

        try {
            document = Jsoup.parse(new URL(url), 5000);
            if (document == null) return hotPageBeanList;
            Element element = document.getElementsByClass("three-coloum fix").first();
            Element middle = element.getElementsByClass("middle").first();
            Element imgList = middle.getElementsByClass("img-List").first();
            Elements elements = imgList.getElementsByTag("li");
            Logger.e(elements.size() + "");
            for (int i = 0; i < elements.size(); i++) {
                HotPageBean hotPageBean = new HotPageBean();
                String imgUrl = elements.get(i).getElementsByClass("fastRead-img").first().select("img").attr("src");
                String href = elements.get(i).getElementsByClass("module-title").first().select("a").attr("href");
                String title = elements.get(i).getElementsByClass("fastRead-img").first().select("img").attr("alt");
                String checknum = elements.get(i).getElementsByClass("module-interact").first().select("a").first().text();
                String time = elements.get(i).getElementsByClass("module-interact").first().select("a").last().text();
                String comment = elements.get(i).getElementsByClass("module-interact").first().getElementsByClass("interact-comment").text();
                Logger.e(imgUrl + "\n" + title + "\n" + "href:" + href + "\n" + checknum + "\n" + comment);
                hotPageBean.setHref("http://www.guancha.cn" + href);
                hotPageBean.setTitle(title);
                hotPageBean.setImgurl(imgUrl);
                hotPageBean.setTime(time);
                hotPageBean.setChecknum(checknum);
                hotPageBean.setIntro(comment);
                hotPageBeanList.add(hotPageBean);
            }
            Collections.shuffle(hotPageBeanList);
            return hotPageBeanList;
        } catch (Exception e) {
            Logger.e(e.getMessage() + "");
        }
        return hotPageBeanList;
    }


    public static List<BlogNewReaderBean> getBlogData(String url) {
//http://www.yxj18.com/xinwen/shehui/index_2.html//http://www.yxj18.com/xinwen/difang/index_2.html
        Document document = null;
        List<BlogNewReaderBean> blogNewReaderBeanList = new ArrayList<>();
        try {
            document = Jsoup.parse(new URL(url), 5000);
            if (document == null) return blogNewReaderBeanList;
            Element element = document.getElementsByClass("list-conl-box").first();
            Elements elements = element.getElementsByTag("li");
            //Logger.e("elements:" + elements.size());
            for (int i = 0; i < elements.size(); i++) {
                BlogNewReaderBean blogNewReaderBean = new BlogNewReaderBean();
                String href = elements.get(i).getElementsByTag("dt").first().select("a").attr("href") + "";
                String imgUrl = elements.get(i).getElementsByTag("a").first().select("img").attr("src") + "";
                String title = elements.get(i).getElementsByTag("a").first().select("img").attr("alt") + "";
                String time = elements.get(i).getElementsByTag("span").first().text().split(" ")[0] + "";
                if (Math.random() > 0.8 && i % 2 == 0)
                    blogNewReaderBean.setFrov(false);
                else
                    blogNewReaderBean.setFrov(true);
                blogNewReaderBean.setTitle(title);
                if (TextUtils.isEmpty(imgUrl))
                    blogNewReaderBean.setImgUrl("");
                else blogNewReaderBean.setImgUrl(imgUrl);
                blogNewReaderBean.setHref("http://www.yxj18.com" + href);
                blogNewReaderBean.setTime(time);
                blogNewReaderBeanList.add(blogNewReaderBean);
                //Logger.e(imgUrl + "\n" + title + "\n" + "href:" + href + "\n" + time);
            }
            return blogNewReaderBeanList;
        } catch (Exception e) {
            Logger.e(e.getMessage() + "");
        }
        return blogNewReaderBeanList;
    }


    public static String HomeNewDetails(Context context, String Url) {
        Document document = null;
        try {
            document = Jsoup.parse(new URL(Url), 5000);
            if (document == null) return "";
            Element element = document.getElementsByClass("tui-content article_slider_container").first();
            Element element1 = element.getElementsByClass("article_slider_bigPic_box cf").first();
            Element element2 = element.getElementsByClass("article_slider_smallPic_bar").first();

            return element1.toString(); //+ element2.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";

    }

    /**
     * 获取NewBlogs详情数据
     *
     * @param context
     * @param Url
     */
    public static String getNewBlogDetails(Context context, String Url) {

        Document document = null;
        try {
            document = Jsoup.parse(new URL(Url), 5000);
            if (document == null) return null;

            Element e = document.getElementsByClass("art-conl-box").first();
            e.getElementsByClass("artBox-head").first().getElementsByTag("div").first().remove();
            if (TextUtils.isEmpty(e.toString())) return null;
            return e.toString();
        } catch (Exception e) {
            Logger.e(e.getMessage() + "");
        }
        return null;
    }

    public static String getNewRecommendDetails(Context context, String Url) {

        Document document = null;
        try {
            document = Jsoup.parse(new URL(Url), 5000);
            if (document == null) return null;

            Element element = document.getElementsByClass("readbox").first();
            if (element == null) return null;
            element.getElementsByClass("banner_660 b660_01").first().remove();
            element.getElementsByClass("banner_660 b660_02").first().remove();
            element.getElementsByClass("banner_660 b660_01").last().remove();
            element.getElementsByClass("infos").first().remove();
            element.getElementsByClass("morepage").first().remove();
            element.getElementsByClass("banner_660 b660_04").first().remove();
            element.getElementsByClass("page").first().remove();
            element.getElementsByClass("banner_660 b660_05").first().remove();
            element.getElementsByClass("banner_660 b660_06").first().remove();
            element.getElementsByClass("banner_660 b660_07").first().remove();
            document.getElementsByTag("script").first().remove();

            return element.toString();

        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
        return null;
    }


    public static List<RecomBean> getRecommendListDetailData(String Url) {
        Document doc = null;
        List<RecomBean> recomBeanList = new ArrayList<>();
        try {
            doc = Jsoup.parse(new URL(Url), 5000);
            if (doc == null) return recomBeanList;
            Element element = doc.getElementsByClass("newslist3").first();
            Elements elements = element.getElementsByTag("li");
            //Logger.e(elements.size() + "");
            for (int i = 0; i < 4; i++) {
                RecomBean recomBean = new RecomBean();
                String href = elements.get(i).getElementsByTag("a").first().attr("href") + "";
                String time = elements.get(i).getElementsByTag("span").first().getElementsByTag("b").text() + "";
                String tag = elements.get(i).getElementsByTag("span").first().text();
                String imgUrl = elements.get(i).getElementsByTag("i").first().select("img").attr("src") + "";
                String title = elements.get(i).getElementsByTag("i").first().select("img").attr("alt") + "";
                recomBean.setTitle(title);
                recomBean.setTime(time);
                recomBean.setImgUrl(imgUrl);
                recomBean.setTag(tag);
                recomBean.setHref("http:" + href);
                recomBeanList.add(recomBean);

            }
            return recomBeanList;
        } catch (Exception e) {

        }
        return recomBeanList;
    }


    public static String getHotSpotDetailsData(Context context, String Url) {
        Document document = null;
        try {
            document = Jsoup.parse(new URL(Url), 5000);
            if (document == null) return null;
            String content = document.getElementsByClass("two-coloum fix").first().getElementsByClass("content all-txt").first().toString();
            return content;

        } catch (Exception e) {
            Logger.e("HotSpot:" + e.getMessage());
        }
        return null;
    }


    public static List<Jokebean> getJokeData(String Url) {

        try {
            List<Jokebean> jokebeanList = new ArrayList<>();
            Document document = Jsoup.parse(new URL(Url), 5000);
            if (document == null) return null;
            Elements elements = document.getElementsByClass("lcommon dz-bg").first().getElementsByClass("dzlist rmargin10");
            Logger.e("size：" + elements.size());
            for (int i = 0; i < elements.size(); i++) {
                String content = elements.get(i).getElementsByClass("dz-list-con").first().toString();
                String jokeSign = elements.get(i).getElementsByClass("list_tag").first().getElementsByTag("a").first().text() + "";
                String linkNum = elements.get(i).getElementsByClass("dz_list_bottom").first().getElementsByClass("dz_list_bottom_left").first().getElementsByTag("a").first().text() + "";
                Jokebean jokebean = new Jokebean();
                jokebean.setContent(content);
                jokebean.setLinkNum(linkNum);
                jokebean.setJokesign(jokeSign);
                jokebeanList.add(jokebean);
                Logger.e("=====" + jokeSign + "===>" + linkNum + "]]]]]:" + content);
            }
            Collections.shuffle(jokebeanList);
            return jokebeanList;
        } catch (Exception e) {

        }
        return null;
    }


    public static List<HotspotBannerModel> getHotSpotBannerData() {
        List<HotspotBannerModel> hotspotBannerModelList = new ArrayList<>();
        HotspotBannerModel hotspotBannerModel = new HotspotBannerModel();
        HotspotBannerModel hotspotBannerModel4 = new HotspotBannerModel();
        hotspotBannerModel.setHotspotIcon(R.mipmap.hotspot_smail_icon);
        hotspotBannerModel.setHotspotTitle("轻松一刻");

        HotspotBannerModel hotspotBannerModel1 = new HotspotBannerModel();
        hotspotBannerModel1.setHotspotIcon(R.mipmap.hotsopt_ke_icon);
        hotspotBannerModel1.setHotspotTitle("阅闻客");

        HotspotBannerModel hotspotBannerModel2 = new HotspotBannerModel();
        hotspotBannerModel2.setHotspotIcon(R.mipmap.hotstop_get_icon);
        hotspotBannerModel2.setHotspotTitle("奖励");

        HotspotBannerModel hotspotBannerModel3 = new HotspotBannerModel();
        hotspotBannerModel3.setHotspotIcon(R.mipmap.hotspot_action_icon);
        hotspotBannerModel3.setHotspotTitle("专题");

        hotspotBannerModelList.add(hotspotBannerModel);
        hotspotBannerModelList.add(hotspotBannerModel1);
        hotspotBannerModelList.add(hotspotBannerModel2);
        hotspotBannerModelList.add(hotspotBannerModel3);

        return hotspotBannerModelList;

    }

}
