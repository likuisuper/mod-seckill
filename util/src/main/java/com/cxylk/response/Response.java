package com.cxylk.response;

/**
 * @Classname Response
 * @Description 返回结果
 * @Author likui
 * @Date 2021/1/9 17:17
 **/
public class Response {

    private static ResponseResult<Object> resOK = new ResponseResult<>().setCode(ResultCode.SUCCESS.getCode()).setMsg(ResultCode.SUCCESS.getMsg());

    public static ResponseResult<Object> makeSuccessRsp() {
        return resOK;
    }

    public static <T> ResponseResult<T> makeSuccessRsp(String message) {
        return new ResponseResult<T>().setCode(ResultCode.SUCCESS.getCode()).setMsg(message);
    }

    public static <T> ResponseResult<T> makeSuccessRsp(T data) {
        return new ResponseResult<T>().setCode(ResultCode.SUCCESS.getCode()).setMsg(ResultCode.SUCCESS.getMsg()).setData(data);
    }

    public static <T> ResponseResult<T> makeErrRsp(String message) {
        return new ResponseResult<T>().setCode(ResultCode.SERVER_ERROR).setMsg(message);
    }

    public static <T> ResponseResult<T> makeErrRsp(ResultCode resultCode) {
        return new ResponseResult<T>().setCode(resultCode.getCode()).setMsg(resultCode.getMsg());
    }

    public static <T> ResponseResult<T> makeErrRsp(String code, String message) {
        return new ResponseResult<T>().setCode(code).setMsg(message);
    }

    public static <T> ResponseResult<T> makeRsp(String code, String msg) {
        return new ResponseResult<T>().setCode(code).setMsg(msg);
    }

    public static <T> ResponseResult<T> makeRsp(String code, String msg, T data) {
        return new ResponseResult<T>().setCode(code).setMsg(msg).setData(data);
    }
}
