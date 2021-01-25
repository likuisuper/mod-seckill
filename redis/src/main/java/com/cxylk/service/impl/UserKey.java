package com.cxylk.service.impl;

/**
 * @Classname UserKey
 * @Description 生成User模块前缀
 * @Author likui
 * @Date 2021/1/17 21:20
 **/
public class UserKey extends BasePrefix{
    public UserKey(String prefix) {
        super(prefix);
    }

    /**
     * 根据id获取前缀
     */
    public static UserKey getById=new UserKey("id");

    /**
     * 根据name获取前缀
     */
    public static UserKey getByName=new UserKey("name");
}
