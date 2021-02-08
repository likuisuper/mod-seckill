package com.cxylk.vo;

import com.cxylk.domain.SeckillGoodsDTO;
import com.cxylk.po.OrderInfo;
import com.cxylk.po.SeckillUser;
import lombok.Data;

/**
 * @Classname OrderDetailVO
 * @Description 订单详情vo
 * @Author likui
 * @Date 2021/2/7 17:55
 **/
@Data
public class OrderDetailVO {
    private OrderInfo orderInfo;

    private SeckillGoodsDTO goodsDTO;

    private SeckillUser seckillUser;
}
