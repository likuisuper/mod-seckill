package com.cxylk.constant;

/**
 * @Classname OrderInfoEnum
 * @Description 下单渠道
 * @Author likui
 * @Date 2021/1/29 14:11
 **/
public enum OrderChannelEnum {
    PC(0),
    ANDROID(1),
    IOS(2);

    private int code;


    OrderChannelEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
