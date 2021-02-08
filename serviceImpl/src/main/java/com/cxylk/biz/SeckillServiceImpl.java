package com.cxylk.biz;

import com.cxylk.domain.SeckillGoodsDTO;
import com.cxylk.po.OrderInfo;
import com.cxylk.po.SeckillGoods;
import com.cxylk.po.SeckillUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Classname SeckillServiceImpl
 * @Description 秒杀serviceImpl
 * @Author likui
 * @Date 2021/1/29 13:46
 **/
@Service
public class SeckillServiceImpl implements SeckillService {
    @Autowired
    private SeckillGoodsService seckillGoodsService;

    @Autowired
    private OrderInfoService orderInfoService;

    @Transactional
    @Override
    public OrderInfo seckill(SeckillUser user, SeckillGoodsDTO goodsDTO) {
        //减库存
        seckillGoodsService.reduceGoods(goodsDTO);
        //下订单(包含订单和秒杀订单)
        return orderInfoService.createOrder(user, goodsDTO);
    }
}
