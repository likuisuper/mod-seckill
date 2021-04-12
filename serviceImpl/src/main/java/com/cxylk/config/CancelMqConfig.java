package com.cxylk.config;

import com.cxylk.constant.DelayQueueEnum;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Classname CancelMqConfig
 * @Description 延迟队列配置
 * @Author likui
 * @Date 2021/4/9 17:09
 **/
@Configuration
public class CancelMqConfig {
    /**
     * 订单延迟插件消息队列所绑定的交换机
     * @return
     */
    @Bean
    CustomExchange orderPluginDirect(){
        //创建一个自定义交换机，可以发送延迟消息
        Map<String,Object> args=new HashMap<>();
        args.put("x-delayed-type","direct");
        return new CustomExchange(DelayQueueEnum.QUEUE_ORDER_PLUGIN_CANCEL.getExchange(),"x-delayed-message",true,false,args);
    }

    /**
     * 订单延迟插件队列
     * @return
     */
    @Bean
    public Queue orderPluginQueue(){
        return new Queue(DelayQueueEnum.QUEUE_ORDER_PLUGIN_CANCEL.getName());
    }

    /**
     * 将订单延迟插件队列绑定到交换机
     * @param orderPluginDirect 交换机
     * @param orderPluginQueue 队列
     * @return
     */
    @Bean
    public Binding orderPluginBinding(CustomExchange orderPluginDirect, Queue orderPluginQueue){
        return BindingBuilder
                .bind(orderPluginQueue)
                .to(orderPluginDirect)
                .with(DelayQueueEnum.QUEUE_ORDER_PLUGIN_CANCEL.getRouteKey())
                .noargs();
    }

}
