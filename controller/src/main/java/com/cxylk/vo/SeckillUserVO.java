package com.cxylk.vo;

import com.cxylk.validator.IsMobile;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Classname SeckillUserVO
 * @Description 用户VO
 * @Author likui
 * @Date 2021/1/25 10:33
 **/
@Data
public class SeckillUserVO {
    @NotNull
    @IsMobile
    private String mobile;

    @NotNull
    @Size(min = 6,message = "密码不能少于{min}个字符")
    private String password;
}
