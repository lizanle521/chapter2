package com.smart4j.chapter2.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Administrator on 2017/3/4.
 */
public class CastUtil {

    /**
     * 转换字符串为整数
     * @param property
     * @return
     */
    public static int castInt(String property,int defaultValue) {
        int v = defaultValue;
        if(StringUtils.isNoneEmpty(property)){
            try {
                v = Integer.parseInt(property);
            } catch (NumberFormatException e) {
                v = defaultValue;
            }
        }
        return v;
    }

    /**
     * 转换字符串为整数
     * @param property
     * @return
     */
    public static int castInt(String property){
        return castInt(property,0);
    }

    /**
     * 转换字符串为布尔值，允许设置默认值
     * @param property
     * @param defaultValue
     * @return 布尔值
     */
    public static boolean castBoolea(String property,boolean defaultValue){
        boolean v = defaultValue;
        if(StringUtils.isNoneEmpty(property)){
            try {
                v = Boolean.parseBoolean(property);
            } catch (Exception e) {
                v = defaultValue;
            }
        }
        return v;
    }

    /**
     * 转换字符串为布尔值
     * @param property
     * @return
     */
    public static boolean castBoolean(String property){
        return castBoolea(property,false);
    }

}
