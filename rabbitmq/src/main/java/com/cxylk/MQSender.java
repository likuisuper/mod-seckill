package com.cxylk;

import com.cxylk.constant.ConstantField;
import com.cxylk.util.ConvertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Classname MQSend
 * @Description 生产者
 * @Author likui
 * @Date 2021/2/9 11:00
 **/
@Service
public class MQSender {
    @Autowired
    RabbitTemplate template;

    private static Logger logger = LoggerFactory.getLogger(MQSender.class);

    /**
     * 简单模式发送者
     *
     * @param message
     */
    public void send(Object message) {
        String msg = ConvertUtil.beanToString(message);
        template.convertAndSend(ConstantField.SIMPLE_QUEUE, msg);
        logger.info("send message:" + msg);
    }

    /**
     * topic模式发送者
     *
     * @param message
     */
    public void sendTopic(Object message) {
        String msg = ConvertUtil.beanToString(message);
        template.convertAndSend(ConstantField.TOPIC_EXCHANGE, "topic.key1", msg + "1");
        template.convertAndSend(ConstantField.TOPIC_EXCHANGE, "topic.key2", msg + "2");
        logger.info("send topic message:" + msg);
    }

    /**
     * 发布订阅模式(广播模式)
     *
     * @param message
     */
    public void sendFanout(Object message) {
        String msg = ConvertUtil.beanToString(message);
        //虽然第二个参数配置不起作用，但还是不能少否则消费者接收不到消息
        template.convertAndSend(ConstantField.FANOUT_EXCHANGE, null, msg);
        logger.info("send topic message:" + msg);
    }

    /**
     * headers模式
     *
     * @param message
     */
    public void sendHeaders(Object message) {
        String msg = ConvertUtil.beanToString(message);
        logger.info("send topic message:" + msg);
        MessageProperties properties = new MessageProperties();
        //这里设置的key和value必须和配置中的一样才能放入queue中
        properties.setHeader("header1", "value1");
        properties.setHeader("header2", "value2");
        assert msg != null;
        Message obj = new Message(msg.getBytes(), properties);
        template.convertAndSend(ConstantField.HEADERS_EXCHANGE, null, obj);
    }
}
