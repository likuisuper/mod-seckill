package com.cxylk.util;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @Classname MessageUtil
 * @Description 获取i18n资源文件
 * @Author likui
 * @Date 2021/1/16 22:16
 **/
public class MessageUtil {
    public static String getMessage(String code,Object... args){
        MessageSource messageSource=SpringUtils.getBean(MessageSource.class);
        return messageSource.getMessage(code,args, LocaleContextHolder.getLocale());
    }
}
