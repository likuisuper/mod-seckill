package com.cxylk.util;

import java.util.UUID;

/**
 * @Classname UUIDUtil
 * @Description UUID生成工具类
 * @Author likui
 * @Date 2021/1/27 10:57
 **/
public class UUIDUtil {
    public static String uuid(){
        //将uuid的“-”去掉
        return UUID.randomUUID().toString().replace("-","");
    }
}
