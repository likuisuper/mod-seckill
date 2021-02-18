package com.cxylk.service.impl;

/**
 * @Classname SeckillKey
 * @Description TODO
 * @Author likui
 * @Date 2021/2/17 16:35
 **/
public class SeckillKey extends BasePrefix{
    public SeckillKey(String prefix) {
        super(prefix);
    }

    public static SeckillKey isGoodsOver=new SeckillKey("go");
}
