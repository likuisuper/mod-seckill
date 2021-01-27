package com.cxylk.exception;

import com.cxylk.response.ResultCode;
import com.cxylk.util.ComUtil;
import com.cxylk.util.MessageUtil;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;

/**
 * @Classname BizException
 * @Description 自定义异常
 * @Author likui
 * @Date 2021/1/16 22:10
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
public class BizException extends Exception {
    private static final long servialVersionUID = 1L;

    private ResultCode resultCode;

//    @Override
//    public String getMessage() {
//        String message=null;
//        if(!ComUtil.isEmpty(errMsg)){
//            message=MessageUtil.getMessage(errMsg,params);
//        }
//        return message;
//    }
}
