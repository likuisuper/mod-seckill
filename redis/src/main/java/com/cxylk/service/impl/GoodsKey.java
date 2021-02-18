package com.cxylk.service.impl;

/**
 * @Classname GoodsKey
 * @Description 商品key
 * @Author likui
 * @Date 2021/2/3 21:24
 **/
public class GoodsKey extends BasePrefix{
    public GoodsKey(String prefix) {
        super(prefix);
    }

    public GoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    //商品列表key,过期时间不宜设置太长，避免及时性
    public static GoodsKey getGoodsList=new GoodsKey(60,"gl");

    //商品详情key
    public static GoodsKey getGoodsDetail=new GoodsKey(60,"gd");

    //商品数量key，设置为永不过期
    public static GoodsKey getSeckillGoodsStock=new GoodsKey(0,"gs");
}
