package com.cxylk;

import com.cxylk.biz.SeckillGoodsService;
import com.cxylk.biz.SeckillOrderService;
import com.cxylk.biz.SeckillService;
import com.cxylk.constant.ConstantField;
import com.cxylk.domain.SeckillGoodsDTO;
import com.cxylk.po.SeckillOrder;
import com.cxylk.po.SeckillUser;
import com.cxylk.util.ConvertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Classname MQReceive
 * @Description 消费者
 * @Author likui
 * @Date 2021/2/9 11:00
 **/
@Service
public class MQReceiver {
    @Autowired
    private SeckillGoodsService seckillGoodsService;

    @Autowired
    private SeckillOrderService seckillOrderService;

    @Autowired
    private SeckillService seckillService;


    private static Logger logger = LoggerFactory.getLogger(MQReceiver.class);

    @RabbitListener(queues = ConstantField.SECKILL_QUEUE)
    public void seckillReceive(String message) {
        logger.info("seckill receive message:" + message);
        SeckillMessage seckillMessage = ConvertUtil.stringToBean(message, SeckillMessage.class);
        assert seckillMessage != null;
        SeckillUser user = seckillMessage.getUser();
        long goodsId = seckillMessage.getGoodsId();
        //这里可以访问数据库是因为很少有数据可以进来，已经找到了
        SeckillGoodsDTO goodsDetail = seckillGoodsService.getGoodsDetail(goodsId);
        int stockCount = goodsDetail.getStockCount();
        if (stockCount <= 0) {
            return;
        }
        //判断是否已经秒杀到了
        SeckillOrder order = seckillOrderService.getOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return;
        }
        //减库存，下订单，写入秒杀订单
        seckillService.seckill(user, goodsDetail);
    }

    /**
     * 简单模式下的消费者
     *
     * @param message
     */
    @RabbitListener(queues = ConstantField.SIMPLE_QUEUE)
    public void receive(String message) {
        logger.info("receive message:" + message);
    }

    /**
     * topic模式下消费者
     *
     * @param message
     */
    @RabbitListener(queues = ConstantField.TOPIC_QUEUE1)
    public void receiveTopic1(String message) {
        logger.info("topic queue1 receive message:" + message);
    }

    @RabbitListener(queues = ConstantField.TOPIC_QUEUE2)
    public void receiveTopic2(String message) {
        logger.info("topic queue2 receive message:" + message);
    }

    /**
     * 发布订阅模式下的消费者
     *
     * @param message
     */
    @RabbitListener(queues = ConstantField.FANOUT_QUEUE1)
    public void receiveFanout1(String message) {
        logger.info("fanout queue1 receive message:" + message);
    }

    @RabbitListener(queues = ConstantField.FANOUT_QUEUE2)
    public void receiveFanout2(String message) {
        logger.info("fanout queue2 receive message:" + message);
    }

    /**
     * headers模式下的消费者
     *
     * @param bytes
     */
    @RabbitListener(queues = ConstantField.HEADERS_QUEUE1)
    public void receiveHeader1(byte[] bytes) {
        logger.info("header queue1 receive message:" + new String(bytes));
    }
}
