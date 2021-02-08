package com.cxylk.response;

/**
 * @Classname ResultCode
 * @Description 响应状态码
 * @Author likui
 * @Date 2021/1/9 17:02
 **/
public class ResultCode {

    private String code;
    private String msg;

    //通用的错误码
    public static final ResultCode SUCCESS = new ResultCode("200", "SUCCESS");
    public static final ResultCode FAIL = new ResultCode("400", "FAIL");
    public static final ResultCode SERVER_ERROR = new ResultCode("500100", "服务端异常");
    public static final ResultCode BIND_ERROR = new ResultCode("500101", "参数校验异常：%s");
    public static final ResultCode DATE_FORMAT_ERROR = new ResultCode("500303", "参数格式化异常");
    //    ACCESS_LIMIT_REACHED(500104, "访问高峰期，请稍等！"),
    //登录模块 5002XX
    public static final ResultCode MOBILE_NOT_EXIST = new ResultCode("500214", "用户不存在");
    public static final ResultCode MOBILE_EMPTY = new ResultCode("500212", "手机号不能为空");
    public static final ResultCode PASSWORD_EMPTY = new ResultCode("500211", "登录密码不能为空");
    public static final ResultCode MOBILE_ERROR = new ResultCode("500213", "手机号格式错误");

    public static final ResultCode SESSION_ERROR=new ResultCode("500210", "Session不存在或者已经失效");


    public static final ResultCode PASSWORD_ERROR = new ResultCode("500215", "密码错误");
//    PRIMARY_ERROR(500216, "主键冲突"),
//
//    //商品模块 5003XX
//
//    //订单模块 5004XX
public static final ResultCode ORDER_NOT_EXIST=new ResultCode("500400", "订单不存在");
//
    //秒杀模块 5005XX
    public static final ResultCode SECKILL_OVER=new ResultCode("500500", "商品已经秒杀完毕");
    public static final ResultCode REPEATE_SECKILL=new ResultCode("500501", "不能重复秒杀");

    private ResultCode() {
    }

    private ResultCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultCode fillArgs(Object... args) {
        String code = this.code;
        String message = String.format(this.msg, args);
        return new ResultCode(code, message);
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ResultCode{");
        sb.append("code='").append(code).append('\'');
        sb.append(", msg='").append(msg).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
