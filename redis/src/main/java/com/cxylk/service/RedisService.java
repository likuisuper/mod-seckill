package com.cxylk.service;

import com.cxylk.exception.BizException;

/**
 * @Classname RedisService
 * @Description RedisService
 * @Author likui
 * @Date 2021/1/16 20:56
 **/
public interface RedisService {
    /**
     * 获取单个对象
     * @param prefix 不同的模块生成不同的前缀
     * @param key
     * @param clazz
     * @param <T>
     * @return value
     */
    <T> T get(KeyPrefix prefix,String key,Class<T> clazz);

    /**
     * 保存对象
     * @param prefix 不同的模块生成不同的前缀
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    <T> boolean set(KeyPrefix prefix,String key,T value);

    /**
     * 判断key是否存在
     * @param prefix 前缀
     * @param key key
     * @return 是否存在
     */
    boolean exists(KeyPrefix prefix,String key);

    /**
     * 删除key
     * @param prefix
     * @param key
     * @return
     */
    boolean delete(KeyPrefix prefix,String key);

    /**
     * 删除Key
     * @param prefix
     * @return
     */
    boolean delete(KeyPrefix prefix);

    /**
     * 将key中存储的value加1。如果键不存在，则新建key并设置为0
     * @param prefix
     * @param key
     * @return 加1后的值
     */
    long incr(KeyPrefix prefix,String key);

    /**
     * 将key中存储的value减1。如果键不存在，则新建key并设置为0
     * @param prefix
     * @param key
     * @return 减一后的值
     */
    long decr(KeyPrefix prefix,String key);
}
