package com.cxylk.service.impl;

import com.cxylk.service.KeyPrefix;
import com.cxylk.service.RedisService;
import com.cxylk.util.ComUtil;
import com.cxylk.util.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.ArrayList;
import java.util.List;

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
            T t = ConvertUtil.stringToBean(value, clazz);
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
            String result = ConvertUtil.beanToString(value);
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

    public boolean delete(KeyPrefix prefix) {
        if(prefix == null) {
            return false;
        }
        List<String> keys = scanKeys(prefix.getPrefix());
        if(keys==null || keys.size() <= 0) {
            return true;
        }
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.del(keys.toArray(new String[0]));
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    private List<String> scanKeys(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            List<String> keys = new ArrayList<>();
            String cursor = "0";
            ScanParams sp = new ScanParams();
            sp.match("*"+key+"*");
            sp.count(100);
            do{
                ScanResult<String> ret = jedis.scan(cursor, sp);
                List<String> result = ret.getResult();
                if(result!=null && result.size() > 0){
                    keys.addAll(result);
                }
                //再处理cursor
                cursor = ret.getCursor();
            }while(!cursor.equals("0"));
            return keys;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
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
     * 使用完毕后将连接返回连接池
     *
     * @param jedis
     */
    public void returnToPool(Jedis jedis) {
        if (jedis != null) {
            //并不会关闭，而是返回连接池
            jedis.close();
        }
    }
}
