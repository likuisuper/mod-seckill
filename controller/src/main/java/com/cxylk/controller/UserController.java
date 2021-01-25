package com.cxylk.controller;

import com.cxylk.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Classname UserController
 * @Description 用户登录接口
 * @Author likui
 * @Date 2021/1/24 21:37
 **/
@Api(value = "用户登录接口",tags = {"UserController"})
@RequestMapping("/login")
@Controller
public class UserController {

    @ApiOperation(value = "登录入口")
    @GetMapping("/to_login")
    public String login(){
        return "login";
    }

//    public ResponseResult doLogin(){
//
//    }
}
