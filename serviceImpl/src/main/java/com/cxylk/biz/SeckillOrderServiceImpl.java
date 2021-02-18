package com.cxylk.biz;

import com.cxylk.constant.OrderChannelEnum;
import com.cxylk.constant.OrderStatusEnum;
import com.cxylk.dao.OrderInfoMapper;
import com.cxylk.dao.SeckillOrderMapper;
import com.cxylk.domain.SeckillGoodsDTO;
import com.cxylk.po.OrderInfo;
import com.cxylk.po.SeckillGoods;
import com.cxylk.po.SeckillOrder;
import com.cxylk.po.SeckillUser;
import com.cxylk.service.RedisService;
import com.cxylk.service.impl.OrderKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

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
    private RedisService redisService;


    @Override
    public SeckillOrder getOrderByUserIdGoodsId(long userId, long goodsId) {
//        return seckillOrderMapper.getOrderByUserIdGoodsId(userId, goodsId);
        //放入缓存
        return redisService.get(OrderKey.getSeckillOrderKey,""+userId+"_"+goodsId,SeckillOrder.class);
    }

    @Override
    public int save(SeckillOrder seckillOrder) {
        return seckillOrderMapper.insert(seckillOrder);
    }
}
