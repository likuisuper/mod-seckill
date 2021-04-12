package com.cxylk.component;

import com.cxylk.biz.OrderInfoService;
import com.cxylk.constant.ConstantField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * @Classname CancelOrderReceiver
 * @Description 取消订单消息接收
 * @Author likui
 * @Date 2021/4/9 17:08
 **/
@Service
public class CancelOrderReceiver {
    @Autowired
    private OrderInfoService orderInfoService;

    private static Logger logger = LoggerFactory.getLogger(CancelOrderReceiver.class);

    /**
     * 1.RabbitListener注解加在类上会无限循环；2.orderId要加上@Payload注解，否则会报错：Payload value must not be empty
     * @param orderId
     */
    @RabbitListener(queues = ConstantField.ORDER_CANCEL_PLUGIN)
    public void handle(@Payload Long orderId){
        logger.info("receive delay message orderId:{}",orderId);
        orderInfoService.cancelOrder(orderId);
    }
}
