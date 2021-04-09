package com.cxylk.service.impl;

import com.cxylk.RedisApplication;
import com.cxylk.service.RedisLuaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.BitSet;

/**
 * @Classname RedisLuaImplTest
 * @Description TODO
 * @Author likui
 * @Date 2021/4/6 11:31
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisApplication.class)
public class RedisLuaImplTest {
    @Autowired
    RedisLuaService redisLuaService;

    @Test
    public void statsAccessCount() {
        Object count = redisLuaService.statsAccessCount("count:login");
        System.out.println(count);
    }

    @Test
    public void test(){
        String key="lk1";
        System.out.println(Arrays.toString(key.getBytes()));
        BitSet bitSet=BitSet.valueOf(key.getBytes());
        System.out.println(bitSet.cardinality());
    }
}