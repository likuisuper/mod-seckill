package com.cxylk.service.impl;

import com.cxylk.service.KeyPrefix;

/**
 * @Classname BasePrefix
 * @Description 调用类的前缀
 * @Author likui
 * @Date 2021/1/17 21:06
 **/
public abstract class BasePrefix implements KeyPrefix {
    private int expireSeconds;
    private String prefix;

    public BasePrefix(String prefix){
        this(0,prefix);
    }

    public BasePrefix(int expireSeconds,String prefix){
        this.expireSeconds=expireSeconds;
        this.prefix=prefix;
    }

    @Override
    public int expireSeconds() {
        return expireSeconds;
    }

    @Override
    public String getPrefix() {
        //this代表引用类的当前实例，也就是子类引用当前类的实例
        String className=this.getClass().getSimpleName();
        return className+":"+prefix;
    }
}
