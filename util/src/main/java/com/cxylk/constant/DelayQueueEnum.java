package com.cxylk.constant;

import lombok.Getter;

/**
 * @Classname DelayQueueEnum
 * @Description 延迟消息队列以及处理取消订单消息队列的常量
 * @Author likui
 * @Date 2021/4/9 14:40
 **/
@Getter
public enum DelayQueueEnum {
    /**
     * 消息通知队列(基于死信)
     */
    QUEUE_ORDER_CANCEL("seckill.order.direct","seckill.order.cancel","seckill.order.cancel"),

    /**
     * 消息通知ttl队列(基于死信)
     */
    QUEUE_TTL_ORDER_CANCEL("seckill.order.ttl","seckill.order.cancel.ttl","seckill.order.cancel.ttl"),

    /**
     * 插件实现延迟队列
     */
    QUEUE_ORDER_PLUGIN_CANCEL("seckill.order.direct.plugin", "seckill.order.cancel.plugin", "seckill.order.cancel.plugin");


    /**
     * 交换机名称
     */
    private String exchange;

    /**
     * 队列名称
     */
    private String name;

    /**
     * 路由键
     */
    private String routeKey;

    DelayQueueEnum(String exchange, String name, String routeKey) {
        this.exchange = exchange;
        this.name = name;
        this.routeKey = routeKey;
    }
}
