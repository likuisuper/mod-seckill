package com.cxylk.service.impl;

import com.cxylk.service.KeyPrefix;

/**
 * @Classname SeckillKey
 * @Description TODO
 * @Author likui
 * @Date 2021/2/17 16:35
 **/
public class SeckillKey extends BasePrefix{

    public SeckillKey(int expireSeconds, String prefix) {
        super(expireSeconds,prefix);
    }


    public static SeckillKey isGoodsOver=new SeckillKey(0,"go");

    //秒杀地址key
    public static SeckillKey getSeckillPath=new SeckillKey(60,"sp");

    public static SeckillKey getSeckillVerifyCode=new SeckillKey(300,"vc");
}
