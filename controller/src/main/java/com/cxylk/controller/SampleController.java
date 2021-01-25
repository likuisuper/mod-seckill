package com.cxylk.controller;

import com.cxylk.biz.UserService;
import com.cxylk.exception.BizException;
import com.cxylk.po.User;
import com.cxylk.response.Response;
import com.cxylk.response.ResponseResult;
import com.cxylk.response.ResultCode;
import com.cxylk.service.RedisService;
import com.cxylk.service.impl.UserKey;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Classname SampleController
 * @Description 环境测试类
 * @Author likui
 * @Date 2021/1/9 14:26
 **/
@Api(value = "环境测试接口", tags = {"环境测试接口"})
@Controller
@RequestMapping("/sample")
public class SampleController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;

    @GetMapping("/thymeleaf/demo")
    public String hello() {
        //返回hello页面
        return "hello";
    }

    @ResponseBody
    @ApiOperation(value = "获取用户信息byId")
    @GetMapping("/userInfo")
    public ResponseResult<User> getUser(Integer id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return Response.makeSuccessRsp(user);
        }
        return Response.makeErrRsp("数据库异常");
    }

    @ResponseBody
    @ApiOperation(value = "保存用户信息判断事务是否生效")
    @GetMapping("/save")
    public ResponseResult<Object> saveUserTx() {
        boolean isSave = userService.insertTx();
        if (isSave) {
            return Response.makeSuccessRsp();
        }
        return Response.makeErrRsp("数据库异常");
    }

    @ResponseBody
    @ApiOperation(value = "从redis中获取value")
    @GetMapping("/getByRedis")
    public ResponseResult<User> getByRedis() {
        User user = redisService.get(UserKey.getById, "" + 1, User.class);
        return Response.makeSuccessRsp(user);
    }

    @ResponseBody
    @ApiOperation(value = "设置value值到redis中")
    @GetMapping("/setByRedis")
    public ResponseResult<Boolean> setByRedis() {
        User user = new User();
        user.setId(1);
        user.setName("12345");
        redisService.set(UserKey.getById, "" + 1, user);
        return Response.makeSuccessRsp(true);
    }

    @ResponseBody
    @ApiOperation(value = "将key中存储的值加1")
    @GetMapping("/incr")
    public ResponseResult<Long> increment() {
        long incr = redisService.incr(UserKey.getById, "" + 1);
        return Response.makeSuccessRsp(incr);
    }
}
