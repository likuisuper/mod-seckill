package com.cxylk.controller;

import com.cxylk.biz.SeckillGoodsService;
import com.cxylk.biz.SeckillOrderService;
import com.cxylk.biz.SeckillService;
import com.cxylk.domain.SeckillGoodsDTO;
import com.cxylk.exception.BizException;
import com.cxylk.po.OrderInfo;
import com.cxylk.po.SeckillOrder;
import com.cxylk.po.SeckillUser;
import com.cxylk.response.Response;
import com.cxylk.response.ResponseResult;
import com.cxylk.response.ResultCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Classname SeckillController
 * @Description 秒杀控制层
 * @Author likui
 * @Date 2021/1/29 10:48
 **/
@Api(value = "SeckillController", tags = "秒杀控制层")
@RestController
@RequestMapping("/seckill")
public class SeckillController {
    @Autowired
    private SeckillGoodsService seckillGoodsService;

    @Autowired
    private SeckillOrderService seckillOrderService;

    @Autowired
    private SeckillService seckillService;

    /**
     * 优化前：QPS:450
     * linux:3000*10
     * get是幂等的，而post是不幂等的
     */
    @ApiOperation(value = "秒杀实现")
    @PostMapping("/do_seckill")
    public ResponseResult<OrderInfo> doSeckill(@RequestParam("goodsId") long goodsId, SeckillUser user) throws BizException {
        //如果未登录则跳转到登录界面
        if (user == null) {
            throw  new BizException(ResultCode.SESSION_ERROR);
        }
        SeckillGoodsDTO goodsDetail = seckillGoodsService.getGoodsDetail(goodsId);
        //1.如果秒杀商品库存<=0秒杀失败
        if (goodsDetail.getStockCount() <= 0) {
            throw new BizException(ResultCode.SECKILL_OVER);
        }
        //2.库存有，判断是否存在重复秒杀
        SeckillOrder seckillOrder = seckillOrderService.getOrderByUserIdGoodsId(user.getId(), goodsId);
        if (seckillOrder != null) {
            throw new BizException(ResultCode.REPEATE_SECKILL);
        }
        //3.到这里可以正常秒杀
        //减库存、下订单、写入秒杀订单(原子操作，使用事务)
        OrderInfo orderInfo = seckillService.seckill(user, goodsDetail);
        return Response.makeSuccessRsp(orderInfo);
    }
}
