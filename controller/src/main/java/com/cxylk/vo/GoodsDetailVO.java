package com.cxylk.vo;

import com.cxylk.domain.SeckillGoodsDTO;
import com.cxylk.po.SeckillUser;
import lombok.Data;

/**
 * @Classname GoodsVO
 * @Description TODO
 * @Author likui
 * @Date 2021/2/5 17:21
 **/
@Data
public class GoodsDetailVO {
    //秒杀状态
    private int seckillStatus;
    //秒杀倒计时
    private int remainSeconds;

    private SeckillGoodsDTO goodsDetail;

    private SeckillUser seckillUser;
}
