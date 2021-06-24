package com.cxylk;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import org.springframework.amqp.core.Queue;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Classname MQReliable
 * @Description MQ怎么确保消息可靠？3种情况导致消息丢失
 *              1、消费者弄丢数据，比如刚消费到，还没处理，结果进程挂了。解决办法：关闭MQ的自动ack，每次确保消息处理完之后手动ack
 * @Author likui
 * @Date 2021/6/24 15:57
 **/
public class MQReliable {
    /**
     * 1、生产者丢失数据，解决方案：
     * （1）开启MQ事务：吞吐量下降，耗费性能，并且是同步的，会阻塞
     * （2）confirm机制：在生产者设置confirm模式，异步
     */
    public void producerLost() throws IOException, TimeoutException, InterruptedException {
        //创建连接
        ConnectionFactory factory=new ConnectionFactory();
        //还需配置用户、密码、ip、端口等，省略
        Connection connection=factory.newConnection();
        //创建信道
        Channel channel=connection.createChannel();
        //还需绑定队列，省略
        try {
            //开启事务
            channel.txSelect();
            //发送消息
        } catch (Exception e) {
            //出现异常进行回滚
            channel.txRollback();
            //再次发送消息
        }finally {
            channel.close();
            connection.close();
        }
        //提交事务
        channel.txCommit();

        //---confirm模式
        //开启confirm
        channel.confirmSelect();
        //发送消息
        channel.basicPublish("","",null,null);
        //消息发送到MQ后，会回传一个ack或者true
        if(channel.waitForConfirms()){
            System.out.println("发送成功");
        }else {
            //否则进行消息重发
        }
    }

    /**
     * 2、rabbitmq丢失消息：
     * (1):创建队列时开启持久化，将消息存储到磁盘中，只会持久化queue的元数据，不会持久化queue中的数据
     * (2):发送消息的时候将消息的 BasicProperties中的deliveryMode 设置为2，或者PERSISTENT_TEXT_PLAIN
     * 必须要同时设置这两个持久化才行。
     * 但如果消息发送到MQ，还没来得及持久化到磁盘，此时MQ挂了，也会导致数据有丢失，这时可以和生产者的confirm机制
     * 配合起来，等到数据持久化到磁盘后再发送ack
     */
    public void MQLost() throws IOException, TimeoutException {
        //第二个参数为true表示开启持久化
        Queue queue=new Queue("queue",true);
        //创建连接
        ConnectionFactory factory=new ConnectionFactory();
        //还需配置用户、密码、ip、端口等，省略
        Connection connection=factory.newConnection();
        //创建信道
        Channel channel=connection.createChannel();
        //还需绑定队列，省略
        //使用channel声明队列和持久化队列，第二个参数为true表示持久化
        channel.queueDeclare("queue",true,false,false,null);
        //将消息持久化，PERSISTENT_TEXT_PLAIN:持久化文本
        channel.basicPublish("","routing_key", MessageProperties.PERSISTENT_TEXT_PLAIN,"message".getBytes());
    }
}
