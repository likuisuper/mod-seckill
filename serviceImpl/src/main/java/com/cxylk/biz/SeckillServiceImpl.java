package com.cxylk.biz;

import com.cxylk.domain.SeckillGoodsDTO;
import com.cxylk.po.OrderInfo;
import com.cxylk.po.SeckillGoods;
import com.cxylk.po.SeckillOrder;
import com.cxylk.po.SeckillUser;
import com.cxylk.service.RedisService;
import com.cxylk.service.impl.SeckillKey;
import com.cxylk.util.MD5Util;
import com.cxylk.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.security.krb5.internal.PAData;

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
        if (seckillOrder != null) {
            //秒杀成功，返回订单id
            return seckillOrder.getOrderId();
        } else {
            boolean isOver = getGoodsOver(goodsId);
            if (isOver) {
                return -1;//说明已经卖完
            } else {
                return 0;//轮询
            }
        }
    }

    @Override
    public void reset(List<SeckillGoodsDTO> goodsList) {
        seckillGoodsService.resetStock(goodsList);
        orderInfoService.deleteOrders();
    }

    @Override
    public String createPath(SeckillUser user, long goodsId) {
        if (user == null || goodsId <= 0) {
            return null;
        }
        String md5 = MD5Util.md5(UUIDUtil.uuid() + "123456");
        redisService.set(SeckillKey.getSeckillPath, "" + user.getId() + "_" + goodsId, md5);
        return md5;
    }

    @Override
    public boolean checkPath(SeckillUser user, long goodsId, String path) {
        if (user == null || path == null || goodsId <= 0) {
            return false;
        }
        String oldPath = redisService.get(SeckillKey.getSeckillPath, "" + user.getId() + "_" + goodsId, String.class);
        return path.equals(oldPath);
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(SeckillKey.isGoodsOver, "" + goodsId, true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(SeckillKey.isGoodsOver, "" + goodsId);
    }
}
