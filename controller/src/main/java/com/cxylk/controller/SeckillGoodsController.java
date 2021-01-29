package com.cxylk.controller;

import com.cxylk.biz.SeckillGoodsService;
import com.cxylk.biz.SeckillUserService;
import com.cxylk.domain.SeckillGoodsDTO;
import com.cxylk.exception.BizException;
import com.cxylk.po.SeckillUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Classname SeckillGoodsController
 * @Description 秒杀商品接口
 * @Author likui
 * @Date 2021/1/27 13:07
 **/
@Api(value = "SeckillGoodsController", tags = "秒杀商品")
@Controller
@RequestMapping("/goods")
public class SeckillGoodsController {
    @Autowired
    private SeckillUserService seckillUserService;

    @Autowired
    private SeckillGoodsService seckillGoodsService;

    @ApiOperation(value = "商品列表")
    @GetMapping("/to_list")
    public String goodsList(Model model, SeckillUser seckillUser) throws BizException {
        model.addAttribute("user", seckillUser);
        List<SeckillGoodsDTO> goodsList = seckillGoodsService.getGoodsList();
        model.addAttribute("goodsList", goodsList);
        return "goods_list";
    }

    @ApiOperation(value = "商品详情")
    @GetMapping("/to_detail/{goodsId}")
    public String goodsDetail(@PathVariable("goodsId") long goodsId, Model model, SeckillUser seckillUser) throws BizException {
        model.addAttribute("user", seckillUser);
        SeckillGoodsDTO goodsDetail = seckillGoodsService.getGoodsDetail(goodsId);
        model.addAttribute("goods", goodsDetail);
        //秒杀开始时间(ms)
        long startTime = goodsDetail.getStartDate().getTime();
        //秒杀结束时间(ms)
        long endTime = goodsDetail.getEndDate().getTime();
        //获取当前时间
        long nowTime = System.currentTimeMillis();
        //维护一个秒杀状态
        int seckillStatus;
        //维护一个秒杀倒计时
        int remainSeconds;
        //如果当前时间<秒杀开始时间，说明秒杀还未开始
        if (nowTime < startTime) {
            //计算还有多久开始秒杀，转换为秒
            seckillStatus = 0;
            remainSeconds = (int) ((startTime - nowTime) / 1000);
        } else if (nowTime > endTime) {//秒杀结束
            seckillStatus = 2;
            remainSeconds = -1;
        } else {//秒杀进行中
            seckillStatus = 1;
            remainSeconds = 0;
        }
        //将秒杀状态和倒计时返回前端页面
        model.addAttribute("remainSeconds", remainSeconds);
        model.addAttribute("seckillStatus", seckillStatus);
        return "goods_detail";
    }
}
