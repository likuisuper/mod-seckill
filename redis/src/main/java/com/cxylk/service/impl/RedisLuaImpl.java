package com.cxylk.service.impl;

import com.cxylk.service.RedisLuaService;
import com.cxylk.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname RedisLuaImpl
 * @Description redis lua 脚本实现
 * @Author likui
 * @Date 2021/4/6 10:02
 **/
@Service
public class RedisLuaImpl implements RedisLuaService {
    @Autowired
    JedisPool jedisPool;

    @Autowired
    RedisService redisService;

    private final Logger logger = LoggerFactory.getLogger(RedisLuaImpl.class);

    @Override
    public Object statsAccessCount(String key) {
        Jedis jedis = null;
        Object result=null;
        try {
            jedis = jedisPool.getResource();
            String count = "local num=redis.call('incr',KEYS[1]) return num";
            String scriptLoad = jedis.scriptLoad(count);
            logger.info(scriptLoad);
            List<String> keys = new ArrayList<>();
            keys.add(key);
            List<String> args = new ArrayList<>();
            result=jedis.evalsha(scriptLoad, keys, args);
        } catch (Exception e) {
            logger.error("统计访问次数失败", e);
        } finally {
            redisService.returnToPool(jedis);
        }
        return result;
    }

    @Override
    public Object statsUser(String key,long userId) {
        Jedis jedis = null;
        Long totalUser=null;
        try {
            jedis=jedisPool.getResource();
            Boolean setbit = jedis.setbit(key, userId, "1");
            if(Boolean.TRUE.equals(setbit)){
                //总活跃用户
                totalUser=jedis.bitcount(key);
                //在线用户
            }
        }catch (Exception e){
            logger.error("统计用户失败",e);
        }finally {
            redisService.returnToPool(jedis);
        }
        return totalUser;
    }
}
