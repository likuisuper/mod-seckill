package com.cxylk.exception;

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
@RequiredArgsConstructor
@Accessors(chain = true)
public class BizException extends Exception {
    @NonNull
    private String code;

    @NonNull
    private String errMsg;

    private Object[] params;

    @Override
    public String getMessage() {
        String message=null;
        if(!ComUtil.isEmpty(errMsg)){
            MessageUtil.getMessage(errMsg,params);
        }
        return message;
    }
}
