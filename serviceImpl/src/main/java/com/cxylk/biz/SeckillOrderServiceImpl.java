package com.cxylk.biz;

import com.cxylk.constant.OrderChannelEnum;
import com.cxylk.constant.OrderStatusEnum;
import com.cxylk.dao.OrderInfoMapper;
import com.cxylk.dao.SeckillOrderMapper;
import com.cxylk.domain.SeckillGoodsDTO;
import com.cxylk.po.OrderInfo;
import com.cxylk.po.SeckillOrder;
import com.cxylk.po.SeckillUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Classname SeckillOrderServiceImpl
 * @Description 秒杀订单serviceImpl
 * @Author likui
 * @Date 2021/1/29 13:30
 **/
@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {
    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Override
    public SeckillOrder getOrderByUserIdGoodsId(long userId, long goodsId) {
        return seckillOrderMapper.getOrderByUserIdGoodsId(userId, goodsId);
    }

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
        seckillOrderMapper.insert(seckillOrder);
        return orderInfo;
    }
}
