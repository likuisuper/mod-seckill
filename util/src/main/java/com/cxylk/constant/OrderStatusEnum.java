package com.cxylk.constant;

/**
 * @Classname OrderStatusEnum
 * @Description 订单状态
 * @Author likui
 * @Date 2021/1/29 14:18
 **/
public enum OrderStatusEnum {
    NO_PAY(0,"新建未支付"),
    ALREADY_PAY(1,"已支付"),
    ALREADY_SEND(2,"已发货"),
    ALREADY_RECEIVE(3,"已收货"),
    ALREADY_BACK(4,"已退款"),
    ALREADY_SUCCESS(5,"已完成");

    private int code;
    private String msg;

    OrderStatusEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
