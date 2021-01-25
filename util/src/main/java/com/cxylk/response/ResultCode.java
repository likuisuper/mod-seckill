package com.cxylk.response;

/**
 * @Classname ResultCode
 * @Description 响应状态码
 * @Author likui
 * @Date 2021/1/9 17:02
 **/
public enum ResultCode {

    //通用的错误码
    SUCCESS("200", "SUCCESS"),
    FAIL("400","FAIL"),
    VALUE_TYPE_ERROR("500101","value类型错误"),
    SERVER_ERROR("500100", "服务端异常");
//    BIND_ERROR(500101, "参数校验异常：%s"),
//    ACCESS_LIMIT_REACHED(500104, "访问高峰期，请稍等！"),
//    //登录模块 5002XX
//    SESSION_ERROR(500210, "Session不存在或者已经失效"),
//    PASSWORD_EMPTY(500211, "登录密码不能为空"),
//    MOBILE_EMPTY(500212, "手机号不能为空"),
//    MOBILE_ERROR(500213, "手机号格式错误"),
//    MOBILE_NOT_EXIST(500214, "手机号不存在"),
//    PASSWORD_ERROR(500215, "密码错误"),
//    PRIMARY_ERROR(500216, "主键冲突"),
//
//    //商品模块 5003XX
//
//    //订单模块 5004XX
//    ORDER_NOT_EXIST(500400, "订单不存在"),
//
//    //秒杀模块 5005XX
//    SECKILL_OVER(500500, "商品已经秒杀完毕"),
//    REPEATE_SECKILL(500501, "不能重复秒杀");

    private String code;
    private String msg;

    private ResultCode() {
    }

    private ResultCode(String  code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
