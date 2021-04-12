package com.cxylk.component;

import com.cxylk.constant.DelayQueueEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Classname CancelOrderSender
 * @Description 取消订单消息发送
 * @Author likui
 * @Date 2021/4/9 17:08
 **/
@Service
public class CancelOrderSender {
    @Autowired
    RabbitTemplate template;

    private static Logger logger = LoggerFactory.getLogger(CancelOrderSender.class);

    /**
     * 取消订单消息的发送者
     * @param orderId
     * @param delayTime
     */
    public void sendMessage(Long orderId,final long delayTime){
        //给延迟队列发送消息
        template.convertAndSend(DelayQueueEnum.QUEUE_ORDER_PLUGIN_CANCEL.getExchange(), DelayQueueEnum.QUEUE_ORDER_PLUGIN_CANCEL.getRouteKey(), orderId, message -> {
            //给消息设置延迟毫秒值
            message.getMessageProperties().setHeader("x-delay",delayTime);
            return message;
        });
        logger.info("send delay message orderId:{}",orderId);
    }
}
