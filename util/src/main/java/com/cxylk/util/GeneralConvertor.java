package com.cxylk.util;

import net.sf.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Classname GeneralConvertor
 * @Description 实体类转换
 * @Author likui
 * @Date 2021/1/25 14:23
 **/
public class GeneralConvertor {
    private GeneralConvertor(){

    }
    /**
     * 1进行深复制，<br>
     * 2必须匹配所有属性，T与R具有相同的属性<br>
     * 参看单元测试
     *
     * @param beanSrc
     * @param
     * @param <R>
     * @param <T>
     * @return
     */
    public static <R, T> R convertAll(T beanSrc, Class<R> descCls) {
        JSONObject jsonObject = JSONObject.fromObject(beanSrc);
        return (R) JSONObject.toBean(jsonObject, descCls);
    }

    public static <R, T> R convertAllByClassMap(T beanSrc, Class<R> descCls, Map classMap) {
        JSONObject jsonObject = JSONObject.fromObject(beanSrc);
        return (R) JSONObject.toBean(jsonObject, descCls,classMap);
    }

    /**
     * convertAll的list版本
     *
     * @param beanSrc
     * @param descCls
     * @param <R>
     * @param <T>
     * @return
     */
    public static <R, T> List<R> convertListAll(List<T> beanSrc, Class<R> descCls) {
        List<R> rList = new ArrayList<>();
        for (T t : beanSrc) {
            R r = convertAll(t, descCls);
            rList.add(r);
        }
        return rList;
    }

    /**
     * 转换匹配到的属性,参看单元测试
     *
     * @param beanSrc
     * @param descCls
     * @param <R>
     * @param <T>
     * @return
     */
    public static <R, T> R convertOnlyMatch(T beanSrc, Class<R> descCls) {
        try {
            R destInstance = descCls.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(beanSrc, destInstance);
            return destInstance;
        } catch (Exception e) {
            LoggerFactory.getLogger(GeneralConvertor.class).error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * convertOnlyMatch的list的版本
     *
     * @param beanSrc
     * @param descCls
     * @param <R>
     * @param <T>
     * @return
     */
    public static <R, T> List<R> convertListOnlyMatch(List<T> beanSrc, Class<R> descCls) {
        List<R> rList = new ArrayList<>();
        beanSrc.forEach(t -> {
            R r = convertOnlyMatch(t, descCls);
            rList.add(r);
        });
        return rList;
    }

}
