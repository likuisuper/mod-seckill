package com.cxylk.validator;

import com.cxylk.util.ComUtil;
import com.cxylk.util.ValidatorUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Classname IsMobileValidator
 * @Description 自定义格式手机校验器，且要实现ConstraintValidator接口，自定义注解才会生效。第一个参数是注解，第二个是要验证字段的类型
 * @Author likui
 * @Date 2021/1/25 16:49
 **/
public class IsMobileValidator implements ConstraintValidator<IsMobile,String> {
    private boolean required=false;

    //初始化
    @Override
    public void initialize(IsMobile isMobile) {
        required=isMobile.required();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(required){
            return ValidatorUtil.isMobile(s);
        }else {
            if(ComUtil.isEmpty(s)){
                return true;
            }else {
                return ValidatorUtil.isMobile(s);
            }
        }
    }
}
