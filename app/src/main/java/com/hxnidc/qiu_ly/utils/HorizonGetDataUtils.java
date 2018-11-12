package com.hxnidc.qiu_ly.utils;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2017/7/25 15:01
 * Author：yrg
 * Describe:
 */


public class HorizonGetDataUtils {

    public static List<String> HorizonGetDatas() {
        try {
            List<String> horizonList = new ArrayList<>();
            horizonList.add("推荐");
            horizonList.add("搞笑");
            horizonList.add("热门");
            //horizonList.add("情感");
            horizonList.add("运动");
            //horizonList.add("短片");
            //horizonList.add("创新");
            //horizonList.add("创新");
            //horizonList.add("活动");
            //horizonList.add("广告");
            return horizonList;
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
        return null;

    }

}
