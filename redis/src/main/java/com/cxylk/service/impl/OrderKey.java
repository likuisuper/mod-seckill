package com.cxylk.service.impl;

/**
 * @Classname OrderKey
 * @Description TODO
 * @Author likui
 * @Date 2021/2/8 13:51
 **/
public class OrderKey extends BasePrefix{
    public OrderKey(String prefix) {
        super(prefix);
    }

    public static OrderKey getSeckillOrderKey=new OrderKey("soug");
}
