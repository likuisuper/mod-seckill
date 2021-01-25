package com.cxylk.exception;

import com.cxylk.response.Response;
import com.cxylk.response.ResponseResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @Classname ExceptionHandlerAdvice
 * @Description 全局异常处理类
 * @Author likui
 * @Date 2021/1/22 23:28
 **/
@ControllerAdvice
@ResponseBody
public class ExceptionHandlerAdvice {

    @ExceptionHandler({BizException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult<Object> badBizException(BizException exception) {
        return Response.makeErrRsp(exception.getCode(), exception.getMessage());
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult<Object> badException(Exception exception) {
        return Response.makeErrRsp(exception.getMessage());
    }
}
