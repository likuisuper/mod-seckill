package com.cxylk.service.impl;

/**
 * @Classname AccessKey
 * @Description TODO
 * @Author likui
 * @Date 2021/2/24 22:50
 **/
public class AccessKey extends BasePrefix {
    public AccessKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static AccessKey withExpire(int expireSeconds){
        return new AccessKey(expireSeconds,"access");
    }
}
