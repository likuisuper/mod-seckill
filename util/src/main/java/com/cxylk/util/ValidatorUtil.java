package com.cxylk.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Classname ValidatorUtil
 * @Description TODO
 * @Author likui
 * @Date 2021/1/25 17:17
 **/
public class ValidatorUtil {
    //是否为合法手机号
    private static final Pattern mobilePattern=Pattern.compile("^((13[0-9])|(14[5-7])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

    public static boolean isMobile(String mobile){
        if(ComUtil.isEmpty(mobile)){
            return false;
        }
        Matcher matcher=mobilePattern.matcher(mobile);
        return matcher.matches();
    }

}
