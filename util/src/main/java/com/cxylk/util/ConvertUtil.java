package com.cxylk.util;

import com.alibaba.fastjson.JSON;

/**
 * @Classname ConvertUtil
 * @Description String和Bean之间的转换
 * @Author likui
 * @Date 2021/2/9 11:02
 **/
public class ConvertUtil {
    /**
     * 将bean转换为String
     *
     * @param value
     * @param <T>
     * @return
     */
    public static  <T> String beanToString(T value) {
        if (ComUtil.isEmpty(value)) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class) {
            return "" + value;
        } else if (clazz == String.class) {
            return (String) value;
        } else if (clazz == long.class || clazz == Long.class) {
            return "" + value;
        } else {
            return JSON.toJSONString(value);
        }
    }

    /**
     * 将String转换为Bean
     *
     * @param str
     * @param clazz
     * @param <T>
     * @return
     */
    public static  <T> T stringToBean(String str, Class<T> clazz) {
        if (ComUtil.isEmpty(str)) {
            return null;
        } else if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(str);
        } else if (clazz == String.class) {
            return (T) str;
        } else if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(str);
        } else {
            return (T) JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }
}
