package com.cxylk.controller;

import com.cxylk.App;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Classname SampleControllerTest
 * @Description TODO
 * @Author likui
 * @Date 2021/1/17 21:49
 **/
@SpringBootTest(classes = App.class)
public class SampleControllerTest {
    @Autowired
    private SampleController sampleController;

    @Test
    public void setByRedis(){
        System.out.println(sampleController);
//        sampleController.setByRedis();
    }
}
