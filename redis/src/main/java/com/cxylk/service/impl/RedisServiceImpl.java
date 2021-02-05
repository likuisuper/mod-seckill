package com.cxylk.service.impl;

import com.alibaba.fastjson.JSON;
import com.cxylk.service.KeyPrefix;
import com.cxylk.service.RedisService;
import com.cxylk.util.ComUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @Classname RedisServiceImpl
 * @Description RedisServiceImpl
 * @Author likui
 * @Date 2021/1/17 20:59
 **/
@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    JedisPool jedisPool;

    /**
     * 通过key来获取value
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return value
     */
    @Override
    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz) {
        //Jedis是线程不安全的，所以使用连接池来获取Jedis
        //Jedis jedis=new Jedis();
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //获取真正的Key
            String realKey = prefix.getPrefix() + key;
            String value = jedis.get(realKey);
            //将字符串转换为bean
            T t = stringToBean(value, clazz);
            return t;
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 将key和value保存到redis中
     *
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    @Override
    public <T> boolean set(KeyPrefix prefix, String key, T value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String result = beanToString(value);
            if (ComUtil.isEmpty(result)) {
                return false;
            }
            String realKey = prefix.getPrefix() + key;
            int expireSecond = prefix.expireSeconds();
            if (expireSecond <= 0) {
                jedis.set(realKey, result);
            } else {
                //set并设置过期时间，该操作是原子性的
                jedis.setex(realKey, expireSecond, result);
            }
            return true;
        } finally {
            returnToPool(jedis);
        }
    }

    @Override
    public boolean exists(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            return jedis.exists(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    @Override
    public boolean delete(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            long del = jedis.del(realKey);
            return del > 0;
        } finally {
            returnToPool(jedis);
        }
    }

    @Override
    public long incr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            return jedis.incr(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    @Override
    public long decr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            return jedis.decr(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 将bean转换为String
     *
     * @param value
     * @param <T>
     * @return
     */
    private <T> String beanToString(T value) {
        if (ComUtil.isEmpty(value)) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class) {
            return "" + value;
        } else if (clazz == String.class) {
            return (String) value;
        } else if (clazz == long.class || clazz == Long.class) {
            return "" + value;
        } else {
            return JSON.toJSONString(value);
        }
    }

    /**
     * 将String转换为Bean
     *
     * @param str
     * @param clazz
     * @param <T>
     * @return
     */
    private <T> T stringToBean(String str, Class<T> clazz) {
        if (ComUtil.isEmpty(str)) {
            return null;
        } else if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(str);
        } else if (clazz == String.class) {
            return (T) str;
        } else if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(str);
        } else {
            return (T) JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }

    /**
     * 使用完毕后将连接返回连接池
     *
     * @param jedis
     */
    private void returnToPool(Jedis jedis) {
        if (jedis != null) {
            //并不会关闭，而是返回连接池
            jedis.close();
        }
    }
}
