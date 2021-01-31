package com.cxylk.biz;

import com.cxylk.App;
import com.cxylk.dao.OrderInfoMapper;
import com.cxylk.domain.SeckillGoodsDTO;
import com.cxylk.po.SeckillUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Classname SeckillOrderServiceImplTest
 * @Description TODO
 * @Author likui
 * @Date 2021/1/29 14:37
 **/
@SpringBootTest(classes = App.class)
public class SeckillOrderServiceImplTest {
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private SeckillOrderService seckillOrderService;

    @Transactional
    @Rollback
    @Test
    public void create(){
        SeckillUser user=new SeckillUser();
        user.setId(1884232131242L);
        SeckillGoodsDTO seckillGoodsDTO=new SeckillGoodsDTO();
        seckillGoodsDTO.setGoodsName("小米");
        seckillGoodsDTO.setGoodsStock(400);
        seckillGoodsDTO.setStockCount(3);
        System.out.println(seckillOrderService.createOrder(user, seckillGoodsDTO));
    }
}
