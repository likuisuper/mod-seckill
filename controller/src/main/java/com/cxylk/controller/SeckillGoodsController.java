package com.cxylk.controller;

import com.cxylk.biz.SeckillUserService;
import com.cxylk.exception.BizException;
import com.cxylk.po.SeckillUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @Classname SeckillGoodsController
 * @Description 秒杀商品接口
 * @Author likui
 * @Date 2021/1/27 13:07
 **/
@Api(value = "SeckillGoodsController",tags = "秒杀商品")
@Controller
@RequestMapping("/goods")
public class SeckillGoodsController {
    @Autowired
    private SeckillUserService seckillUserService;

    @ApiOperation(value = "商品页面")
    @GetMapping("/to_list")
    public String goods(Model model,SeckillUser seckillUser) throws BizException {
        model.addAttribute("user",seckillUser);
        return "goods_list";
    }
}
