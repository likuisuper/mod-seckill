package com.cxylk.biz;

import com.cxylk.domain.SeckillGoodsDTO;
import com.cxylk.po.OrderInfo;
import com.cxylk.po.SeckillOrder;
import com.cxylk.po.SeckillUser;

import java.util.List;

/**
 * @Classname SeckillOrderService
 * @Description 秒杀订单service
 * @Author likui
 * @Date 2021/1/29 13:28
 **/
public interface SeckillOrderService {
    /**
     * 判断是否重复秒杀
     *
     * @param userId
     * @param goodsId
     * @return
     */
    SeckillOrder getOrderByUserIdGoodsId(long userId, long goodsId);


    int save(SeckillOrder seckillOrder);
}
