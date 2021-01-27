package com.cxylk.service.impl;

/**
 * @Classname UserKey
 * @Description 生成User模块前缀
 * @Author likui
 * @Date 2021/1/17 21:20
 **/
public class SeckillUserKey extends BasePrefix{
    //过期时间设置为1天
    private static final int EXPIRE_TOKEN=3600*24;

    private SeckillUserKey(String prefix) {
        super(prefix);
    }

    private SeckillUserKey(int expireSeconds, String prefix) {
        super(expireSeconds,prefix);
    }

    public static SeckillUserKey token=new SeckillUserKey(EXPIRE_TOKEN,"lk");

    /**
     * 根据id获取前缀
     */
    public static SeckillUserKey getById=new SeckillUserKey("id");

    /**
     * 根据name获取前缀
     */
    public static SeckillUserKey getByName=new SeckillUserKey("name");
}
