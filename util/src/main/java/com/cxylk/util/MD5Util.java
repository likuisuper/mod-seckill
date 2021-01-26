package com.cxylk.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @Classname MD5Util
 * @Description 实现明文密码两次入库
 * @Author likui
 * @Date 2021/1/23 17:33
 **/
public class MD5Util {
    /**
     * 固定盐
     */
    private static final String salt = "1a2b3c4d";

    /**
     * 生成MD5加密后的密码(该密码可以被彩虹表破解)
     *
     * @param password
     * @return 32位字符串
     */
    public static String md5(String password) {
        return DigestUtils.md5Hex(password);
    }

    /**
     * 第一次MD5加密，用于网络传输，使用固定的salt是为了让服务端知道
     *
     * @param inputPass 明文密码
     * @return
     */
    public static String inputPassToForm(String inputPass) {
        String password = "" + salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);
        return md5(password);
    }

    /**
     * 第二次MD5加密，用于密码入库
     *
     * @param formPass 用户输入
     * @param salt     随机盐
     * @return
     */
    public static String formPassToDB(String formPass, String salt) {
        String password = "" + salt.charAt(0) + salt.charAt(2) + formPass + salt.charAt(5) + salt.charAt(4);
        return md5(password);
    }

    /**
     * 将两次密码MD5加密写在一起
     *
     * @param inputPass 输入密码
     * @param saltDB    随机盐
     * @return 入库密码
     */
    public static String inputPassToDB(String inputPass, String saltDB) {
        String formPass = inputPassToForm(inputPass);
        return formPassToDB(formPass, saltDB);
    }

    public static void main(String[] args) {
        System.out.println(formPassToDB("83633463e1cd3b49da4ca974c0d3b3eb","1a2b3c4d"));
        System.out.println(inputPassToDB("lk1998","1a2b3c4d"));
    }

}
