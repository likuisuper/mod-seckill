package com.cxylk.biz;

import com.cxylk.domain.SeckillGoodsDTO;
import com.cxylk.po.OrderInfo;
import com.cxylk.po.SeckillUser;

/**
 * @Classname OrderInfoService
 * @Description TODO
 * @Author likui
 * @Date 2021/2/7 17:37
 **/
public interface OrderInfoService {
    /**
     * 生成订单和秒杀订单(原子操作)
     *
     * @param user     用户信息
     * @param goodsDTO 商品信息
     * @return 订单
     */
    OrderInfo createOrder(SeckillUser user, SeckillGoodsDTO goodsDTO);

    /**
     * 取消订单
     * @param orderId
     */
    void cancelOrder(Long orderId);


    OrderInfo getOrderInfoById(long orderId);

    /**
     * 重置订单
     */
    void deleteOrders();
}
