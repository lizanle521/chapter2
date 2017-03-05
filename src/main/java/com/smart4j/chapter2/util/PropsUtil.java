package com.smart4j.chapter2.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * Created by Administrator on 2017/3/4.
 */
public class PropsUtil
{
    private static  final Logger logger = LoggerFactory.getLogger(PropsUtil.class);

    /**
     * 加载属性文件
     * @param fileName
     * @return
     */
    public static Properties loadProperties(String fileName){
        Properties properties = null;
        InputStream inputStream = null;
        try {
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if(inputStream == null){
                throw  new FileNotFoundException(fileName+" is not found");
            }
            properties = new Properties();
            properties.load(inputStream);
        } catch (Exception e) {
            logger.error("load properties file failure",e);
        }finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("close inputSream failure",e);
                }
            }
        }
        return properties;
    }

    /**
     * 获取属性文件的某个键值对，如果键不存在，就返回默认值
     * @param properties
     * @param key
     * @param defaultString
     * @return
     */
    public static String getString(Properties properties,String key,String defaultString){
        String str = defaultString;
        if(properties.containsKey(key)){
            str = properties.getProperty(key);
        }
        return  str;
    }

    /**
     * 获取属性文件的某个键值对，如果键不存在，返回空字符串
     * @param properties
     * @param key
     * @return
     */
    public static  String getString(Properties properties,String key){
        return getString(properties,key,"");
    }

    /**
     * 获取属性文件的某个键值对，如果键不存在，返回默认整数值
     * @param properties
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getInt(Properties properties,String key,int defaultValue){
        int v = defaultValue;
        if(properties.containsKey(key)){
            v = CastUtil.castInt(properties.getProperty(key));
        }
        return v;
    }

    /**
     * 获取布尔值属性，允许设置默认值
     * @param properties
     * @param key
     * @param defaultValue
     * @return 对应的key的布尔值
     */
    public static boolean getBoolean(Properties properties,String key,boolean defaultValue){
        boolean v = defaultValue;
        if(properties.containsKey(key)){
            v = CastUtil.castBoolea(properties.getProperty(key),defaultValue);
        }
        return v;
    }

    /**
     * 获取布尔值属性
     * @param properties
     * @param key
     * @return
     */
    public static boolean getBoolean(Properties properties,String key){
        return getBoolean(properties,key,false);
    }
}
