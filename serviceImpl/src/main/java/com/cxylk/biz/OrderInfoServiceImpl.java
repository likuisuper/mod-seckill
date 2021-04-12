package com.cxylk.biz;

import com.cxylk.component.CancelOrderSender;
import com.cxylk.constant.OrderChannelEnum;
import com.cxylk.constant.OrderStatusEnum;
import com.cxylk.dao.OrderInfoMapper;
import com.cxylk.domain.SeckillGoodsDTO;
import com.cxylk.po.OrderInfo;
import com.cxylk.po.SeckillOrder;
import com.cxylk.po.SeckillUser;
import com.cxylk.service.RedisService;
import com.cxylk.service.impl.OrderKey;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class OrderInfoServiceImpl implements OrderInfoService {
    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private SeckillOrderService seckillOrderService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private CancelOrderSender mqSender;

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
        orderInfoMapper.insert(orderInfo);
        //生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(goodsDTO.getId());
        seckillOrder.setUserId(user.getId());
        seckillOrder.setOrderId(orderInfo.getId());
        redisService.set(OrderKey.getSeckillOrderKey, "" + user.getId() + "_" + goodsDTO.getId(), seckillOrder);
        seckillOrderService.save(seckillOrder);
        //下单完成后开启一个延时消息，用于当用户没有付款时取消订单(orderId在下单后生成)
        sendDelayMessageCancelOrder(orderInfo.getId());
        log.info("create order...");
        return orderInfo;
    }

    @Override
    public void cancelOrder(Long orderId) {
        log.info("cancelOrder,orderId:{}",orderId);
        OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey(orderId);
        Byte status = orderInfo.getStatus();
        if(OrderStatusEnum.NO_PAY.getCode()==(status)){
            OrderInfo updateOrder=new OrderInfo();
            updateOrder.setId(orderId);
            updateOrder.setStatus((byte) OrderStatusEnum.NOT_COMPLETED.getCode());
            orderInfoMapper.updateByPrimaryKeySelective(updateOrder);
        }
    }

    @Override
    public OrderInfo getOrderInfoById(long orderId) {
        return orderInfoMapper.selectByPrimaryKey(orderId);
    }

    @Override
    public void deleteOrders() {
        orderInfoMapper.deleteOrders();
        orderInfoMapper.deleteSeckillOrders();
    }

    /**
     * 用于下单后发送延迟消息
     * @param orderId
     */
    private void sendDelayMessageCancelOrder(Long orderId){
        //订单超时时间，为了简单，在这里设置(应该在数据库设置)。为了测试方便，设置为30s
        long delayTimes=30*1000;
        //发送延迟消息
        mqSender.sendMessage(orderId,delayTimes);
    }
}
