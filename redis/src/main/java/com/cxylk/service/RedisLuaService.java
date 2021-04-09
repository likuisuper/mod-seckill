package com.cxylk.service;

/**
 * @Classname RedisLua
 * @Description redis lua脚本使用
 * @Author likui
 * @Date 2021/4/6 9:52
 **/
public interface RedisLuaService {

    /**
     * 统计网站访问次数
     * @param key
     */
    Object statsAccessCount(String key);

    Object statsUser(String key,long userId);
}
