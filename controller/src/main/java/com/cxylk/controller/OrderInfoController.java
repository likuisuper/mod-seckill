package com.cxylk.controller;

import com.cxylk.biz.OrderInfoService;
import com.cxylk.biz.SeckillGoodsService;
import com.cxylk.domain.SeckillGoodsDTO;
import com.cxylk.po.OrderInfo;
import com.cxylk.po.SeckillUser;
import com.cxylk.response.Response;
import com.cxylk.response.ResponseResult;
import com.cxylk.response.ResultCode;
import com.cxylk.vo.OrderDetailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Classname OrderInfoController
 * @Description 商品接口
 * @Author likui
 * @Date 2021/2/7 17:30
 **/
@Api(value = "OrderInfoController",tags = "订单接口")
@RestController
@RequestMapping("/order")
public class OrderInfoController {
    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private SeckillGoodsService seckillGoodsService;

    @ApiOperation(value = "根据orderId获取订单详情")
    @GetMapping("/detail")
    public ResponseResult<OrderDetailVO> orderDetail(@RequestParam("orderId") long orderId, SeckillUser user){
        //判断用户是否存在或者session是否过期
        if(user==null){
            return Response.makeErrRsp(ResultCode.SESSION_ERROR);
        }
        OrderInfo orderInfo = orderInfoService.getOrderInfoById(orderId);
        //判断是否存在订单
        if(orderInfo==null){
            return Response.makeErrRsp(ResultCode.ORDER_NOT_EXIST);
        }
        SeckillGoodsDTO goodsDetail = seckillGoodsService.getGoodsDetail(orderInfo.getGoodsId());
        OrderDetailVO orderDetailVO=new OrderDetailVO();
        orderDetailVO.setGoodsDTO(goodsDetail);
        orderDetailVO.setOrderInfo(orderInfo);
        orderDetailVO.setSeckillUser(user);
        return Response.makeSuccessRsp(orderDetailVO);
    }

    @ApiOperation(value = "手动取消订单")
    @PostMapping("/cancel")
    public ResponseResult<Object> cancelOrder(@RequestParam("orderId") long orderId){
        //判断用户是否存在或者session是否过期
//        if(user==null){
//            return Response.makeErrRsp(ResultCode.SESSION_ERROR);
//        }
        orderInfoService.cancelOrder(orderId);
        return Response.makeSuccessRsp();
    }
}
