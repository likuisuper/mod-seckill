package com.cxylk;

import com.cxylk.constant.ConstantField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @Classname MQReceive
 * @Description 消费者
 * @Author likui
 * @Date 2021/2/9 11:00
 **/
@Service
public class MQReceiver {
    private static Logger logger = LoggerFactory.getLogger(MQReceiver.class);

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
