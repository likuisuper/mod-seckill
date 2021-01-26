package com.cxylk.exception;

import com.cxylk.response.Response;
import com.cxylk.response.ResponseResult;
import com.cxylk.response.ResultCode;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * @Classname ExceptionHandlerAdvice
 * @Description 全局异常处理类
 * @Author likui
 * @Date 2021/1/22 23:28
 **/
@ControllerAdvice
@ResponseBody
public class ExceptionHandlerAdvice {

    /**
     * 拦截自定义业务异常
     * @param exception
     * @return
     */
    @ExceptionHandler(value = BizException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult<Object> badBizException(BizException exception) {
        return Response.makeErrRsp(exception.getResultCode());
    }

    /**
     * 拦截所有异常和绑定异常(为了校验手机号)
     * @param exception
     * @return
     */
    @ExceptionHandler(value = Exception.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult<Object> badException(Exception exception) {
        if(exception instanceof BindException){
            BindException bindException= (BindException) exception;
            //获取所有异常
            List<ObjectError> allErrors = bindException.getAllErrors();
            //这里只需要第一个异常即可
            ObjectError error = allErrors.get(0);
            //获取msg
            String defaultMessage = error.getDefaultMessage();
            //给状态码填充参数
            return Response.makeErrRsp(ResultCode.BIND_ERROR.fillArgs(defaultMessage));
        }else {
            return Response.makeErrRsp(ResultCode.SERVER_ERROR);
        }
    }
}
