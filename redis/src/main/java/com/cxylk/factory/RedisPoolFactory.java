package com.cxylk.factory;

import com.cxylk.config.RedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Classname RedisPoolFactory
 * @Description RedisPool工厂
 * @Author likui
 * @Date 2021/1/17 20:27
 **/
@Configuration
public class RedisPoolFactory {
    @Autowired
    RedisConfig redisConfig;

    /**
     * 创建连接池并配置属性
     * @return jedisPool
     */
    @Bean
    public JedisPool jedisPoolFactory(){
        JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(redisConfig.getPoolMaxTotal());
        jedisPoolConfig.setMaxIdle(redisConfig.getPoolMaxIdle());
        //设置为秒
        jedisPoolConfig.setMaxWaitMillis(redisConfig.getPoolMaxWait()*1000);
        //创建连接池。该构造函数中的timeout为ms,通过源码查阅可知(Socket类的setSoTimeout方法有明确注释)。我们是按秒来配置的，所以*1000
        //数据库默认从0开始
        JedisPool jedisPool=new JedisPool(jedisPoolConfig,redisConfig.getHost(),redisConfig.getPort(),
                redisConfig.getTimeout()*1000, redisConfig.getPassword(),0);
        return jedisPool;
    }
}
