package com.cxylk.biz;

import com.cxylk.constant.OrderChannelEnum;
import com.cxylk.constant.OrderStatusEnum;
import com.cxylk.dao.OrderInfoMapper;
import com.cxylk.domain.SeckillGoodsDTO;
import com.cxylk.po.OrderInfo;
import com.cxylk.po.SeckillOrder;
import com.cxylk.po.SeckillUser;
import com.cxylk.service.RedisService;
import com.cxylk.service.impl.OrderKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Classname OrderInfoServiceImpl
 * @Description TODO
 * @Author likui
 * @Date 2021/2/7 17:36
 **/
@Service
public class OrderInfoServiceImpl implements OrderInfoService {
    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private SeckillOrderService seckillOrderService;

    @Autowired
    private RedisService redisService;

    @Transactional
    @Override
    public OrderInfo createOrder(SeckillUser user, SeckillGoodsDTO goodsDTO) {
        //生成订单
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUserId(user.getId());
        orderInfo.setGoodsId(goodsDTO.getId());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsName(goodsDTO.getGoodsName());
        //一个订单只能生成一个秒杀商品数量
        orderInfo.setGoodsCount(1);
        //秒杀商品价格
        orderInfo.setGoodsPrice(goodsDTO.getSeckillPrice());
        orderInfo.setOrderChannel((byte) OrderChannelEnum.PC.getCode());
        orderInfo.setStatus((byte) OrderStatusEnum.NO_PAY.getCode());
        orderInfo.setCreateDate(new Date());
        //保存
        long orderId = orderInfoMapper.insert(orderInfo);
        //生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(goodsDTO.getId());
        seckillOrder.setUserId(user.getId());
        seckillOrder.setOrderId(orderId);
        redisService.set(OrderKey.getSeckillOrderKey, "" + user.getId() + "_" + goodsDTO.getId(), seckillOrder);
        seckillOrderService.save(seckillOrder);
        return orderInfo;
    }

    @Override
    public OrderInfo getOrderInfoById(long orderId) {
        return orderInfoMapper.selectByPrimaryKey(orderId);
    }
}
