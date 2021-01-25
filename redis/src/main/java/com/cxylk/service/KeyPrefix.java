package com.cxylk.service;

/**
 * @Classname KeyPrefix
 * @Description 不同的key(模块)设置不同的前缀
 * @Author likui
 * @Date 2021/1/17 21:04
 **/
public interface KeyPrefix {
    /**
     * 过期时间，0代表永不过期
     * @return
     */
    int expireSeconds();

    /**
     * 获取前缀。小技巧，通过类面生成不同的前缀
     * @return
     */
    String getPrefix();
}
