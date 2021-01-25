package com.cxylk.biz;

import com.cxylk.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @Classname UserServiceImplTest
 * @Description TODO
 * @Author likui
 * @Date 2021/1/10 14:35
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class UserServiceImplTest {
    @Autowired
    private UserService userService;

    @Test
    public void insertTx() {
    }
}