package com.cxylk.biz;

import com.cxylk.domain.SeckillGoodsDTO;
import com.cxylk.po.OrderInfo;
import com.cxylk.po.SeckillGoods;
import com.cxylk.po.SeckillOrder;
import com.cxylk.po.SeckillUser;
import com.cxylk.service.RedisService;
import com.cxylk.service.impl.SeckillKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Autowired
    private SeckillOrderService seckillOrderService;


    @Autowired
    private RedisService redisService;

    @Transactional
    @Override
    public OrderInfo seckill(SeckillUser user, SeckillGoodsDTO goodsDTO) {
        //减库存
        boolean success = seckillGoodsService.reduceGoods(goodsDTO);
        //减库存成功才生成订单
        if (success) {
            //下订单(包含订单和秒杀订单)
            return orderInfoService.createOrder(user, goodsDTO);
        } else {
            //秒杀失败，做一个标记
            setGoodsOver(goodsDTO.getId());
            return null;
        }
    }


    @Override
    public long getSeckillResult(long userId, long goodsId) {
        SeckillOrder seckillOrder = seckillOrderService.getOrderByUserIdGoodsId(userId, goodsId);
        if(seckillOrder!=null){
            //秒杀成功，返回订单id
            return seckillOrder.getOrderId();
        }else{
            boolean isOver=getGoodsOver(goodsId);
            if(isOver){
                return -1;//说明已经卖完
            }else {
                return 0;//轮询
            }
        }
    }

    @Override
    public void reset(List<SeckillGoodsDTO> goodsList) {
        seckillGoodsService.resetStock(goodsList);
        orderInfoService.deleteOrders();
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(SeckillKey.isGoodsOver,""+goodsId,true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(SeckillKey.isGoodsOver,""+goodsId);
    }
}
