package com.cxylk.access;

import com.cxylk.po.SeckillUser;

/**
 * @Classname UserContext
 * @Description 使用ThreadLocal保存本地线程
 * @Author likui
 * @Date 2021/2/25 21:43
 **/
public class UserContext {
    private static ThreadLocal<SeckillUser> userThreadLocal=new ThreadLocal<>();

    public static void setUser(SeckillUser user){
        userThreadLocal.set(user);
    }

    public static SeckillUser getUser(){
        return userThreadLocal.get();
    }

    public static void removeUser(){
        userThreadLocal.remove();
    }
}
