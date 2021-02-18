package com.cxylk.biz;

import com.cxylk.domain.SeckillGoodsDTO;
import com.cxylk.po.OrderInfo;
import com.cxylk.po.SeckillUser;

/**
 * @Classname SeckillService
 * @Description 秒杀service
 * @Author likui
 * @Date 2021/1/29 13:40
 **/
public interface SeckillService {
    /**
     * 秒杀实现，包括减库存，下订单，写入订单，这是一个原子操作
     * @param user user对象
     * @param goodsDTO 商品
     * @return 订单
     */
    OrderInfo seckill(SeckillUser user, SeckillGoodsDTO goodsDTO);

    /**
     * 获取秒杀结果
     * @param userId
     * @param goodsId
     * @return -1：失败，0：处理中，orderId：秒杀成功
     */
    long getSeckillResult(long userId, long goodsId);
}
