package com.cxylk.controller;

import com.cxylk.biz.SeckillGoodsService;
import com.cxylk.biz.SeckillOrderService;
import com.cxylk.biz.SeckillService;
import com.cxylk.domain.SeckillGoodsDTO;
import com.cxylk.po.OrderInfo;
import com.cxylk.po.SeckillOrder;
import com.cxylk.po.SeckillUser;
import com.cxylk.response.Response;
import com.cxylk.response.ResponseResult;
import com.cxylk.response.ResultCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Classname SeckillController
 * @Description 秒杀控制层
 * @Author likui
 * @Date 2021/1/29 10:48
 **/
@Api(value = "SeckillController", tags = "秒杀控制层")
@Controller
@RequestMapping("/seckill")
public class SeckillController {
    @Autowired
    private SeckillGoodsService seckillGoodsService;

    @Autowired
    private SeckillOrderService seckillOrderService;

    @Autowired
    private SeckillService seckillService;

    @ApiOperation(value = "秒杀实现")
    @PostMapping("/do_seckill/{goodsId}")
    public String doSeckill(@PathVariable("goodsId") long goodsId, Model model, SeckillUser user) {
        //如果未登录则跳转到登录界面
        if (user == null) {
            return "login";
        }
        SeckillGoodsDTO goodsDetail = seckillGoodsService.getGoodsDetail(goodsId);
        //1.如果秒杀商品库存<=0秒杀失败
        if (goodsDetail.getStockCount() <= 0) {
            model.addAttribute("errorMsg", ResultCode.SECKILL_OVER.getMsg());
            return "seckill_fail";
        }
        //2.库存有，判断是否存在重复秒杀
        SeckillOrder seckillOrder = seckillOrderService.getOrderByUserIdGoodsId(user.getId(), goodsId);
        if (seckillOrder != null) {
            model.addAttribute("errorMsg", ResultCode.REPEATE_SECKILL.getMsg());
            return "seckill_fail";
        }
        //3.到这里可以正常秒杀
        //减库存、下订单、写入秒杀订单(原子操作，使用事务)
        OrderInfo orderInfo = seckillService.seckill(user, goodsDetail);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goodsDetail", goodsDetail);
        return "order_detail";
    }
}
