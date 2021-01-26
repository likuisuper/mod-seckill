package com.cxylk.po;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SeckillUser implements Serializable {
    @ApiModelProperty(value = "用户ID，手机号码")
    private Long id;

    private String nickname;

    @ApiModelProperty(value = "MD5(MD5(pass明文+固定salt) + salt)")
    private String password;

    private String salt;

    @ApiModelProperty(value = "头像，云存储的ID")
    private String head;

    @ApiModelProperty(value = "注册时间")
    private Date registerDate;

    @ApiModelProperty(value = "上次登录时间")
    private Date lastLoginDate;

    @ApiModelProperty(value = "登录次数")
    private Integer loginCount;

    private static final long serialVersionUID = 1L;

}