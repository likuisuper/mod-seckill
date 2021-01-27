package com.cxylk.controller;

import com.cxylk.biz.SeckillUserService;
import com.cxylk.dto.SeckillUserDTO;
import com.cxylk.exception.BizException;
import com.cxylk.response.Response;
import com.cxylk.response.ResponseResult;
import com.cxylk.util.GeneralConvertor;
import com.cxylk.vo.SeckillUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @Classname UserController
 * @Description 用户接口
 * @Author likui
 * @Date 2021/1/24 21:37
 **/
@Api(value = "用户接口",tags = {"用户接口"})
@Slf4j
@RequestMapping("/user")
@Controller
public class SeckillUserController {
    private static final Logger LOGGER= LoggerFactory.getLogger(SeckillUserController.class);

    @Autowired
    private SeckillUserService seckillUserService;

    @ApiOperation(value = "登录页面")
    @GetMapping("/loginPage")
    public String login(){
        return "login";
    }

    @ApiOperation(value = "登录实现")
    @PostMapping("/login")
    @ResponseBody
    public ResponseResult<Boolean> doLogin(@Validated SeckillUserVO seckillUserVO,
                                           HttpServletResponse response) throws BizException {
        SeckillUserDTO seckillUserDTO = GeneralConvertor.convertOnlyMatch(seckillUserVO, SeckillUserDTO.class);
        seckillUserService.login(response,seckillUserDTO);
        return Response.makeSuccessRsp(true);
    }
}
