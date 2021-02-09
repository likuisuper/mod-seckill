package com.cxylk;

import com.cxylk.constant.ConstantField;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Classname MQConfig
 * @Description 消息队列配置
 * @Author likui
 * @Date 2021/2/9 10:58
 **/
@Configuration
public class MQConfig {
    /*简单模式*/
    @Bean
    public Queue queue() {
        //true代表开启持久化
        return new Queue(ConstantField.SIMPLE_QUEUE, true);
    }

    /*-------------------------topic模式-----------------------*/

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(ConstantField.TOPIC_EXCHANGE);
    }

    @Bean
    public Queue topicQueue1() {
        return new Queue(ConstantField.TOPIC_QUEUE1, true);
    }

    @Bean
    public Queue topicQueue2() {
        return new Queue(ConstantField.TOPIC_QUEUE2, true);
    }

    @Bean
    public Binding topicBinding1() {
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with("topic.key1");
    }

    @Bean
    public Binding topicBinding2() {
        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with("topic.#");
    }

    /*-------------------------fanout(发布订阅也叫广播模式)-------------------*/

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(ConstantField.FANOUT_EXCHANGE);
    }

    @Bean
    public Queue fanoutQueue1() {
        return new Queue(ConstantField.FANOUT_QUEUE1, true);
    }

    @Bean
    public Queue fanoutQueue2() {
        return new Queue(ConstantField.FANOUT_QUEUE2, true);
    }

    @Bean
    public Binding fanoutBinding1() {
        return BindingBuilder.bind(fanoutQueue1()).to(fanoutExchange());
    }

    @Bean
    public Binding fanoutBinding2() {
        return BindingBuilder.bind(fanoutQueue2()).to(fanoutExchange());
    }

    /*--------------------headers(需要配置key-value)模式---------------------*/
    @Bean
    public HeadersExchange headersExchange() {
        return new HeadersExchange(ConstantField.HEADERS_EXCHANGE);
    }

    @Bean
    public Queue headersQueue1() {
        return new Queue(ConstantField.HEADERS_QUEUE1, true);
    }

    @Bean
    public Queue headersQueue2() {
        return new Queue(ConstantField.HEADERS_QUEUE2, true);
    }

    @Bean
    public Binding headersBinding() {
        Map<String, Object> map = new HashMap<>();
        map.put("header1", "value1");
        map.put("header2", "value2");
        //只有当header满足key和value才会往queue里面放,whereAll表示匹配所有
        return BindingBuilder.bind(headersQueue1()).to(headersExchange()).whereAll(map).match();
    }
}
