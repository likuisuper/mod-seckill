package com.cxylk.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @Classname ResponseResult
 * @Description 返回结果集封装
 * @Author likui
 * @Date 2021/1/9 17:02
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class ResponseResult<T> {
    private String code;
    private String msg;
    private T data;

    public ResponseResult<T> setCode(ResultCode retCode) {
        this.code = retCode.getCode();
        return this;
    }

    public ResponseResult<T> setCode(String code) {
        this.code = code;
        return this;
    }

    public ResponseResult<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public ResponseResult<T> setData(T data) {
        this.data = data;
        return this;
    }
}
